package cn.com.infcn.superspider.io.output.adapter.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputAdapter;

import com.alibaba.fastjson.JSONObject;


public class fileOutputAdapter extends OutputAdapter{

	private OutputStream os = null;

	private byte[] LINE = "\n".getBytes();
	
	@SuppressWarnings("unused")
	private String charEncoding = "utf-8";
	private boolean append = false ;


	@Override
	public void init(JSONObject obj){
	
	}
	
	public fileOutputAdapter(JSONObject job, String jobName) {
		String path = job.getString("path");
		if (job.containsKey("charEncoding")) {
			charEncoding = job.getString("charEncoding");
		}
		
		if(job.containsKey("append")){
			append = job.getBooleanValue("append") ;
		}
		
		try {
			os = new FileOutputStream(new File(path), append);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void save(Doc doc) throws Exception {
		os.write(doc.getResult().toString().getBytes());
		os.write(LINE);
	}


	@Override
	public void shutdown(boolean isShutdownNow){
		try {
			if(os != null)
			{
				os.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
