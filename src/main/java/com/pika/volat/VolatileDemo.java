package com.pika.volat;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1.验证volatile的可见性
 *  1.1 假如 int number = 0;  number变量之前根本没有添加volatile关键字修饰, 没有可见性
 *  1.2 添加了volatile，可以解决可见性问题
 *
 * 2.验证volatile不保证原子性
 *  2.1 原子性什么意思
 *      不可分割，原子性，某个线程在进行某个业务时中间不能被加塞或者被分割。需要整体完整，要么同时成功，要么同时失败
 *
 */
public class VolatileDemo {
    public static void main(String[] args) {
//        seeOkByVolatile();

        MyData myData = new MyData();
        for (int i = 1; i <= 20; i++) {
            new Thread(() -> {
                for (int j = 1; j <= 1000; j++) {
                    myData.addPlusVolatile();
                    myData.addAtomic();
                }
            }, String.valueOf(i)).start();
        }

        //需要等待上面20个线程都完成后再取得结果值看是多少

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        System.out.println(Thread.currentThread().getName() + "\t int type, finally number: " + myData.number);

        System.out.println(Thread.currentThread().getName() + "\t atomicIntger type, finally number: " + myData.atomicInteger.get());
    }

    //volatile可以保证可见性，及时通知其它线程，主物理内存的值已经被修改
    private static void seeOkByVolatile() {
        MyData myData = new MyData();
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "\t come in");

            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }

            myData.add60();
            System.out.println(Thread.currentThread().getName() + "\t updated number values: " + myData.number);
        }, "AAA").start();


        while (myData.number == 0) {
            //main线程就一直等待，直到number不再等于0

        }

        System.out.println(Thread.currentThread().getName() + "\t mission is over, main get number: " + myData.number);
    }
}

class MyData { // MyData.java ==> MyData.class
    volatile int number = 0;
    AtomicInteger atomicInteger = new AtomicInteger();


    public void add60() {
        this.number = 60;
    }
    //此时number是用volatile修饰的
    public void addPlusVolatile() {
        number ++;
    }

    public void addAtomic() {
        atomicInteger.getAndIncrement();
    }
}