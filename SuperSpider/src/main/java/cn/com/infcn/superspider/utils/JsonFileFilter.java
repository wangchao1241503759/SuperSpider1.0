/**
 * Copyright(c) 2016 Beijing INFCN Software Co.,Ltd.
 * @date：2016年10月27日
 */
package cn.com.infcn.superspider.utils;

import java.io.File;
import java.io.FileFilter;

/**
 * @author lihf
 * @date 2016年10月27日
 */
public class JsonFileFilter implements FileFilter
{
	 public static final String JSON_SUFFIX = ".json";
	  
	@Override
	public boolean accept(File pathname) {
		// TODO Auto-generated method stub
		boolean flag = false;
		if(pathname.isFile()){
			String name = pathname.getName();
			if((name.endsWith(JSON_SUFFIX)))
			{
				flag = true;
			}
		}
		return flag;
	}
}
