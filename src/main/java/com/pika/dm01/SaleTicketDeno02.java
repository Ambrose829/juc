package com.pika.dm01;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SaleTicketDeno02 {
    public static void main(String[] args) {
        //并发：多线程操作同一个类
        Ticket2 ticket = new Ticket2();
        //@FunctionalInterface 函数式接口, jdk1.8 Lambda表达式
        new Thread(() -> {
            for (int i = 0; i < 40; i ++) ticket.sale();
        },"A" ).start();
        new Thread(() -> {
            for (int i = 0; i < 40; i ++) ticket.sale();
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 40; i ++) ticket.sale();
        }, "C").start();
    }
}
//Lock三部曲
//建锁
//加锁
//解锁
class Ticket2{
    //1.属性 2.方法
    private int number = 50;
    Lock lock = new ReentrantLock();
    public void sale() {
        lock.lock();

        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName()+"卖出前:" + (number--)+"剩余：" + number);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
