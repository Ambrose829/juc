package com.pika.cas;

import com.sun.org.apache.xml.internal.serializer.ToTextSAXHandler;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Pika
 * @create 2020/9/27
 * @since 1.0.0
 */
public class AtomicStampedReferenceDemo {
    static AtomicReference atomicReference = new AtomicReference<Integer>(100);

    static AtomicStampedReference<Integer> atomicStampedReference =
            new AtomicStampedReference<Integer>(100, 1);
    public static void main(String[] args) {
        System.out.println("======================ABA问题的产生");
        new Thread(() -> {
            atomicReference.compareAndSet(100, 101);
            atomicReference.compareAndSet(101, 100);
        }, "T1").start();

        new Thread(() -> {
            //暂停1秒钟线程，保证上面的T1完成了一次ABA操作
            try { SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            System.out.println(atomicReference.compareAndSet(100, 2019) +
                    "\t " + atomicReference.get());
        }, "T2").start();


        try { SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
        System.out.println("======================ABA问题的解决");

        new Thread(() -> {

            System.out.println(Thread.currentThread().getName() + "\t第1次版本号：" +
                    atomicStampedReference.getStamp());
            //暂停1秒钟T3线程，确保T4线程获取同样的版本号
            try { SECONDS.sleep(1); } catch (InterruptedException e) { e.printStackTrace(); }
            atomicStampedReference.compareAndSet(100, 101,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t第2次版本号：" +
                    atomicStampedReference.getStamp());
            atomicStampedReference.compareAndSet(101, 100,
                    atomicStampedReference.getStamp(), atomicStampedReference.getStamp() + 1);
            System.out.println(Thread.currentThread().getName() + "\t第3次版本号：" +
                    atomicStampedReference.getStamp());

        }, "T3").start();

        new Thread(() -> {
            int stamp = atomicStampedReference.getStamp();
            System.out.println(Thread.currentThread().getName() + "\t第1次版本号：" + stamp);
            //暂停3秒钟T4线程
            try { SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            boolean bl = atomicStampedReference.compareAndSet(100, 2019,
                    stamp, stamp + 1);
            System.out.println(Thread.currentThread().getName() + "\t修改成功/否：" + bl + "\t当前最新版本号" +
                    atomicStampedReference.getStamp());
            System.out.println(Thread.currentThread().getName() + "\t当前实际最新值" +
                    atomicStampedReference.getReference());
        }, "T4").start();
    }
}
