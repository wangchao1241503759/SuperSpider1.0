package cn.com.infcn.superspider.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import cn.com.infcn.superspider.common.Constant;

/**
 * @author WChao
 * @date 2016年1月17日
 */
public class TikaParserUtil {
	private static Logger logger = Logger.getLogger(TikaParserUtil.class);
	
	private static final Integer WRITELIMIT = Integer.MAX_VALUE;//Tika所提取文件最大内存设置;
	/**
	 * 根据指定文件提取其文件内容+元数据信息;
	 * 
	 * @param f
	 * @param writeLimit
	 * @return
	 */
	public static Map<String, Object> parse(File f, int writeLimit) {
		if (f == null)
			return null;
		InputStream is = null;
		Metadata metadata = new Metadata();
		try {
			is = new FileInputStream(f);
			metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return excParse(metadata, is, writeLimit);
	}

	/**
	 * 获取元数据+文件内容信息;
	 * 
	 * @param blobField
	 * @return
	 */
	public static Map<String, Object> tikaMeta(byte[] blobField) {
		InputStream is = new ByteArrayInputStream(blobField);
		Map<String, Object> metaData = TikaParserUtil.parse(is,WRITELIMIT);
		return metaData;
	}
	/**
	 * 获取元数据+文件内容信息;
	 * 
	 * @param blobField
	 * @return
	 */
	public static Map<String, Object> tikaMeta(File file) {
		Map<String, Object> metaData = TikaParserUtil.parse(file,WRITELIMIT);
		return metaData;
	}
	/**
	 * 根据文件流提取其文件内容+元数据信息;
	 * 
	 * @param f
	 * @param writeLimit
	 * @return
	 */
	public static Map<String, Object> parse(InputStream is, int writeLimit) {
		if (is == null)
			return null;
		return excParse(new Metadata(), is, writeLimit);
	}

	/**
	 * 执行文件内容+元数据信息提取;
	 * 
	 * @param metadata
	 * @param is
	 * @param writeLimit
	 * @return
	 */
	private static Map<String, Object> excParse(Metadata metadata,
			InputStream is, Integer writeLimit) {
		if (is == null || writeLimit == null || writeLimit < 0)
			return null;
		try {
			Map<String, Object> resultMap = new HashMap<String, Object>();
			Map<String, Object> metadataMap = new HashMap<String, Object>();
			// 1、创建一个parser

			Parser parser = new AutoDetectParser();
			ContentHandler handler = new BodyContentHandler(writeLimit);
			ParseContext context = new ParseContext();
			context.set(Parser.class, parser);
			// 2、执行parser的parse()方法。
			parser.parse(is, handler, metadata, context);
			for (String name : metadata.names()) {
				metadataMap.put(name, metadata.get(name));
			}
			resultMap.put(Constant.CONTENT, handler.toString());
			logger.info(metadataMap);
			resultMap.put(Constant.METADATA, metadataMap);
			logger.info("文件元数据提取完毕...");
			return resultMap;
		} catch (Exception e) {
			logger.error("文件元数据提取失败!",e);
		} finally {
			try {
				if (is != null) {
					is.close();
					is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, SAXException, TikaException {
		// File file = new File("C:\\Users\\yangc\\Desktop\\55.docx");
		//
		// Map<String,Object> resultMap = new HashMap<String, Object>();
		// Map<String,Object> metadataMap = new HashMap<String, Object>();
		// //1、创建一个parser
		// Parser parser = new AutoDetectParser();
		// ContentHandler handler = new BodyContentHandler(1024*1024*10*10);
		// ParseContext context = new ParseContext();
		// context.set(Parser.class,parser);
		// //2、执行parser的parse()方法。
		// Metadata metadata = new Metadata() ;
		// parser.parse(new FileInputStream(new
		// File("C:\\Users\\yangc\\Desktop\\55.docx")),handler, metadata
		// ,context);
		//
		// System.out.println(metadata.names()[0]);
		// /*for(String name:metadata.names()) {
		// metadataMap.put(name, metadata.get(name));
		// }
		// resultMap.put(Constant.CONTENT,handler.toString());
		// System.out.println(metadataMap);
		// resultMap.put(Constant.METADATA, metadataMap);*/
		// System.out.println("文件元数据提取完毕...");
	}
}
