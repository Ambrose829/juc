package com.pika.lock8;

import java.util.concurrent.TimeUnit;

public class Test3 {
    public static void main(String[] args) {
        Phone3 phone1 = new Phone3();
        Phone3 phone2 = new Phone3();
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

class Phone3 {
    //synchronized 锁的对象是方法的调用者
    //static 静态方法
    //类加载时就有了！Class模板，这里锁的是class对象，class全局唯一
    public static synchronized void sendMes() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendMes");
    }

    public static synchronized void call() {
        System.out.println("call");
    }
}
