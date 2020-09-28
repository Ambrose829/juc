package com.pika.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Pika
 * @create 2020/9/27
 * @since 1.0.0
 * 可重入锁（递归锁）
 * 指的是同一个线程外层函数获得锁之后，内层递归函数仍然能获取该锁的代码，
 * 在同一个线程在外层方法获取锁的时候，在进入内层方法会自动获取锁，
 * 也即是说 ，线程可以进入任何一个它已经拥有的锁所同步着的代码块
 */
public class ReetrantLockDemo {
    public static void main(String[] args) {
        IPhone phone = new IPhone();
        new Thread(()->{
            phone.sms();
        },"T1").start();

        new Thread(()->{
            phone.sms();
        },"T2").start();

        try { TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println();
        System.out.println();
        System.out.println();

        Thread t3 = new Thread(phone);
        Thread t4 = new Thread(phone);
        t3.start();
        t4.start();

    }
}

class IPhone implements Runnable{
        Lock lock = new ReentrantLock();

    public synchronized void sms() {
        System.out.println(Thread.currentThread().getName() + "sms");
        call();
    }

    public synchronized void call() {
        System.out.println(Thread.currentThread().getName() + "call");

    }

    @Override
    public void run() {
        get();
    }

    public void get() {
        lock.lock();
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "调用get()");
            set();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
            lock.unlock();
        }

    }
    public void set() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "调用set()");
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }


}