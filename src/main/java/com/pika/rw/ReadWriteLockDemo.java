package com.pika.rw;

import com.sun.xml.internal.bind.v2.runtime.output.StAXExStreamWriterOutput;

import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;




/**
 * 独占锁（写锁 一次只能被一个线程占有）
 * 共享锁（读锁 一次可以被多个线程占有）
 * ReadWriteLock
 * 读读   可以共存
 * 读写   不可以共存
 * 写写   不能共存
 */
public class ReadWriteLockDemo {

    public static void main(String[] args) {
        MyCacheLock myCache = new MyCacheLock();
        for (int i = 1; i <= 5; i++) {
            final int temp = i;
            new Thread(() -> {
                myCache.put(temp+"", temp);
                myCache.get(temp+"");
            }, String.valueOf(i)).start();


        }

//        for (int i = 1; i <= 5; i++) {
//            final int temp = i;
//            new Thread(() -> {
//                myCache.get(temp+"");
//            }, String.valueOf(i)).start();
//        }
    }
}
/*
    自定义缓存
 */
class MyCacheLock {
    private volatile Map<String, Object> map = new HashMap<>();
    //读写锁，更加细粒度的控制
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    // 存，写入的时候希望同时只有一个线程写
    public void put(String key, Object value) {
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "写入" + key);
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "写入成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    // 取，读的时候希望所有人都可以读
    public void get(String key) {
        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "读取" + key);
            Object o = map.get(key);
            System.out.println(Thread.currentThread().getName() + "读取成功，数据为" + o);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    public void test() {
        readWriteLock.readLock().lock();
        try {

        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            readWriteLock.readLock().unlock();
        }
    }
}