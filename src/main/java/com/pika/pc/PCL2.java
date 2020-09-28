package com.pika.pc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PCL2 {
    public static void main(String[] args) {
        Data3 data = new Data3();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.printA();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.printB();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.printC();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();


    }
}
//等待，业务，通知
class Data3 { //数字 资源类
    private int num = 1;
    Lock lock = new ReentrantLock();
    Condition condition1 = lock.newCondition();
    Condition condition2 = lock.newCondition();
    Condition condition3 = lock.newCondition();

    public void printA() throws InterruptedException {
        lock.lock();
        try {
            while (num != 1) {
                condition1.await();
            }
            System.out.println(Thread.currentThread().getName() + "printA");
            num = 2;
            condition2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printB() throws InterruptedException {
        lock.lock();
        try {
            while (num != 2) {
                condition2.await();
            }
            System.out.println(Thread.currentThread().getName() + "printB");
            num = 3;
            condition3.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printC() throws InterruptedException {
        lock.lock();
        try {
            while (num != 3) {
                condition3.await();
            }
            System.out.println(Thread.currentThread().getName() + "printC");
            num = 1;
            condition1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

