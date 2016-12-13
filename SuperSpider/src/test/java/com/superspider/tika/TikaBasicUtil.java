package com.superspider.tika;

import java.io.File;
import java.util.Map;

import cn.com.infcn.superspider.utils.TikaParserUtil;

/**
 * @author WChao
 * @date 2016年1月17日
 */
public class TikaBasicUtil {
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Map<String,Object> metaData = TikaParserUtil.parse(new File("C:/Users/yangc/Desktop/MSP V3.1开发计划.xlsx"), 1024*1024*10*10);
		System.out.println(metaData);
        System.out.println(System.currentTimeMillis()-start);
	}
}
