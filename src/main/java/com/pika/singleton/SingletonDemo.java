package com.pika.singleton;

/**
 * @author Pika
 * @create 2020/9/23
 * @since 1.0.0
 */
public class SingletonDemo {
    private static volatile SingletonDemo instance = null;
    private SingletonDemo() {
        System.out.println(Thread.currentThread().getName() + "\t 构造方法");
    }

    //双端检索机制
    private static  SingletonDemo getInstance() {
        if (instance == null) {
            synchronized (SingletonDemo.class) {
                if (instance == null) {
                    instance = new SingletonDemo();
                }
            }
        }
        return instance;
    }


    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            new Thread(() -> {
                SingletonDemo.getInstance();
            }, String.valueOf(i)).start();
        }
    }
}
