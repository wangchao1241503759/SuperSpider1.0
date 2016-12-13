/**
 *
 */
package cn.com.infcn.superspider.service.impl;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bson.BsonDocument;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.infcn.ade.common.dao.BaseDaoI;
import cn.com.infcn.ade.common.persistence.HibernateDao;
import cn.com.infcn.ade.common.service.BaseService;
import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.common.MongoDao;
import cn.com.infcn.superspider.dao.DbDao;
import cn.com.infcn.superspider.model.DbSource;
import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.FileAttribute;
import cn.com.infcn.superspider.model.FileContentMapping;
import cn.com.infcn.superspider.model.FilePubAttribute;
import cn.com.infcn.superspider.model.PluginModel;
import cn.com.infcn.superspider.model.PluginPointModel;
import cn.com.infcn.superspider.model.SchedulePlan;
import cn.com.infcn.superspider.model.Task;
import cn.com.infcn.superspider.model.TaskOutput;
import cn.com.infcn.superspider.model.Trigger;
import cn.com.infcn.superspider.pagemodel.DbConfig;
import cn.com.infcn.superspider.pagemodel.LicenseParam;
import cn.com.infcn.superspider.pagemodel.TaskExportImportModel;
import cn.com.infcn.superspider.service.CheckHandlerI;
import cn.com.infcn.superspider.service.DbServiceI;
import cn.com.infcn.superspider.utils.ConnectionUtil;
import cn.com.infcn.superspider.utils.DbUtil;
import cn.com.infcn.superspider.utils.StringUtil;
import cn.com.infcn.superspider.utils.UUIDCreater;
import cn.com.infcn.superspider.utils.XmlConfigUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.plugin.db.adapter.DbProtocol;
import com.justme.superspider.url.UrlRuleChecker;
import com.justme.superspider.xml.Field;
import com.justme.superspider.xml.Model;
import com.justme.superspider.xml.Option;
import com.justme.superspider.xml.Options;
import com.justme.superspider.xml.Outputs;
import com.justme.superspider.xml.Parser;
import com.justme.superspider.xml.Parsers;
import com.justme.superspider.xml.Plugins;
import com.justme.superspider.xml.Rule;
import com.justme.superspider.xml.Rules;
import com.justme.superspider.xml.Site;
import com.justme.superspider.xml.Target;
import com.justme.superspider.xml.Targets;
import com.mongodb.BasicDBObject;
import com.trs.client.TRSConnection;
import com.trs.client.TRSDataBase;
import com.trs.client.TRSDataBaseColumn;
import com.trs.client.TRSException;

/**
 * @description
 * @author WChao
 * @date 2015年12月21日 下午5:47:27
 */
@Service(value = "dbService")
public class DbServiceImpl extends BaseService<DbConfig, String> implements DbServiceI
{

	@Autowired
	private DbDao dbDao;
	@Autowired
	private TaskServiceImpl taskService;
	@Autowired
	private DbSourceServiceImpl dbSourceService;
	@Autowired
	private TaskOutputServiceImpl taskOutputService;
	@Autowired
	private DbFieldMappingServiceImpl fieldMappingService;
	@Autowired
	private FileContentMappingServiceImpl fileContentMappingService;
	@Autowired
	private FileAttributeServiceImpl fileAttributeService;
	@Autowired
	private FilePubArributeServiceImpl filePubAttributeService;
	@Autowired
	private TriggerServiceImpl triggerService;
	@Autowired
	private PluginServiceImpl pluginService;
	@Autowired
	private PointServiceImpl pointService;
	@Autowired CheckHandlerI checkHandler;
	@Autowired
	private BaseDaoI<Task> taskDao;
	@Autowired
	private BaseDaoI<DbSource> dbSourceDao;
	@Autowired
	private BaseDaoI<TaskOutput> taskOutputDao;
	@Autowired
	private SchedulePlanServiceImpl schedulePlanService;

	@Override
	public HibernateDao<DbConfig, String> getEntityDao()
	{

		return dbDao;
	}

	@Override
	public void saveDb(DbConfig dbConfig) throws Exception
	{
		String fieldDataJsonStr = dbConfig.getFieldMappingData().replace("&quot;", "\"");
		String attributeDataJsonStr = dbConfig.getAttributeMappingData().replace("&quot;", "\"");
		JSONArray fieldArray = JSON.parseArray(fieldDataJsonStr);// 将jSon串转化为json数组对象
		JSONArray attributeArray = JSON.parseArray(attributeDataJsonStr);
		if (fieldArray.size() > 0)
		{
			List<FieldMapping> fields = new ArrayList<FieldMapping>();
			for (int i = 0; i < fieldArray.size(); i++)
			{
				JSONObject obj = fieldArray.getJSONObject(i);
				FieldMapping fieldMapping = JSON.parseObject(obj.toJSONString(), FieldMapping.class);
				fields.add(fieldMapping);
			}
			dbConfig.setFieldList(fields);
		}
		if (attributeArray.size() > 0)
		{
			List<FileContentMapping> attributes = new ArrayList<FileContentMapping>();
			for (int i = 0; i < attributeArray.size(); i++)
			{
				JSONObject obj = attributeArray.getJSONObject(i);
				FileContentMapping fileContentMapping = JSON.parseObject(obj.toJSONString(), FileContentMapping.class);
				attributes.add(fileContentMapping);
			}
			dbConfig.setAttributeList(attributes);
		}
		if (dbConfig.getIsOpen() == null || "".equals(dbConfig.getIsOpen()))
		{
			dbConfig.setIsOpen("0");
		}
		Task task = null;
		DbSource ds = new DbSource();
		TaskOutput top = new TaskOutput();
		Trigger trigger = new Trigger();
		BeanUtils.copyProperties(dbConfig, ds);
		BeanUtils.copyProperties(dbConfig, top);
		BeanUtils.copyProperties(dbConfig, trigger);
		LicenseParam licenseParam = new LicenseParam();
		licenseParam.setDbType(dbConfig.getDsType());
		licenseParam.setTaskType(dbConfig.getTaskType());
		if (dbConfig.getTaskId() == null || "".equals(dbConfig.getTaskId()))
		{
			licenseParam.setOperation("add");
			checkHandler.checkDB(licenseParam);
			task = new Task();
			BeanUtils.copyProperties(dbConfig, task);
			String taskId = StringUtil.generateUUID();
			task.setTaskId(taskId);
			task.setTaskState("0");
		}
		else
		{
			licenseParam.setOperation("edit");
			checkHandler.checkDB(licenseParam);
			task = taskService.get(dbConfig.getTaskId());
			Integer finishCount = task.getFinishCount();
			Integer successCount = task.getSuccessCount();
			Integer failCount = task.getFailCount();
			BeanUtils.copyProperties(dbConfig, task);
			task.setFinishCount(finishCount);
			task.setSuccessCount(successCount);
			task.setFailCount(failCount);
		}
		if (ds.getDsId() == null || "".equals(ds.getDsId()))
		{
			ds.setDsId(StringUtil.generateUUID());
			ds.setTaskId(task.getTaskId());
		}
		if (top.getTopId() == null || "".equals(top.getTopId()))
		{
			top.setTopId(StringUtil.generateUUID());
			top.setTaskId(task.getTaskId());
		}
		if (trigger.getTriggerId() == null || "".equals(trigger.getTriggerId()))
		{
			trigger.setTriggerId(StringUtil.generateUUID());
		}
		trigger.setTaskId(task.getTaskId());
		if (dbConfig.getTopTableName() == null || "".equals(dbConfig.getTopTableName()))
		{
			dbConfig.setTopTableName(ds.getDsTableName());
			top.setTopTableName(ds.getDsTableName());
		}
		/********************************************** 站点配置文件设置 ***************************************/
		Site site = new Site();
		site.setUrl(DbProtocol.DB_URL);
		site.setName(task.getTaskName());
		site.setId(task.getTaskId());
		String dsThread = "1";
		if (ds.getDsThread().toString() != null)
		{
			if (Float.parseFloat(ds.getDsThread().toString()) <= 0)
			{
				dsThread = "1";
			}
			else
			{
				dsThread = ds.getDsThread().toString();
			}
		}
		ds.setDsThread(Float.parseFloat(dsThread));
		site.setWaitQueue(dsThread);
		site.setThread("1");
		site.setDownloader("com.justme.superspider.plugin.db.util.DbDownloader");
		site.setOptions(getOptions(dbConfig, "ds"));
		site.setTargets(getTargets(ds, dbConfig));
		site.setOutputs(getOutputs(dbConfig));
		if ("1".equals(ds.getDsTableType()))//SQL语句采集的话,将SQL语句作为Task->URL;
		{
			site.setUrl(ds.getDsSql());
			Option digOption = new Option("is_dig", "1");
			site.getOptions().getOption().add(digOption);
		}
		Option pointOption = new Option("isPoint", dbConfig.getTopIsClear());
		site.getOptions().getOption().add(pointOption);
		List<PluginModel> pluginModels = pluginService.findPluginByType(Constant.DB);
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
		taskService.save(task);
		dbSourceService.save(ds);
		taskOutputService.save(top);
		fieldMappingService.saveBatch(dbConfig.getFieldList(), task);
		fileContentMappingService.saveBatch(dbConfig.getAttributeList(), task);
		triggerService.save(trigger);
	}

	public Outputs getOutputs(DbConfig dbConfig)
	{
		Outputs outputs = new Outputs();
		List<Options> optionList = new ArrayList<Options>();
		Options options = getOptions(dbConfig, "top");
		optionList.add(options);
		outputs.setOptions(optionList);
		return outputs;
	}

	public List<Field> getFields(DbConfig dbConfig)
	{
		List<Field> fields = new ArrayList<Field>();

		List<FieldMapping> fieldList = dbConfig.getFieldList();

		boolean isHaveTikaField = false;

		if (fieldList != null && fieldList.size() > 0)
		{
			for (FieldMapping fieldMapping : fieldList)
			{
				Field field = new Field();
				Parsers parsers = new Parsers();
				Parser parser = new Parser();
				parser.setExp(fieldMapping.getFieldExp());
				parsers.getParser().add(parser);
				if (fieldMapping.getFieldName().equals(dbConfig.getSourceFieldName()))
				{
					isHaveTikaField = true;
				}
				field.setName(fieldMapping.getFieldName());
				// 转换字段的话标识为参数，不参与SQL语句组装字段查询;
				if (Constant.CHANGE_FIELD.equals(fieldMapping.getFiledSource()))
				{
					field.setIsParam("1");
				}
				field.setParsers(parsers);// 设置解析表达式;
				fields.add(field);// 添加field;
			}
		}
		if (!isHaveTikaField && dbConfig.getSourceFieldName() != null && !"".equals(dbConfig.getSourceFieldName()))
		{
			Field tikaField = new Field();
			Parsers parsers = new Parsers();
			Parser parser = new Parser();
			parser.setExp("");
			parsers.getParser().add(parser);
			tikaField.setName(dbConfig.getSourceFieldName());
			tikaField.setParsers(parsers);// 设置解析表达式;
			fields.add(tikaField);// 添加field;
		}
		return fields;
	}

	public Targets getTargets(DbSource dbSource, DbConfig dbConfig)
	{
		Targets targets = new Targets();

		List<Target> tables = new ArrayList<Target>();
		Target table = new Target();
		Rules rules = new Rules();
		List<Rule> ruleList = new ArrayList<Rule>();
		Rule rule = new Rule();
		rule.setType(UrlRuleChecker.regex_rule);
		rule.setValue(".*");
		ruleList.add(rule);
		rules.setRule(ruleList);
		rules.setPolicy("or");
		table.setUrlRules(rules);
		table.setName(dbSource.getDsTableName());
		Model model = new Model();
		model.setField(getFields(dbConfig));
		table.setModel(model);
		tables.add(table);
		targets.setTarget(tables);
		return targets;
	}

	public Options getOptions(DbConfig dbConfig, String dbType)
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
		Option fetchSize = new Option();
		fetchSize.setName(Constant.FETCH_SIZE);
		Option thread = new Option();
		thread.setName(Constant.THREAD);
		Option tablename = new Option();
		tablename.setName(Constant.TABLE_NAME);
		Option schema = new Option();
		schema.setName(Constant.SCHEMA);

		if ("ds".equals(dbType))
		{
			host.setValue(dbConfig.getDsHost());
			port.setValue(dbConfig.getDsPort().toString());
			type.setValue(dbConfig.getDsType());
			dbname.setValue(dbConfig.getDsDbName());
			tablename.setValue(dbConfig.getDsTableName());
			username.setValue(dbConfig.getDsUserName());
			password.setValue(dbConfig.getDsPassWord());
			Integer fetchSizeValue = dbConfig.getDsFetchSize() == null || "".equals(dbConfig.getDsFetchSize()) ? Constant.THREAD_NUM : dbConfig.getDsFetchSize();
			fetchSize.setValue(fetchSizeValue.toString());
			String schemaValue = dbConfig.getDsSchema() == null ? "" : dbConfig.getDsSchema();
			schema.setValue(schemaValue);
		}
		else if ("top".equals(dbType))
		{
			host.setValue(dbConfig.getTopHost());
			port.setValue(dbConfig.getTopPort().toString());
			type.setValue(dbConfig.getTopType());
			dbname.setValue(dbConfig.getTopDbName());
			username.setValue(dbConfig.getTopUserName());
			password.setValue(dbConfig.getTopPassWord());
			thread.setValue(dbConfig.getTopThread().toString());
			tablename.setValue(dbConfig.getTopTableName());
			Option tableStruct = new Option();
			tableStruct.setName(Constant.TABLE_STRUCT);
			tableStruct.setValue(getTableStruct(dbConfig));
			optionList.add(tableStruct);
			options.setType(type.getValue().toString());
			String schemaValue = dbConfig.getTopSchema() == null ? "" : dbConfig.getTopSchema();
			schema.setValue(schemaValue);
		}
		optionList.add(host);
		optionList.add(port);
		optionList.add(type);
		optionList.add(dbname);
		optionList.add(username);
		optionList.add(password);
		optionList.add(tablename);
		optionList.add(fetchSize);
		optionList.add(thread);
		optionList.add(schema);
		options.setOption(optionList);

		return options;
	}

	public String getTableStruct(DbConfig dbConfig)
	{
		JSONObject fields = new JSONObject();// 字段映射关系
		JSONObject sourcefields = new JSONObject();// 源字段关系
		JSONObject targetfields = new JSONObject();// 映射完关系;
		JSONObject blobfields = new JSONObject();// 大字段提取映射关系;
		String blobfield = "";// 待提取的blob字段;
		List<FieldMapping> fieldList = dbConfig.getFieldList();
		if (fieldList != null && fieldList.size() > 0)
		{
			String dsType = dbConfig.getDsType();// 源数据库类型
			String topType = dbConfig.getTopType();// 目标数据库类型;
			for (FieldMapping fieldMapping : fieldList)
			{
				// 转换字段的话标识为参数，不参与SQL语句组装字段查询;
				String targetFieldName = fieldMapping.getTargetFieldName();
				String oldFieldName = fieldMapping.getFieldName();
				String fieldName = "".equals(targetFieldName) || targetFieldName == null ? oldFieldName : targetFieldName;
				String fieldType = " ";
				String defaultVarcharLength = Constant.DEFAULT_VARCHAR_LENGTH;
				if ("".equals(fieldMapping.getTargetFieldType()) || fieldMapping.getTargetFieldType() == null)
				{
					if (fieldMapping.getFieldType().contains("("))
					{
						fieldType = fieldMapping.getFieldType().substring(0, fieldMapping.getFieldType().indexOf("("));
						defaultVarcharLength = fieldMapping.getFieldType().substring(fieldMapping.getFieldType().indexOf("(") + 1, fieldMapping.getFieldType().indexOf(")"));
						if ("".equals(defaultVarcharLength))
						{
							defaultVarcharLength = Constant.DEFAULT_VARCHAR_LENGTH;
						}
					}
					else
					{
						fieldType = fieldMapping.getFieldType();
					}
					fieldType = DbUtil.toTargetFieldType(dsType, topType, fieldType);
					if(dsType.equals(topType)){//源库与目标库同一种数据库类型则字段类型一样;
						fieldType = fieldMapping.getFieldType();
					}else if (fieldType.contains("varchar") && fieldMapping.getFieldType().contains("(") && !fieldType.contains("(") || (Constant.TRS.equals(dbConfig.getDsType()) && fieldType.contains("varchar")))
					{
						if (fieldMapping.getFieldType().toLowerCase().contains("long") || Integer.parseInt(defaultVarcharLength) > Integer.parseInt(Constant.DEFAULT_VARCHAR_LENGTH))
						{// long varchar设置为text
							fieldType = DbUtil.getTypeText(topType);
						}
						else
						{
							fieldType = fieldType + "(" + defaultVarcharLength + ")";
						}
					}
				}
				else
				{
					fieldType = fieldMapping.getTargetFieldType();
				}
				targetfields.put(fieldName, fieldType);
				sourcefields.put(oldFieldName, fieldName);
			}
			if (dbConfig.getAttributeList() != null && dbConfig.getAttributeList().size() > 0)// 待提取blob大字段映射关系;
			{
				for (FileContentMapping fileContentMapping : dbConfig.getAttributeList())
				{
					blobfield = fileContentMapping.getSourceFieldName();
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
			if (Constant.MSS.equals(dbConfig.getTopType()))
			{
				fields.put(Constant.META_DB_ID, dbConfig.getTopTableName());// 元数据仓储库ID;
				fields.put(Constant.MSS_TYPE, dbConfig.getTopMssType());// 元数据仓储类型;
			}
			fields.put(Constant.IS_CLEAR, dbConfig.getTopIsClear());// 是否清空目标表;
			fields.put(Constant.SOURCE_FIELDS, sourcefields);
			fields.put(Constant.TARGET_FIELDS, targetfields);
			fields.put(Constant.BLOB_FIELDS, blobfields);
			fields.put(Constant.BLOB_FIELD, blobfield);
			fields.put(Constant.INCREMENT, dbConfig.getIsOpen());
			fields.put(Constant.INCREMENT_PRIMARY_FIELD, dbConfig.getPrimaryField());
		}
		return fields.toJSONString().replace("\"", "'");
	}

	@SuppressWarnings("resource")
	@Override
	public List<String> getAllTables(DbSource dbSource)
	{
		List<String> tables = null;
		if (Constant.MYSQL.equals(dbSource.getDsType()) || Constant.SQLSERVER.equals(dbSource.getDsType()) || Constant.ORACLE.equals(dbSource.getDsType()) || Constant.DB2.equals(dbSource.getDsType()))
		{
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet tableRet = null;
			try
			{
				conn = ConnectionUtil.getConnection(dbSource.getDsType(), dbSource.getDsHost(), dbSource.getDsPort(), dbSource.getDsDbName(), dbSource.getDsUserName(), dbSource.getDsPassWord());
				if (conn != null)
				{
					tables = new ArrayList<String>();
					String sql = "";
					if (Constant.ORACLE.equals(dbSource.getDsType()))// Oracle数据库;有USER_TABLES、ALL_TABLES、DBA_TABLES三种区分，我们需要的是USER_TABLES;
					{
						sql = "SELECT TABLE_NAME as name FROM USER_TABLES";
						pstmt = conn.prepareStatement(sql);
						tableRet = pstmt.executeQuery();
						while (tableRet.next())
						{
							tables.add(tableRet.getString("name"));
						}
						sql = "select * from user_views";
						pstmt = conn.prepareStatement(sql);
						tableRet = pstmt.executeQuery();
						while (tableRet.next())
						{
							tables.add(tableRet.getString("VIEW_NAME"));
						}
					}
					else if(Constant.SQLSERVER.equals(dbSource.getDsType()))
					{
						//其他关系型数据库;
						DatabaseMetaData m_DBMetaData = conn.getMetaData();
						String schema = "%";
						if (dbSource.getDsSchema() != null && !"".equals(dbSource.getDsSchema()))
						{
							schema = dbSource.getDsSchema();
						}
						tableRet = m_DBMetaData.getTables(null, schema, "%", new String[] { "TABLE"});
						while (tableRet.next())
						{
							tables.add(tableRet.getString("TABLE_NAME"));
						}
						sql = "select * from sysobjects where xtype='V'";
						pstmt = conn.prepareStatement(sql);
						tableRet = pstmt.executeQuery();
						while (tableRet.next())
						{
							tables.add(tableRet.getString("name"));
						}
					}else{
						//其他关系型数据库;
						DatabaseMetaData m_DBMetaData = conn.getMetaData();
						String schema = "%";
						if (dbSource.getDsSchema() != null && !"".equals(dbSource.getDsSchema()))
						{
							schema = dbSource.getDsSchema();
						}
						tableRet = m_DBMetaData.getTables(null, schema, "%", new String[] { "TABLE","VIEW"});
						while (tableRet.next())
						{
							tables.add(tableRet.getString("TABLE_NAME"));
						}
					}
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return tables;
			}
			finally
			{
				if (conn != null)
				{
					ConnectionUtil.closeConnection(conn, pstmt, tableRet);
				}
			}
		}
		else if (Constant.MONGO.equals(dbSource.getDsType()))
		{
			MongoDao mongoDao = null;
			try
			{
				mongoDao = new MongoDao(dbSource.getDsHost(), dbSource.getDsPort(), dbSource.getDsDbName());
				Set<String> collections = mongoDao.getDB(dbSource.getDsDbName()).getCollectionNames();
				if (collections != null)
				{
					tables = new ArrayList<String>();
					for (String name : collections)
					{
						if (!(name.contains("$") || name.contains(Constant.INDEX_TABLENAME) || name.contains(Constant.PROFILE_TABLENAME)))
						{
							tables.add(name);
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return tables;
			}
			finally
			{
				if (mongoDao != null)
				{
					mongoDao.getClient().close();
				}
			}
		}else if(Constant.TRS.equals(dbSource.getDsType())){
			TRSConnection conn = null;
			try {
				conn = new TRSConnection();
				conn.connect(dbSource.getDsHost(),dbSource.getDsPort().toString(), dbSource.getDsUserName(), dbSource.getDsPassWord());
				if(conn != null)
				tables = new ArrayList<String>();
				TRSDataBase[] trsDbs = conn.getDataBases("");// TRS数据库对象类
				for(TRSDataBase trsDataBase : trsDbs){
					tables.add(trsDataBase.getName());
				}
			} catch (TRSException e) {
				e.printStackTrace();
			}finally{
				if(conn != null){
					conn.close();
				}
			}
		}
		return tables;
	}

	@SuppressWarnings("static-access")
	@Override
	public List<FieldMapping> getAllColumns(DbSource dbSource)
	{
		List<FieldMapping> columns = new ArrayList<FieldMapping>();
		if (Constant.MYSQL.equals(dbSource.getDsType()) || Constant.SQLSERVER.equals(dbSource.getDsType()) || Constant.ORACLE.equals(dbSource.getDsType()) || Constant.DB2.equals(dbSource.getDsType()))
		{
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				conn = ConnectionUtil.getConnection(dbSource.getDsType(), dbSource.getDsHost(), dbSource.getDsPort(), dbSource.getDsDbName(), dbSource.getDsUserName(), dbSource.getDsPassWord());
				if (conn != null)
				{
					DatabaseMetaData m_DBMetaData = conn.getMetaData();
					String schema = "%";
					if (dbSource.getDsSchema() != null && !"".equals(dbSource.getDsSchema()))
					{
						schema = dbSource.getDsSchema();
					}
					ResultSet colRet = m_DBMetaData.getColumns(null, schema, dbSource.getDsTableName(), "%");
					while (colRet.next())
					{
						FieldMapping row = new FieldMapping();
						row.setFieldName(colRet.getString("COLUMN_NAME"));
						row.setFiledSource("源字段");
						if (colRet.getString("TYPE_NAME").toLowerCase().contains("blob") || colRet.getString("TYPE_NAME").toLowerCase().contains("clob")
						        || colRet.getString("TYPE_NAME").toLowerCase().contains("text") || colRet.getString("TYPE_NAME").toLowerCase().contains("image"))
						{
							row.setFieldType(colRet.getString("TYPE_NAME"));// 如果是大字段去掉类型大小限制;
						}
						else
						{
							String typeName = "";
							if (colRet.getString("TYPE_NAME").contains("()"))
							{
								typeName = colRet.getString("TYPE_NAME").substring(0, colRet.getString("TYPE_NAME").indexOf("(") + 1) + colRet.getInt("COLUMN_SIZE")
								        + colRet.getString("TYPE_NAME").substring(colRet.getString("TYPE_NAME").indexOf("(") + 1);
							}
							else
							{
								typeName = colRet.getString("TYPE_NAME") + "(" + colRet.getInt("COLUMN_SIZE") + ")";
							}
							row.setFieldType(typeName);
						}
						row.setFieldOrdinalPosition(colRet.getInt("ORDINAL_POSITION"));
						columns.add(row);
					}
				}
			}
			catch (SQLException e)
			{
				e.printStackTrace();
				return columns;
			}
			finally
			{
				if (conn != null)
				{
					ConnectionUtil.closeConnection(conn, pstmt, rs);
				}
			}
		}
		else if (Constant.MONGO.equals(dbSource.getDsType()))
		{
			MongoDao mongoDao = null;
			try
			{
				mongoDao = new MongoDao(dbSource.getDsHost(), dbSource.getDsPort(), dbSource.getDsDbName());
				BasicDBObject basicDbObject = (BasicDBObject) mongoDao.findOne(dbSource.getDsTableName());
				BsonDocument bsonDocument = basicDbObject.toBsonDocument(null, mongoDao.getClient().getDefaultCodecRegistry());
				int i = 0;
				for (String key : basicDbObject.keySet())
				{
					FieldMapping row = new FieldMapping();
					row.setFieldName(key);
					row.setFiledSource("源字段");
					row.setFieldType(bsonDocument.get(key).getBsonType().toString());
					row.setFieldOrdinalPosition(i + 1);
					columns.add(row);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return columns;
			}
			finally
			{
				if (mongoDao != null)
				{
					mongoDao.getClient().close();
				}
			}
		}else if(Constant.TRS.equals(dbSource.getDsType())){
			
			TRSConnection conn = null;
			try {
				conn = new TRSConnection();
				conn.connect(dbSource.getDsHost(),dbSource.getDsPort().toString(), dbSource.getDsUserName(), dbSource.getDsPassWord());
				TRSDataBase[] trsDbs = conn.getDataBases(dbSource.getDsTableName());// TRS数据库对象类
				TRSDataBaseColumn[] column = trsDbs[0].getColumns();// 获取连接到的数据库中的所有字段
				for (int i = 0; i < column.length; i++) {
					FieldMapping row = new FieldMapping();
					row.setFieldName(column[i].getFullName());
					row.setFiledSource("源字段");
					row.setFieldType(column[i].getColTypeName());
					row.setFieldOrdinalPosition(i + 1);
					columns.add(row);
				}
			} catch (TRSException e) {
				e.printStackTrace();
			}finally{
				if(conn != null){
					conn.close();
				}
			}
		}
		return columns;
	}
	
	@SuppressWarnings("static-access")
	@Override
	public List<FieldMapping> getAllColumnsBySQL(DbSource dbSource) throws Exception
	{
		List<FieldMapping> columns = new ArrayList<FieldMapping>();
		if (Constant.MYSQL.equals(dbSource.getDsType()) || Constant.SQLSERVER.equals(dbSource.getDsType()) || Constant.ORACLE.equals(dbSource.getDsType()) || Constant.DB2.equals(dbSource.getDsType()))
		{
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				conn = ConnectionUtil.getConnection(dbSource.getDsType(), dbSource.getDsHost(), dbSource.getDsPort(), dbSource.getDsDbName(), dbSource.getDsUserName(), dbSource.getDsPassWord());
				if (conn != null)
				{
					String sql = dbSource.getDsSql();
					sql = sql.replaceAll("&quot;", "\"").replaceAll("&lt;", "<").replaceAll("&gt;", ">");
					if(StringUtil.isEmpty(sql))
					{
						throw new Exception("sql语句不能为空！");
					}
					if(sql.indexOf("where")!=-1 || sql.indexOf("WHERE")!=-1)
					{
						sql.replaceAll("where", " where 1=2 and ");
					}
					else 
					{
						sql += " where 1=2 ";
					}
					pstmt = conn.prepareStatement(sql);
					ResultSet colRet =pstmt.executeQuery();
					ResultSetMetaData metaData = colRet.getMetaData();
					int count = metaData.getColumnCount();
					for(int i=0;i<count;i++)
					{
						String name = metaData.getColumnName(i+1);
						String typeName = metaData.getColumnTypeName(i+1);
						int size = metaData.getColumnDisplaySize(i+1);
						if(DbUtil.isSpecialColumnTypeChar(typeName))
						{
							String typeNameSize = "";
							if(size>Constant.MYSQL_VARCHAR_MAX_LENGTH)
							{
								typeNameSize = Constant.MYSQL_TEXT;
							}
							else
							{
								if (typeName.contains("()"))
								{
									typeNameSize = typeName.substring(0, typeName.indexOf("(") + 1) + size
											+ typeName.substring(typeName.indexOf("(") + 1);
								}
								else
								{
									typeNameSize = typeName + "(" + size + ")";
								}
							}
							typeName = typeNameSize;
						}
						
						FieldMapping row = new FieldMapping();
						row.setFieldName(name);
						row.setFiledSource("源字段");
						row.setFieldType(typeName);// 如果是大字段去掉类型大小限制;
						columns.add(row);
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				
				throw e;
			}
			finally
			{
				if (conn != null)
				{
					ConnectionUtil.closeConnection(conn, pstmt, rs);
				}
			}
		}
		else if (Constant.MONGO.equals(dbSource.getDsType()))
		{
			MongoDao mongoDao = null;
			try
			{
				mongoDao = new MongoDao(dbSource.getDsHost(), dbSource.getDsPort(), dbSource.getDsDbName());
				BasicDBObject basicDbObject = (BasicDBObject) mongoDao.findOne(dbSource.getDsTableName());
				BsonDocument bsonDocument = basicDbObject.toBsonDocument(null, mongoDao.getClient().getDefaultCodecRegistry());
				int i = 0;
				for (String key : basicDbObject.keySet())
				{
					FieldMapping row = new FieldMapping();
					row.setFieldName(key);
					row.setFiledSource("源字段");
					row.setFieldType(bsonDocument.get(key).getBsonType().toString());
					row.setFieldOrdinalPosition(i + 1);
					columns.add(row);
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				throw new Exception(e.getMessage(),e);
			}
			finally
			{
				if (mongoDao != null)
				{
					mongoDao.getClient().close();
				}
			}
		}
		return columns;
	}

	@Override
	public void deleteByTaskId(String taskId) throws Exception
	{
		// TODO Auto-generated method stub

	}
	
	/**
	 * 导出db任务
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
		DbSource dbSource = dbSourceService.getByTaskId(taskId);
		TaskOutput taskOutput = taskOutputService.getByTaskId(taskId);
		List<FieldMapping> exprotFieldMappings = fieldMappingService.findByTaskId(taskId);
		List<FileContentMapping> exportFileContentMappings = fileContentMappingService.findContentMappingByTaskId(taskId);
		List<SchedulePlan> schedulePlans = new ArrayList<SchedulePlan>();
		if(task.getTaskPlanId() != null && !"".equals(task.getTaskPlanId())){
			SchedulePlan schedulePlan = schedulePlanService.get(task.getTaskPlanId());
			schedulePlans.add(schedulePlan);
		}
		taskExportImportModel.setTask(task);
		taskExportImportModel.setDbSource(dbSource);
		taskExportImportModel.setTaskOutput(taskOutput);
		taskExportImportModel.setFieldMappings(exprotFieldMappings);
		taskExportImportModel.setFileContentMappings(exportFileContentMappings);
		taskExportImportModel.setSchedulePlans(schedulePlans);
		
		return taskExportImportModel;
	}
	
	/**
	 * 导入db任务
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
		if("Y".equalsIgnoreCase(isReplace))
		{
			Task task_temp = taskExportImportModel.getTask();
			Task task = taskService.getTaskById(task_temp.getTaskId());
			licenseParam.setDbType(taskExportImportModel.getDbSource().getDsType());
			licenseParam.setTaskType(task_temp.getTaskType());
			if(task!=null)
			{
				licenseParam.setOperation("edit");
				checkHandler.checkDB(licenseParam);
				BeanUtils.copyProperties(task_temp, task);
			}
			else
			{
				licenseParam.setOperation("add");
				checkHandler.checkDB(licenseParam);
				task = task_temp;
				task.setTaskState("0");
				task.setFinishCount(0);
				task.setSuccessCount(0);
				task.setFailCount(0);
			}
			taskDao.saveOrUpdate(task);
			
			DbSource dbSource_temp = taskExportImportModel.getDbSource();
			DbSource dbSource = dbSourceDao.get(DbSource.class, dbSource_temp.getDsId());
			if(dbSource != null)
			{
				BeanUtils.copyProperties(dbSource_temp, dbSource);
			}
			else
			{
				dbSource = dbSource_temp;
			}
			dbSourceDao.saveOrUpdate(dbSource);
			
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
			
			//保存计划
			List<SchedulePlan> schedulePlanList=taskExportImportModel.getSchedulePlans();
			if(schedulePlanList != null && schedulePlanList.size()>0)
			{
				this.schedulePlanService.saveList(schedulePlanList);
			}
			
			List<FileContentMapping> fileContentMappingList=taskExportImportModel.getFileContentMappings();
			if(fileContentMappingList != null && fileContentMappingList.size()>0)
			{
				this.fileContentMappingService.saveList(fileContentMappingList);
			}
			
			//保存字段映射
			List<FieldMapping> fieldList = taskExportImportModel.getFieldMappings();
			fieldMappingService.saveList(fieldList);
		}
		else
		{
			
			licenseParam.setDbType(taskExportImportModel.getDbSource().getDsType());
			licenseParam.setTaskType(taskExportImportModel.getTask().getTaskType());
			licenseParam.setOperation("add");
			checkHandler.checkDB(licenseParam);
			Task task = taskExportImportModel.getTask();
			task.setTaskState("0");
			task.setFinishCount(0);
			task.setSuccessCount(0);
			task.setFailCount(0);
			taskDao.save(task);
			
			DbSource dbSource = taskExportImportModel.getDbSource();
			if(dbSource != null)
			{
				dbSourceDao.save(dbSource);
			}
			
			TaskOutput taskOutput = taskExportImportModel.getTaskOutput();
			taskOutputDao.save(taskOutput);
			
			//保存计划
			List<SchedulePlan> schedulePlanList=taskExportImportModel.getSchedulePlans();
			if(schedulePlanList != null && schedulePlanList.size()>0)
			{
				this.schedulePlanService.saveList(schedulePlanList);
			}
			
			List<FileContentMapping> fileContentMappingList=taskExportImportModel.getFileContentMappings();
			if(fileContentMappingList != null && fileContentMappingList.size()>0)
			{
				this.fileContentMappingService.saveList(fileContentMappingList);
			}
			
			//保存字段映射
			List<FieldMapping> fieldList = taskExportImportModel.getFieldMappings();
			fieldMappingService.saveList(fieldList);
		}
		
	}
	
	
	/**
	 * 复制db任务
	 * @author lihf
	 * @date 2016年10月17日	下午2:18:47
	 * @param taskExportImportModel
	 * @throws Exception
	 */
	@Override
	public void copyTask(TaskExportImportModel taskExportImportModel) throws Exception
	{
		if (null == taskExportImportModel)
		{
			throw new Exception("参数不能为空！");
		}
		LicenseParam licenseParam = new LicenseParam();
		licenseParam.setDbType(taskExportImportModel.getDbSource().getDsType());
		licenseParam.setTaskType(taskExportImportModel.getTask().getTaskType());
		licenseParam.setOperation("add");
		checkHandler.checkDB(licenseParam);
		
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

		String dbSourceId = UUIDCreater.getUUID();
		DbSource dbSource = new DbSource();
		DbSource dbSourceExport = taskExportImportModel.getDbSource();
		BeanUtils.copyProperties(dbSourceExport, dbSource);
		dbSource.setDsId(dbSourceId);
		dbSource.setTaskId(taskId);
		dbSourceDao.save(dbSource);

		String taskOutputId = UUIDCreater.getUUID();
		TaskOutput taskOutput = new TaskOutput();
		TaskOutput taskOutputExport = taskExportImportModel.getTaskOutput();
		BeanUtils.copyProperties(taskOutputExport, taskOutput);
		taskOutput.setTopId(taskOutputId);
		taskOutput.setTaskId(taskId);
		taskOutputDao.save(taskOutput);

		// 保存计划
		List<SchedulePlan> schedulePlanList = taskExportImportModel.getSchedulePlans();
		if (schedulePlanList != null && schedulePlanList.size() > 0)
		{
			this.schedulePlanService.saveList(schedulePlanList);
		}

		List<FileContentMapping> fileContentMappingList = taskExportImportModel.getFileContentMappings();
		if (fileContentMappingList != null && fileContentMappingList.size() > 0)
		{
			this.fileContentMappingService.saveList(fileContentMappingList);
		}

		// 保存字段映射
		List<FieldMapping> fieldList = new ArrayList<FieldMapping>();
		List<FieldMapping> fieldListExport = taskExportImportModel.getFieldMappings();
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
