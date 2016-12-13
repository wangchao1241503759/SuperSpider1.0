package com.superspider.mq;

import cn.com.infcn.superspider.io.mq.disk.DiskQueue;
import cn.com.infcn.superspider.io.mq.disk.DiskQueuePool;

public class DiskQuequeConsumer {
	
		 public static void main(String[] args) throws Exception{
			 	String path = "d://test"; 
				DiskQueuePool pool = new DiskQueuePool(path);
				DiskQueue q = pool.getDiskQueue("MyMQ");
				
				long start = System.currentTimeMillis();
				int i = 0;
				int count = 100; 
				while(true){   
					byte[] data = q.poll();
					if(data == null) break;
					i++;
				}
				
				long end = System.currentTimeMillis();
				System.out.format("Total: %d\n", i);
				System.out.format("QPS: %.2f\n", i*1000.0/(end-start));
				System.out.format("MPS: %.2fM/s\n", count*i*1000.0/(end-start)/1024/1024);
				
				pool.close();
		}
}
