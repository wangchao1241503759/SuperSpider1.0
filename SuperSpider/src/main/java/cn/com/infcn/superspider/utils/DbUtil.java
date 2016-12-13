/**
 * 
 */
package cn.com.infcn.superspider.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.common.DbFieldEnum;
import cn.com.infcn.superspider.model.FieldMapping;
import cn.com.infcn.superspider.model.Trigger;
import cn.com.infcn.superspider.pagemodel.DbConfig;

import com.justme.superspider.plugin.db.adapter.db2.Db2Adapter;
import com.justme.superspider.plugin.db.adapter.mysql.MysqlAdapter;
import com.justme.superspider.plugin.db.adapter.oracle.OracleAdapter;
import com.justme.superspider.plugin.db.adapter.sqlserver.SqlServerAdapter;
import com.justme.superspider.task.Task;
import com.justme.superspider.xml.Target;

/**
 * @description 
 * @author WChao
 * @date   2016年1月15日 	上午11:30:52
 */
public class DbUtil {
	
	public static String toTargetFieldType(String sourceType,String targetType,String fieldType)
	{
		String result = "";
		if(sourceType != null && !"".equals(fieldType) && targetType != null && !"".equals(fieldType) && fieldType != null && !"".equals(fieldType))
		{
			fieldType = fieldType.toLowerCase().trim();
			if(sourceType.equals(targetType))
			return fieldType;
			for(DbFieldEnum field : DbFieldEnum.values())
			{
				if(field.getType().equals(sourceType)){
					if(fieldType.toLowerCase().equals("blob") || fieldType.toLowerCase().endsWith("blob") || fieldType.toLowerCase().equals(Constant.SQLSERVER_BLOB)){
						result = getTypeBlob(targetType);
						break;
					}
					if(field.getValue().equals(fieldType) || fieldType.contains(field.getValue()))
					{
						result = field.getMappingTypes().get(targetType);
						break;
					}
				}
			}
		}
		if(result == null || "".equals(result))
		{
			if(Constant.ORACLE.equals(targetType)){
				result=Constant.ORACLE_VARCHAR2;
			}else if(Constant.MYSQL.equals(targetType))
			{
				result=Constant.MYSQL_VARCHAR;
			}else if(Constant.SQLSERVER.equals(targetType))
			{
				result=Constant.SQLSERVER_VARCHAR;
			}else if(Constant.DB2.equals(targetType))
			{
				result=Constant.DB2_VARCHAR;
			}else if(Constant.MONGO.equals(targetType)){
				result=Constant.MONGO_VARCHAR;
			}
			if(!Constant.MONGO.equals(targetType))
			result+="("+Constant.DEFAULT_VARCHAR_LENGTH+")";
		}
		return result.toLowerCase();
	}
	public static Map<String,String> getTypes(String type)
	{
		if(type == null || "".equals(type))
			return null;
		Map<String,String> types = new HashMap<String, String>();
		for(DbFieldEnum field : DbFieldEnum.values())
		{
			if(field.getType().equals(type)){
				String fieldValue = field.getValue();
				if(fieldValue.contains("varchar"))
				{
					fieldValue += "("+Constant.DEFAULT_VARCHAR_LENGTH+")";
				}
				types.put(field.getDesc(),fieldValue);
			}
		}
		return types;
	}
	
	public static String getTypeText(String type)
	{
		String result = "";
		if(Constant.ORACLE.equals(type)){
			result=Constant.ORACLE_TEXT;
		}else if(Constant.MYSQL.equals(type))
		{
			result=Constant.MYSQL_TEXT;
		}else if(Constant.SQLSERVER.equals(type))
		{
			result=Constant.SQLSERVER_TEXT;
		}else if(Constant.DB2.equals(type))
		{
			result=Constant.DB2_TEXT;
		}
		return result;
	}
	
	public static String getTypeBlob(String targetType){
		String result = "";
		if(result == null || "".equals(result))
		{
			if(Constant.ORACLE.equals(targetType)){
				result=Constant.ORACLE_BLOB;
			}else if(Constant.MYSQL.equals(targetType))
			{
				result=Constant.MYSQL_BLOB;
			}else if(Constant.SQLSERVER.equals(targetType))
			{
				result=Constant.SQLSERVER_BLOB;
			}else if(Constant.DB2.equals(targetType))
			{
				result=Constant.DB2_BLOB;
			}else if(Constant.MONGO.equals(targetType)){
				result=Constant.MONGO_VARCHAR;
			}
		}
		return result;
	}
	public static String[] getTrigger(List<FieldMapping> fieldList,DbConfig dbConfig,Connection conn)
	{
		String dsType = dbConfig.getDsType();
		if(Constant.MYSQL .equals(dsType))
		{
			return mysqlTrigger(fieldList, dbConfig);
		}	
		else if(Constant.ORACLE .equals(dsType))
		{
//			return oracleTrigger(fieldList, dbConfig);
			return getTriggerByDbType(fieldList, dbConfig,conn);
		}	
		else if(Constant.SQLSERVER .equals(dsType))
		{
//			return oracleTrigger(fieldList, dbConfig);
			return getTriggerByDbType(fieldList, dbConfig,conn);
		}
		else if(Constant.DB2.equalsIgnoreCase(dsType))
		{
			return getTriggerByDbType(fieldList, dbConfig,conn);
		}

		return null;
	}
	
	public static String[] mysqlTrigger(List<FieldMapping> fieldList,DbConfig dbConfig)
	{
		String[] triggerArry = new String[9];
		String triggerName = dbConfig.getTriggerName();
		String dsTable = dbConfig.getDsTableName();
		String triggerTableName = dbConfig.getTriggerTableName();
		String primaryField = dbConfig.getPrimaryField();
		StringBuffer modelSql = new StringBuffer("INSERT INTO "+triggerTableName);
		StringBuffer fieldStr = new StringBuffer("(");
		StringBuffer fieldValueStr = new StringBuffer(" VALUES(");
		for(FieldMapping fieldMapping : fieldList)
		{
			fieldStr.append("`"+fieldMapping.getFieldName()+"`,");
			fieldValueStr.append("NEW."+fieldMapping.getFieldName()+",");
		}
		if(fieldStr.toString().endsWith(","))
		{
			modelSql.append(fieldStr+"trigger_status)");
			modelSql.append(fieldValueStr);
		}
		String insertSql = modelSql.toString()+("'insert');");
		String updateSql = modelSql.toString()+("'update');");
		String deleteSql = modelSql.toString().replaceAll("NEW.","OLD.")+"'delete');";
		String deleteRowTempSql = "delete from "+triggerTableName+" where "+primaryField+"=old."+primaryField+";";
		String dropInsertTriggerSql = "drop trigger IF EXISTS "+triggerName+"_insert;";
		String dropUpdateTriggerSql ="drop trigger IF EXISTS "+triggerName+"_update;";
		String dropDelteTriggerSql = "drop trigger IF EXISTS "+triggerName+"_delete;";
		String dropTableSql = "DROP TABLE IF EXISTS "+triggerTableName;
		String createTableSql = "CREATE TABLE "+triggerTableName+" AS SELECT * FROM "+dbConfig.getDsTableName()+" WHERE 1=2";
		String alterTableSql = "ALTER TABLE "+triggerTableName+" ADD COLUMN trigger_status varchar(10);";
		modelSql.append(";");
		String insertedTriggerScript =
				"CREATE  TRIGGER "+triggerName+"_insert AFTER INSERT "
					+ "ON "+dsTable+" FOR EACH ROW \n"
					+ "BEGIN\n"
					+ insertSql+"\n"
					+"END";
		String updatedTriggerScript =
				"CREATE  TRIGGER "+triggerName+"_update AFTER UPDATE "
					+ "ON "+dsTable+" FOR EACH ROW \n"
					+ "BEGIN\n"
					+ deleteRowTempSql+"\n"
					+ updateSql+"\n"
					+"END";
		String deletedTriggerScript =
				"CREATE  TRIGGER "+triggerName+"_delete AFTER DELETE "
					+ "ON "+dsTable+" FOR EACH ROW \n"
					+ "BEGIN\n"
					+ deleteRowTempSql+"\n"
					+ deleteSql+"\n"
					+"END";
		triggerArry[0] = dropTableSql;
		triggerArry[1] = createTableSql;
		triggerArry[2] = alterTableSql;
		triggerArry[3] = dropInsertTriggerSql;
		triggerArry[4] = dropUpdateTriggerSql;
		triggerArry[5] = dropDelteTriggerSql;
		triggerArry[6] = insertedTriggerScript;
		triggerArry[7] = updatedTriggerScript;
		triggerArry[8] = deletedTriggerScript;
		return triggerArry;
	}
	
	/**
	 * 获取oralce触发器
	 * @author lihf
	 * @date 2016年4月15日	下午6:07:49
	 * @param fieldList
	 * @param dbConfig
	 * @return
	 */
	public static String[] oracleTrigger(List<FieldMapping> fieldList,DbConfig dbConfig,Connection conn)
	{
		String[] triggerArry = new String[9];
		String triggerName = dbConfig.getTriggerName();
		String dsTable = dbConfig.getDsTableName();
		String triggerTableName = dbConfig.getTriggerTableName();
		String primaryField = dbConfig.getPrimaryField();
		StringBuffer modelSql = new StringBuffer("INSERT INTO "+triggerTableName);
		StringBuffer fieldStr = new StringBuffer("(");
		StringBuffer fieldValueStr = new StringBuffer(" VALUES(");
		for(FieldMapping fieldMapping : fieldList)
		{
			fieldStr.append(""+fieldMapping.getFieldName()+",");
			fieldValueStr.append(":NEW."+fieldMapping.getFieldName()+",");
		}
		if(fieldStr.toString().endsWith(","))
		{
			modelSql.append(fieldStr+"trigger_status)");
			modelSql.append(fieldValueStr);
		}
		String insertSql = modelSql.toString()+("'insert');");
		String updateSql = modelSql.toString()+("'update');");
		String deleteSql = modelSql.toString().replaceAll(":NEW.",":OLD.")+"'delete');";
		String deleteRowTempSql = "delete from \""+triggerTableName+"\" where "+primaryField+"=:old."+primaryField+";";
		String dropInsertTriggerSql = getDeleteTriggerByDbType(Constant.ORACLE, triggerName+"_inserte",conn);//"drop trigger IF EXISTS "+triggerName+"_inserted;";
		String dropUpdateTriggerSql =getDeleteTriggerByDbType(Constant.ORACLE, triggerName+"_update",conn);//"drop trigger IF EXISTS "+triggerName+"_updated;";
		String dropDelteTriggerSql = getDeleteTriggerByDbType(Constant.ORACLE, triggerName+"_delete",conn);//"drop trigger IF EXISTS "+triggerName+"_deleted;";
		String dropTableSql = getDeleteTriggerTableByDbType(Constant.ORACLE,triggerTableName,conn);//"DROP TABLE IF EXISTS "+triggerTableName;
		String createTableSql = "CREATE TABLE "+triggerTableName+" AS SELECT * FROM \""+dbConfig.getDsTableName()+"\" WHERE 1=2";
		String alterTableSql = "ALTER TABLE "+triggerTableName+" ADD ( trigger_status varchar(10));";
		modelSql.append(";");
		String insertedTriggerScript =
				"CREATE  OR REPLACE  TRIGGER "+triggerName+"_INSERTE AFTER INSERT "
						+ "ON "+dsTable+" FOR EACH ROW \n"
						+ "BEGIN\n"
						+ insertSql+"\n"
						+"END";
		String updatedTriggerScript =
				"CREATE  OR REPLACE   TRIGGER "+triggerName+"_UPDATE AFTER UPDATE "
						+ "ON "+dsTable+" FOR EACH ROW \n"
						+ "BEGIN\n"
						+ deleteRowTempSql+"\n"
						+ updateSql+"\n"
						+"END";
		String deletedTriggerScript =
				"CREATE  OR REPLACE  TRIGGER "+triggerName+"_DELETE AFTER DELETE "
						+ "ON "+dsTable+" FOR EACH ROW \n"
						+ "BEGIN\n"
						+ deleteRowTempSql+"\n"
						+ deleteSql+"\n"
						+"END";
		triggerArry[0] = dropTableSql;
		triggerArry[1] = createTableSql;
		triggerArry[2] = alterTableSql;
		triggerArry[3] = dropInsertTriggerSql;
		triggerArry[4] = dropUpdateTriggerSql;
		triggerArry[5] = dropDelteTriggerSql;
		triggerArry[6] = insertedTriggerScript;
		triggerArry[7] = updatedTriggerScript;
		triggerArry[8] = deletedTriggerScript;
		return triggerArry;
	}
	
	
	/**
	 * 获取oralce触发器
	 * @author lihf
	 * @date 2016年4月15日	下午6:07:49
	 * @param fieldList
	 * @param dbConfig
	 * @return
	 */
	public static String[] getTriggerByDbType(List<FieldMapping> fieldList,DbConfig dbConfig,Connection conn)
	{
		String[] triggerArry = new String[9];
		
		String triggerName = dbConfig.getTriggerName();
		String dsTable = dbConfig.getDsTableName();
		String triggerTableName = dbConfig.getTriggerTableName();
		String dbType = dbConfig.getDsType();
		
		String dropTableSql = getDeleteTriggerTableByDbType(dbType,triggerTableName,conn);
		String createTableSql = createTableSql(dbType,triggerTableName,dsTable);
		String alterTableSql = alterTableSql(dbType,triggerTableName);
		String dropInsertTriggerSql = getDeleteTriggerByDbType(dbType, triggerName+"_"+Constant.TRIGGER_TYPE_INSERT,conn);
		String dropUpdateTriggerSql = getDeleteTriggerByDbType(dbType, triggerName+"_"+Constant.TRIGGER_TYPE_UPDATE,conn);
		String dropDelteTriggerSql = getDeleteTriggerByDbType(dbType, triggerName+"_"+Constant.TRIGGER_TYPE_DELETE,conn);;
		String insertedTriggerScript = getCreateTriggerScript(Constant.TRIGGER_TYPE_INSERT,fieldList,dbConfig);
		String updatedTriggerScript = getCreateTriggerScript(Constant.TRIGGER_TYPE_UPDATE,fieldList,dbConfig);;
		String deletedTriggerScript = getCreateTriggerScript(Constant.TRIGGER_TYPE_DELETE,fieldList,dbConfig);;
		triggerArry[0] = dropTableSql;
		triggerArry[1] = createTableSql;
		triggerArry[2] = alterTableSql;
		triggerArry[3] = dropInsertTriggerSql;
		triggerArry[4] = dropUpdateTriggerSql;
		triggerArry[5] = dropDelteTriggerSql;
		triggerArry[6] = insertedTriggerScript;
		triggerArry[7] = updatedTriggerScript;
		triggerArry[8] = deletedTriggerScript;
		return triggerArry;
	}
	
	
	/**
	 * 根据不同的触类型创建触发器
	 * @author lihf
	 * @date 2016年4月15日	下午6:36:44
	 * @param triggerType
	 * @param triggerName
	 * @return
	 */
	public static  String getCreateTriggerScript(String triggerType,List<FieldMapping> fieldList,DbConfig dbConfig)
	{
		String sql = "";
		String triggerSQL = "";
		String triggerName = dbConfig.getTriggerName()+"_"+triggerType;
		String triggerTableName = dbConfig.getTriggerTableName();
		String dsTableName = dbConfig.getDsTableName();
		
		String trigger_status = "trigger_status";
		
		StringBuffer fieldSB= new StringBuffer();  //表字段
		StringBuffer fieldValueSB = new StringBuffer(); //值 字段
		
		for(FieldMapping fieldMapping : fieldList)
		{
			fieldSB.append(fieldMapping.getFieldName()).append(",");
			fieldValueSB.append(":new."+fieldMapping.getFieldName()).append(",");
		}
		String fieldStr = fieldSB.toString();
		String fieldValueStr =fieldValueSB.toString();
		String fieldValueStrold =fieldValueSB.toString();
		
		StringBuffer triggerSQL_sb = new StringBuffer();
		if(Constant.MYSQL.equalsIgnoreCase(dbConfig.getDsType()))
		{
			if(Constant.TRIGGER_TYPE_INSERT.equalsIgnoreCase(triggerType))
			{
				//如果是insert的触发器，需要执行的SQL语句
			}
			else if(Constant.TRIGGER_TYPE_UPDATE.equalsIgnoreCase(triggerType))
			{
				//如果是update的触发器，需要执行的SQL语句
			}
			else if(Constant.TRIGGER_TYPE_DELETE.equalsIgnoreCase(triggerType))
			{
				//如果是delete的触发器，需要执行的SQL语句
			}
		}
		else if (Constant.ORACLE.equalsIgnoreCase(dbConfig.getDsType()))
        {
			
			triggerName = triggerName.toUpperCase();
			triggerTableName = triggerTableName.toUpperCase();
			dsTableName = ("\""+dsTableName+"\"");
			String deleteRowTempSql = "delete from \""+triggerTableName+"\" where "+dbConfig.getPrimaryField().toUpperCase()+"=:old."+dbConfig.getPrimaryField().toUpperCase()+";";
			if(Constant.TRIGGER_TYPE_UPDATE.equalsIgnoreCase(triggerType))
			{
				//如果是update的触发器，需要执行的SQL语句
				triggerSQL_sb.append(deleteRowTempSql);
			}
			else if(Constant.TRIGGER_TYPE_DELETE.equalsIgnoreCase(triggerType))
			{
				//如果是delete的触发器，需要执行的SQL语句
				triggerSQL_sb.append(deleteRowTempSql);
				fieldValueStr = fieldValueStr.toString().replaceAll(":new", ":old").replaceAll(":NEW", ":old");
			}
			triggerSQL_sb.append(" INSERT INTO \"").append(triggerTableName).append("\"(").append(fieldStr).append(trigger_status.toUpperCase())
			.append(") VALUES (").append(fieldValueStr).append("'").append(triggerType).append("');");
//			triggerSQL_sb.append(" INSERT INTO \"").append(triggerTableName).append("\"(").append(fieldStr).append(trigger_status.toUpperCase())
//			.append(") VALUES (").append(fieldValueStr).append("'").append(triggerType).append("');");
			
//			triggerSQL_sb.append(" INSERT INT.O \"AA_TEMP\"(ID , AREA, SCHOOLNAME,trigger_status ) select t.*,'insert' from \"TEST\" t where t.ID=:new.ID");
			triggerSQL = triggerSQL_sb.toString();
        }
		else if(Constant.SQLSERVER.equalsIgnoreCase(dbConfig.getDsType()))
		{
			fieldValueStr = fieldValueStr.toString().replaceAll(":new.", "@new").replaceAll(":NEW.", "@new");
			fieldValueStrold = fieldValueStrold.toString().replaceAll(":new.", "@old").replaceAll(":NEW.", "@old");
			StringBuffer declareNewSB = new StringBuffer();
			StringBuffer declareOldSB = new StringBuffer();
			for(FieldMapping fieldMapping : fieldList)
			{
				declareNewSB.append("declare ").append("@new").append(fieldMapping.getFieldName()).append(" ").append(fieldMapping.getFieldType().indexOf("int")!=-1?"int":fieldMapping.getFieldType()).append("; ");
				declareNewSB.append(" select ").append("@new").append(fieldMapping.getFieldName()).append(" = ").append(fieldMapping.getFieldName()).append(" from inserted ; ");
				declareOldSB.append("declare ").append("@old").append(fieldMapping.getFieldName()).append(" ").append(fieldMapping.getFieldType().indexOf("int")!=-1?"int":fieldMapping.getFieldType()).append("; ");
				declareOldSB.append(" select ").append("@old").append(fieldMapping.getFieldName()).append(" = ").append(fieldMapping.getFieldName()).append(" from deleted ; "); 
			}
			triggerSQL_sb.append(declareNewSB).append(declareOldSB);
			String deleteRowTempSql = "delete from "+triggerTableName+" where "+dbConfig.getPrimaryField()+"=@old"+dbConfig.getPrimaryField()+"; ";
			if(Constant.TRIGGER_TYPE_INSERT.equalsIgnoreCase(triggerType))
			{
				//如果是insert的触发器，需要执行的SQL语句
				
				triggerSQL_sb.append(" INSERT INTO ").append(triggerTableName).append("(").append(fieldStr).append(trigger_status)
				.append(") VALUES (").append(fieldValueStr).append("'").append(triggerType).append("');");
			}
			else if(Constant.TRIGGER_TYPE_UPDATE.equalsIgnoreCase(triggerType))
			{
				//如果是update的触发器，需要执行的SQL语句
//				 delete from a_temp where id = @oldid;
//				   INSERT INTO a_temp(id,name,trigger_status)
//				       VALUES (@newid,@newname,'update');
				triggerSQL_sb.append(deleteRowTempSql);
				triggerSQL_sb.append(" INSERT INTO ").append(triggerTableName).append("(").append(fieldStr).append(trigger_status)
				.append(") VALUES (").append(fieldValueStr).append("'").append(triggerType).append("');");
			}
			else if(Constant.TRIGGER_TYPE_DELETE.equalsIgnoreCase(triggerType))
			{
				//如果是delete的触发器，需要执行的SQL语句
//				update a_temp set trigger_status='delete' where id=@oldid;
//				triggerSQL_sb.append(" update ").append(triggerTableName).append(" set trigger_status = ").append("'delete'")
//				.append(" where ").append(dbConfig.getPrimaryField()).append(" = ").append("@old").append(dbConfig.getPrimaryField()).append(";");
				
				//先删除临时表中的数据，再插入删除的数据信息
				triggerSQL_sb.append(deleteRowTempSql);
				triggerSQL_sb.append(" INSERT INTO ").append(triggerTableName).append("(").append(fieldStr).append(trigger_status)
				.append(") VALUES (").append(fieldValueStrold).append("'").append(triggerType).append("');");
			}
			triggerSQL = triggerSQL_sb.toString();
		}
		else if(Constant.DB2.equalsIgnoreCase(dbConfig.getDsType()))
		{
			
			if(Constant.TRIGGER_TYPE_INSERT.equalsIgnoreCase(triggerType))
			{
				StringBuffer db2fieldSB= new StringBuffer();  //表字段
				StringBuffer db2fieldValueSB = new StringBuffer(); //值 字段
				
				for(FieldMapping fieldMapping : fieldList)
				{
					db2fieldSB.append("\"").append(fieldMapping.getFieldName()).append("\"").append(",");
					db2fieldValueSB.append("N.").append("\"").append(fieldMapping.getFieldName()).append("\"").append(",");
				}
				String db2fieldStr = db2fieldSB.toString();
				String db2fieldValueStr =db2fieldValueSB.toString();
				//如果是insert的触发器，需要执行的SQL语句
//				INSERT INTO "b_temp"("id","name","trigger_status") VALUES (N."id",N."name",'insert');
				triggerSQL_sb.append("INSERT INTO \"").append(triggerTableName).append("\"").append("(").append(db2fieldStr).append("\"").append(trigger_status).append("\"")
				.append(") VALUES (").append(db2fieldValueStr).append("'insert'").append(")");
			}
			else if(Constant.TRIGGER_TYPE_UPDATE.equalsIgnoreCase(triggerType))
			{
//				//如果是update的触发器，需要执行的SQL语句
////				UPDATE "b_temp" set "id"=N."id","name"=N."name","trigger_status"='update' where "id"=O."id";
//				StringBuffer db2fieldUpdateSB= new StringBuffer();  //表字段
//				
//				for(FieldMapping fieldMapping : fieldList)
//				{
//					db2fieldUpdateSB.append("\"").append(fieldMapping.getFieldName()).append("\"").append("=").append("N.").append("\"").append(fieldMapping.getFieldName()).append("\"").append(",");
//				}
//				String db2fieldUpdateStr = db2fieldUpdateSB.toString();
//				triggerSQL_sb.append(" UPDATE ").append("\"").append(triggerTableName).append("\"").append(" set ").append(db2fieldUpdateStr).append("\"").append("trigger_status").append("\"").append("=")
//				.append("'update' ").append(" where ").append("\"").append(dbConfig.getPrimaryField()).append("\"").append("=").append("O.").append("\"").append(dbConfig.getPrimaryField()).append("\"");
				
				StringBuffer db2fieldSB= new StringBuffer();  //表字段
				StringBuffer db2fieldValueSB = new StringBuffer(); //值 字段
				
				for(FieldMapping fieldMapping : fieldList)
				{
					db2fieldSB.append("\"").append(fieldMapping.getFieldName()).append("\"").append(",");
					db2fieldValueSB.append("N.").append("\"").append(fieldMapping.getFieldName()).append("\"").append(",");
				}
				String db2fieldStr = db2fieldSB.toString();
				String db2fieldValueStr =db2fieldValueSB.toString();
				triggerSQL_sb.append("INSERT INTO \"").append(triggerTableName).append("\"").append("(").append(db2fieldStr).append("\"").append(trigger_status).append("\"")
				.append(") VALUES (").append(db2fieldValueStr).append("'update'").append(")");
			}
			else if(Constant.TRIGGER_TYPE_DELETE.equalsIgnoreCase(triggerType))
			{
//				//如果是delete的触发器，需要执行的SQL语句
////				update "b_temp" set "trigger_status"='delete' where "id"=O."id";
//				triggerSQL_sb.append(" UPDATE ").append("\"").append(triggerTableName).append("\"").append(" set ").append("\"").append("trigger_status").append("\"")
//				.append("='delete' ").append(" where ").append("\"").append(dbConfig.getPrimaryField()).append("\"").append("=").append("O.").append("\"").append(dbConfig.getPrimaryField()).append("\"");


				StringBuffer db2fieldSB= new StringBuffer();  //表字段
				StringBuffer db2fieldValueSB = new StringBuffer(); //值 字段
				
				for(FieldMapping fieldMapping : fieldList)
				{
					db2fieldSB.append("\"").append(fieldMapping.getFieldName()).append("\"").append(",");
					db2fieldValueSB.append("O.").append("\"").append(fieldMapping.getFieldName()).append("\"").append(",");
				}
				String db2fieldStr = db2fieldSB.toString();
				String db2fieldValueStr =db2fieldValueSB.toString();
				triggerSQL_sb.append("INSERT INTO \"").append(triggerTableName).append("\"").append("(").append(db2fieldStr).append("\"").append(trigger_status).append("\"")
				.append(") VALUES (").append(db2fieldValueStr).append("'delete'").append(")");
			}
			triggerSQL = triggerSQL_sb.toString();
		}

		//创建触发器的结构框架
		StringBuffer sb = new StringBuffer();
		if(Constant.SQLSERVER.equalsIgnoreCase(dbConfig.getDsType()))
		{
			sb.append("CREATE TRIGGER ").append(triggerName).append(" ON ").append(dsTableName).append(" AFTER ").append(triggerType).append(" as ")
			.append(" BEGIN ")
			.append(triggerSQL)
			.append(" END; ");
		}
		else if(Constant.DB2.equalsIgnoreCase(dbConfig.getDsType()))
		{
			//CREATE TRIGGER "a_insert" AFTER INSERT ON "a" REFERENCING NEW AS N FOR EACH ROW MODE DB2SQL 
			//INSERT INTO "b_temp"("id","name","trigger_status") VALUES (N."id",N."name",'insert');
			sb.append("CREATE TRIGGER ").append("\"").append(triggerName).append("\"").append(" AFTER ").append(triggerType).append(" ON ").append("\"").append(dsTableName).append("\"");
//			.append(" REFERENCING NEW AS N FOR EACH ROW MODE DB2SQL ");
			
			if(Constant.TRIGGER_TYPE_INSERT.equalsIgnoreCase(triggerType))
			{
				//如果是insert的触发器，需要执行的SQL语句
				sb.append(" REFERENCING NEW AS N FOR EACH ROW MODE DB2SQL ");
			}
			else if(Constant.TRIGGER_TYPE_UPDATE.equalsIgnoreCase(triggerType))
			{
				//如果是update的触发器，需要执行的SQL语句
				sb.append(" REFERENCING NEW AS N  OLD AS O FOR EACH ROW MODE DB2SQL ");
			}
			else if(Constant.TRIGGER_TYPE_DELETE.equalsIgnoreCase(triggerType))
			{
				//如果是delete的触发器，需要执行的SQL语句
				sb.append(" REFERENCING OLD AS O FOR EACH ROW MODE DB2SQL ");
			}
			sb.append(triggerSQL);
		}
		else
		{
			sb.append("CREATE OR REPLACE TRIGGER \"").append(triggerName).append("\" AFTER ").append(triggerType)
			.append(" ON ").append(dsTableName)
			.append(" FOR EACH ROW ")
			.append(" BEGIN ")
			.append(triggerSQL)
			.append(" END; ");
		}
		sql = sb.toString();
		return sql;
	}
	/**
	 * 删除源表增量数据;
	 * @param task
	 */
	public static void deleteIncrementData(Task task){
		//增量标识;
		String increment = task.site.getOption(Constant.INCREMENT) == null ? "" : task.site.getOption(Constant.INCREMENT).toString();
		if(increment != "1")
			return;
		try {
			if(task.site.getFetcher().getClient() instanceof MysqlAdapter){
				MysqlAdapter mysqlAdapter = (MysqlAdapter)task.site.getFetcher().getClient();
				executeIncrementData(mysqlAdapter.conn,task);
			}else if(task.site.getFetcher().getClient() instanceof OracleAdapter){
				OracleAdapter oracleAdapter = (OracleAdapter)task.site.getFetcher().getClient();
				executeIncrementData(oracleAdapter.conn,task);
			}else if(task.site.getFetcher().getClient() instanceof SqlServerAdapter){
				SqlServerAdapter sqlServerAdapter = (SqlServerAdapter)task.site.getFetcher().getClient();
				executeIncrementData(sqlServerAdapter.conn,task);
			}else if(task.site.getFetcher().getClient() instanceof Db2Adapter){
				Db2Adapter db2Adapter = (Db2Adapter)task.site.getFetcher().getClient();
				executeIncrementData(db2Adapter.conn, task);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void executeIncrementData(Connection conn,Task task){
		//目标源数据库模式
		String schema = task.site.getOption(Constant.SCHEMA) == null || "".equals(task.site.getOption(Constant.SCHEMA))? "" : task.site.getOption(Constant.SCHEMA).toString()+".";
		Target target = task.site.getTargets().getTarget().get(0);
		int pageSize = Integer.parseInt(task.site.getOption(Constant.FETCH_SIZE).toString());
		Object dbType = task.site.getOption(Constant.TYPE);
		String deleteSql = getDeleteSqlByPageSize(dbType.toString(),pageSize,schema,target.getName());
		try {
			PreparedStatement stmt = conn.prepareStatement(deleteSql);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 根据数据包大小获取不同数据库删除语句;
	 * @param dbType
	 * @param pageSize
	 */
	public static String getDeleteSqlByPageSize(String dbType,int pageSize,String schema,String tableName){
		String sql = "";
		if (Constant.MYSQL.equals(dbType)) {
			sql = "delete FROM "+schema+tableName +" limit "+pageSize;
		}else if (Constant.SQLSERVER.equals(dbType)) {
			sql= "delete top ("+pageSize+") FROM "+schema+tableName;
		}else if (Constant.ORACLE.equals(dbType)) {
			sql="delete FROM \""+schema+tableName.toUpperCase() +"\" t WHERE ROWNUM <="+pageSize+" AND ROWNUM >= 0";
		}else if (Constant.DB2.equals(dbType)) {
			sql="delete FROM (select * from \""+schema+tableName+"\" fetch first "+pageSize+" rows only)";
		}
		return sql;
	}
	
	/**
	 * 获取删除触发器的Sql语句
	 * @author lihf
	 * @date 2016年4月15日	下午5:00:25
	 * @param trigger
	 * @param dbConfig
	 * @param isDelete   是否强制删除，是：true,否:false
	 * @return
	 */
	public static String[] getDeleteTriggerSql(Trigger trigger,DbConfig dbConfig,boolean isDelete,Connection conn)
	{
		String[] oldTriggerArry = null;
		String oldTriggerName = trigger.getTriggerName() == null || "".equals(trigger.getTriggerName()) ? "notrigger" : trigger.getTriggerName();
		String triggerTableName = trigger.getTriggerTableName() == null || "".equals(trigger.getTriggerTableName()) ? "notriggername" : trigger.getTriggerTableName();
		if(!oldTriggerName.equals(dbConfig.getTriggerName()) || isDelete){
			oldTriggerArry = new String[4];
			String dropInsertTriggerSql = getDeleteTriggerByDbType(dbConfig.getDsType(),oldTriggerName+"_insert",conn);
			String dropUpdateTriggerSql = getDeleteTriggerByDbType(dbConfig.getDsType(),oldTriggerName+"_update",conn);
			String dropDelteTriggerSql =  getDeleteTriggerByDbType(dbConfig.getDsType(),oldTriggerName+"_delete",conn);
			String dropTableSql = getDeleteTriggerTableByDbType(dbConfig.getDsType(),triggerTableName,conn);
			oldTriggerArry[0] = dropInsertTriggerSql;
			oldTriggerArry[1] = dropUpdateTriggerSql;
			oldTriggerArry[2] = dropDelteTriggerSql;
			oldTriggerArry[3] = dropTableSql;
		}else if(!triggerTableName.equals(dbConfig.getTriggerTableName()) || isDelete){
			oldTriggerArry = new String[1];
			String dropTableSql = getDeleteTriggerTableByDbType(dbConfig.getDsType(),triggerTableName,conn);
			oldTriggerArry[0] = dropTableSql;
		}
		return oldTriggerArry;
	}
	
	/**
	 * 根据数据类型生成删除触发器的SQL
	 * @author lihf
	 * @date 2016年4月15日	下午5:22:56
	 * @param dbType
	 * @return
	 */
	public static String getDeleteTriggerByDbType(String dbType,String triggerName,Connection conn)
	{
		String sql = "";
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try
        {
	        StringBuffer sb = new StringBuffer();
	        if (Constant.MYSQL.equalsIgnoreCase(dbType))
	        {
		        sql = "drop trigger IF EXISTS " + triggerName + ";";
	        }
	        else if (Constant.ORACLE.equalsIgnoreCase(dbType))
	        {
		        triggerName = triggerName.toUpperCase();
		        /*declare   
		         V_NUM number;   
		        BEGIN  
		           ----多次删除时，每次都将v_num设置成为0
		            V_NUM := 0;  
		            ----判断触发器  TABLE_NAME 是否存在（区分大小写）
		            select count(0) into V_NUM from user_triggers where trigger_name = 'TEST_INSERTED'; 
		            ----如果存在立即删除  
		            if V_NUM > 0 then   
		            execute immediate 'DROP TRIGGER  TEST_INSERTED';   
		            end if;
		        END;*/
		        sb.append(" declare ").append(" V_NUM number; ").append(" BEGIN ").append(" V_NUM := 0; ").append(" select count(0) into V_NUM from user_triggers where trigger_name = '")
		                .append(triggerName).append("'; ").append(" if V_NUM > 0 then ").append("execute immediate 'DROP TRIGGER  ").append(triggerName).append("';").append(" end if; ")
		                .append(" END; ");
		        sql = sb.toString();
	        }
	        else if (Constant.SQLSERVER.equalsIgnoreCase(dbType))
	        {
		        sb.append("if Exists(select name from sysobjects where name='").append(triggerName).append("' and xtype='TR')").append(" drop trigger ").append(triggerName);
		        sql = sb.toString();
	        }
	        else if (Constant.DB2.equalsIgnoreCase(dbType))
	        {
		        String countSQL = "select count(1) from SYSCAT.TRIGGERS where trigname = '" + triggerName + "'";
		        psmt = conn.prepareStatement(countSQL);
		        rs = psmt.executeQuery();
		        int count = 0;
		        if(rs.next())
		        {
		        	count = rs.getInt(1);
		        }
		        if(count>0)
		        {
		        	sb.append(" drop trigger \"").append(triggerName).append("\"");
		        }
		        sql = sb.toString();
	        }
        }
        catch (Exception e)
        {
	        // TODO: handle exception
        }
		finally
		{
			try
            {
	            if (null != rs)
	            {
		            rs.close();
	            }
	            if (null != psmt)
	            {
	            	psmt.close();
	            }
            }
            catch (Exception e2)
            {
	            // TODO: handle exception
            }
		}
		return sql;
	}
	
	/**
	 * 根据数据类型生成删除触发器临时表的SQL
	 * @author lihf
	 * @date 2016年4月15日	下午5:37:05
	 * @param dbType
	 * @param tableaName
	 * @return
	 */
	public static String getDeleteTriggerTableByDbType(String dbType,String tableaName,Connection conn)
	{
		String sql = "";
		PreparedStatement psmt = null;
		ResultSet rs = null;
		try
        {
	        StringBuffer sb = new StringBuffer();
	        if (Constant.MYSQL.equalsIgnoreCase(dbType))
	        {
		        sql = "drop table IF EXISTS " + tableaName;
	        }
	        else if (Constant.ORACLE.equalsIgnoreCase(dbType))
	        {
		        tableaName = tableaName.toUpperCase();
		        sb.append(" declare ").append(" V_NUM number; ").append(" BEGIN ").append(" V_NUM := 0; ").append(" select count(0) into V_NUM from user_tables where table_name = '")
		                .append(tableaName).append("'; ").append(" if V_NUM > 0 then ").append("execute immediate 'DROP TABLE  ").append(tableaName).append("';").append(" end if; ").append(" END; ");
		        sql = sb.toString();
	        }
	        else if (Constant.SQLSERVER.equalsIgnoreCase(dbType))
	        {
		        //			IF EXISTS (SELECT *  FROM   sysobjects   WHERE   name = 'a_temp' AND xtype = 'U')  drop table a_temp; 
		        sb.append("IF EXISTS (SELECT *  FROM   sysobjects   WHERE   name = '").append(tableaName).append("' AND xtype = 'U')").append(" drop table ").append(tableaName);
		        sql = sb.toString();
	        }
	        else if (Constant.DB2.equalsIgnoreCase(dbType))
	        {
		        String countSQL = " select count(1) from syscat.tables where tabname='"+tableaName+"'";
		        psmt = conn.prepareStatement(countSQL);
		        rs = psmt.executeQuery();
		        int count = 0;
		        if(rs.next())
		        {
		        	count = rs.getInt(1);
		        }
		        if(count>0)
		        {
		        	sb.append(" drop table \"").append(tableaName).append("\"");
		        }
		        sql = sb.toString();
	        }
        }
        catch (Exception e)
        {
	        try
            {
	            if (null != rs)
	            {
		            rs.close();
	            }
	            if (null != psmt)
	            {
	            	psmt.close();
	            }
            }
            catch (Exception e2)
            {
	            // TODO: handle exception
            }
        }
		return sql;
	}
	
	/**
	 * 创建临时表的语句
	 * @author lihf
	 * @date 2016年4月18日	下午5:31:42
	 * @param dbType
	 * @param triggerTableName
	 * @param tableName
	 * @return
	 */
	public static String createTableSql(String dbType,String triggerTableName,String tableName)
	{
		String sql  = "";
		if(Constant.MYSQL.equalsIgnoreCase(dbType))
		{
			sql = "CREATE TABLE "+triggerTableName+" AS SELECT * FROM "+tableName+" WHERE 1=2 ";
		}
		else if(Constant.ORACLE.equalsIgnoreCase(dbType))
		{
			triggerTableName = triggerTableName.toUpperCase();
			sql = "CREATE TABLE "+triggerTableName+" AS SELECT * FROM \""+tableName+"\" WHERE 1=2 ";
		}
		else if(Constant.SQLSERVER.equalsIgnoreCase(dbType))
		{
			sql = " select  *  into  "+ triggerTableName +"  from  "+ tableName +" WHERE 1=2 ";
		}
		else if(Constant.DB2.equalsIgnoreCase(dbType))
		{
			sql = "create table  \""+ triggerTableName +"\"  like \""+ tableName +"\"";
		}
		return sql;
	}
	
	/**
	 * 临时表增加状态字段
	 * @author lihf
	 * @date 2016年4月18日	下午5:35:49
	 * @param dbType
	 * @param triggerTableName
	 * @return
	 */
	public static String alterTableSql(String dbType,String triggerTableName)
	{
		String sql  = "";
		if(Constant.MYSQL.equalsIgnoreCase(dbType))
		{
			sql = "ALTER TABLE "+triggerTableName+" ADD COLUMN trigger_status varchar(10)";
		}
		else if(Constant.ORACLE.equalsIgnoreCase(dbType))
		{
			triggerTableName = triggerTableName.toUpperCase();
			sql = "ALTER TABLE \""+triggerTableName+"\" ADD ( TRIGGER_STATUS varchar(10))";
		}
		else if(Constant.SQLSERVER.equalsIgnoreCase(dbType))
		{
			sql = " alter table "+ triggerTableName +" add trigger_status varchar(10);";
		}
		else if(Constant.DB2.equalsIgnoreCase(dbType))
		{
			sql = "alter table \""+triggerTableName+"\" add \"trigger_status\" varchar(10)";
		}
		return sql;
		
	}
	
	public static boolean isSpecialColumnTypeChar(String typeName){
		if("".equals(typeName) || typeName == null)
			return false;
		if(typeName.equalsIgnoreCase(Constant.MYSQL_VARCHAR) || typeName.equalsIgnoreCase("char") || typeName.equalsIgnoreCase("varchar2"))
		 return true;
		
		return false;
	}
	
	public static void main(String[] args) {
		System.out.println(DbUtil.toTargetFieldType("mysql","oracle","LONG TEXT"));
	}
}
