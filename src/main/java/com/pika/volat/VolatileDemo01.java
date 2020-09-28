package com.pika.volat;

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileDemo01 {
    private volatile static AtomicInteger num = new AtomicInteger();

    public static void add() {
//        num ++;
        num.getAndIncrement(); //执行原子类的+1操作   AtomicInteger + 1
    }

    public static void main(String[] args) {
        //理论上num结果是两万
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    add();
                }
            }).start();
        }

        while (Thread.activeCount()>2) { //main gc
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "  " + num);

    }
}
