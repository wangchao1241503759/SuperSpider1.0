package cn.com.infcn.superspider.common;

import java.math.BigDecimal;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;

@SuppressWarnings("deprecation")
public class MongoDao {

	private static Logger LOG = Logger.getLogger(MongoDao.class);

	public static enum SORT {
		DESC, ASC
	};

	private MongoClient mongoClient;

	private String dbName;

	public MongoDao(String host, int port, String dbName)
			throws UnknownHostException {

		this.dbName = dbName;

		ServerAddress serverAddress = new ServerAddress(host, port);

		mongoClient = new MongoClient(serverAddress, getConfOptions());
	}

	private MongoClientOptions getConfOptions() {
		return new MongoClientOptions.Builder().socketKeepAlive(true) // 是否保持长链接
				.connectTimeout(5000) // 链接超时时间
				.socketTimeout(5000) // read数据超时时间
				.readPreference(ReadPreference.primary()) // 最近优先策略
				.connectionsPerHost(30) // 每个地址最大请求数
				.maxWaitTime(1000 * 60 * 2) // 长链接的最大等待时间
				.threadsAllowedToBlockForConnectionMultiplier(50) // 一个socket最大的等待请求数
				.writeConcern(WriteConcern.NORMAL).build();
	}

	/**
	 * 获得ｄｂ的名字
	 * 
	 * @param dbName
	 * @return
	 */
	public DB getDB(String dbName) {
		return this.mongoClient.getDB(dbName);
	}

	/**
	 * 获得单独一个数据库
	 * 
	 * @param prefix
	 * @param time
	 * @return
	 */
	public DB getDB(String prefix, long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return getDB(prefix + sdf.format(time));
	}

	/**
	 * 批量获得命中数据库
	 * 
	 * @param prefix
	 * @param start
	 * @param end
	 * @return
	 */
	public List<DB> getDBLists(String prefix, long start, long end) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		List<DB> dbs = new ArrayList<DB>();
		for (long i = start; i < end; i += 24 * 3600 * 1000L) {
			dbs.add(getDB(prefix + sdf.format(i)));
		}
		return dbs;
	}

	public void save(String tableName, Object obj) {
		this.save(tableName, (JSONObject) JSON.toJSON(obj));
	}

	public void insert(String tableName, Object obj) {
		this.insert(tableName, (JSONObject) JSON.toJSON(obj));
	}

	public void saveBatch(String tableName, List<DBObject> list) {
		fixJsonType(list);
		mongoClient.getDB(dbName).getCollection(tableName).insert(list);
	}

	public long count(String tableName) {
		return mongoClient.getDB(dbName).getCollection(tableName).count();
	}

	public void save(String tableName, JSONObject job) {
		DBObject dbo = new BasicDBObject(job);
		this.save(tableName, dbo);
	}

	public void insert(String tableName, JSONObject job) {
		DBObject dbo = new BasicDBObject(job);
		this.insert(tableName, dbo);
	}

	public void save(String tableName, DBObject dbo) {
		mongoClient.getDB(dbName).getCollection(tableName).save(dbo);
	}

	public void insert(String tableName, DBObject dbo) {
		mongoClient.getDB(dbName).getCollection(tableName).insert(dbo);
	}

	public DBObject findById(String tableName, Object id) {
		DBObject dbo = new BasicDBObject();
		dbo.put("_id", id);
		return mongoClient.getDB(dbName).getCollection(tableName).findOne(dbo);
	}

	public boolean check(String tableName, Object id) {
		DBObject dbo = new BasicDBObject();
		dbo.put("_id", id);
		return mongoClient.getDB(dbName).getCollection(tableName)
				.findOne(dbo, dbo) != null;
	}

	public List<DBObject> findByKV(String tableName, Map<String, ?> map) {
		DBObject dbo = new BasicDBObject();
		if (map != null) {
			dbo.putAll(map);
		}
		return findByQuery(tableName, dbo);
	}

	public List<DBObject> findByQuery(String tableName, DBObject dbo) {
		DBCursor dbcursor = mongoClient.getDB(dbName).getCollection(tableName)
				.find(dbo);
		List<DBObject> list = new ArrayList<DBObject>();
		try {
			while (dbcursor.hasNext()) {
				DBObject next = dbcursor.next();
				list.add(next);
			}
		} finally {
			dbcursor.close();
		}
		return list;
	}

	public MongoClient getClient() {
		return mongoClient;
	}

	public DBCollection getCollection(String tableName) {
		return mongoClient.getDB(dbName).getCollection(tableName);
	}

	public void remove(String tableName, DBObject o) {
		mongoClient.getDB(dbName).getCollection(tableName).remove(o);
	}

	public void remove(String tableName, Map<String, ?> map) {
		DBObject o = new BasicDBObject();
		o.putAll(map);
		mongoClient.getDB(dbName).getCollection(tableName).remove(o);
	}

	public DBObject findOne(String tableName) {
		return mongoClient.getDB(dbName).getCollection(tableName).findOne();
	}

	public DBObject findOne(String tableName, Map<String, ?> map) {
		DBObject o = new BasicDBObject();
		o.putAll(map);
		return mongoClient.getDB(dbName).getCollection(tableName).findOne(o);
	}

	/**
	 * 递归修正json无法存储ｂｉｇｉｎｔｅｇｅｒ等类型
	 * 
	 * @param obj
	 */
	public static void fixJsonType(Object obj) {

		if (obj instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> job = (Map<String, Object>) obj;
			for (Entry<String, Object> entry : job.entrySet()) {
				if (entry.getValue() == null) {
					continue;
				}
				if (entry.getValue() instanceof JSONObject
						|| entry.getValue() instanceof JSONArray) {
					fixJsonType(entry.getValue());
				} else if (entry.getValue() instanceof BigDecimal) {
					try {
						if (entry.getValue() instanceof java.math.BigDecimal) {
							job.put(entry.getKey(), ((java.math.BigDecimal) job
									.get(entry.getKey())).doubleValue());
						} else if (entry.getValue() instanceof java.math.BigInteger) {
							job.put(entry.getKey(), ((java.math.BigInteger) job
									.get(entry.getKey())).longValue());
						}
					} catch (Exception e) {
						e.printStackTrace();
						LOG.error(e);
						job.put(entry.getKey(), entry.getValue().toString());
					}
				}
			}
		} else if (obj instanceof Collection) {
			Collection<?> jar = (Collection<?>) obj;
			for (Object o : jar) {
				fixJsonType(o);
			}
		}
	}
}