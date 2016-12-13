package cn.com.infcn.superspider.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.justme.superspider.xml.Site;

/**
 * User: WChao
 * Date: 16-09-22
 * Time: 下午2:03
 */
public class Downloader implements Callable<File> {
    protected int connectTimeout = 10 * 1000; // 连接超时:10s
    protected int readTimeout = 1 * 1000 * 1000; // IO超时:1min

    protected int speedRefreshInterval = 500; // 即时速度刷新最小间隔:500ms

    protected byte[] buffer;

    private URL url;
    private File file;
    private Site site ;

    private float averageSpeed;
    private float currentSpeed;
    enum SpecialChar {
   	左斜("/"),右斜("\\"),冒号(":"),星号("*"),问号("?"),双引号("\""),左尖括号("<"),右尖括号(">"),竖杠("|");
   	private String value;//值
   	private SpecialChar(){}
   	private SpecialChar(String value){
   		this.value = value;
   	}
    }
    public Downloader() {
        buffer = new byte[8 * 1024]; // IO缓冲区:8KB
    }
    public Downloader(Site site) {
    	this();
    	this.site = site;
    }
    public void setUrlAndFile(URL url, File file) {
        this.url = url;
        this.file = autoRenameIfExist(file);
        this.averageSpeed = 0;
        this.currentSpeed = 0;
    }

    public URL getUrl() {
        return url;
    }

    public File getFile() {
        return file;
    }

    public float getAverageSpeed() {
        return averageSpeed;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    @Override
    public File call() throws Exception {
        StopWatch watch = new StopWatch();
        watch.start();

        InputStream in = null;
        OutputStream out = null;
        try {
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            conn.connect();
            
            Map<String,List<String>> map = conn.getHeaderFields();
            String fileName = getFileName(map);
            this.setUrlAndFile(url, checkSpecialFileName(new File(this.file.getAbsolutePath()+File.separator+fileName)));
            in = conn.getInputStream();
            out = new FileOutputStream(file);

            int time = 0;
            int bytesInTime = 0;
            for (; ; ) {
            	if(site.isStop)
            		break;
                watch.split();
                int bytes = in.read(buffer);
                if (bytes == -1) {
                    break;
                }
                out.write(buffer, 0, bytes);

                time += watch.getTimeFromSplit();
                if (time >= speedRefreshInterval) {
                    currentSpeed = getSpeed(bytesInTime, time);
                    time = 0;
                    bytesInTime = 0;
                }
            }
        } catch (IOException e) {
            file.delete();
            throw e;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                }
            }
        }

        watch.stop();
        averageSpeed = getSpeed(file.length(), watch.getTime());

        return file;
    }
    
    /**
     * 获取下载文件名称
     * @author lihf
     * @date 2016年11月16日	下午1:52:45
     * @param map
     * @return
     * @throws UnsupportedEncodingException 
     */
    @SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
    private String getFileName(Map<String,List<String>> map) throws UnsupportedEncodingException
    {
        String net_path = "";
        String charset = site.getCharset();
        for(Iterator iter = map.entrySet().iterator();iter.hasNext();)
        {
        	Map.Entry entry = (Map.Entry)iter.next(); 
        	if("Content-disposition".equalsIgnoreCase((String) entry.getKey()))
        	{
        		List<String> headerFieldList = (List) entry.getValue();
        		for(String str:headerFieldList)
        		{
        			if(str.indexOf("filename")!=-1)
        			{
        				String[] name = str.split("=");
        				net_path = URLDecoder.decode(name[1]);
        				
        				net_path = new String(net_path.getBytes(),charset);
        				if(!StringUtil.isEmpty(net_path))
        				{
        					net_path = net_path.substring(1, net_path.length()-1);
        				}
        			}
        		}
        	}
        }
        String fileName = net_path.substring(net_path.lastIndexOf("/")+1);
        return fileName;
    }

    private static float getSpeed(long bytesInTime, long time) {
        return (float) bytesInTime / 1024 / ((float) time / 1000);
    }

    private static String getExtension(String string) {
        int lastDotIndex = string.lastIndexOf('.');
        // . ..
        if (lastDotIndex > 0) {
            return string.substring(lastDotIndex + 1);
        } else {
            return "";
        }
    }

    private static File autoRenameIfExist(File file) {
        if (file.exists() && file.isFile()) {
            String path = file.getAbsolutePath();

            String extension = getExtension(path);
            int baseLength = path.length();
            if (extension.length() > 0) {
                baseLength = path.length() - extension.length() - 1;
            }

            StringBuilder buffer = new StringBuilder(path);
            for (int index = 1; index < Integer.MAX_VALUE; ++index) {
                buffer.setLength(baseLength);
                buffer.append('(').append(index).append(')');
                if (extension.length() > 0) {
                    buffer.append('.').append(extension);
                }
                file = new File(buffer.toString());
                if (!file.exists()) {
                    break;
                }
            }

        }
        return file;
    }
    
    private static File checkSpecialFileName(File file){
    	String fileName = file.getName();
    	String filePath = file.getAbsolutePath();
    	for(SpecialChar specialChar : SpecialChar.values()){
        	fileName = fileName.replace(specialChar.value, "");
        }
    	File resetFile = new File(filePath.substring(0,filePath.lastIndexOf(File.separator)+1)+fileName);
    	return resetFile;
    }
    public static void main(String[] args){
    	URL url = null;
		try {
			url = new URL("http://www.cninfo.com.cn/cninfo-new/disclosure/sse/download/1202824192?announceTime=2016-11-11");
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		//System.out.println(SpecialChar.);
		/*String rename = "\\/:\\*\\?\\"<>|";
*/        String dir = "e:/canima/2016/09/22/";
        String fileName = "1202823316?announceTime=2016-11-11";
        final Site site = new Site();
        Downloader downloader = new Downloader(site);
        System.out.println(new File(dir+fileName).exists());
        
        downloader.setUrlAndFile(url, new File(dir+fileName));
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
    	final Future<File> fileFuture =  executorService.submit(downloader);
    	/* Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true){
						System.out.println("==========");
						try {
							Thread.currentThread().sleep(5000);
							site.isStop = true;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			});
	        thread.start();*/
    	//Thread thread1 = null ;
		try {
	       /* thread1 = new Thread(new Runnable() {
				
				@Override
				public void run() {
					int i= 30;
					while(i>25){
						try {
							i--;
							System.out.println(i);
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					System.out.println("=======================");
					fileFuture.cancel(true);
					executorService.shutdownNow();
				}
				
				
			});
	        thread1.start();*/
	        
	        File downloadFIle = fileFuture.get();
	        System.out.println("download " + downloadFIle.getAbsolutePath() +
	                " from " + url +
	                " @" + downloader.getAverageSpeed() + "KB/s");
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(fileFuture != null){
				fileFuture.cancel(true);
			}
			
			executorService.shutdownNow();
			System.out.println("====Fuck");
		}
    }
}
