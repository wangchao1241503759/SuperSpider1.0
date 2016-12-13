package cn.com.infcn.superspider.io;

import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.justme.superspider.xml.Options;
import com.justme.superspider.xml.Outputs;

public class ConfigParse {

	public static JSONArray parse(Outputs output) {
		List<Options> outputs = output.getOptions();
		JSONArray result = new JSONArray();
		if(outputs != null && outputs.size()>0)
		{
			for(Options options : outputs)
			{
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("type", options.getType());
				StringBuffer sb = new StringBuffer("{");
				for(int i = 0 ; i<options.getOption().size() ; i++)
				{
					sb.append("\""+options.getOption().get(i).getName()+"\":\""+options.getOption().get(i).getValue()+"\"");
					if(i < options.getOption().size()-1)
					{
						sb.append(",");
					}
				}
				sb.append("}");
				jsonObject.put("obj", JSONObject.parseObject(sb.toString()));
				result.add(jsonObject);
			}
		}
		return result;
	}
}
