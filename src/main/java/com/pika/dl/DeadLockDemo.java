package com.pika.dl;

import lombok.AllArgsConstructor;

import java.util.concurrent.TimeUnit;

/**
 * @author Pika
 * @create 2020/9/30
 * @since 1.0.0
 *
 * 死锁是指两个或两个以上线程在执行过程中，
 * 因争夺资源而造成的一种互相等待的现象，
 * 若无外力干涉，那么它们都将无法推进下去
 *
 */
public class DeadLockDemo {
    public static void main(String[] args) {
        String lock1 = "lockA";
        String lock2 = "lockB";

        new Thread(new HoldLockThread(lock1, lock2), "T1").start();
        new Thread(new HoldLockThread(lock2, lock1), "T2").start();

        /**
         * linux    ps -ef|grep xxxx
         * windows下的java运行程序 也有类似ps的查看进程的命令，查看的是java
         *
         * jps = java ps
         *
         * jps -l
         */
    }
}

@AllArgsConstructor
class HoldLockThread implements Runnable {
    private String l1;
    private String l2;


    @Override
    public void run() {
        synchronized (l1) {
            System.out.println(Thread.currentThread().getName() + "\t 自己持有：" + l1 + "尝试获得：" + l2);
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

            synchronized (l2) {
                System.out.println(Thread.currentThread().getName() + "\t 自己持有：" + l2 + "尝试获得：" + l1);
            }
        }



    }
}
