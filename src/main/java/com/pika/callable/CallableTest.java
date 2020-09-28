package com.pika.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        new Thread(new MyThread()).start();
        /**
         * new Thread(new Runnable()).start();
         * new Thread(new FutureTask<>()).start();
         * new Thread(new FutureTask<>(Callable)).start();
         */

        MyThread myThread = new MyThread();
        FutureTask<Integer> futureTask = new FutureTask<>(myThread);
        //打印几个call，结果会被缓存，效率高
        new Thread(futureTask, "A").start();
        new Thread(futureTask, "B").start();
        //获取callable的返回值
        //get方法可能会导致阻塞，把他放在最后，或者使用异步通信
        Integer integer = futureTask.get();
        System.out.println(integer);

    }
}

//class MyThread implements Runnable {
//
//    @Override
//    public void run() {
//
//    }
//}

class MyThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("call()");
        return 1024;
    }
}