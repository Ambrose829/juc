package com.pika.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Pika
 * @create 2020/9/24
 * @since 1.0.0
 *
 * 1 CAS是什么？
 *      比较并交换
 */

public class CASDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        System.out.println(atomicInteger.compareAndSet(5, 6) + "\t current data: " + atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5, 2020) + "\t current data: " + atomicInteger.get());

    }
}
