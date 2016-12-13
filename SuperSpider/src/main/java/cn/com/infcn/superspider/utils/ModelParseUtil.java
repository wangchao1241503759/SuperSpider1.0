/**
 * 
 */
package cn.com.infcn.superspider.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import oracle.sql.CLOB;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import cn.com.infcn.superspider.common.Constant;
import cn.com.infcn.superspider.io.output.DbModel;
import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputAdapter;
import cn.com.infcn.superspider.io.output.Record;
import cn.com.infcn.superspider.listener.OutputDispatcher;
import cn.com.infcn.superspider.listener.WebSpiderListener;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.task.Task;
import com.justme.superspider.util.FileUtil;
import com.justme.superspider.xml.Field;
import com.justme.superspider.xml.Site;
import com.justme.superspider.xml.Target;

/**
 * @author WChao
 *
 */
public class ModelParseUtil {

	private static Logger logger = Logger.getLogger(ModelParseUtil.class);
	/**
	 * 获取Ftp/File本地文件路径;
	 * @param task
	 * @return
	 */
	public static File getLocalFile(Task task){
		String localPath = task.site.getOption(Constant.LOCAL_PATH).toString();
		String filePath = localPath+task.url;
		File file = new File(filePath);
		return file;
	}
	/**
	 * Ftp业务数据处理函数;
	 * @param outputDispatcher
	 * @param models
	 * @return
	 */
	public static JSONArray toFtpJsonData(Task task,OutputDispatcher outputDispatcher,List<Map<String, Object>> models)
	{
		JSONArray jsonArray = new JSONArray();
		File file = getLocalFile(task);
		if(file.isFile())//是文件才进行文件提取;
		{
			try {
				JSONObject jsonObject = new JSONObject();
				parseFileContentFeildMappingtoJsonObject(outputDispatcher, jsonObject, file);
				if(jsonObject.size() > 0)
				jsonArray.add(jsonObject);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e.getMessage(),e);
			}
		}
		return jsonArray;
	}
	/**
	 * File业务数据处理函数;
	 * @param outputDispatcher
	 * @param models
	 * @return
	 */
	public static JSONArray toFileJsonData(Task task,List<Map<String, Object>> models)
	{
		if(models == null || models.isEmpty())
			return null;
		JSONArray jsonArray = new JSONArray();
		Iterator<Map<String,Object>> it = models.iterator();
		while(it.hasNext()){
			JSONObject jsonObject = new JSONObject();
			Map<String,Object> dataMap = it.next();
			jsonObject.putAll(dataMap);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	/**
	 * Tika提取文件信息;
	 * @param outputDispatcher
	 * @param jsonObject
	 * @param file
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject parseFileContentFeildMappingtoJsonObject(OutputDispatcher outputDispatcher,JSONObject jsonObject,File file)
	{
		try{
			JSONObject blobFields = null;//提取字段映射关系;
			for(OutputAdapter outputAdapter : outputDispatcher.outputs)
			{
				if(outputAdapter.dbModel == null)
					continue;
				blobFields = outputAdapter.dbModel.getTableStruct().getJSONObject(Constant.BLOB_FIELDS);
			}
			Map<String,Object> resultData = TikaParserUtil.tikaMeta(file);
			if(resultData == null)
				return jsonObject;
			Object metaContent = resultData.get(Constant.CONTENT);//文件内容;
			Map<String,Object> metaData = (Map<String, Object>)resultData.get(Constant.METADATA);//文件元数据信息;
			for(String attributeField : blobFields.keySet())
			{
				String fieldName = blobFields.getString(attributeField);
				if(attributeField.contains(","))
				{
					String[] attributeArry = attributeField.split(",");
					for(String attribute :attributeArry)
					{
						if(Constant.CONTENT.equals(attribute))//全文内容
						{
							jsonObject.put(fieldName, metaContent);
							continue;
						}
						if(Constant.FILE_SITE.equals(attribute)){//文件大小
							jsonObject.put(fieldName, file.length());
							continue;
						}
						Object meta =  metaData.get(attribute);
						if(meta!=null){
							jsonObject.put(fieldName,meta);
							continue;
						}
					}
					if(!jsonObject.containsKey(fieldName))
					jsonObject.put(fieldName,null);
				}else{
					if(Constant.CONTENT.equals(attributeField))//文件内容
					{
						jsonObject.put(fieldName, metaContent);
						continue;
					}
					if(Constant.FILE_SITE.equals(attributeField)){//文件大小
						jsonObject.put(fieldName, file.length());
						continue;
					}
					jsonObject.put(fieldName,metaData.get(attributeField));
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return jsonObject;
	}
	/**
	 * Db业务数据处理函数;
	 * @param outputDispatcher
	 * @param models
	 * @return
	 */
	public static JSONArray toDbJsonData(OutputDispatcher outputDispatcher,List<Map<String, Object>> models)
	{
		JSONArray jsonArray = new JSONArray();
		for(Map<String,Object> model : models)
		{
			Map<String,Object> filedContent = new HashMap<String, Object>();//存放非数组的字段;
			for(String fieldName : model.keySet())//遍历所有的filed获取数据;
			{
				Object data = model.get(fieldName);
				filedContent.put(fieldName, data);
			}
			JSONObject jsonObject = new JSONObject();
			parseAllCotentFiledtoJsonObject(outputDispatcher,filedContent, jsonObject);//遍历所有的非数组字段
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	/**
	 * Web业务数据处理函数;
	 * @param outputDispatcher
	 * @param models
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONArray toWebJsonData(Target target,List<Map<String, Object>> models)
	{
		JSONArray jsonArray = new JSONArray();
		for(Map<String,Object> model : models)
		{
			int maxNum = 0;
			Map<String,ArrayList<String>> maxArray = new HashMap<String, ArrayList<String>>();//存放数量最多的字段数组;
			Map<String,ArrayList<String>> fieldArray = new HashMap<String, ArrayList<String>>();//存放是数组的字段;
			Map<String,Object> filedContent = new HashMap<String, Object>();//存放非数组的字段;
			for(String fieldName : model.keySet())//遍历所有的filed获取数据;
			{
				if("$target".equals(fieldName))//去除内置对象$target
					continue;
				Object data = model.get(fieldName);
				if(data instanceof ArrayList){//数组的数据
					fieldArray.put(fieldName,(ArrayList<String>)data);
				}else{//非数组的数据;
					filedContent.put(fieldName, data);
				}
			}
			if(fieldArray.size() > 0)//存在数组字段的话比较出数据最多的数组并取出它待遍历;
			{
				for(String arrayFieldName : fieldArray.keySet())
				{
					ArrayList<String> datas = (ArrayList<String>)fieldArray.get(arrayFieldName);
					if(datas.size() > maxNum)
					{
						maxNum = datas.size();
						maxArray.clear();
						maxArray.put(arrayFieldName, datas);
					}
				}
			}
			if(maxArray.size() > 0)//遍历数据最多的数组;
			{
				for(String maxFieldName : maxArray.keySet())
				{
					fieldArray.remove(maxFieldName);
					ArrayList<String> datas = maxArray.get(maxFieldName);
					for(int i = 0 ; i<datas.size();i++)
					{
						JSONObject jsonObject = new JSONObject();
						jsonObject.put(maxFieldName, datas.get(i));//最大数组字段;
						for(String arrayFieldName : fieldArray.keySet())//遍历所有的数组字段;
						{
							ArrayList<String> arrayDatas = fieldArray.get(arrayFieldName);
							if(i < arrayDatas.size()){
								jsonObject.put(arrayFieldName, arrayDatas.get(i));
							}else{
								jsonObject.put(arrayFieldName,"");
							}
						}
						if(!filedContent.isEmpty()){
							for(String contentFieldName : filedContent.keySet())//遍历所有的非数组字段;
							{
								jsonObject.put(contentFieldName,filedContent.get(contentFieldName));
							}
						}
						jsonArray.add(jsonObject);
					}
				}
			}else{//不存在数组字段或是数组字段数据为空的话;
				JSONObject jsonObject = new JSONObject();
				if(!fieldArray.isEmpty())
				{
					for(String arrayFieldName : fieldArray.keySet())//遍历所有的数组字段;
					{
						jsonObject.put(arrayFieldName,"");
					}
				}
				if(!filedContent.isEmpty()){
					for(String contentFieldName : filedContent.keySet())//遍历所有的非数组字段;
					{
						jsonObject.put(contentFieldName,filedContent.get(contentFieldName));
					}
				}
				jsonArray.add(jsonObject);
			}
		}
		return jsonArray;
	}
	
	public static String getAppendixField(Site site,Target target){
		String appendix_fields = (String) site.getOption(Constant.APPENDIX_FIELDS);
		if (appendix_fields == null || "".equals(appendix_fields)|| StringUtil.isEmpty(appendix_fields))
			return null;
		JSONObject jsonObject = JSONObject.parseObject(appendix_fields);
		for (String key : jsonObject.keySet())
		{
			if (target.getId().equals(key))
			{
				return jsonObject.getString(key);
			}
		}
		return null;
	}
	/**
	 * 下载并重置Web附件路径;
	 * @param site
	 * @param target
	 * @param result
	 * @return
	 */
	public static JSONArray resetAndDownloadAppendixFile(Site site,WebSpiderListener listener,Target target,JSONArray result){
		if(result.isEmpty() && !site.isStop)
			return result;
		String localDir = site.getOption(Constant.LOCAL_PATH).toString();
		if(!localDir.endsWith(File.separator))
			localDir += File.separator;
		Calendar cal = Calendar.getInstance();
		String appendixField = getAppendixField(site, target);
		localDir += site.getName() + File.separator + cal.get(Calendar.YEAR)+File.separator+(cal.get(Calendar.MONTH) + 1)+File.separator+cal.get(Calendar.DATE)+File.separator;
		for(Object jsonObj : result){
			if(site.isStop)
				 break;
			JSONObject dataObj = (JSONObject)jsonObj;
			String appendixValue = dataObj.getString(appendixField);
			if(appendixValue != null && !"".equals(appendixValue)){
				//String fileName = appendixValue.substring(appendixValue.lastIndexOf("/")+1);
				Future<File> fileFuture = null;
		    	 ExecutorService executorService = Executors.newSingleThreadExecutor();
				try {
					URL url = new URL(appendixValue);
			        FileUtil.createDir(localDir);
			        Downloader downloader = new Downloader(site);
			        downloader.setUrlAndFile(url, new File(localDir));
			        fileFuture = executorService.submit(downloader);
			        File downloadFile = fileFuture.get();
			        dataObj.put(appendixField, downloadFile.getAbsolutePath());
			        String downInfo = "download " + downloadFile.getAbsolutePath() +  " from " + url + " @" + downloader.getAverageSpeed() + "KB/s";
			        listener.onInfo(Thread.currentThread(),null, downInfo);
				} catch (Exception e) {
					String errorInfo = e.getMessage();
					listener.onError(Thread.currentThread(),null,errorInfo, e);
				}finally{
					if(fileFuture != null)
						fileFuture.cancel(true);
					executorService.shutdownNow();
				}
			}
		}
		return result;
	}
	/**
	 * 判断数据中是否都为空的
	 * @author lihf
	 * @date 2016年6月21日	下午2:59:11
	 * @param target
	 * @param jsonArray
	 * @return
	 */
	public static JSONArray checkEmptyData(Target target,JSONArray jsonArray)
	{
		List<Field> fieldList = target.getModel().getField();

		for (int i = 0; i < jsonArray.size(); i++)
		{
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			boolean flag = false;
			for(Field field:fieldList)
			{
				String value = jsonObject.getString(field.getName());
				//只要字段中有值，就不需要移除,都为空的，移除掉
				if(!StringUtils.isEmpty(value))
				{
					flag = true;
					break;
				}
			}
			if(!flag)
			{
				jsonArray.remove(i);
			}

		}
		return jsonArray;
	}
	/**
	 * 将所有的非数组字段转化为JSONObject
	 * @param filedContent
	 * @param jsonObject
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject parseAllCotentFiledtoJsonObject(OutputDispatcher outputDispatcher,Map<String,Object> filedContent,JSONObject jsonObject)
	{
		for(String contentFieldName : filedContent.keySet())//遍历所有的非数组字段
		{
			Object content = filedContent.get(contentFieldName);
			if(content == null)
			{
				jsonObject.put(contentFieldName, content);
				continue;
			}
			if(content instanceof Blob || content instanceof byte[])//Blob大字段处理;对其进行文件内容+元数据提取;
			{
				try {
					if(content instanceof Blob)
					{
						Blob blobContent = (Blob)content;
						content = input2byte(blobContent.getBinaryStream());
					}
					String blob_field = "";//待提取的blob大字段;
					JSONObject blobFields = null;//提取字段映射关系;
					JSONObject targetFields = null;//目标字段总映射关系;
					for(OutputAdapter outputAdapter : outputDispatcher.outputs)
					{
						if(outputAdapter.dbModel == null)
							continue;
						blob_field = outputAdapter.dbModel.getTableStruct().getString(Constant.BLOB_FIELD);
						blobFields = outputAdapter.dbModel.getTableStruct().getJSONObject(Constant.BLOB_FIELDS);
						targetFields = outputAdapter.dbModel.getTableStruct().getJSONObject(Constant.TARGET_FIELDS);
					}
					if(contentFieldName.equals(blob_field) && blobFields != null && blobFields.size() > 0)
					{
						Map<String,Object> resultData = TikaParserUtil.tikaMeta((byte[])content);
						if(resultData == null)
							continue;
						Object metaContent = resultData.get(Constant.CONTENT);//文件内容;
						Map<String,Object> metaData = (Map<String, Object>)resultData.get(Constant.METADATA);//文件元数据信息;
						for(String attributeField : blobFields.keySet())
						{
							if(attributeField.contains(","))
							{
								String[] attributeArry = attributeField.split(",");
								for(String attribute :attributeArry)
								{
									if(Constant.CONTENT.equals(attribute))
									{
										jsonObject.put(attributeField, metaContent);
										continue;
									}
									Object meta =  metaData.get(attribute);
									if(meta!=null){
										jsonObject.put(attributeField,meta);
										continue;
									}
								}
								if(!jsonObject.containsKey(attributeField))
								jsonObject.put(attributeField,null);
							}else{
								if(Constant.CONTENT.equals(attributeField))
								{
									jsonObject.put(attributeField, metaContent);
									continue;
								}
								jsonObject.put(attributeField,metaData.get(attributeField));
							}
						}
						if(!targetFields.containsKey(contentFieldName))//如果字段映射中勾选同步blob字段才同步,否则只做提取,不同步该字段;
						continue;
					}
					
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				}
			}else if(content instanceof CLOB)//Clob大字段处理;
			{
				CLOB clobConent = (CLOB)content;
				try {
					content = ClobToString(clobConent);
				} catch (Exception e) {
					logger.error(e.getMessage(),e);
				} 
			}else if(content instanceof InputStream){//TRS中的BIT型字段转化为InputStream读取;
				InputStream is = (InputStream)content;
				try {
					content = input2byte(is);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			jsonObject.put(contentFieldName, content);
		}
		return jsonObject;
	}
	
	public static List<Doc> toDocs(OutputDispatcher outputDispatcher,Target target,JSONArray jsonArray)
	{
		List<Doc> list = new ArrayList<Doc>();
		if(!jsonArray.isEmpty())
		{
			if(outputDispatcher.outputs != null)
			{
				for(OutputAdapter output : outputDispatcher.outputs)
				{
					DbModel dbModel = output.dbModel;
					Doc doc = null;
					JSONArray result = null;
					
					for(int i = 0 ; i<jsonArray.size() ; i++)
					{
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						if(i == 0 || (i%Constant.THREAD_NUM) == 0)
						{
							doc = new Record();
							result = new JSONArray();
							doc.setResult(result);
							String tableName = dbModel.getTablename() == null || "".equals(dbModel.getTablename()) ? target.getName() : dbModel.getTablename();
							JSONObject ctx = new JSONObject();
							ctx.put(Constant.TABLE_NAME,"spider_"+tableName);
							doc.setCtx(ctx);
							list.add(doc);
						}
						result.add(jsonObj);
					}
				}
			}
		}
		return list;
	}
	
	public static Doc toDoc(OutputDispatcher outputDispatcher,JSONArray jsonArray,Target target)
	{
		Doc doc = null;
		if(outputDispatcher.outputs != null)
		{
			for(OutputAdapter outputAdapter : outputDispatcher.outputs)
			{
				if(outputAdapter.dbModel == null)
					continue;
				JSONObject sourceFields = outputAdapter.dbModel.getTableStruct().getJSONObject(Constant.SOURCE_FIELDS);
				if(sourceFields == null || sourceFields.size() <= 0)
					continue;
				if(jsonArray.isEmpty())
					continue;
				for(Object object : jsonArray){
					JSONObject row = (JSONObject)object;
					for(String key : new ArrayList<String>(row.keySet()))
					{
						String sourceKey = sourceFields.getString(key);
						if(sourceKey == null)
						 row.remove(key);
						/*if(sourceKey != null)
						row.put(sourceKey, row.remove(key));*/
					}
				}
			}
		}
		if(jsonArray != null && !jsonArray.isEmpty())
		{
			JSONArray result = new JSONArray();
			result.addAll(jsonArray);
			doc = new Record();
			String docId = outputDispatcher.site.getId();
			docId = target != null ? docId +"_"+target.getId() : "";
			doc.setId(docId);
			doc.setResult(result);
		}
		return doc;
	}
	
	/*public static Doc toSpiderDoc(OutputDispatcher outputDispatcher,JSONArray jsonArray,Target target , com.justme.superspider.task.Task task , WebLevelUrls level_urls)
	{
		Doc doc = null;
		if(outputDispatcher.outputs != null)
		{
			for(OutputAdapter outputAdapter : outputDispatcher.outputs)
			{
				if(outputAdapter.dbModel == null)
					continue;
				JSONObject sourceFields = outputAdapter.dbModel.getTableStruct().getJSONObject(Constant.SOURCE_FIELDS);
				if(sourceFields == null || sourceFields.size() <= 0)
					continue;
				if(jsonArray.isEmpty())
					continue;
			}
		}
		if(!jsonArray.isEmpty())
		{
			String root_url = level_urls.getRootMatchUrl(target.getId(), task);
			for(Object obj : jsonArray){
				JSONObject jsonObj = (JSONObject)obj;
				jsonObj.put(Constant.ROOT_URL, root_url);
			}
			doc = new Record();
			String docId = outputDispatcher.site.getId();
			docId = target != null ? docId +"_"+target.getId() : "";
			doc.setId(docId);
			doc.setResult(jsonArray);
		}
		return doc;
	}*/
	/**
	 * 将字节转换为对象
	 * @param bytes
	 * @return
	 */
    public static Object ByteToObject(byte[] bytes) {
		Object obj = null;
		try {
			// bytearray to object
			ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
			ObjectInputStream oi = new ObjectInputStream(bi);
	
			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
        return obj;
    }
    /**
     * 将对像转换为字节
     * @param obj
     * @return
     */
	public static byte[] ObjectToByte(java.lang.Object obj) {
		byte[] bytes = null;
		try {
			// object to bytearray
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream oo = new ObjectOutputStream(bo);
			oo.writeObject(obj);
			bytes = bo.toByteArray();

			bo.close();
			oo.close();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return bytes;
	}
	
	public static byte[] blob2ByteArr(Blob blob) throws Exception {
	    System.out.println(blob.length());
        byte[] b = null;
        try {
            if (blob != null) {
                long in = 0;
                int blobLength = (int)blob.length();
                InputStream ss = blob.getBinaryStream();
                System.out.println(ss);
                b = blob.getBytes(in,blobLength);
            }
        } catch (Exception e) {
        	logger.error(e.getMessage(),e);
            throw new Exception("fault");
        }
 
        return b;
    }

	public static final InputStream byte2Input(byte[] buf) {
		return new ByteArrayInputStream(buf);
	}

	public static final byte[] input2byte(InputStream inStream)throws IOException {
		ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
		byte[] buff = new byte[100];
		int rc = 0;
		while ((rc = inStream.read(buff, 0, 100)) > 0) {
			swapStream.write(buff, 0, rc);
		}
		byte[] in2b = swapStream.toByteArray();
		return in2b;
	}

	/**
	 * 
	 * @param clob
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	public static String ClobToString(CLOB clob) throws SQLException, IOException {

        String reString = "";

        Reader is = clob.getCharacterStream();// 得到流

        BufferedReader br = new BufferedReader(is);

        String s = br.readLine();

        StringBuffer sb = new StringBuffer();

        while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer由StringBuffer转成STRING

            sb.append(s);

            s = br.readLine();

        }
       reString = sb.toString();
       return reString;

    }
	
	public static void main(String[] args) {
		JSONObject testJsonObject = new JSONObject();
		testJsonObject.put("ni", 2);
		testJsonObject.put("wo",null);
		System.out.println(testJsonObject);
		String ss = "http://sdfsdf/sdfsdf/234234.jpg";
		System.out.println(ss.substring(ss.lastIndexOf("/")+1));
		Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);       //日
        int month = cal.get(Calendar.MONTH) + 1;//月
        int year = cal.get(Calendar.YEAR);      //年
        System.out.println(day+"===="+month+"======="+year);
		/*Doc doc = null;
		JSONArray result = null;
		String str = "{\"ABSTRACT\":\"该文从挂篮荷载计算、施工流程、支座及临时固结施工、挂篮安装及试验、合拢段施工、模板制作安装、钢筋安装、混凝土的浇筑及养生、测量监控等方面人手,介绍了S226海滨大桥主桥连续预应力混凝土变截面箱梁2#～7#块、8#、8\",\"AUTHORS\":\"陈一飞%袁达贵%邵劭%瞿洪海\",\"KEYWORDS\":\"连续箱梁%挂篮%设计%施工\",\"LONGTIME\":\"1453376277581\",\"NEWFIELD32\":\"王超页数:731-734作者:陈一飞%袁达贵%邵劭%瞿洪海\",\"PAGEINFO\":\"731-734\",\"TITLE\":\"S226海滨大桥连续箱梁挂篮悬浇施工\"}";
		String str2 = "{\"ABSTRACT\":\"\",\"AUTHORS\":\"李永火%汪松能\",\"KEYWORDS\":\"\",\"LONGTIME\":\"1453376277581\",\"NEWFIELD32\":\"王超页数:39-40作者:李永火%汪松能\",\"PAGEINFO\":\"39-40\",\"TITLE\":\"茶叶博览园\"}";
		for(int i = 0 ;i<100;i++){
			JSONObject obj = JSONObject.parseObject(str);
			JSONObject obj2 = JSONObject.parseObject(str2);
			obj.put(i+"",i+"===");
			if(i == 0 || (i%10) == 0)
			{
				doc = new Record();
				result = new JSONArray();
				doc.setResult(result);
				doc.getCtx().put(Constant.TABLE_NAME,"==="+i);
			}
			result.add(obj);
			result.add(obj2);
		}
		System.out.println(ModelParseUtil.ObjectToByte(doc));*/
	}
}
