package com.pika.lock;

import java.util.concurrent.atomic.AtomicReference;

public class SpinLockDemo {
    AtomicReference<Thread> atomicReference = new AtomicReference<Thread>();

    public void myLock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "myLock()");
        do {

        }while (!atomicReference.compareAndSet(null, thread));
    }

    public void myUnLock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "myUnLock()");
        atomicReference.compareAndSet(thread, null);
    }

}
