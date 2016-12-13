/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年4月7日
 */
package cn.com.infcn.superspider.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import opennlp.tools.util.StringUtil;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.PluginModel;
import cn.com.infcn.superspider.model.PluginPointModel;
import cn.com.infcn.superspider.model.SchedulePlan;
import cn.com.infcn.superspider.model.TWebFieldExtract;
import cn.com.infcn.superspider.model.TWebParamSetting;
import cn.com.infcn.superspider.model.TWebSource;
import cn.com.infcn.superspider.model.TWebSourceProxy;
import cn.com.infcn.superspider.model.TWebTypeMatchLabel;
import cn.com.infcn.superspider.model.TWebTypeRule;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.pagemodel.EasyUIDataGrid;
import cn.com.infcn.superspider.pagemodel.LicenseParam;
import cn.com.infcn.superspider.pagemodel.ProxyServer;
import cn.com.infcn.superspider.pagemodel.TaskExportImportModel;
import cn.com.infcn.superspider.pagemodel.WebConfig;
import cn.com.infcn.superspider.pagemodel.WebDownloader;
import cn.com.infcn.superspider.pagemodel.WebFieldExtract;
import cn.com.infcn.superspider.pagemodel.WebParamSetting;
import cn.com.infcn.superspider.pagemodel.WebTypeMatchLabel;
import cn.com.infcn.superspider.pagemodel.WebTypeRule;
import cn.com.infcn.superspider.service.CheckHandlerI;
import cn.com.infcn.superspider.service.ProxyServerServiceI;
import cn.com.infcn.superspider.service.TaskOutputServiceI;
import cn.com.infcn.superspider.service.WebDownloaderServiceI;
import cn.com.infcn.superspider.service.WebParamSettingServiceI;
import cn.com.infcn.superspider.service.WebServiceI;
import cn.com.infcn.superspider.service.WebSourceServiceI;
import cn.com.infcn.superspider.service.WebTypeRuleServiceI;
import cn.com.infcn.superspider.utils.DbUtil;
import cn.com.infcn.superspider.utils.UUIDCreater;
import cn.com.infcn.superspider.utils.WebUtil;
import cn.com.infcn.superspider.utils.XmlConfigUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.plugin.util.UrlUtils;
import com.justme.superspider.url.TargetMatcher;
import com.justme.superspider.xml.Field;
import com.justme.superspider.xml.Header;
import com.justme.superspider.xml.Headers;
import com.justme.superspider.xml.Model;
import com.justme.superspider.xml.Option;
import com.justme.superspider.xml.Options;
import com.justme.superspider.xml.Outputs;
import com.justme.superspider.xml.Parser;
import com.justme.superspider.xml.Parsers;
import com.justme.superspider.xml.Plugins;
import com.justme.superspider.xml.Proxy;
import com.justme.superspider.xml.Proxys;
import com.justme.superspider.xml.Rule;
import com.justme.superspider.xml.Rules;
import com.justme.superspider.xml.Seed;
import com.justme.superspider.xml.Seeds;
import com.justme.superspider.xml.Site;
import com.justme.superspider.xml.Target;
import com.justme.superspider.xml.Targets;



/**
 * @author lihf
 * @date 2016年4月7日
 */
@Service(value = "webService")
public class WebServiceImpl implements WebServiceI
{

	@Autowired
	private BaseDaoI<Task> taskDao;
	@Autowired
	private BaseDaoI<TWebSource> webSourceDao;
	@Autowired
	private BaseDaoI<TWebSourceProxy> webSourceProxyDao;
	@Autowired
	private BaseDaoI<TaskOutput> taskOutputDao;
	@Autowired
	private BaseDaoI<TWebTypeRule> webTypeRuleDao;
	@Autowired
	private BaseDaoI<TWebParamSetting> webParamSettingDao;
	@Autowired
	private BaseDaoI<TWebFieldExtract> webFieldExtractDao;
	@Autowired
	private BaseDaoI<TWebTypeMatchLabel> webTypeMatchLabelDao;
	@Autowired
	private DbFieldMappingServiceImpl fieldMappingService;
	@Autowired
	private WebTypeRuleServiceI webTypeRuleService;
	@Autowired
	private WebParamSettingServiceI webParamSettingService;
	@Autowired
	private PluginServiceImpl pluginService;
	@Autowired
	private PointServiceImpl pointService;
	@Autowired
	private WebDownloaderServiceI webDownloaderService;
	@Autowired
	private WebSourceServiceI webSourceService;
	@Autowired
	private TaskOutputServiceI taskOutputService;
	@Autowired
	private SchedulePlanServiceImpl schedulePlanService;
	@Autowired
	private ProxyServerServiceI proxyServerService;
	@Autowired 
	private CheckHandlerI checkHandler;
	@Autowired
	private TaskServiceImpl taskService;

	/**
	 * 添加web任务
	 * 
	 * @author lihf
	 * @date 2016年4月7日 下午5:33:09
	 * @param webConfig
	 * @throws Exception
	 */
	@Override
	public void add(WebConfig webConfig) throws Exception
	{
		if (null == webConfig)
		{
			throw new Exception("参数不能为空！");
		}
		LicenseParam licenseParam = new LicenseParam();
		licenseParam.setTaskType(webConfig.getTaskType());
		licenseParam.setOperation("add");
		checkHandler.checkWeb(licenseParam);
		String taskId = UUIDCreater.getUUID();
		webConfig.setTaskId(taskId);
		Task task = new Task();
		BeanUtils.copyProperties(webConfig, task);
		task.setTaskId(taskId);

		String webSourceId = UUIDCreater.getUUID();
		TWebSource webSource = new TWebSource();
		BeanUtils.copyProperties(webConfig, webSource);
		webSource.setId(webSourceId);
		
		
		WebDownloader webDownloader  = webDownloaderService.getWebDownloaderById(webConfig.getDownloaderId());
		webConfig.setWebDownloader(webDownloader);

		String proxyServerIds = webConfig.getProxyServerIds();
		List<TWebSourceProxy> twebSourceProxyList = null;
		if (!StringUtil.isEmpty(proxyServerIds))
		{
			String[] ids = proxyServerIds.split(",");
			if (null != ids && ids.length > 0)
			{
				twebSourceProxyList = new ArrayList<TWebSourceProxy>();
				for (String proxyServerId : ids)
				{
					TWebSourceProxy twebSourceProxy = new TWebSourceProxy();
					twebSourceProxy.setId(UUIDCreater.getUUID());
					twebSourceProxy.setWebSourceId(webSourceId);
					twebSourceProxy.setProxyServerId(proxyServerId);
					twebSourceProxyList.add(twebSourceProxy);
				}
			}

		}
		

		TaskOutput taskOutput = new TaskOutput();
		BeanUtils.copyProperties(webConfig, taskOutput);
		taskOutput.setTopId(UUIDCreater.getUUID());
		taskOutput.setTaskId(taskId);
		webSourceDao.save(webSource);
		// 保存代理服务
		if (null != twebSourceProxyList && twebSourceProxyList.size() > 0)
		{
			for (TWebSourceProxy tWebSourceProxy : twebSourceProxyList)
			{
				webSourceProxyDao.save(tWebSourceProxy);
			}
		}
		taskOutputDao.save(taskOutput);
		
		//保存页面类型规则
		List<TWebTypeRule> typelist = JSONArray.parseArray(webConfig.getWebTypeRuleJson(), TWebTypeRule.class);
		List<TWebParamSetting> paramlist = JSONArray.parseArray(webConfig.getParamObjectListJson(), TWebParamSetting.class);
		List<FieldMapping> fieldList = JSONArray.parseArray(webConfig.getFieldMappingJson(), FieldMapping.class);
		webConfig.setTypelist(typelist);//设置页面类型规则集合
		webConfig.setParamlist(paramlist);//设置参数集合;
		webConfig.setFieldList(fieldList);//设置字段映射集合;
		for(TWebTypeRule t:typelist)
		{
			t.setTaskId(taskId);
			webTypeRuleDao.save(t);
			//保存规则
			List<TWebTypeMatchLabel> twebTypeMatchLabelList = t.getWebTypeMatchLabelList();
			for(TWebTypeMatchLabel t_twebTypeMatchLabel:twebTypeMatchLabelList)
			{
//				t_twebTypeMatchLabel.setRuleType(WebUtil.convertRuleTypeNameToEn(t_twebTypeMatchLabel.getRuleType()));
				webTypeMatchLabelDao.save(t_twebTypeMatchLabel);
			}
			//保存参数设置
			for(TWebParamSetting t_param:paramlist)
			{
				if(t_param.getTypeId().equalsIgnoreCase(t.getTypeId()))
				{
					String paramId = UUIDCreater.getUUID();
					t_param.setTaskId(taskId);
					t_param.setParamId(paramId);
					webParamSettingDao.save(t_param);
					List<TWebFieldExtract> fieldExtractList = t_param.getFieldExtractList();
					int index=1;
					for(TWebFieldExtract t_field:fieldExtractList)
					{
						t_field.setParamId(paramId);
						t_field.setSort(index);
						webFieldExtractDao.save(t_field);
						index++;
					}
					t.setWebParamSetting(t_param);
				}
			}
		}
		
		//保存字段映射
		fieldMappingService.saveBatch(fieldList, task);
		//生成爬虫配置文件信息;
		writeSiteConfig(task,webConfig);
		// 保存页面中的各个对象
		taskDao.save(task);
	}
	


	/**
	 * 修改web任务
	 * 
	 * @author lihf
	 * @date 2016年4月7日 下午5:33:09
	 * @param webConfig
	 * @throws Exception
	 */
	@Override
    public void edit(WebConfig webConfig) throws Exception
	{
		
		if (null == webConfig)
		{
			throw new Exception("参数不能为空！");
		}
		LicenseParam licenseParam = new LicenseParam();
		licenseParam.setTaskType(webConfig.getTaskType());
		licenseParam.setOperation("edit");
		checkHandler.checkWeb(licenseParam);
		Task task = taskDao.get(Task.class, webConfig.getTaskId());
		BeanUtils.copyProperties(webConfig, task,new String[]{"taskId","taskState","completed"});

		TWebSource webSource = webSourceDao.get(TWebSource.class, webConfig.getWebId());
		BeanUtils.copyProperties(webConfig, webSource);
		
		WebDownloader webDownloader  = webDownloaderService.getWebDownloaderById(webConfig.getDownloaderId());
		webConfig.setWebDownloader(webDownloader);
		
		//先删除原来的代理服务
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("webSourceId", webSource.getId());
		String hql = " from TWebSourceProxy t where t.webSourceId=:webSourceId ";
		List<TWebSourceProxy> has_twebSourceProxyList = webSourceProxyDao.find(hql, params);
		for(TWebSourceProxy websourceproxy:has_twebSourceProxyList)
		{
			webSourceProxyDao.delete(websourceproxy);
		}
		
		String proxyServerIds = webConfig.getProxyServerIds();
		List<TWebSourceProxy> twebSourceProxyList = null;
		if (!StringUtil.isEmpty(proxyServerIds))
		{
			String[] ids = proxyServerIds.split(",");
			if (null != ids && ids.length > 0)
			{
				twebSourceProxyList = new ArrayList<TWebSourceProxy>();
				for (String proxyServerId : ids)
				{
					TWebSourceProxy twebSourceProxy = new TWebSourceProxy();
					twebSourceProxy.setId(UUIDCreater.getUUID());
					twebSourceProxy.setWebSourceId(webSource.getId());
					twebSourceProxy.setProxyServerId(proxyServerId);
					twebSourceProxyList.add(twebSourceProxy);
				}
			}

		}

		TaskOutput taskOutput = taskOutputDao.get(TaskOutput.class, webConfig.getTopId());
		BeanUtils.copyProperties(webConfig, taskOutput);
		webSourceDao.saveOrUpdate(webSource);
		// 保存代理服务
		if (null != twebSourceProxyList && twebSourceProxyList.size() > 0)
		{
			for (TWebSourceProxy tWebSourceProxy : twebSourceProxyList)
			{
				webSourceProxyDao.save(tWebSourceProxy);
			}
		}
		taskOutputDao.saveOrUpdate(taskOutput);
		
		//保存页面类型规则
		List<TWebTypeRule> typelist = JSONArray.parseArray(webConfig.getWebTypeRuleJson(), TWebTypeRule.class);
		List<TWebParamSetting> paramlist = JSONArray.parseArray(webConfig.getParamObjectListJson(), TWebParamSetting.class);
		List<FieldMapping> fieldList = JSONArray.parseArray(webConfig.getFieldMappingJson(), FieldMapping.class);
		webConfig.setTypelist(typelist);//设置页面类型规则集合
		webConfig.setParamlist(paramlist);//设置参数集合;
		webConfig.setFieldList(fieldList);//设置字段映射集合;
		
		String taskId = task.getTaskId();
		
		webParamSettingService.deleteWebParamSettingByTaskId(taskId);
		webTypeRuleService.deleteWebTypeRuleListByTaskId(taskId);
		for(TWebTypeRule t:typelist)
		{
			t.setTaskId(taskId);
			String oldTypeId = t.getTypeId();
			String typeId = UUIDCreater.getUUID();
			t.setTypeId(typeId);
			webTypeRuleDao.saveOrUpdate(t);
			//保存规则
			List<TWebTypeMatchLabel> twebTypeMatchLabelList = t.getWebTypeMatchLabelList();
			for(TWebTypeMatchLabel t_twebTypeMatchLabel:twebTypeMatchLabelList)
			{
				t_twebTypeMatchLabel.setId(UUIDCreater.getUUID());
				t_twebTypeMatchLabel.setTypeId(typeId);
//				t_twebTypeMatchLabel.setRuleType(WebUtil.convertRuleTypeNameToEn(t_twebTypeMatchLabel.getRuleType()));
				webTypeMatchLabelDao.saveOrUpdate(t_twebTypeMatchLabel);
			}
			//保存参数设置
			for(TWebParamSetting t_param:paramlist)
			{
				if(t_param.getTypeId().equalsIgnoreCase(oldTypeId))
				{
					String paramId = UUIDCreater.getUUID();
					t_param.setTaskId(taskId);
					t_param.setParamId(paramId);
					t_param.setTypeId(typeId);
					webParamSettingDao.saveOrUpdate(t_param);
					List<TWebFieldExtract> fieldExtractList = t_param.getFieldExtractList();
					int index=1;
					for(TWebFieldExtract t_field:fieldExtractList)
					{
						t_field.setId(UUIDCreater.getUUID());
						t_field.setParamId(paramId);
						t_field.setSort(index);
						webFieldExtractDao.saveOrUpdate(t_field);
						index++;
					}
					t.setWebParamSetting(t_param);
				}
			}
		}
		fieldMappingService.deleteByTaskId(taskId);
		//保存字段映射
		fieldMappingService.saveBatch(fieldList, task);
		//生成爬虫配置文件信息;
		writeSiteConfig(task,webConfig);
		//保存页面中的各个对象
		taskDao.saveOrUpdate(task);
	}
	/**
	 * 生成爬虫配置文件;
	 * @param task
	 * @param webConfig
	 * @throws Exception
	 */
	public void writeSiteConfig(Task task,WebConfig webConfig)throws Exception{
		/********************************************** 站点配置文件设置 ***************************************/
		Site site = new Site();
		site.setUrl(UrlUtils.urlFilterSpecialChar(webConfig.getUrl()));
		String[] urls = webConfig.getUrl().split("\r\n");
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
		if("1".equals(webConfig.getTopIsClear())){
			site.setIncrement(null);
		}else{
			site.setIncrement("1");
		}
		site.setName(task.getTaskName());
		site.setId(task.getTaskId());
		String wsThread = webConfig.getFetchThreadNum() <= 0 ? "1" : webConfig.getFetchThreadNum() + "";
		site.setWaitQueue("5");//设置队列为空时的等待时间;
		site.setThread(wsThread);//设置站点爬取线程数;
		site.setCharset(webConfig.getCharset());//设置站点字符集;
		site.setReqDelay(webConfig.getFetchIntervalTime()+"");//设置爬取间隔时间;
		site.setIncludeHttps("1");//设置是否爬取https页面;
		site.setTimeout(webConfig.getRequestTimeout()+"");//HTTP请求超时时间;
		site.setHttpMethod(webConfig.getHttpMethod());//HTTP请求方式(POST、GET);
		Options options = new Options();
		options.setOption(new ArrayList<Option>());
		site.setOptions(options);
		if("2".equals(webConfig.getWebDownloader().getDownloaderId())){//设置WebDriver驱动环境变量绝对路径;
			Option chromeDriverTypeOption = new Option("drivertype", "chrome");
			Option chromeDriverOption = new Option("webdriver.chrome.driver", "#{ClassPath}\\chromedriver.exe");
			site.getOptions().getOption().add(chromeDriverTypeOption);
			site.getOptions().getOption().add(chromeDriverOption);
		}
		ResourceBundle bundle = ResourceBundle.getBundle("application");
		String appendix_Path = bundle.getString("appendixPath");
		Option localPath = new Option(Constant.LOCAL_PATH,appendix_Path);
		site.getOptions().getOption().add(localPath);
		site.setDownloader(webConfig.getWebDownloader().getDownloader());
		Header header = new Header();
		header.setName("User-Agent");
		header.setValue("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2272.89 Safari/537.36");
		List<Header> headerList = new ArrayList<Header>();
		headerList.add(header);
		Headers headers = new Headers();
		headers.setHeader(headerList);
		site.setHeaders(headers);//设置站点请求头信息;
		
		if("Y".equals(webConfig.getNeedProxy())){//设置HTTP代理服务器
			site.setProxys(getProxys(webConfig));
		}
		
		site.setTargets(getTargets(webConfig));
		site.setOutputs(getOutputs(webConfig));
		Option pointOption = new Option("isPoint", webConfig.getTopIsClear());
		Option matchFields = new Option(Constant.MATCH_FIELDS, getMatchFields(webConfig).toJSONString());
		Option appendixFields = new Option(Constant.APPENDIX_FIELDS, getAppendixFields(webConfig).toJSONString());
		site.getOptions().getOption().add(pointOption);
		site.getOptions().getOption().add(matchFields);//各级规则之间关联字段;
		site.getOptions().getOption().add(appendixFields);//各级附件字段;
		List<PluginModel> pluginModels = pluginService.findPluginByType(Constant.WEB);
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
	@SuppressWarnings("unchecked")
	public Proxys getProxys(WebConfig webConfig)throws Exception
	{
		Proxys proxys = new Proxys();
		List<Proxy> proxyList = new ArrayList<Proxy>();
		EasyUIDataGrid<ProxyServer> proxyDataGrid = proxyServerService.getProxyServerList(webConfig.getProxyServerIds());
		for(ProxyServer proxyServer : proxyDataGrid.getRows()){
			Proxy proxy = new Proxy();
			proxy.setHost(proxyServer.getIp());
			proxy.setPort(Integer.parseInt(proxyServer.getPort()));
			proxyList.add(proxy);
		}
		proxys.setProxy(proxyList);
		return proxys;
	}
	
	public Outputs getOutputs(WebConfig webConfig)
	{
		Outputs outputs = new Outputs();
		List<Options> optionList = new ArrayList<Options>();
		Options options = getOptions(webConfig,null);
		optionList.add(options);
		outputs.setOptions(optionList);
		return outputs;
	}
	
	public Options getOptions(WebConfig webConfig,Map<String,String> outParam)
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
		String type_value = outParam == null ? webConfig.getTopType() : outParam.get(Constant.TYPE) == null ? webConfig.getTopType() : outParam.get(Constant.TYPE);
		String db__type_value = outParam == null ? webConfig.getTopType() : outParam.get(Constant.DB_TYPE) == null ? webConfig.getTopType() : outParam.get(Constant.DB_TYPE);
		String host_value = outParam == null ? webConfig.getTopHost() : outParam.get(Constant.HOST) == null ? webConfig.getTopHost() : outParam.get(Constant.HOST);
		String port_value = outParam == null ? webConfig.getTopPort().toString() : outParam.get(Constant.PORT) == null ? webConfig.getTopPort().toString() : outParam.get(Constant.PORT);
		String dbname_value = outParam == null ? webConfig.getTopDbName() : outParam.get(Constant.DBNAME) == null ? webConfig.getTopDbName() : outParam.get(Constant.DBNAME);
		String username_value = outParam == null ? webConfig.getTopUserName() : outParam.get(Constant.USERNAME) == null ? webConfig.getTopUserName() : outParam.get(Constant.USERNAME);
		String password_value = outParam == null ? webConfig.getTopPassWord() : outParam.get(Constant.PASSWORD) == null ? webConfig.getTopPassWord() : outParam.get(Constant.PASSWORD);
		String thread_value = outParam == null ? webConfig.getTopThread().toString() : outParam.get(Constant.THREAD) == null ? webConfig.getTopThread().toString() : outParam.get(Constant.THREAD);
		String tablename_value = outParam == null ? webConfig.getTopTableName() : outParam.get(Constant.TABLE_NAME) == null ? webConfig.getTopTableName() : outParam.get(Constant.TABLE_NAME);
		type.setValue(db__type_value);
		host.setValue(host_value);
		port.setValue(port_value);
		dbname.setValue(dbname_value);
		username.setValue(username_value);
		password.setValue(password_value);
		thread.setValue(thread_value);
		tablename.setValue(tablename_value);
		
		Option tableStruct = new Option();
		tableStruct.setName(Constant.TABLE_STRUCT);
		tableStruct.setValue(getTableStruct(webConfig));
		optionList.add(tableStruct);
		Option targetStruct = new Option();
		targetStruct.setName(Constant.TARGET_STRUCT);
		targetStruct.setValue(getTargetTableStruct(webConfig));
		optionList.add(targetStruct);
		options.setType(type_value);
		String schemaValue = webConfig.getTopSchema() == null ? "" : webConfig.getTopSchema();
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
	
	public String getTargetTableStruct(WebConfig webConfig){
		JSONObject targetsJson = new JSONObject();
		//String topType = webConfig.getTopType();// 目标数据库类型;
		//设置爬虫内置参数url;
		//String defaultType = DbUtil.toTargetFieldType(Constant.MYSQL,Constant.MYSQL,  Constant.MYSQL_VARCHAR)+"("+Constant.DEFAULT_VARCHAR_LENGTH+")";
		String defaultTaskUrl = DbUtil.toTargetFieldType(Constant.MYSQL,Constant.MYSQL,  Constant.MYSQL_VARCHAR)+"(2000)";
		try{
			Targets  targets = getTargets(webConfig);
			for(Target target : targets.getTarget()){
				JSONObject fieldObject = new JSONObject();
				for(Field field : target.getModel().getField()){
					fieldObject.put(field.getName(),Constant.MYSQL_TEXT);
				}
				fieldObject.put(Constant.TASK_URL, defaultTaskUrl);
				fieldObject.put(Constant.SOURCE_URL,defaultTaskUrl);
				targetsJson.put(target.getId(), fieldObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return targetsJson.toJSONString().replace("\"", "'");
	}
	
	/**
	 * 获取输出表结构;
	 * @param webConfig
	 * @return
	 */
	public String getTableStruct(WebConfig webConfig)
	{
		JSONObject fields = new JSONObject();// 字段映射关系
		JSONObject sourcefields = new JSONObject();// 源字段关系
		JSONObject targetfields = new JSONObject();// 映射完关系;
		List<FieldMapping> fieldList = webConfig.getFieldList();
		if (fieldList != null && fieldList.size() > 0)
		{
			String topType = webConfig.getTopType();// 目标数据库类型;
			for (FieldMapping fieldMapping : fieldList)
			{
				if("1".equalsIgnoreCase(fieldMapping.getFieldStatus()))
				{
					// 转换字段的话标识为参数，不参与SQL语句组装字段查询;
					String targetFieldName = fieldMapping.getTargetFieldName();
					String oldFieldName = fieldMapping.getFieldName();
					String fieldName = "".equals(targetFieldName) || targetFieldName == null ? oldFieldName : targetFieldName;
					String fieldType = " ";
					if ("".equals(fieldMapping.getTargetFieldType()) || fieldMapping.getTargetFieldType() == null)
					{
						fieldType = DbUtil.getTypeText(topType);
					}
					else
					{
						fieldType = fieldMapping.getTargetFieldType();
					}
					targetfields.put(fieldName, fieldType);
					sourcefields.put(oldFieldName, fieldName);
				}
			}
			if (Constant.MSS.equals(webConfig.getTopType()))
			{
				fields.put(Constant.META_DB_ID, webConfig.getTopTableName());// 元数据仓储库ID;
				fields.put(Constant.MSS_TYPE, webConfig.getTopMssType());// 元数据仓储类型;
			}
			//设置爬虫内置参数url;
			//String defaultType = DbUtil.toTargetFieldType(Constant.MYSQL,topType,  Constant.MYSQL_VARCHAR)+"("+Constant.DEFAULT_VARCHAR_LENGTH+")";
			/*targetfields.put(Constant.TASK_URL, DbUtil.getTypeText(topType));
			targetfields.put(Constant.SOURCE_URL,DbUtil.getTypeText(topType));*/
			fields.put(Constant.IS_CLEAR, webConfig.getTopIsClear());// 是否清空目标表;
			fields.put(Constant.SOURCE_FIELDS, sourcefields);
			fields.put(Constant.TARGET_FIELDS, targetfields);
		}
		return fields.toJSONString().replace("\"", "'");
	}
	/**
	 * 获取并初始化目标字段
	 * @param webTypeRule
	 * @param webConfig
	 * @return
	 */
	public List<Field> getFields(TWebTypeRule webTypeRule,WebConfig webConfig) throws Exception
	{
		List<Field> fields = new ArrayList<Field>();
		List<FieldMapping> fieldList = webConfig.getFieldList();
		TWebParamSetting webParamSetting = webTypeRule.getWebParamSetting();
		List<TWebFieldExtract> fieldExtractList = webParamSetting.getFieldExtractList();
		if(fieldExtractList != null && fieldExtractList.size() > 0){
			for(TWebFieldExtract fieldExtract : fieldExtractList){
				Field field = new Field();
				if("".equals(fieldExtract.getIsMultValue()) || "是".equals(fieldExtract.getIsMultValue())){
					field.setIsArray("1");
				}else{
					field.setIsArray("0");
				}
				if("是".equals(fieldExtract.getIsAutoJoin())){
					field.setIsRelation("1");
				}
				Parsers parsers = new Parsers();
				Parser parser = new Parser();
				List<Parser> parserList = new ArrayList<Parser>();
				if("链接地址".equals(fieldExtract.getContent())){
					parser.setAttribute(fieldExtract.getUrlAttr());
				}
				if("自动挖掘".equals(fieldExtract.getContent())){
					//field.setIsForDigNewUrl("1");
					if(fieldExtract.getXpath()!= null && !"".equals(fieldExtract.getXpath())){
						parser.setAttribute(fieldExtract.getUrlAttr());
					}
				}
				if(fieldExtract.getRegex() != null && !"".equals(fieldExtract.getRegex().trim())){
					parser.setRegex(fieldExtract.getRegex());
				}
				parser.setXpath(fieldExtract.getXpath());
				parser.setExp(fieldExtract.getExp());
				parserList.add(parser);
				parsers.setParser(parserList);
				field.setName(fieldExtract.getName());
				field.setParsers(parsers);// 设置解析表达式;
				fields.add(field);// 添加field;
			}
		}
		
		if (fieldList != null && fieldList.size() > 0)
		{
			for (FieldMapping fieldMapping : fieldList)
			{
				for(Field field : fields){
					if(field.getName().equals(fieldMapping.getFieldName())){
						if(fieldMapping.getFieldExp() == null || fieldMapping.getFieldExp().equals(""))
						continue;
						Parser parser = new Parser();
						parser.setExp(fieldMapping.getFieldExp());
						field.getParsers().getParser().add(parser);
					}
				}
				if(fieldMapping.getFiledSource().equals("转换字段")){
					Field field = new Field();
					Parsers parsers = new Parsers();
					Parser parser = new Parser();
					parser.setExp(fieldMapping.getFieldExp());
					parsers.getParser().add(parser);
					field.setName(fieldMapping.getFieldName());
					field.setParsers(parsers);// 设置解析表达式;
					fields.add(field);// 添加field;
				}
			}
		}
		return fields;
	}
	/**
	 * 获取并初始化目标字段
	 * @param webTypeRule
	 * @param webConfig
	 * @return
	 */
	public JSONObject getMatchFields(WebConfig webConfig) throws Exception
	{
		JSONObject matchFields = new JSONObject();
		if(webConfig.getTypelist() != null && webConfig.getTypelist().size() > 0){
			for(TWebTypeRule webTypeRule : webConfig.getTypelist()){
				String ruleLevel = webTypeRule.getTypeLevel();
				TWebParamSetting webParamSetting = webTypeRule.getWebParamSetting();
				for(TWebFieldExtract webFieldExtract : webParamSetting.getFieldExtractList()){
					if("是".equals(webFieldExtract.getIsAutoJoin())){
						matchFields.put(ruleLevel, webFieldExtract.getName());
						break;
					}
				}
			}
		}
		return matchFields;
	}
	/**
	 * 获取并初始化目标字段
	 * @param webTypeRule
	 * @param webConfig
	 * @return
	 */
	public JSONObject getAppendixFields(WebConfig webConfig) throws Exception
	{
		JSONObject appendixFields = new JSONObject();
		if(webConfig.getTypelist() != null && webConfig.getTypelist().size() > 0){
			for(TWebTypeRule webTypeRule : webConfig.getTypelist()){
				String ruleLevel = webTypeRule.getTypeLevel();
				TWebParamSetting webParamSetting = webTypeRule.getWebParamSetting();
				for(TWebFieldExtract webFieldExtract : webParamSetting.getFieldExtractList()){
					if("附件".equals(webFieldExtract.getContent())){
						appendixFields.put(ruleLevel, webFieldExtract.getName());
						break;
					}
				}
			}
		}
		return appendixFields;
	}
	/**
	 * 获取并初始化目标对象
	 * @param webSource
	 * @param webConfig
	 * @return
	 */
	public Targets getTargets(WebConfig webConfig) throws Exception
	{
		Targets targets = new Targets();
		List<Rule> sourceRulesList = getSourceRules(webConfig);
		if(sourceRulesList != null && !sourceRulesList.isEmpty()){//设置全局挖掘链接规则;
			Rules sourceRules = new Rules();
			sourceRules.setPolicy("or");
			sourceRules.setRule(sourceRulesList);
			targets.setSourceRules(sourceRules);
		}
		List<Target> targetList = new ArrayList<Target>();
		if(webConfig.getTypelist() != null && webConfig.getTypelist().size() > 0){
			Map<String,String> relations = new HashMap<String,String>();
			for(TWebTypeRule webTypeRule : webConfig.getTypelist()){
				Target target = new Target();
				target.setId(webTypeRule.getTypeLevel());
				target.setName(webTypeRule.getTypeName());
				if("是".equals(webTypeRule.getIsIncrement())){
					target.setIsIncrement("1");
				}else{
					target.setIsIncrement(null);
				}
				if(relations.get(target.getId()) != null)
					target.setRelation(relations.get(target.getId()));
				Rules rules = new Rules();
				rules.setPolicy(webTypeRule.getTypeFilterRule());
				target.setUrlRules(rules);
				List<Rule> ruleList = new ArrayList<Rule>();
				rules.setRule(ruleList);
				for(TWebTypeMatchLabel matchLabel : webTypeRule.getWebTypeMatchLabelList()){
					ruleList.add(getRule(matchLabel));
				}
				Model model = new Model();
				model.setDelay(webConfig.getLoadWaitTime()+"");
				model.setParser(webConfig.getWebDownloader().getParseName());
				model.setField(getFields(webTypeRule,webConfig));
				target.setModel(model);
				if(TargetMatcher.getRelationField(target) != null){
					String nextLevel = (Integer.parseInt(target.getId())+1)+"";
					relations.put(nextLevel, target.getId());
				}
				targetList.add(target);
			}
		}
		targets.setTarget(targetList);
		return targets;
	}
	
	public List<Rule> getSourceRules(WebConfig webConfig)throws Exception{
		if(webConfig.getTypelist() != null && webConfig.getTypelist().size() > 0){
			List<Rule> ruleList = new ArrayList<Rule>();
			for(TWebTypeRule webTypeRule : webConfig.getTypelist()){
				for(TWebTypeMatchLabel matchLabel : webTypeRule.getWebTypeMatchLabelList()){
					Rule rule = getRule(matchLabel);
					Model digUrls = new Model();
					digUrls.setDelay(webConfig.getLoadWaitTime()+"");
					digUrls.setParser(webConfig.getWebDownloader().getParseName());
					digUrls.setField(getSourceFields(webTypeRule,webConfig));
					if(!digUrls.getField().isEmpty()){
						rule.setDigUrls(digUrls);
					}
					ruleList.add(rule);
				}
				
			}
			return ruleList;
		}
		return null;
	}
	
	public List<Field> getSourceFields(TWebTypeRule webTypeRule,WebConfig webConfig) throws Exception
	{
		List<Field> fields = new ArrayList<Field>();
		List<FieldMapping> fieldList = webConfig.getFieldList();
		TWebParamSetting webParamSetting = webTypeRule.getWebParamSetting();
		List<TWebFieldExtract> fieldExtractList = webParamSetting.getFieldExtractList();
		if(fieldExtractList != null && fieldExtractList.size() > 0){
			for(TWebFieldExtract fieldExtract : fieldExtractList){
				if("自动挖掘".equals(fieldExtract.getContent())){
					Field field = new Field();
					if("".equals(fieldExtract.getIsMultValue()) || "是".equals(fieldExtract.getIsMultValue())){
						field.setIsArray("1");
					}else{
						field.setIsArray("0");
					}
					if("是".equals(fieldExtract.getIsAutoJoin())){
						field.setIsRelation("1");
					}
					Parsers parsers = new Parsers();
					Parser parser = new Parser();
					List<Parser> parserList = new ArrayList<Parser>();
					field.setIsForDigNewUrl("1");
					if(fieldExtract.getXpath()!= null && !"".equals(fieldExtract.getXpath())){
						parser.setAttribute(fieldExtract.getUrlAttr());
					}
					if(fieldExtract.getRegex() != null && !"".equals(fieldExtract.getRegex().trim())){
						parser.setRegex(fieldExtract.getRegex());
					}
					parser.setXpath(fieldExtract.getXpath());
					parser.setExp(fieldExtract.getExp());
					parserList.add(parser);
					parsers.setParser(parserList);
					field.setName(fieldExtract.getName());
					field.setParsers(parsers);// 设置解析表达式;
					fields.add(field);// 添加field;
				}
			}
		}
		
		if (fieldList != null && fieldList.size() > 0)
		{
			for (FieldMapping fieldMapping : fieldList)
			{
				if(fields.isEmpty())
					break;
				for(Field field : fields){
					if(field.getName().equals(fieldMapping.getFieldName())){
						if(fieldMapping.getFieldExp() == null || fieldMapping.getFieldExp().equals(""))
						continue;
						Parser parser = new Parser();
						parser.setExp(fieldMapping.getFieldExp());
						field.getParsers().getParser().add(parser);
					}
				}
			}
		}
		return fields;
	}
	/**
	 * 获取并初始化目标规则;
	 * @param matchLabel
	 * @return
	 */
	public Rule getRule(TWebTypeMatchLabel matchLabel){
		Rule rule = new Rule();
		rule.setType(WebUtil.convertRuleTypeNameToEn(matchLabel.getRuleType()));
		rule.setValue(matchLabel.getRuleTypeValue());
		return rule;
	}
	
	/**
	 * 导出web任务
	 * @author lihf
	 * @date 2016年7月8日	下午2:13:57
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
		TWebSource webSource = webSourceService.getWebSourceByTaskId(taskId);
		TaskOutput taskOutput = taskOutputService.getByTaskId(taskId);
		List<WebTypeRule> typelist =  webTypeRuleService.getWebTypeRuleListByTaskId(taskId);
		List<WebParamSetting> webParamSettingList = webParamSettingService.getWebParamSettingByTaskId(taskId);
		List<FieldMapping> exprotFieldMappings = fieldMappingService.findByTaskId(taskId);
		List<SchedulePlan> schedulePlans = new ArrayList<SchedulePlan>();
		if(task.getTaskPlanId() != null && !"".equals(task.getTaskPlanId())){
			SchedulePlan schedulePlan = schedulePlanService.get(task.getTaskPlanId());
			schedulePlans.add(schedulePlan);
		}
		taskExportImportModel.setTask(task);
		taskExportImportModel.setWebSource(webSource);
		taskExportImportModel.setTaskOutput(taskOutput);
		taskExportImportModel.setTypelist(typelist);
		taskExportImportModel.setParamlist(webParamSettingList);
		taskExportImportModel.setFieldMappings(exprotFieldMappings);
		taskExportImportModel.setSchedulePlans(schedulePlans);
		
		return taskExportImportModel;
	}
	
	/**
	 * 导入web任务
	 * @author lihf
	 * @date 2016年7月8日	下午4:07:15
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
			checkHandler.checkWeb(licenseParam);
			taskDao.saveOrUpdate(task);
			
			TWebSource twebSource_temp = taskExportImportModel.getWebSource();
			TWebSource twebSource = webSourceDao.get(TWebSource.class, twebSource_temp.getId());
			if(twebSource!=null)
			{
				BeanUtils.copyProperties(twebSource_temp, twebSource);
			}
			else
			{
				twebSource = twebSource_temp;
			}
			webSourceDao.saveOrUpdate(twebSource);
			
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
			
			webParamSettingService.deleteWebParamSettingByTaskId(task.getTaskId());
			webTypeRuleService.deleteWebTypeRuleListByTaskId(task.getTaskId());
			
			List<WebTypeRule> webTypeRuleList = taskExportImportModel.getTypelist();
//			for(WebTypeRule webTypeRule:webTypeRuleList)
//			{
//				TWebTypeRule tWebTypeRule = webTypeRuleDao.get(TWebTypeRule.class, webTypeRule.getTypeId());
//				if(tWebTypeRule==null)
//				{
//					tWebTypeRule = new TWebTypeRule();
//				}
//				BeanUtils.copyProperties(webTypeRule, tWebTypeRule);
//				webTypeRuleDao.saveOrUpdate(tWebTypeRule);
//				
//				List<WebTypeMatchLabel> webTypeMatchLabelList = webTypeRule.getWebTypeMatchLabelList();
//				for(WebTypeMatchLabel webTypeMatchLabel:webTypeMatchLabelList)
//				{
//					TWebTypeMatchLabel twebTypeMatchLabel = webTypeMatchLabelDao.get(TWebTypeMatchLabel.class, webTypeMatchLabel.getId());
//					if(twebTypeMatchLabel==null)
//					{
//						twebTypeMatchLabel = new TWebTypeMatchLabel();
//					}
//					BeanUtils.copyProperties(webTypeMatchLabel, twebTypeMatchLabel);
////					twebTypeMatchLabel.setRuleType(WebUtil.convertRuleTypeNameToEn(twebTypeMatchLabel.getRuleType()));
//					webTypeMatchLabelDao.saveOrUpdate(twebTypeMatchLabel);
//				}
//			}
//			
//			List<WebParamSetting>  paramlist = taskExportImportModel.getParamlist();
//			//保存参数设置
//			for(WebParamSetting param:paramlist)
//			{
//				TWebParamSetting t_param = webParamSettingDao.get(TWebParamSetting.class, param.getParamId());
//				if(t_param==null)
//				{
//					t_param = new TWebParamSetting();
//				}
//				BeanUtils.copyProperties(param, t_param,new String[]{"fieldExtractList","webTypeRule"});
//				webParamSettingDao.saveOrUpdate(t_param);
//				List<WebFieldExtract> fieldExtractList = param.getFieldExtractList();
//				for (WebFieldExtract field : fieldExtractList)
//				{
//					TWebFieldExtract t_field = webFieldExtractDao.get(TWebFieldExtract.class, field.getId());
//					if(t_field==null)
//					{
//						t_field = new TWebFieldExtract();
//					}
//					BeanUtils.copyProperties(field, t_field);
//					webFieldExtractDao.saveOrUpdate(t_field);
//				}
//			}
			
			for(WebTypeRule webTypeRule:webTypeRuleList)
			{
				TWebTypeRule tWebTypeRule = new TWebTypeRule();
				BeanUtils.copyProperties(webTypeRule, tWebTypeRule);
				webTypeRuleDao.save(tWebTypeRule);
				List<WebTypeMatchLabel> webTypeMatchLabelList = webTypeRule.getWebTypeMatchLabelList();
				for(WebTypeMatchLabel webTypeMatchLabel:webTypeMatchLabelList)
				{
					TWebTypeMatchLabel twebTypeMatchLabel = new TWebTypeMatchLabel();
					BeanUtils.copyProperties(webTypeMatchLabel, twebTypeMatchLabel);
//					twebTypeMatchLabel.setRuleType(WebUtil.convertRuleTypeNameToEn(twebTypeMatchLabel.getRuleType()));
					webTypeMatchLabelDao.save(twebTypeMatchLabel);
				}
			}
			
			List<WebParamSetting>  paramlist = taskExportImportModel.getParamlist();
			//保存参数设置
			for(WebParamSetting param:paramlist)
			{
				TWebParamSetting t_param = new TWebParamSetting();
				BeanUtils.copyProperties(param, t_param,new String[]{"fieldExtractList","webTypeRule"});
				webParamSettingDao.save(t_param);
				List<WebFieldExtract> fieldExtractList = param.getFieldExtractList();
				for (WebFieldExtract field : fieldExtractList)
				{
					TWebFieldExtract t_field = new TWebFieldExtract();
					BeanUtils.copyProperties(field, t_field);
					webFieldExtractDao.save(t_field);
				}
			}
			
			//保存计划
			List<SchedulePlan> schedulePlanList=taskExportImportModel.getSchedulePlans();
			if(schedulePlanList != null && schedulePlanList.size()>0)
			{
				this.schedulePlanService.saveList(schedulePlanList);
			}
			
			//保存字段映射
			List<FieldMapping> fieldList = taskExportImportModel.getFieldMappings();
//			fieldMappingService.saveBatch(fieldList, task);
			fieldMappingService.saveList(fieldList);
		}
		else
		{
			Task task = taskExportImportModel.getTask();
			licenseParam.setOperation("add");
			checkHandler.checkWeb(licenseParam);
			task.setTaskState("0");
			task.setFinishCount(0);
			task.setSuccessCount(0);
			task.setFailCount(0);
			taskDao.save(task);
			
			TWebSource twebSource = taskExportImportModel.getWebSource();
			webSourceDao.save(twebSource);
			
			TaskOutput taskOutput = taskExportImportModel.getTaskOutput();
			taskOutputDao.save(taskOutput);
			
			List<WebTypeRule> webTypeRuleList = taskExportImportModel.getTypelist();
			for(WebTypeRule webTypeRule:webTypeRuleList)
			{
				TWebTypeRule tWebTypeRule = new TWebTypeRule();
				BeanUtils.copyProperties(webTypeRule, tWebTypeRule);
				webTypeRuleDao.save(tWebTypeRule);
				List<WebTypeMatchLabel> webTypeMatchLabelList = webTypeRule.getWebTypeMatchLabelList();
				for(WebTypeMatchLabel webTypeMatchLabel:webTypeMatchLabelList)
				{
					TWebTypeMatchLabel twebTypeMatchLabel = new TWebTypeMatchLabel();
					BeanUtils.copyProperties(webTypeMatchLabel, twebTypeMatchLabel);
//					twebTypeMatchLabel.setRuleType(WebUtil.convertRuleTypeNameToEn(twebTypeMatchLabel.getRuleType()));
					webTypeMatchLabelDao.save(twebTypeMatchLabel);
				}
			}
			
			List<WebParamSetting>  paramlist = taskExportImportModel.getParamlist();
			//保存参数设置
			for(WebParamSetting param:paramlist)
			{
				TWebParamSetting t_param = new TWebParamSetting();
				BeanUtils.copyProperties(param, t_param,new String[]{"fieldExtractList","webTypeRule"});
				webParamSettingDao.save(t_param);
				List<WebFieldExtract> fieldExtractList = param.getFieldExtractList();
				for (WebFieldExtract field : fieldExtractList)
				{
					TWebFieldExtract t_field = new TWebFieldExtract();
					BeanUtils.copyProperties(field, t_field);
					webFieldExtractDao.save(t_field);
				}
			}
			
			//保存计划
			List<SchedulePlan> schedulePlanList=taskExportImportModel.getSchedulePlans();
			if(schedulePlanList != null && schedulePlanList.size()>0)
			{
				this.schedulePlanService.saveList(schedulePlanList);
			}
			
			//保存字段映射
			List<FieldMapping> fieldList = taskExportImportModel.getFieldMappings();
//			fieldMappingService.saveBatch(fieldList, task);
			fieldMappingService.saveList(fieldList);
		}
		
	}
	
	/**
	 * 复制任务
	 * @author lihf
	 * @date 2016年10月14日	下午2:49:15
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
		licenseParam.setOperation("add");
		checkHandler.checkWeb(licenseParam);
		taskDao.save(task);
		
		String webSourceId = UUIDCreater.getUUID();
		TWebSource twebSource = new TWebSource();
		TWebSource twebSourceExport = taskExportImportModel.getWebSource();
		BeanUtils.copyProperties(twebSourceExport, twebSource);
		twebSource.setId(webSourceId);
		twebSource.setTaskId(taskId);
		webSourceDao.save(twebSource);
		
		String taskOutputId = UUIDCreater.getUUID();
		TaskOutput taskOutput = new TaskOutput();
		TaskOutput taskOutputExport = taskExportImportModel.getTaskOutput();
		BeanUtils.copyProperties(taskOutputExport, taskOutput);
		taskOutput.setTopId(taskOutputId);
		taskOutput.setTaskId(taskId);
		taskOutputDao.save(taskOutput);
		
		List<WebTypeRule> webTypeRuleList = taskExportImportModel.getTypelist();
		List<WebParamSetting>  paramlist = taskExportImportModel.getParamlist();
		for(WebTypeRule webTypeRule:webTypeRuleList)
		{
			String typeId_old = webTypeRule.getTypeId();
			String typeId = UUIDCreater.getUUID();
			TWebTypeRule tWebTypeRule = new TWebTypeRule();
			BeanUtils.copyProperties(webTypeRule, tWebTypeRule);
			tWebTypeRule.setTypeId(typeId);
			tWebTypeRule.setTaskId(taskId);
			webTypeRuleDao.save(tWebTypeRule);
			List<WebTypeMatchLabel> webTypeMatchLabelList = webTypeRule.getWebTypeMatchLabelList();
			for(WebTypeMatchLabel webTypeMatchLabel:webTypeMatchLabelList)
			{
				String webTypeMatchLabelId = UUIDCreater.getUUID();
				TWebTypeMatchLabel twebTypeMatchLabel = new TWebTypeMatchLabel();
				BeanUtils.copyProperties(webTypeMatchLabel, twebTypeMatchLabel);
				twebTypeMatchLabel.setId(webTypeMatchLabelId);
				twebTypeMatchLabel.setTypeId(typeId);
//				twebTypeMatchLabel.setRuleType(WebUtil.convertRuleTypeNameToEn(twebTypeMatchLabel.getRuleType()));
				webTypeMatchLabelDao.save(twebTypeMatchLabel);
			}
			
			
			//保存参数设置
			for(WebParamSetting param:paramlist)
			{
				if(typeId_old.equalsIgnoreCase(param.getTypeId()))
				{
					String paramId = UUIDCreater.getUUID();
					TWebParamSetting t_param = new TWebParamSetting();
					BeanUtils.copyProperties(param, t_param,new String[]{"fieldExtractList","webTypeRule"});
					t_param.setParamId(paramId);
					t_param.setTypeId(typeId);
					t_param.setTaskId(taskId);
					webParamSettingDao.save(t_param);
					List<WebFieldExtract> fieldExtractList = param.getFieldExtractList();
					for (WebFieldExtract field : fieldExtractList)
					{
						String field_id = UUIDCreater.getUUID();
						TWebFieldExtract t_field = new TWebFieldExtract();
						BeanUtils.copyProperties(field, t_field);
						t_field.setId(field_id);
						t_field.setParamId(paramId);
						webFieldExtractDao.save(t_field);
					}
					break;
				}
			}
		}
		
		

		
		//保存计划
		List<SchedulePlan> schedulePlanList=taskExportImportModel.getSchedulePlans();
		if(schedulePlanList != null && schedulePlanList.size()>0)
		{
			this.schedulePlanService.saveList(schedulePlanList);
		}
		
		//保存字段映射
		List<FieldMapping> fieldList = new ArrayList<FieldMapping>();
		List<FieldMapping> fieldListExport = taskExportImportModel.getFieldMappings();
//		fieldMappingService.saveBatch(fieldList, task);
		for(FieldMapping fieldMappingExport:fieldListExport)
		{
			FieldMapping fieldMapping = new FieldMapping();
			BeanUtils.copyProperties(fieldMappingExport, fieldMapping);
			String fieldId = UUIDCreater.getUUID();
			fieldMapping.setFieldId(fieldId);
			fieldMapping.setTaskId(taskId);
			fieldList.add(fieldMapping);
		}
		fieldMappingService.saveList(fieldList);
	}
}
