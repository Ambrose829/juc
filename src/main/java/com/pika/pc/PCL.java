package com.pika.pc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PCL {
    public static void main(String[] args) {
        Data2 data = new Data2();
        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();
    }
}
//等待，业务，通知
class Data2 { //数字 资源类
    private int num = 0;
    Lock lock = new ReentrantLock();
    Condition notfull = lock.newCondition();
    Condition notEmpty = lock.newCondition();

    public void increment() throws InterruptedException {
        lock.lock();
        try {
            while (num != 0) {
                //等待
                notEmpty.await();
            }
            num ++;
            notfull.signal();
            System.out.println(Thread.currentThread().getName() + "---->" + num);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public void decrement() throws InterruptedException {
        lock.lock();
        try {
            while (num == 0) {
                //等待
                notfull.await();
            }
            num --;
            notEmpty.signal();
            System.out.println(Thread.currentThread().getName() + "---->" + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}
