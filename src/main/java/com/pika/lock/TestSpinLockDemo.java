package com.pika.lock;

import java.util.concurrent.TimeUnit;

public class TestSpinLockDemo {
    public static void main(String[] args) throws InterruptedException {
        SpinLockDemo lock = new SpinLockDemo();

        new Thread(()->{
            lock.myLock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnLock();
            }
        }, "T1").start();


        TimeUnit.SECONDS.sleep(1);

        new Thread(()->{
            lock.myLock();
            try {
//                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnLock();
            }
        }, "T2").start();
    }
}
