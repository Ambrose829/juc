package com.pika.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author Pika
 * @create 2020/9/29
 * @since 1.0.0
 */
public class CallableDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //两个线程，一个主线程，一个是AA futureTask
        FutureTask<Integer> futureTask = new FutureTask<>(new MyThread2());
         new Thread(futureTask, "AA").start();
         new Thread(futureTask, "BB").start();


        int result1 = 100;

        int result2= 0;
        while (!futureTask.isDone()) {
            result2 = futureTask.get();

        }
//        int result2 = futureTask.get();//要求获得Callable线程的计算结果，如果没有计算完成，则等待计算完成，会造成阻塞。
        System.out.println("=======result：" +(result1 + result2));
    }
}



class MyThread2 implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println(Thread.currentThread().getName() + "********");
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
        return 1024;
    }
}
