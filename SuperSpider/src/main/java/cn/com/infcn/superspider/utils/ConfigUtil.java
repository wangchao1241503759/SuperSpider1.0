package cn.com.infcn.superspider.utils;

import java.util.ResourceBundle;

/**
 * 项目参数工具类
 * 
 * @author infcn
 * 
 */
public class ConfigUtil {

	private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("application");


	/**
	 * 通过键获取值
	 * 
	 * @param key
	 * @return
	 */
	public static final String get(String key) {
		return bundle.getString(key);
	}

}
