package com.superspider.queue;

import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;

import com.justme.superspider.task.Task;

public class ConcurrentLinkedQueueTest {
	
    private static ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<Integer>();
    
    private static int count = 2; // 线程个数
    //CountDownLatch，一个同步辅助类，在完成一组正在其他线程中执行的操作之前，它允许一个或多个线程一直等待。
    private static CountDownLatch latch = new CountDownLatch(count);
    public  static void main(String[] args) throws Exception {
    	final PriorityBlockingQueue<Task> queues = new PriorityBlockingQueue<>(5,new Comparator<Task>() {

			@Override
			public int compare(Task t1, Task t2) {
				if (t1.sort == t2.sort) return 0;
				return t1.sort > t2.sort ? 1 : -1;
			}
    		
		});
    	/*switch (5) {
		case 1:
		case 2:
		case 3:
		{
			System.out.println("----------");
		}
		break;
		case 5:
		{
			System.out.println("nihao");
			break;
		}
		

		default:
			System.out.println("sssssssssssssssss");
			break;
		}*/
        /*final ThreadLocal<Integer>  thread_num = new ThreadLocal<Integer>();
        Thread thread1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				int i = 0;
				while(true){
					i++;
					System.out.println("==="+i);
					if(i>20){
						Thread.currentThread().interrupt();
					}
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
        thread1.start();*/
    	Thread thread1 = new Thread(new Runnable() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				int i = 130;
				while(i > 20){
					Task task = new Task(null,null,null,new Random().nextDouble()*20);
					queues.offer(task);
					i -= 1;
					try {
						Thread.currentThread().sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
    	Thread thread2 = new Thread(new Runnable() {
			@SuppressWarnings("static-access")
			@Override
			public void run() {
				int i = 130;
				while(i > 20){
					for(int j=0 ;j<queues.size();j++){
						System.out.println(queues.poll().sort);
					}
					System.out.println("=============================");
					i -= 2;
					try {
						Thread.currentThread().sleep(2000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
    	thread1.start();
    	thread2.start();
        /*long timeStart = System.currentTimeMillis();
        ExecutorService es = Executors.newFixedThreadPool(4);
        ConcurrentLinkedQueueTest.offer();
        for (int i = 0; i < count; i++) {
            es.submit(new Poll());
        }
        latch.await(); //使得主线程(main)阻塞直到latch.countDown()为零才继续执行
        System.out.println("cost time " + (System.currentTimeMillis() - timeStart) + "ms");
        es.shutdown();*/
    }
    
    /**
     * 生产
     */
    public static void offer() {
        for (int i = 0; i < 100000; i++) {
            queue.offer(i);
        }
    }


    /**
     * 消费
     *  
     * @author 林计钦
     * @version 1.0 2013-7-25 下午05:32:56
     */
    static class Poll implements Runnable {
        public void run() {
            // while (queue.size()>0) {
            while (!queue.isEmpty()) {
                System.out.println(Thread.currentThread().getName()+"===="+queue.poll());
                try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            latch.countDown();
        }
    }
}