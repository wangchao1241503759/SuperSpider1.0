package cn.com.infcn.superspider.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.log4j.Logger;

public class MyInputStreamReader {
	static Logger LOG = Logger.getLogger(MyInputStreamReader.class.getName());
	private static final int BUFFER_SIZE = 1024;

	public static byte[] read(InputStream inputStream) {
		byte[] result=null;
		
		byte[] bytes = new byte[BUFFER_SIZE];
		int len = 0;
		ByteArrayOutputStream outputStream =null;
		try{
			outputStream = new ByteArrayOutputStream();
			
			while ((len = inputStream.read(bytes, 0, BUFFER_SIZE)) != -1) {
				outputStream.write(bytes, 0, len);
			}

			outputStream.flush();

			result=outputStream.toByteArray();
		}catch(Exception ex){
			LOG.info(ex.getMessage(),ex);
		}finally{
			if(outputStream!=null){
				try{
					outputStream.close();
				}catch(Exception ex){
					LOG.info(ex.getMessage(),ex);
				}
			}
		}
		
		return result;
	}
	public static String read(InputStream inputStream,String encoding){
		String result="";
		
		StringBuffer lines = new StringBuffer();
		try {
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, encoding);
			BufferedReader reader = new BufferedReader(inputStreamReader);

			String line = null;
			while ((line = reader.readLine()) != null) {
				lines.append(line).append("\r\n");
			}
			result = lines.toString();
		} catch (Exception ex) {
			LOG.info(ex.getMessage(), ex);
		} 

		return result;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
    public static List readGzip(InputStream inputStream,String encode) {
		byte[] result=null;
		byte[] bytes = new byte[BUFFER_SIZE];
		int len = 0;
		GZIPInputStream gzin = null;
		ByteArrayOutputStream outputStream =null;
		String html = null;
		List list = new ArrayList();
		try{
			outputStream = new ByteArrayOutputStream();
			gzin = new GZIPInputStream(inputStream);
			while ((len = gzin.read(bytes, 0, BUFFER_SIZE)) != -1) {
				outputStream.write(bytes, 0, len);
			}
			outputStream.flush();
			result=outputStream.toByteArray();
			if(result[0]==-17&&result[1]==-69&&result[2]==-65){
				encode="UTF-8";
			}else{
				encode="GBK";
			}
			html = new String(result,encode);
		}catch(Exception ex){
			ex.printStackTrace();
		}finally{
			if(outputStream!=null){
				try{
					outputStream.close();
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
		list.add(html);
		list.add(encode);
		return list;
	}
	
	public static String readByEncode(InputStream inputStream,String encoding){
		String result="";
		
		BufferedReader reader =null;
		InputStreamReader inputStreamReader =null;
		StringBuffer lines = new StringBuffer();
		try {
			inputStreamReader = new InputStreamReader(inputStream, encoding);
			reader = new BufferedReader(inputStreamReader);

			String line = null;
			while ((line = reader.readLine()) != null) {
				lines.append(line).append("\r\n");
			}
			result = lines.toString();
		} catch (Exception ex) {
			LOG.info(ex.getMessage(), ex);
		} finally{
			try{
				inputStreamReader.close();
			}catch(Exception ex){
				LOG.info(ex.getMessage(), ex);
			}
			try{
				reader.close();
			}catch(Exception ex){
				LOG.info(ex.getMessage(), ex);
			}
		}

		return result;
	}
}

