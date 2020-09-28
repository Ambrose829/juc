package com.pika.lock8;

import java.util.concurrent.TimeUnit;

/**
 * 3.增加一个普通方法hello，将线程B的call换成hello，先sendMes还是hello
 * 答案：先hello再sendMes
 * 4.两个对象，两个线程分别执行不同的对象的sendMes方法、call方法，先sendMes还是call
 * 答案：先call再sendMes
 */
public class Test2 {
    public static void main(String[] args) {
        Phone2 phone1 = new Phone2();
        Phone2 phone2 = new Phone2();
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

class Phone2 {
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

    public void hello() {
        System.out.println("hello");
    }
}
