package com.superspider.mappedbyptebuffer;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class MapMemoryBuffer {
	/** 
     * 通过RandomAccessFile读取文件，能出来大数据文件，效率低 
     * 
     * @param file     源文件 
     * @param encoding 文件编码 
     * @param pos      偏移量 
     * @param num      读取量 
     * @return pins文件内容，pos当前偏移量 
     */  
    @SuppressWarnings("resource")
	public static Map<String, Object> readLine(File file, String encoding, long pos, int num) {  
        Map<String, Object> res = Maps.newHashMap();  
        List<String> pins = Lists.newArrayList();  
        res.put("pins", pins);  
        RandomAccessFile reader = null;  
        try {  
            reader = new RandomAccessFile(file, "r");  
            reader.seek(pos);  
            for (int i = 0; i < num; i++) {  
                String pin = reader.readLine();  
                if (StringUtils.isBlank(pin)) {  
                    break;  
                }  
                pins.add(new String(pin.getBytes("8859_1"), encoding));  
            }  
            res.put("pos", reader.getFilePointer());  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
           // IOUtils.closeQuietly(reader);  
        }  
        return res;  
    }  
	@SuppressWarnings("static-access")
	public static void main(String[] args)throws Exception{
		MapMemoryBuffer mmb = new MapMemoryBuffer();
		File file = new File("d://VMware-workstation-full-10.0.2-1744117.1398244508.exe");
		System.out.println(mmb.readLine(file, "utf-8", 0, 100000000));
		/*ByteBuffer byteBuffer = ByteBuffer.allocate(14 * 1024 * 1024);
		byte[] bbb = new byte[14 * 1024 * 1024];
		RandomAccessFile fis1 = new RandomAccessFile(new File("d://VMware-workstation-full-10.0.2-1744117.1398244508.exe"), "rw");
		FileInputStream fis = new FileInputStream("d://VMware-workstation-full-10.0.2-1744117.1398244508.exe");
		FileOutputStream fos = new FileOutputStream("d://mapbuffer/output.txt");
		FileChannel fc  = fis.getChannel();
		long timeStar = System.currentTimeMillis();//得到当前时间;
		//fc.read(byteBuffer);//读取
		MappedByteBuffer mbb = fc.map(MapMode.READ_WRITE,0,fc.size());
		System.out.println(fc.size()/1024);
		long timeEnd = System.currentTimeMillis();
		System.out.println("Read time :" + (timeEnd - timeStar) + "ms");
		timeStar = System.currentTimeMillis();
		//fos.write(bbb);//写入
		mbb.flip();
		timeEnd = System.currentTimeMillis();
		System.out.println("Write time : " + (timeEnd - timeStar) + "ms");
		fos.flush();
		fc.close();
		fis.close();*/
	}
}
