package cn.com.infcn.superspider.io.output.adapter.console;

import org.apache.log4j.Logger;

import cn.com.infcn.superspider.io.output.Doc;
import cn.com.infcn.superspider.io.output.OutputAdapter;

import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.xml.Site;


public class consoleOutputAdapter extends OutputAdapter{
	
	Logger logger = Logger.getLogger(consoleOutputAdapter.class);
	
	public consoleOutputAdapter(JSONObject job, Site site) {
		this.site = site;
	}

	@Override
	public void init(JSONObject obj){
		
	}
	@Override
	public void save(Doc doc) throws Exception {
		logger.info(doc.getResult());
	}

	@Override
	public void shutdown(boolean isShutdownNow){

	}

}