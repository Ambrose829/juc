package com.pika.pc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * @author Pika
 * @create 2020/9/28
 * @since 1.0.0
 * volatile/CAS/atomicInteger/BlockingQueue/线程交互
 */
public class ProducerConsumer_BlockQueueDemo {
    public static void main(String[] args) throws Exception {
        MyResource myResource = new MyResource(new ArrayBlockingQueue<>(10));
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 生产线程启动");
            try {
                myResource.myProducer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Producer").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t 消费线程启动");
            try {
                myResource.myConsumer();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Consumer").start();


        try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println();
        System.out.println("5秒钟时间到， 叫停生产");
        myResource.stop();
    }
}

class MyResource {
    private volatile boolean FLAG = true;//默认开启，生产+消费
    private AtomicInteger atomicInteger = new AtomicInteger();

    BlockingQueue<String> blockingQueue = null;

    public MyResource(BlockingQueue<String> blockingQueue) {
        this.blockingQueue = blockingQueue;
        System.out.println(blockingQueue.getClass().getName());
    }

    public void myProducer() throws InterruptedException {
        String data = null;
        boolean retValue;
        while (FLAG) {
            data = atomicInteger.incrementAndGet() + "";
            retValue = blockingQueue.offer(data, 2L, TimeUnit.SECONDS);
            if (retValue) {
                System.out.println(Thread.currentThread().getName() + "\t数据：" + data + "\t插入队列成功");
            } else {
                System.out.println(Thread.currentThread().getName() + "\t数据：" + data + "\t插入队列失败");
            }
            try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }

        }

        System.out.println(Thread.currentThread().getName() + "\t 大老板叫停，生产动作结束");
    }

    public void myConsumer() throws InterruptedException {
        String result = null;
        while (FLAG) {
            result = blockingQueue.poll(2L, TimeUnit.SECONDS);
            if (result == null || result.equalsIgnoreCase("")) {
                FLAG = false;
                System.out.println(Thread.currentThread().getName() + "\t 超过两秒钟没有取到数据，消费退出");
                return;
            }
            System.out.println(Thread.currentThread().getName() + "\t 消费队列成功" + result);
        }
    }

    public void stop() throws Exception {
        this.FLAG = false;
    }
}
