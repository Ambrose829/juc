package com.pika.bq;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 同步队列
 * 和其它的BlockingQueue不一样，SynchronousQueue 不存储元素
 * 如果向里面put了一个元素，必须从里面取出来，否则不能在put进去值
 */
public class SychronousQueueDemo {
    public static void main(String[] args) {
        //同步队列
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<String>();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "put 1");
                synchronousQueue.put("1");
                System.out.println(Thread.currentThread().getName() + "put 2");
                synchronousQueue.put("2");
                System.out.println(Thread.currentThread().getName() + "put 3");
                synchronousQueue.put("3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T1").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName() + "get " + synchronousQueue.take());
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName() + "get " + synchronousQueue.take());
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName() + "get " + synchronousQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T2").start();

    }
}
