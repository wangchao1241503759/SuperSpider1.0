/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年11月4日
 */
package cn.com.infcn.superspider.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import org.nutz.http.Http;
import org.nutz.http.HttpException;

import com.alibaba.fastjson.JSONObject;

/**
 * @author lihf
 * @date 2016年11月4日
 */
public class HttpConnctionApi
{
	private String ip;
	private int port;

	public HttpConnctionApi(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	/**
	 * 接口post请求
	 * @author lihf
	 * @date 2016年11月4日	下午2:07:05
	 * @param className
	 * @param methodName
	 * @param timeout
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("static-access")
    public String post(String className, String methodName, int timeout, Map<String, Object> params) throws Exception {
		String result="";
		String encoding = "UTF-8";
		String url = "http://" + ip + ":" + port + "/api/" + className + "/" + methodName.toString();
		HttpURLConnection connection = null;
		URL targetUrl = new URL(url);
		connection = (HttpURLConnection) targetUrl.openConnection();
		if (connection == null)
		{
			return result;
		}
		connection.setDoInput(true);
		connection.setDoOutput(true);
        //获得输出流，向服务器写入数据
        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(getPostParams(params).getBytes());
        outputStream.close();
        
		connection.setConnectTimeout(timeout);
		connection.setReadTimeout(timeout);
//		connection.setRequestMethod("POST");
		connection.connect();
		
		InputStream inputStream = null;
		if(connection.getResponseCode()!=connection.HTTP_OK)
		{
			inputStream = connection.getErrorStream();
			String error = MyInputStreamReader.readByEncode(inputStream, encoding);
			/**
			 * ResponseCode=502时，是mss建索引失败了，但数据传输是正常成功的，信息从错误流中取，不需要抛出异常！
			 */
			if(connection.getResponseCode()!=connection.HTTP_BAD_GATEWAY )
			{
				JSONObject jsonResult = JSONObject.parseObject(error);
				String message = jsonResult.getString("message");
				throw new Exception(message);
			}
			result = error;
		}
		else
		{
			inputStream = connection.getInputStream();
			result = MyInputStreamReader.read(inputStream, encoding);
		}
		return result;
		
		
	}
	
    /**
     * 获取请求地址
     * @author lihf
     * @date 2016年11月4日	下午2:06:51
     * @param url
     * @param params
     * @return
     */
    public URL getUrl(String url,Map<String,Object> params) {
    	URL cacheUrl = null;
        StringBuilder sb = new StringBuilder(url);
        try {
            if (null != params && params.size() > 0) {
                sb.append(url.indexOf('?') > 0 ? '&' : '?');
                sb.append(getURLEncodedParams(params));
            }
            cacheUrl = new URL(sb.toString());
            return cacheUrl;
        }
        catch (Exception e) {
            throw new HttpException(sb.toString(), e);
        }
    }
    
    public String getPostParams(Map<String,Object> params) {
    	StringBuilder sb = new StringBuilder();
    	try {
    		if (null != params && params.size() > 0) {
    			sb.append(getURLEncodedParams(params));
    		}
    		return sb.toString();
    	}
    	catch (Exception e) {
    		throw new HttpException(sb.toString(), e);
    	}
    }
    
    /**
     * 把参数连接成url地址
     * @author lihf
     * @date 2016年11月4日	下午2:06:15
     * @param params
     * @return
     */
    public String getURLEncodedParams(Map<String,Object> params) {
        StringBuilder sb = new StringBuilder();
        for (Iterator<String> it = params.keySet().iterator(); it.hasNext();) {
            String key = it.next();
            sb.append(Http.encode(key)).append('=').append(Http.encode(params.get(key)));
            if (it.hasNext())
                sb.append('&');
        }
        return sb.toString();
    }
}
