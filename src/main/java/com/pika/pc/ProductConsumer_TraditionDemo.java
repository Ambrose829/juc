package com.pika.pc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Pika
 * @create 2020/9/28
 * @since 1.0.0
 * 题目： 一个初始值为0的变量，两个线程对其交替操作，一个加1一个减1，来5轮
 *
 * 线程   操作(方法)      资源类
 * 判断   干活             通知
 * 防止虚假唤醒机制
 *
 */
public class ProductConsumer_TraditionDemo {
    public static void main(String[] args) {
        ShareData shareData = new ShareData();

        new Thread(() -> {
            for (int i = 0; i <= 5; i++) {
                try {
                    shareData.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "T1").start();

        new Thread(() -> {
            for (int i = 0; i <= 5; i++) {
                try {
                    shareData.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "T2").start();
    }
}

class ShareData {
    private int number = 0;
    private Lock lock = new ReentrantLock();
    private Condition condition1 = lock.newCondition();

    public void increment() throws InterruptedException {
        lock.lock();
        try {
            //1.判断
            while (number != 0) {
                //2.等待不能生产
                condition1.await();
            }
            //2.生产
            number ++;
            System.out.println(Thread.currentThread().getName() + "\t生产线程\t" + number);
            //3.通知唤醒
            condition1.signalAll();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }


    public void decrement() throws InterruptedException {
        lock.lock();
        try {
            //1.判断
            while (number == 0) {
                //2.等待不能生产
                condition1.await();
            }
            //2.生产
            number --;
            System.out.println(Thread.currentThread().getName() + "\t消费线程\t" + number);
            condition1.signalAll();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }


}
