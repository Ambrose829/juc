package com.pika.add;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

//加法计数器
public class CyclicBarrierDemo {
    public static void main(String[] args) {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(7, () -> {
            System.out.println("召唤神龙成功");
        });

        for (int i = 1; i <= 7; i ++) {
            final int temp = i;
            //lambda怎样拿到a
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName()+"已经收集了"+temp+"颗龙珠");

                //当计数到7时，触发
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }


            }).start();
        }
    }
}
