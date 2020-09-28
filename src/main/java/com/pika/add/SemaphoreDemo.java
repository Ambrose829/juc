package com.pika.add;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo {
    public static void main(String[] args) {

        // 线程数量，停车位
        Semaphore semaphore = new Semaphore(3);

        for (int i = 1; i <= 6; i ++) {
            new Thread(() -> {
                //每个acquire()都会阻塞，直到许可证可用，然后才能使用它

                try {
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName() + "\t抢到车位");
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName() + "\t离开车位");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    semaphore.release();
                }

                //每个release()添加许可证，潜在地释放阻塞获取方
            }, String.valueOf(i)).start();
        }
    }
}
