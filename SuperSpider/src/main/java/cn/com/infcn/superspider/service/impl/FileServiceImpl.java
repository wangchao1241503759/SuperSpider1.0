/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年7月13日
 */
package cn.com.infcn.superspider.service.impl;

import java.util.ArrayList;
import java.util.List;

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
import cn.com.infcn.superspider.model.TFileSource;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.pagemodel.FileConfig;
import cn.com.infcn.superspider.pagemodel.LicenseParam;
import cn.com.infcn.superspider.pagemodel.TaskExportImportModel;
import cn.com.infcn.superspider.service.CheckHandlerI;
import cn.com.infcn.superspider.service.FileServiceI;
import cn.com.infcn.superspider.service.FileSourceServiceI;
import cn.com.infcn.superspider.service.TaskOutputServiceI;
import cn.com.infcn.superspider.utils.DbUtil;
import cn.com.infcn.superspider.utils.StringUtil;
import cn.com.infcn.superspider.utils.UUIDCreater;
import cn.com.infcn.superspider.utils.XmlConfigUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
 * @date 2016年7月13日
 */
@Service
public class FileServiceImpl implements FileServiceI
{

	@Autowired
	private BaseDaoI<Task> taskDao;
	@Autowired
	private BaseDaoI<TFileSource> fileSourceDao;
	@Autowired
	private BaseDaoI<TaskOutput> taskOutputDao;
	@Autowired
	private FileContentMappingServiceImpl fileContentMappingService;
	@Autowired
	private FileSourceServiceI fileSourceService;
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
	 * 添加本地文件采集任务
	 * @author lihf
	 * @date 2016年7月13日	下午4:09:46
	 * @param fileConfig
	 * @throws Exception
	 */
	public void add(FileConfig fileConfig) throws Exception
	{
		if (null == fileConfig)
		{
			throw new Exception("参数不能为空！");
		}
		String taskId = fileConfig.getTaskId();
		Task task = null;
		LicenseParam licenseParam = new LicenseParam();
		licenseParam.setTaskType(fileConfig.getTaskType());
		if(StringUtil.isEmpty(taskId))
		{
			licenseParam.setOperation("add");
			taskId = UUIDCreater.getUUID();
			task = new Task();
			task.setTaskId(taskId);
		}
		else
		{
			licenseParam.setOperation("edit");
			task = taskDao.get(Task.class, taskId);
		}
		checkHandler.checkFile(licenseParam);
		BeanUtils.copyProperties(fileConfig, task,new String[]{"taskId","taskState"});
		
		String fileSourceId = fileConfig.getFileId();
		TFileSource fileSource = null;
		if(StringUtil.isEmpty(fileSourceId))
		{
			fileSourceId = UUIDCreater.getUUID();
			fileSource = new TFileSource();
			fileSource.setFileId(fileSourceId);
		}
		else
		{
			fileSource = fileSourceDao.get(TFileSource.class, fileSourceId);
		}
		BeanUtils.copyProperties(fileConfig, fileSource,new String[]{"fileId"});
		fileSource.setTaskId(task.getTaskId());

		String topId = fileConfig.getTopId();
		TaskOutput taskOutput = null;
		if(StringUtil.isEmpty(topId))
		{
			topId = UUIDCreater.getUUID();
			taskOutput = new TaskOutput();
			taskOutput.setTopId(topId);
		}
		else
		{
			taskOutput = taskOutputDao.get(TaskOutput.class, topId);
		}
		
		BeanUtils.copyProperties(fileConfig, taskOutput,new String[]{"topId"});
		taskOutput.setTopId(topId);
		taskOutput.setTaskId(task.getTaskId());
		fileSourceDao.saveOrUpdate(fileSource);
		taskOutputDao.saveOrUpdate(taskOutput);
		
		taskDao.saveOrUpdate(task);
		
		List<FileContentMapping> fieldList = JSONArray.parseArray(fileConfig.getFileattrJson(), FileContentMapping.class);
		fileConfig.setFileattrList(fieldList);
		fileContentMappingService.saveBatch(fileConfig.getFileattrList(), task);
		
		writeSiteConfig(task, fileConfig);//生成爬虫配置文件
	}
	/**
	 * 生成爬虫配置文件;
	 * @param task
	 * @param ftpConfig
	 * @throws Exception
	 */
	public void writeSiteConfig(Task task,FileConfig fileConfig)throws Exception{
		/********************************************** 站点配置文件设置 ***************************************/
		Site site = new Site();
		site.setUrl(UrlUtils.urlFilterSpecialChar(fileConfig.getFilePath()));
		String[] urls = fileConfig.getFilePath().split("\r\n");
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
		String wsThread = Integer.parseInt(fileConfig.getFileExtractThreadNum()) <= 0 ? "1" : fileConfig.getFileExtractThreadNum();
		site.setWaitQueue("5");//设置队列为空时的等待时间;
		site.setThread(wsThread);//设置站点爬取线程数;
		//site.setCharset(FileConfig.getCharset());//设置站点字符集;
		//site.setHttpMethod(Http.Method.POST);
		site.setOptions(getSourceOptions(fileConfig));
		site.setDownloader("com.justme.superspider.plugin.file.util.FileDownloader");
		Headers headers = new Headers();
		List<Header> headerList = new ArrayList<Header>();
		Header header = new Header();
		header.setName("User-Agent");
		header.setValue("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36");
		headerList.add(header);
		headers.setHeader(headerList);
		site.setHeaders(headers);
		site.setTargets(getTargets(fileConfig));
		site.setOutputs(getOutputs(fileConfig));
		Option pointOption = new Option("isPoint", fileConfig.getTopIsClear());
		site.getOptions().getOption().add(pointOption);
		List<PluginModel> pluginModels = pluginService.findPluginByType(Constant.FILE);
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
	
	public Outputs getOutputs(FileConfig fileConfig)
	{
		Outputs outputs = new Outputs();
		List<Options> optionList = new ArrayList<Options>();
		Options options = getOptions(fileConfig,null);
		optionList.add(options);
		outputs.setOptions(optionList);
		return outputs;
	}
	
	public Options getSourceOptions(FileConfig fileConfig){
		Options options = new Options();
		List<Option> optionList = new ArrayList<Option>();
		String fileLimitStart = fileConfig.getFileLimitStart();
		String fileLimitEnd	= fileConfig.getFileLimitEnd();
		String fileSizeLimit	= "";
		String fileIsSubDir = fileConfig.getFileIsSubDir();
		if("Y".equals(fileConfig.getFileLimit())){
			if(fileLimitStart == null || "".equals(fileLimitStart)){
				fileSizeLimit += "0";
			}else{
				fileSizeLimit += (Integer.parseInt(fileLimitStart) * 1024);
			}
			if(fileLimitEnd  != null && !"".equals(fileLimitEnd)){
				fileSizeLimit += ","+(Integer.parseInt(fileLimitEnd) * 1024);
			}else{
				fileSizeLimit += ","+Integer.MAX_VALUE;
			}
			Option fileSize = new Option(Constant.FILE_SIZE,fileSizeLimit);
			optionList.add(fileSize);
		}
		Option subDirectory = new Option(Constant.SUB_DIRECTORY,fileIsSubDir);//是否采集子目录;
		optionList.add(subDirectory);
		options.setOption(optionList);
		return options;
	}
	
	public Options getOptions(FileConfig fileConfig,String typeValue)
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
		host.setValue(fileConfig.getTopHost());
		port.setValue(fileConfig.getTopPort().toString());
		if(typeValue != null && !"".equals(typeValue)){
			type.setValue(typeValue);
		}else{
			type.setValue(fileConfig.getTopType());
		}
		dbname.setValue(fileConfig.getTopDbName());
		username.setValue(fileConfig.getTopUserName());
		password.setValue(fileConfig.getTopPassWord());
		thread.setValue(fileConfig.getTopThread().toString());
		tablename.setValue(fileConfig.getTopTableName());
		Option tableStruct = new Option();
		tableStruct.setName(Constant.TABLE_STRUCT);
		tableStruct.setValue(getTableStruct(fileConfig));
		optionList.add(tableStruct);
		Option targetStruct = new Option();
		targetStruct.setName(Constant.TARGET_STRUCT);
		targetStruct.setValue(getTargetTableStruct(fileConfig));
		optionList.add(targetStruct);
		options.setType(type.getValue().toString());
		String schemaValue = fileConfig.getTopSchema() == null ? "" : fileConfig.getTopSchema();
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
	
	public String getTargetTableStruct(FileConfig fileConfig){
		JSONObject targetsJson = new JSONObject();
		//设置爬虫内置参数url;
		String defaultType = DbUtil.toTargetFieldType(Constant.MYSQL,Constant.MYSQL,  Constant.MYSQL_VARCHAR)+"("+Constant.DEFAULT_VARCHAR_LENGTH+")";
		try{
			Targets  targets = getTargets(fileConfig);
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
	
	public String getTableStruct(FileConfig fileConfig)
	{
		JSONObject fields = new JSONObject();// 字段映射关系
		JSONObject sourcefields = new JSONObject();// 源字段关系
		JSONObject targetfields = new JSONObject();// 映射完关系;
		JSONObject blobfields = new JSONObject();// 大字段提取映射关系;
		String topType = fileConfig.getTopType();//输出类型;
		if (fileConfig.getFileattrList() != null && fileConfig.getFileattrList().size() > 0)// 待提取blob大字段映射关系;
		{
			for (FileContentMapping fileContentMapping : fileConfig.getFileattrList())
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
		if (Constant.MSS.equals(fileConfig.getTopType()))
		{
			fields.put(Constant.META_DB_ID, fileConfig.getTopTableName());// 元数据仓储库ID;
			fields.put(Constant.MSS_TYPE, fileConfig.getTopMssType());// 元数据仓储类型;
		}
		fields.put(Constant.IS_CLEAR, fileConfig.getTopIsClear());// 是否清空目标表;
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
	public List<Field> getFields(FileConfig fileConfig)
	{
		List<Field> fields = new ArrayList<Field>();
		if (fileConfig.getFileattrList() != null && fileConfig.getFileattrList().size() > 0)
		{
			for (FileContentMapping fileContentMapping : fileConfig.getFileattrList())
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
	public Targets getTargets(FileConfig fileConfig) throws Exception
	{
		Targets targets = new Targets();
		Target target = new Target();
		List<Target> targetList = new ArrayList<Target>();
		if(fileConfig.getFileName() != null && !"".equals(fileConfig.getFileName())){
			String[] rules = fileConfig.getFileName().split(";");
			Rules urlRules = new Rules();
			List<Rule> ruleList = new ArrayList<Rule>();
			for(String urlRule : rules){
				Rule rule = new Rule();
				if(fileConfig.getExclusion() != null && "Y".equals(fileConfig.getExclusion())){
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
		target.setName(fileConfig.getTaskName());
		Model model = new Model();
		model.setField(getFields(fileConfig));
		target.setModel(model);
		targetList.add(target);
		targets.setTarget(targetList);
		return targets;
	}
	/**
	 * 导出file任务
	 * @author lihf
	 * @date 2016年7月8日	下午2:13:57
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public TaskExportImportModel exportTask(String taskId) throws Exception
	{
		if(StringUtil.isEmpty(taskId))
		{
			throw new Exception("参数不能为空！");
		}
		TaskExportImportModel taskExportImportModel = new TaskExportImportModel();
		Task task = taskDao.get(Task.class, taskId);
		TFileSource fileSource = fileSourceService.getFileSourceByTaskId(taskId);
		TaskOutput taskOutput = taskOutputService.getByTaskId(taskId);
		List<FileContentMapping> exportFileContentMappings = fileContentMappingService.findContentMappingByTaskId(taskId);
		
		taskExportImportModel.setTask(task);
		taskExportImportModel.setFileSource(fileSource);
		taskExportImportModel.setTaskOutput(taskOutput);
		taskExportImportModel.setFileContentMappings(exportFileContentMappings);
		
		return taskExportImportModel;
	}
	
	/**
	 * 导入file任务
	 * @author lihf
	 * @date 2016年7月8日	下午4:07:15
	 * @param taskExportImportModel
	 * @throws Exception
	 */
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
			checkHandler.checkFile(licenseParam);
			taskDao.saveOrUpdate(task);
			
//			TFileSource tfileSource = taskExportImportModel.getFileSource();
//			fileSourceDao.save(tfileSource);
			
			TFileSource tfileSource_temp = taskExportImportModel.getFileSource();
			TFileSource tfileSource = fileSourceDao.get(TFileSource.class, tfileSource_temp.getFileId());
			if(tfileSource != null)
			{
				BeanUtils.copyProperties(tfileSource_temp, tfileSource);
			}
			else
			{
				tfileSource = tfileSource_temp;
			}
			fileSourceDao.saveOrUpdate(tfileSource);
			
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
			checkHandler.checkFile(licenseParam);
			Task task = taskExportImportModel.getTask();
			task.setTaskState("0");
			task.setFinishCount(0);
			task.setSuccessCount(0);
			task.setFailCount(0);
			taskDao.save(task);
			
			TFileSource tfileSource = taskExportImportModel.getFileSource();
			fileSourceDao.save(tfileSource);
			
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
	 * 复件file任务
	 * @author lihf
	 * @date 2016年10月17日	下午2:47:45
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
		checkHandler.checkFile(licenseParam);
		
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

		
		String fileId = UUIDCreater.getUUID();
		TFileSource tfileSource = new TFileSource();
		TFileSource tfileSourceExport = taskExportImportModel.getFileSource();
		BeanUtils.copyProperties(tfileSourceExport, tfileSource);
		tfileSource.setFileId(fileId);
		tfileSource.setTaskId(taskId);
		fileSourceDao.save(tfileSource);

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
