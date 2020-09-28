package com.pika.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Pika
 * @create 2020/9/27
 * @since 1.0.0
 *
 * 多个线程同时读一个资源没有问题，所以为了满足并发量，读取共享资源可以同时进行。
 * 但是，如果有一个线程去写共享资源，就不应该再有其它线程可以对该资源进行读或写
 * 小总结：
 *  读读共存
 *  读写不能共存
 *  写写不能共存
 *
 *  写操作：原子+独占，整个过程必须是一个完整的统一的整体，中间不许被分割，被打断
 *  读操作：共享
 *
 */
public class ReadWriteLockDemo {
    public static void main(String[] args) {
        MyCache myCache = new MyCache();
        for (int i = 1; i <= 5; i++) {
            final int tempInt = i;
            new Thread(() -> {
                myCache.put(tempInt + "", tempInt + "");
            }, String.valueOf(i)).start();
        }

        for (int i = 1; i <= 5; i++) {
            final int tempInt = i;
            new Thread(() -> {
                myCache.get(tempInt + "");
            }, String.valueOf(i)).start();
        }


    }
}

class MyCache {
    private volatile Map<String, Object> map = new HashMap<>();
    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void put(String key, Object value) {

        rwLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t 正在写入" + key);
            try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "\t 写入完成");
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            rwLock.writeLock().unlock();
        }

    }

    public Object get(String key) {
        rwLock.readLock().lock();
        try {

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            rwLock.readLock().unlock();
        }

        System.out.println(Thread.currentThread().getName() + "\t 正在读取" + key);
        try { TimeUnit.MILLISECONDS.sleep(300); } catch (InterruptedException e) { e.printStackTrace(); }
        Object result = map.get(key);
        System.out.println(Thread.currentThread().getName() + "\t读取完成" + result);
        return result;
    }
}