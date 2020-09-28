package com.pika.lock8;

import java.util.concurrent.TimeUnit;

/**
 * 1.标准情况下，两个线程谁先打印，sendMes，还是call
 * 答案：先sendMes再call
 * 2.sendmes延迟四秒，两个线程谁先打印，sendMes，还是call
 * 答案：先sendMes再call
 */
public class Test1 {
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(() -> {
            phone.sendMes();
        }, "A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.call();
        },"B").start();
    }
}

class Phone {
    public synchronized void sendMes() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendMes");
    }

    public synchronized void call() {
        System.out.println("call");
    }
}
