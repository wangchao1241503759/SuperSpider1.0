package cn.com.infcn.superspider.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Blob;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;


public class FileUtils {
	public static File createFile(String fileName,String filePath){
		File pathFile = new File(filePath);
		File file=new File(filePath+fileName);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	public static void writeToFile(String filePath,String content,boolean append){
		 FileWriter writer;
		try {
			writer = new FileWriter(filePath, append);
			writer.write(content);  
	        writer.close();  
		} catch (IOException e) {
			e.printStackTrace();
		}  
	}
	public static void writeToFileByStream(String filePath,String content,boolean append){
	   try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(filePath,append),"UTF-8");
            out.write(content);
            out.flush();
            out.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
	}
	public static String readeFile(String filePath){
		  File file = new File(filePath);  
	      BufferedReader reader = null;  
	      String tempString = null;  
	      String result="";
	      try {  
	            reader = new BufferedReader(new FileReader(file));  
	            int line = 1;  
	            // 一次读入一行，直到读入null为文件结束  
	            while ((tempString = reader.readLine()) != null) {  
	            	result+=tempString;
	                // 显示行号  
	                System.out.println("line " + line + ": " + tempString);  
	                line++;  
	            }  
	            reader.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (reader != null) {  
	                try {  
	                    reader.close();  
	                } catch (IOException e1) {  
	                }  
	            }  
	       }  
	      return result;
	}
	public static String readeFile(File file){
	      BufferedReader reader = null;  
	      String tempString = null;  
	      String result="";
	      try {  
	            reader = new BufferedReader(new FileReader(file));  
	            int line = 1;  
	            // 一次读入一行，直到读入null为文件结束  
	            while ((tempString = reader.readLine()) != null) {  
	            	result+=tempString;
	                // 显示行号  
	                System.out.println("line " + line + ": " + tempString);  
	                line++;  
	            }  
	            reader.close();  
	        } catch (IOException e) {  
	            e.printStackTrace();  
	        } finally {  
	            if (reader != null) {  
	                try {  
	                    reader.close();  
	                } catch (IOException e1) {  
	                }  
	            }  
	       }  
	      return result;
	}
	public static String convertStreamToString(InputStream is) throws IOException {   
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));   
		StringBuilder sb = new StringBuilder();   
		String line = null;   
		try {   
				while ((line = reader.readLine()) != null) {   
					sb.append(line);   
				}   
	        }catch (IOException e) {   
	        	e.printStackTrace();   
	        } finally {   
	        	try {   
	                is.close();   
	            } catch (IOException e) {   
	                e.printStackTrace();   
	            }   
	    }   
	    return sb.toString();   
	}   
	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean deleteFolder(String path) {

		boolean flag = false;
		File file = new File(path);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile() && file.exists()) {
				file.delete();
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * 修改文件名
	 * 
	 * @param args
	 */
	public boolean updateFileName(String path, String newName) {
		boolean flag = false;
		File file = new File(path);
		String paren = file.getParent();
		File mm = new File(paren + File.pathSeparator + newName);
		if (file.renameTo(mm)) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 解压缩文件
	 * 
	 * @param zipFile
	 * @param descDir
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static void unZipFiles(File zipFile, String descDir)throws IOException {
		System.out.println("******************解压开始******************");
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		// 控制中文乱码问题
		ZipFile zip = new ZipFile(zipFile, "gbk");
		for (Enumeration entries = zip.getEntries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			InputStream in = zip.getInputStream(entry);
			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
			;
			// 判断路径是否存在,不存在则创建文件路径
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}
			// 输出文件路径信息

			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[5120];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			if(null != in){
				in.close();  //关闭
				in=null;
			}
			if(null != out){
				out.flush();
				out.close();
				out=null;
			}
		}
		if(zip!=null){
			zip.close();
		}
		System.out.println("******************解压完毕********************");
	}
	 /**
	 2  * 压缩文件或者文件夹 压缩采用gb2312编码，其它编码方式可能造成文件名与文件夹名使用中文的情况下压缩后为乱码。。。
	 3  * 
	 4 * @param source
	 5  *            要压缩的文件或者文件夹 
	 6 *            建议使用"c:/abc"或者"c:/abc/aaa.txt"这种形式来给定压缩路径
	 7  *            使用"c:\\abc" 或者"c:\\abc\\aaa.txt"这种形式来给定路径的话，可能导致出现压缩和解压缩路径意外故障。。。
	 8  * @param zipFileName
	 9  *            压缩后的zip文件名称 压缩后的目录组织与windows的zip压缩的目录组织相同。
	10  *            会根据压缩的目录的名称，在压缩文件夹中创建一个改名的根目录， 其它压缩的文件和文件夹都在该目录下依照原来的文件目录组织形式
	11  * @throws IOException
	12  *             压缩文件的过程中可能会抛出IO异常，请自行处理该异常。
	13  */
	public static void ZIP(String source, String zipFileName)throws IOException {
		ZipOutputStream zos = new ZipOutputStream(new File(zipFileName));
		// 设置压缩的时候文件名编码为gb2312
		zos.setEncoding("gb2312");
		// System.out.println(zos.getEncoding());
		File f = new File(source);
		if (f.isDirectory()) {
		// 如果直接压缩文件夹
		ZIPDIR(source, zos, f.getName() + "/");// 此处使用/来表示目录，如果使用\\来表示目录的话，会导致压缩后的文件目录组织形式在解压缩的时候不能正确识别。
		} else {
		 // 如果直接压缩文件
		 ZIPDIR(f.getPath(), zos, new File(f.getParent()).getName() + "/");
		 ZIPFile(f.getPath(), zos, new File(f.getParent()).getName() + "/" + f.getName());
		}
		zos.closeEntry();
		zos.close();
	}
	public static void main(String[] args) {
		String dirPath="D:/upload/2015/05/27";
		try {
			FileUtils.ZIP(dirPath, "D:/upload/A.zip");
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*FileLoadUtils fil = new FileLoadUtils();
		File file = new File("D://黄继业.pdf");
		fil.pdfTojpg(file, "1467cab0-5d42-46bf-ad43-10b3cfef3252",
				"06934ece-5024-42a6-9cb3-b20010718ba9", "海贼王");*/
		 /* int[] arg = new int[]{2,1,4,3,8,7,6,3,9,0};
		  Arrays.sort(arg);
		  for(int i: arg){
			                           System.out.print(i);
			                  }*/
			
		 // new FileLoadUtils().sort(arg);
	}
	/**
	 * 
	 * 第二个ZIPFile方法，专门将指定的文件压缩成为zip文件的方法
	 2  * zip 压缩单个文件。 除非有特殊需要，否则请调用ZIP方法来压缩文件！
	 3  * 
	 4 * @param sourceFileName
	 5  *            要压缩的原文件
	 6  * @param zipFileName
	 7  *            压缩后的文件名
	 8  * @param zipFileName
	 9  *            压缩后的文件名
	10  * @throws IOException
	11  *             抛出文件异常
	12  */
	public static void ZIPFile(String sourceFileName, ZipOutputStream zos,  String tager) throws IOException {
			// System.out.println(tager);
			ZipEntry ze = new ZipEntry(tager);
			zos.putNextEntry(ze);
			// 读取要压缩文件并将其添加到压缩文件中
			FileInputStream fis = new FileInputStream(new File(sourceFileName));
			byte[] bf = new byte[10240];
			int location = 0;
			while ((location = fis.read(bf)) != -1) {
				zos.write(bf, 0, location);
			}
			fis.close();
	}
	/**
	 2  * 压缩目录。 除非有特殊需要，否则请调用ZIP方法来压缩文件！
	 3  * 
	 4 * @param sourceDir
	 5  *            需要压缩的目录位置
	 6  * @param zos
	 7  *            压缩到的zip文件
	 8  * @param tager
	 9  *            压缩到的目标位置
	10  * @throws IOException
	11  *             压缩文件的过程中可能会抛出IO异常，请自行处理该异常。
	12  */
	public static void ZIPDIR(String sourceDir, ZipOutputStream zos,String tager) throws IOException {
		// System.out.println(tager);
		ZipEntry ze = new ZipEntry(tager);
		zos.putNextEntry(ze);
		// 提取要压缩的文件夹中的所有文件
		File f = new File(sourceDir);
		File[] flist = f.listFiles();
		if (flist != null) {
		 // 如果该文件夹下有文件则提取所有的文件进行压缩
		for (File fsub : flist) {
				if (fsub.isDirectory()) {
					 // 如果是目录则进行目录压缩
					 ZIPDIR(fsub.getPath(), zos, tager + fsub.getName() + "/");
	             } else {
	                  // 如果是文件，则进行文件压缩
	                 ZIPFile(fsub.getPath(), zos, tager + fsub.getName());
	             }
	          }
	      }
	  }
	/**
	 * 将Blob二进制流生成文件存储;
	 * @param bo
	 * @param absoluteFilePath
	 */
	public static void blobToImgFile(Blob bo,String absoluteFilePath){
		try{
			BufferedInputStream ins=null;//取得BLOB的IO流
	        byte[] bt = null;
	        if (bo == null){
	        	return ;
	        }
	        InputStream is = bo.getBinaryStream();
	        ins = new BufferedInputStream(is);
	        int bufferSize = (int) bo.length();//取得BLOB的长度
	        bt = new byte[bufferSize];
	        ins.read(bt, 0, bufferSize);
	        File f = new File(absoluteFilePath);
	        FileOutputStream outStream = new FileOutputStream(f);
	        outStream.write(bt);  //写出流,保存在文件test.jpg中
	        outStream.close();
	        ins.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
