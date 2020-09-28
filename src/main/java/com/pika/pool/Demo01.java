package com.pika.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executors 工具类、三大方法
 * 创建一个单个线程的线程池
 * Executors.newSingleThreadExecutor();
 * 创建一个固定的线程数量的线程池
 * Executors.newFixedThreadPool(5);
 * //创建一个可伸缩的线程池
 * Executors.newCachedThreadPool();
 */
public class Demo01 {
    public static void main(String[] args) {
        //创建一个单个线程的线程池
//        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        //创建一个固定的线程数量的线程池
//        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        //创建一个可伸缩的线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();

        try {
            for (int i = 0; i < 10; i++) {
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"111");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //线程池用完，程序结束，关闭线程池
            threadPool.shutdown();
        }




    }
}
