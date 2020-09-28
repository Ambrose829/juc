package com.pika.volat;

import sun.security.timestamp.TSRequest;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;

public class JMMDemo {
    private  static volatile int num = 0;
    public static void main(String[] args) {//main线程




        new Thread(() -> {//线程1 如果不加volitale对主内存的变化不知道
            while (num==0) {

            }
        }).start();

        try {
            SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        num = 1;
        System.out.println(num);
    }
}
