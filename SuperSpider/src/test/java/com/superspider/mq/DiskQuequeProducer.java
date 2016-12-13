package com.superspider.mq;

import cn.com.infcn.superspider.io.mq.disk.DiskQueue;
import cn.com.infcn.superspider.io.mq.disk.DiskQueuePool;

public class DiskQuequeProducer {


	public static void main(String[] args) throws Exception {
		/*Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				DiskQueque.init("d://test"); // 初始化MQ所在路径
				DiskQueque diskq = new DiskQueque("Thread-0===wangchao");
				int num = 0;
				while(true){
					diskq.offer(new byte[1024]);
					diskq.poll();
					try {
						if(num >= 50){
							diskq.close();
							break;
						}
						System.out.println(diskq.size()+"========="+Thread.currentThread().getName());
						Thread.currentThread().sleep(1000);
						num++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int num = 0;
				DiskQueque.init("d://test"); // 初始化MQ所在路径
				DiskQueque diskq = new DiskQueque(Thread.currentThread().getName()+"===bingbing");
				while(true){
					diskq.offer(new byte[1024]);
					try {
						if(num >= 50){
								diskq.close();
								try {
									diskq.destory();
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							break;
						}
						Thread.currentThread().sleep(2000);
						num++;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.out.println(diskq.size()+"========="+Thread.currentThread().getName());
				}
			}
		});
		t1.start();*/
		//t2.start();
		DiskQueuePool pool = new DiskQueuePool("d://test",false);// 初始化MQ所在路径
		
		DiskQueue diskq = pool.getDiskQueue("MyMQ");
		int totalCount = 1000000;
		for(int i=0;i<totalCount;i++){
			diskq.offer(new byte[1024]);
		}
		
		System.out.println(diskq.size());
		
		pool.close(); // 清理掉Q环境
	}
}
