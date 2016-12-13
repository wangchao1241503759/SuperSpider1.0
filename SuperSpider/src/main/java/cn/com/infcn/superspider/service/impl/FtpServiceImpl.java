/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月12日
 */
package cn.com.infcn.superspider.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.model.FileAttribute;
import cn.com.infcn.superspider.model.FileContentMapping;
import cn.com.infcn.superspider.model.FilePubAttribute;
import cn.com.infcn.superspider.model.PluginModel;
import cn.com.infcn.superspider.model.PluginPointModel;
import cn.com.infcn.superspider.model.TFtpSource;
import cn.com.infcn.superspider.model.TWebTypeMatchLabel;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.pagemodel.FtpConfig;
import cn.com.infcn.superspider.pagemodel.LicenseParam;
import cn.com.infcn.superspider.pagemodel.TaskExportImportModel;
import cn.com.infcn.superspider.service.CheckHandlerI;
import cn.com.infcn.superspider.service.FtpServiceI;
import cn.com.infcn.superspider.service.FtpSourceServiceI;
import cn.com.infcn.superspider.service.TaskOutputServiceI;
import cn.com.infcn.superspider.utils.DbUtil;
import cn.com.infcn.superspider.utils.StringUtil;
import cn.com.infcn.superspider.utils.UUIDCreater;
import cn.com.infcn.superspider.utils.XmlConfigUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.plugin.util.UrlUtils;
import com.justme.superspider.url.UrlRuleChecker;
import com.justme.superspider.xml.Field;
import com.justme.superspider.xml.Header;
import com.justme.superspider.xml.Headers;
import com.justme.superspider.xml.Model;
import com.justme.superspider.xml.Option;
import com.justme.superspider.xml.Options;
import com.justme.superspider.xml.Outputs;
import com.justme.superspider.xml.Plugins;
import com.justme.superspider.xml.Rule;
import com.justme.superspider.xml.Rules;
import com.justme.superspider.xml.Seed;
import com.justme.superspider.xml.Seeds;
import com.justme.superspider.xml.Site;
import com.justme.superspider.xml.Target;
import com.justme.superspider.xml.Targets;

/**
 * @author lihf
 * @date 2016年7月12日
 */
@Service
public class FtpServiceImpl implements FtpServiceI
{
	@Autowired
	private BaseDaoI<Task> taskDao;
	@Autowired
	private BaseDaoI<TFtpSource> ftpSourceDao;
	@Autowired
	private BaseDaoI<TaskOutput> taskOutputDao;
	@Autowired
	private FileContentMappingServiceImpl fileContentMappingService;
	@Autowired
	private FtpSourceServiceI ftpSourceService;
	@Autowired
	private TaskOutputServiceI taskOutputService;
	@Autowired
	private FileAttributeServiceImpl fileAttributeService;
	@Autowired
	private FilePubArributeServiceImpl filePubAttributeService;
	@Autowired
	private PluginServiceImpl pluginService;
	@Autowired
	private PointServiceImpl pointService;
	
	@Autowired 
	private CheckHandlerI checkHandler;
	@Autowired
	private TaskServiceImpl taskService;

	/**
	 * 添加FTP采集任务
	 * 
	 * @author lihf
	 * @date 2016年7月12日 下午5:05:20
	 * @param ftpConfig
	 * @throws Exception
	 */
	@Override
	public void add(FtpConfig ftpConfig) throws Exception
	{
		if (null == ftpConfig)
		{
			throw new Exception("参数不能为空！");
		}
		String taskId = ftpConfig.getTaskId();
		Task task = taskDao.get(Task.class, taskId);
		LicenseParam licenseParam = new LicenseParam();
		licenseParam.setTaskType(ftpConfig.getTaskType());
		if (StringUtil.isEmpty(taskId))
		{
			licenseParam.setOperation("add");
			checkHandler.checkFtp(licenseParam);
			task = new Task();
			taskId = UUIDCreater.getUUID();
		}
		else
		{
			licenseParam.setOperation("edit");
			checkHandler.checkFtp(licenseParam);
		}
		ftpConfig.setTaskId(taskId);

		BeanUtils.copyProperties(ftpConfig, task,new String[]{"taskState"});

		String ftpSourceId = ftpConfig.getFtpId();
		if (StringUtil.isEmpty(ftpSourceId))
		{
			ftpSourceId = UUIDCreater.getUUID();
		}
		TFtpSource ftpSource = new TFtpSource();
		BeanUtils.copyProperties(ftpConfig, ftpSource);
		ftpSource.setFtpId(ftpSourceId);

		String topId = ftpConfig.getTopId();
		if (StringUtil.isEmpty(topId))
		{
			topId = UUIDCreater.getUUID();
		}
		TaskOutput taskOutput = new TaskOutput();
		BeanUtils.copyProperties(ftpConfig, taskOutput);
		taskOutput.setTopId(topId);
		taskOutput.setTaskId(taskId);
		ftpSourceDao.saveOrUpdate(ftpSource);
		taskOutputDao.saveOrUpdate(taskOutput);
		
		taskDao.saveOrUpdate(task);

		List<FileContentMapping> fieldList = JSON.parseArray(ftpConfig.getFileattrJson(), FileContentMapping.class);
		ftpConfig.setFileattrList(fieldList);
		writeSiteConfig(task, ftpConfig);//生成爬虫配置文件
		fileContentMappingService.saveBatch(ftpConfig.getFileattrList(), task);
		
	}
	/**
	 * 生成爬虫配置文件;
	 * @param task
	 * @param ftpConfig
	 * @throws Exception
	 */
	public void writeSiteConfig(Task task,FtpConfig ftpConfig)throws Exception{
		/********************************************** 站点配置文件设置 ***************************************/
		Site site = new Site();
		site.setUrl(UrlUtils.urlFilterSpecialChar(ftpConfig.getFtpPath()));
		String[] urls = ftpConfig.getFtpPath().split("\r\n");
		if(urls.length>1){//多个添加种子链接;
			Seeds seeds = new Seeds();
			List<Seed> seedList = new ArrayList<Seed>();
			for(String url : urls){
				Seed seed = new Seed();
				seed.setUrl(url);
				seedList.add(seed);
			}
			seeds.setSeed(seedList);
			site.setSeeds(seeds);//设置种子链接;
		}
		site.setName(task.getTaskName());
		site.setId(task.getTaskId());
		String wsThread = Integer.parseInt(ftpConfig.getFtpDownloadThreadNum()) <= 0 ? "1" : ftpConfig.getFtpDownloadThreadNum();
		site.setWaitQueue("5");//设置队列为空时的等待时间;
		site.setThread(wsThread);//设置站点爬取线程数;
		site.setCharset(ftpConfig.getCharset());//设置站点字符集;
		//site.setHttpMethod(Http.Method.POST);
		site.setOptions(getSourceOptions(ftpConfig));
		site.setDownloader("com.justme.superspider.plugin.ftp.util.FtpDownloader");
		Headers headers = new Headers();
		List<Header> headerList = new ArrayList<Header>();
		Header header = new Header();
		header.setName("User-Agent");
		header.setValue("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36");
		headerList.add(header);
		headers.setHeader(headerList);
		site.setHeaders(headers);
		site.setTargets(getTargets(ftpConfig));
		site.setOutputs(getOutputs(ftpConfig));
		Option pointOption = new Option(Constant.IS_CLEAR, ftpConfig.getTopIsClear());
		site.getOptions().getOption().add(pointOption);
		List<PluginModel> pluginModels = pluginService.findPluginByType(Constant.FTP);
		if (pluginModels != null && pluginModels.size() > 0)
		{
			PluginModel pluginModel = pluginModels.get(0);
			PluginPointModel pluginPointModel = new PluginPointModel();
			BeanUtils.copyProperties(pluginModel, pluginPointModel);
			pluginPointModel.setPoints(pointService.findPointByPlugin(pluginModel));
			Plugins plugins = XmlConfigUtil.getPlugins(pluginPointModel);
			site.setPlugins(plugins);
		}
		XmlConfigUtil.writeXml(site.getName(), site);
		task.setTaskJson(JSON.toJSONString(site));
	}
	
	public Outputs getOutputs(FtpConfig ftpConfig)
	{
		Outputs outputs = new Outputs();
		List<Options> optionList = new ArrayList<Options>();
		Options options = getOptions(ftpConfig,null);
		optionList.add(options);
		outputs.setOptions(optionList);
		return outputs;
	}
	
	public Options getSourceOptions(FtpConfig ftpConfig){
		Options options = new Options();
		List<Option> optionList = new ArrayList<Option>();
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		String ftp_Path = bundle.getString("ftpPath");
		Option host = new Option(Constant.HOST,ftpConfig.getFtpIP());
		Option port = new Option(Constant.PORT,ftpConfig.getFtpPort());
		Option username = new Option(Constant.USERNAME,UrlUtils.urlFilterSpecialChar(ftpConfig.getFtpUserName()));
		Option password = new Option(Constant.PASSWORD,UrlUtils.urlFilterSpecialChar(ftpConfig.getFtpPassword()));
		Option localPath = new Option(Constant.LOCAL_PATH,ftp_Path);
		String ftpPacketSize = ftpConfig.getFtpPacketSize();
		String ftpLimitStart = ftpConfig.getFtpLimitStart();
		String ftpLimitEnd	= ftpConfig.getFtpLimitEnd();
		String fileSizeLimit	= "";
		String ftpIsSubDir = ftpConfig.getFtpIsSubDir();
		Integer packetSize = 1024 * 1024 * 5;
		if(ftpPacketSize != null && !"".equals(ftpPacketSize)){
			packetSize = 1024 * Integer.parseInt(ftpConfig.getFtpPacketSize());
		}
		ftpPacketSize = packetSize.toString();
		if("Y".equals(ftpConfig.getFtpLimit())){
			if(ftpLimitStart == null || "".equals(ftpLimitStart)){
				fileSizeLimit += "0";
			}else{
				fileSizeLimit += (Integer.parseInt(ftpLimitStart) * 1024);
			}
			if(ftpLimitEnd  != null && !"".equals(ftpLimitEnd)){
				fileSizeLimit += ","+(Integer.parseInt(ftpLimitEnd) * 1024);
			}else{
				fileSizeLimit += ","+Integer.MAX_VALUE;
			}
			Option fileSize = new Option(Constant.FILE_SIZE,fileSizeLimit);
			optionList.add(fileSize);
		}
		Option dataByteSize = new Option(Constant.DATA_BYTE_SIZE,ftpPacketSize);//数据包大小限制;
		Option subDirectory = new Option(Constant.SUB_DIRECTORY,ftpIsSubDir);//是否采集子目录;
		optionList.add(host);
		optionList.add(port);
		optionList.add(username);
		optionList.add(password);
		optionList.add(localPath);
		optionList.add(dataByteSize);
		optionList.add(subDirectory);
		options.setOption(optionList);
		return options;
	}
	
	public Options getOptions(FtpConfig ftpConfig,String typeValue)
	{
		Options options = new Options();
		List<Option> optionList = new ArrayList<Option>();
		Option host = new Option();
		host.setName(Constant.HOST);
		Option port = new Option();
		port.setName(Constant.PORT);
		Option type = new Option();
		type.setName(Constant.TYPE);
		Option dbname = new Option();
		dbname.setName(Constant.DBNAME);
		Option username = new Option();
		username.setName(Constant.USERNAME);
		Option password = new Option();
		password.setName(Constant.PASSWORD);
		Option thread = new Option();
		thread.setName(Constant.THREAD);
		Option tablename = new Option();
		tablename.setName(Constant.TABLE_NAME);
		Option schema = new Option();
		schema.setName(Constant.SCHEMA);
		host.setValue(ftpConfig.getTopHost());
		port.setValue(ftpConfig.getTopPort().toString());
		if(typeValue != null && !"".equals(typeValue)){
			type.setValue(typeValue);
		}else{
			type.setValue(ftpConfig.getTopType());
		}
		dbname.setValue(ftpConfig.getTopDbName());
		username.setValue(ftpConfig.getTopUserName());
		password.setValue(ftpConfig.getTopPassWord());
		thread.setValue(ftpConfig.getTopThread().toString());
		tablename.setValue(ftpConfig.getTopTableName());
		Option tableStruct = new Option();
		tableStruct.setName(Constant.TABLE_STRUCT);
		tableStruct.setValue(getTableStruct(ftpConfig));
		optionList.add(tableStruct);
		Option targetStruct = new Option();
		targetStruct.setName(Constant.TARGET_STRUCT);
		targetStruct.setValue(getTargetTableStruct(ftpConfig));
		optionList.add(targetStruct);
		options.setType(type.getValue().toString());
		String schemaValue = ftpConfig.getTopSchema() == null ? "" : ftpConfig.getTopSchema();
		schema.setValue(schemaValue);
		optionList.add(host);
		optionList.add(port);
		optionList.add(type);
		optionList.add(dbname);
		optionList.add(username);
		optionList.add(password);
		optionList.add(tablename);
		optionList.add(thread);
		optionList.add(schema);
		options.setOption(optionList);
		return options;
	}
	
	public String getTargetTableStruct(FtpConfig ftpConfig){
		JSONObject targetsJson = new JSONObject();
		//设置爬虫内置参数url;
		String defaultType = DbUtil.toTargetFieldType(Constant.MYSQL,Constant.MYSQL,  Constant.MYSQL_VARCHAR)+"("+Constant.DEFAULT_VARCHAR_LENGTH+")";
		try{
			Targets  targets = getTargets(ftpConfig);
			for(Target target : targets.getTarget()){
				JSONObject fieldObject = new JSONObject();
				for(Field field : target.getModel().getField()){
					fieldObject.put(field.getName(),Constant.MYSQL_TEXT);
				}
				fieldObject.put(Constant.TASK_URL, defaultType);
				fieldObject.put(Constant.SOURCE_URL,defaultType);
				targetsJson.put(target.getName(), fieldObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetsJson.toJSONString().replace("\"", "'");
	}
	
	public String getTableStruct(FtpConfig ftpConfig)
	{
		JSONObject fields = new JSONObject();// 字段映射关系
		JSONObject sourcefields = new JSONObject();// 源字段关系
		JSONObject targetfields = new JSONObject();// 映射完关系;
		JSONObject blobfields = new JSONObject();// 大字段提取映射关系;
		String topType = ftpConfig.getTopType();//输出类型;
		if (ftpConfig.getFileattrList() != null && ftpConfig.getFileattrList().size() > 0)// 待提取blob大字段映射关系;
		{
			for (FileContentMapping fileContentMapping : ftpConfig.getFileattrList())
			{
				List<FileAttribute> fileAttributes = fileAttributeService.findAttributeByPub(fileContentMapping.getFileAttributeId());
				FilePubAttribute filePubAttribute = filePubAttributeService.get(fileContentMapping.getFileAttributeId());
				if (fileAttributes.size() == 0)
				{
					continue;
				}
				String attributeValue = "";
				for (int i = 0; i < fileAttributes.size(); i++)
				{
					FileAttribute fileAttribute = fileAttributes.get(i);
					if (fileAttribute.getAttributeValue() == null || "".equals(fileAttribute.getAttributeValue()))
					{
						continue;
					}
					attributeValue += fileAttribute.getAttributeValue();
					if (i < fileAttributes.size() - 1)
					{
						attributeValue += ",";
					}
				}
				String targetFieldName = fileContentMapping.getTargetFieldName() == null || "".equals(fileContentMapping.getTargetFieldName()) ? filePubAttribute.getAttributeName()
				        : fileContentMapping.getTargetFieldName();
				blobfields.put(attributeValue, targetFieldName);
				String attributeFieldType = "varchar(255)";
				if (fileContentMapping.getTargetFieldName() == null || "".equals(fileContentMapping.getTargetFieldName()))
				{
					if (attributeValue.contains(","))
					{
						String[] attributeValues = attributeValue.split(",");
						attributeValue = attributeValues[attributeValues.length - 1];
					}
					if (Constant.CONTENT.equals(attributeValue))
					{
						attributeFieldType = DbUtil.getTypeText(topType);
					}
				}
				else
				{
					attributeFieldType = fileContentMapping.getTargetFieldType();
				}
				targetfields.put(targetFieldName, attributeFieldType);// 将提取字段加入目标字段中;
				sourcefields.putAll(blobfields);// 将提取字段映射关系添加到源字段映射关系中;
			}
		}
		if (Constant.MSS.equals(ftpConfig.getTopType()))
		{
			fields.put(Constant.META_DB_ID, ftpConfig.getTopTableName());// 元数据仓储库ID;
			fields.put(Constant.MSS_TYPE, ftpConfig.getTopMssType());// 元数据仓储类型;
		}
		fields.put(Constant.IS_CLEAR, ftpConfig.getTopIsClear());// 是否清空目标表;
		fields.put(Constant.SOURCE_FIELDS, sourcefields);
		fields.put(Constant.TARGET_FIELDS, targetfields);
		fields.put(Constant.BLOB_FIELDS, blobfields);
		return fields.toJSONString().replace("\"", "'");
	}
	
	/**
	 * 获取并初始化目标字段
	 * @param webTypeRule
	 * @param ftpConfig
	 * @return
	 */
	public List<Field> getFields(FtpConfig ftpConfig)
	{
		List<Field> fields = new ArrayList<Field>();
		if (ftpConfig.getFileattrList() != null && ftpConfig.getFileattrList().size() > 0)
		{
			for (FileContentMapping fileContentMapping : ftpConfig.getFileattrList())
			{
				String attributeValue = "";
				Field tikaField = new Field();
				tikaField.setName(fileContentMapping.getFileAttributeName());
				List<FileAttribute> fileAttributes = fileAttributeService.findAttributeByPub(fileContentMapping.getFileAttributeId());
				if (fileAttributes.size() == 0)
				{
					continue;
				}
				for (int i = 0; i < fileAttributes.size(); i++)
				{
					FileAttribute fileAttribute = fileAttributes.get(i);
					if (fileAttribute.getAttributeValue() == null || "".equals(fileAttribute.getAttributeValue()))
					{
						continue;
					}
					attributeValue += fileAttribute.getAttributeValue();
					if (i < fileAttributes.size() - 1)
					{
						attributeValue += ",";
					}
				}
				tikaField.setIsArray(attributeValue);
				fields.add(tikaField);// 添加field;
			}
		}
		return fields;
	}
	/**
	 * 获取并初始化目标对象
	 * @param webSource
	 * @param ftpConfig
	 * @return
	 */
	public Targets getTargets(FtpConfig ftpConfig) throws Exception
	{
		Targets targets = new Targets();
		Target target = new Target();
		List<Target> targetList = new ArrayList<Target>();
		if(ftpConfig.getFtpFileName() != null && !"".equals(ftpConfig.getFtpFileName())){
			String[] rules = ftpConfig.getFtpFileName().split(";");
			Rules urlRules = new Rules();
			List<Rule> ruleList = new ArrayList<Rule>();
			for(String urlRule : rules){
				Rule rule = new Rule();
				if(ftpConfig.getExclusion() != null && "Y".equals(ftpConfig.getExclusion())){
					rule.setType(UrlRuleChecker.not_regex_rule);
				}else{
					rule.setType(UrlRuleChecker.regex_rule);
				}
				rule.setValue(urlRule);
				ruleList.add(rule);
			}
			urlRules.setRule(ruleList);
			urlRules.setPolicy("or");
			target.setUrlRules(urlRules);
		}
		target.setName(ftpConfig.getTaskName());
		Model model = new Model();
		model.setField(getFields(ftpConfig));
		target.setModel(model);
		targetList.add(target);
		targets.setTarget(targetList);
		return targets;
	}
	/**
	 * 获取并初始化目标规则;
	 * @param matchLabel
	 * @return
	 */
	public Rule getRule(TWebTypeMatchLabel matchLabel){
		Rule rule = new Rule();
		rule.setType(matchLabel.getRuleType());
		rule.setValue(matchLabel.getRuleTypeValue());
		return rule;
	}
	/**
	 * 导出ftp任务
	 * 
	 * @author lihf
	 * @date 2016年7月8日 下午2:13:57
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	@Override
	public TaskExportImportModel exportTask(String taskId) throws Exception
	{
		if(StringUtil.isEmpty(taskId))
		{
			throw new Exception("参数不能为空！");
		}
		TaskExportImportModel taskExportImportModel = new TaskExportImportModel();
		Task task = taskDao.get(Task.class, taskId);
		TFtpSource ftpSource = ftpSourceService.getFtpSourceByTaskId(taskId);
		TaskOutput taskOutput = taskOutputService.getByTaskId(taskId);
		List<FileContentMapping> exportFileContentMappings = fileContentMappingService.findContentMappingByTaskId(taskId);
		
		taskExportImportModel.setTask(task);
		taskExportImportModel.setFtpSource(ftpSource);
		taskExportImportModel.setTaskOutput(taskOutput);
		taskExportImportModel.setFileContentMappings(exportFileContentMappings);
		
		return taskExportImportModel;
	}

	/**
	 * 导入ftp任务
	 * 
	 * @author lihf
	 * @date 2016年7月8日 下午4:07:15
	 * @param taskExportImportModel
	 * @throws Exception
	 */
	@Override
	public void importTask(TaskExportImportModel taskExportImportModel,String isReplace) throws Exception
	{
		if(null==taskExportImportModel)
		{
			throw new Exception("参数不能为空！");
		}
		LicenseParam licenseParam = new LicenseParam();
		licenseParam.setTaskType(taskExportImportModel.getTask().getTaskType());
		if("Y".equalsIgnoreCase(isReplace))
		{
			Task task_temp = taskExportImportModel.getTask();
			Task task = taskDao.get(Task.class, task_temp.getTaskId());
			if(task!=null)
			{
				licenseParam.setOperation("edit");
				BeanUtils.copyProperties(task_temp, task);
			}
			else
			{
				licenseParam.setOperation("add");
				task = task_temp;
				task.setTaskState("0");
				task.setFinishCount(0);
				task.setSuccessCount(0);
				task.setFailCount(0);
			}
			checkHandler.checkFtp(licenseParam);
			taskDao.saveOrUpdate(task);
			
			TFtpSource tftpSource_temp = taskExportImportModel.getFtpSource();
			TFtpSource tftpSource = ftpSourceDao.get(TFtpSource.class, tftpSource_temp.getFtpId());
			if(tftpSource != null)
			{
				BeanUtils.copyProperties(tftpSource_temp, tftpSource);
			}
			else
			{
				tftpSource = tftpSource_temp;
			}
			ftpSourceDao.saveOrUpdate(tftpSource);
			
			TaskOutput taskOutput_temp = taskExportImportModel.getTaskOutput();
			TaskOutput taskOutput = taskOutputDao.get(TaskOutput.class, taskOutput_temp.getTopId());
			if(taskOutput!=null)
			{
				BeanUtils.copyProperties(taskOutput_temp, taskOutput);
			}
			else
			{
				taskOutput = taskOutput_temp;
			}
			taskOutputDao.saveOrUpdate(taskOutput);

		}
		else
		{
			licenseParam.setOperation("add");
			checkHandler.checkFtp(licenseParam);
			Task task = taskExportImportModel.getTask();
			task.setTaskState("0");
			task.setFinishCount(0);
			task.setSuccessCount(0);
			task.setFailCount(0);
			taskDao.save(task);
			
			TFtpSource tftpSource = taskExportImportModel.getFtpSource();
			ftpSourceDao.save(tftpSource);
			
			TaskOutput taskOutput = taskExportImportModel.getTaskOutput();
			taskOutputDao.save(taskOutput);
			
		}
		//保存字段映射
		List<FileContentMapping> fileContentMappingList = taskExportImportModel.getFileContentMappings();
		if(fileContentMappingList != null && fileContentMappingList.size()>0)
		{
			fileContentMappingService.saveList(fileContentMappingList);
		}
	}
	
	/**
	 * 复制ftp任务
	 * @author lihf
	 * @date 2016年10月17日	下午2:33:54
	 * @param taskExportImportModel
	 * @throws Exception
	 */
	@Override
	public void copyTask(TaskExportImportModel taskExportImportModel) throws Exception
	{
		if(null==taskExportImportModel)
		{
			throw new Exception("参数不能为空！");
		}
		LicenseParam licenseParam = new LicenseParam();
		licenseParam.setTaskType(taskExportImportModel.getTask().getTaskType());
		licenseParam.setOperation("add");
		checkHandler.checkFtp(licenseParam);
		
		String taskId=UUIDCreater.getUUID();
		Task task = new Task();
		Task taskExport = taskExportImportModel.getTask();
		BeanUtils.copyProperties(taskExport, task);
		task.setTaskId(taskId);
		String taskName = taskService.getTaskNameByName(task.getTaskName()+"_副本", 0);
		task.setTaskName(taskName);
		task.setStartCount(0);
		task.setTaskState("0");
		task.setFinishCount(0);
		task.setSuccessCount(0);
		task.setFailCount(0);
		taskDao.save(task);

		String ftpSourceId = UUIDCreater.getUUID();
		TFtpSource tftpSource = new TFtpSource();
		TFtpSource tftpSourceExport = taskExportImportModel.getFtpSource();
		BeanUtils.copyProperties(tftpSourceExport, tftpSource);
		tftpSource.setFtpId(ftpSourceId);
		tftpSource.setTaskId(taskId);
		ftpSourceDao.save(tftpSource);

		String taskOutputId = UUIDCreater.getUUID();
		TaskOutput taskOutput = new TaskOutput();
		TaskOutput taskOutputExport = taskExportImportModel.getTaskOutput();
		BeanUtils.copyProperties(taskOutputExport, taskOutput);
		taskOutput.setTopId(taskOutputId);
		taskOutput.setTaskId(taskId);
		taskOutputDao.save(taskOutput);
		
		//保存字段映射
		List<FileContentMapping> fileContentMappingList = new ArrayList<FileContentMapping>();
		List<FileContentMapping> fileContentMappingListExport = taskExportImportModel.getFileContentMappings();
		if(fileContentMappingList != null && fileContentMappingList.size()>0)
		{
			for(FileContentMapping fieldMappingExport:fileContentMappingListExport)
			{
				FileContentMapping fieldContentMapping = new FileContentMapping();
				BeanUtils.copyProperties(fieldMappingExport, fieldContentMapping);
				String fieldId = UUIDCreater.getUUID();
				fieldContentMapping.setId(fieldId);
				fieldContentMapping.setTaskId(taskId);
				fileContentMappingList.add(fieldContentMapping);
			}
			fileContentMappingService.saveList(fileContentMappingList);
		}
	}

}
