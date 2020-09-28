package com.pika.lock8;

import java.util.concurrent.TimeUnit;

/**
 * 7.一个静态同步方法sendMes，一个普通同步方法call，一个对象，两个线程谁先打印，sendMes，还是call
 * 8.一个静态同步方法sendMes，一个普通同步方法call，两个对象，两个线程谁先打印，sendMes，还是call
 */
public class Test4 {
    public static void main(String[] args) {
        Phone4 phone1 = new Phone4();
        Phone4 phone2 = new Phone4();


        new Thread(() -> {
            phone1.sendMes();
        }, "A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone2.call();
        },"B").start();
    }
}

class Phone4 {

    //静态同步方法
    public static synchronized void sendMes() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendMes");
    }
    //普通同步方法
    public synchronized void call() {
        System.out.println("call");
    }
}
