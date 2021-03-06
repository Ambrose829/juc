package com.pika.pc;

import java.util.Date;

/**
 * 线程之间的通信问题，生产者和消费者问题
 * 线程交替执行 A B操作同一个变量 num = 0
 * A num + 1
 * B num - 1
 */
public class PCS {
    public static void main(String[] args) {
        Data data = new Data();
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
class Data { //数字 资源类
    private int num = 0;
    public synchronized void increment() throws InterruptedException {
        while (num != 0) {
            //等待
            this.wait();
        }
        num ++;
        System.out.println(Thread.currentThread().getName() + "---->" + num);
        //通知其他线程，生产了一个
        this.notifyAll();
    }

    public synchronized void decrement() throws InterruptedException {
        while (num == 0) {
            this.wait();
        }
        num --;
        System.out.println(Thread.currentThread().getName() + "---->" + num);
        //通知其它线程，我消费了一个
        this.notifyAll();
    }
}
