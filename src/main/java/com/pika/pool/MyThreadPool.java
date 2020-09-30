package com.pika.pool;

import java.util.concurrent.*;

/**
 * @author Pika
 * @create 2020/9/29
 * @since 1.0.0
 * 第四种获得/使用java多线程的方式 线程池
 *
 * 第一种继承Thread类
 * 第二种实现Runnable接口
 * 第三种实现Callable接口有返回值
 *
 *自己定义一个线程池
 * 核心数为2，最大线程数为5
 */
public class MyThreadPool {
    public static void main(String[] args) {
        System.out.println(Runtime.getRuntime().availableProcessors());
        ExecutorService threadPool =
                new ThreadPoolExecutor(
                        2,
                        5,
                        1L,
                        TimeUnit.SECONDS,
                        new LinkedBlockingQueue<>(3),
                        Executors.defaultThreadFactory(),
                        new ThreadPoolExecutor.DiscardPolicy()
                );

        try {
            for (int i = 1; i <= 10; i++) {
                threadPool.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + "\t 办理业务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }

    }
}
