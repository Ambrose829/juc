package com.pika.dm01;

/**
 * 简单的卖票
 * 真正的多线程开发，公司中的开发
 * 线程就是一个单独的资源类，没有任何的附属操作
 * 1.属性 2.方法
 */
public class SaleTicketDemo1 {
    public static void main(String[] args) {
        //并发：多线程操作同一个类
        Ticket ticket = new Ticket();

        //@FunctionalInterface 函数式接口, jdk1.8 Lambda表达式
        new Thread(() -> {
            for (int i = 0; i < 40; i ++) {
                ticket.sale();
            }
        },"A" ).start();
        new Thread(() -> {
            for (int i = 0; i < 40; i ++) {
                ticket.sale();
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 40; i ++) {
                ticket.sale();
            }
        }, "C").start();


    }

}

//资源类
class Ticket{
   //1.属性 2.方法
   private int number = 50;

   //卖票的方式
   //synchronized 本质：队列，锁
   public synchronized void sale() {
       if (number > 0) {
           System.out.println(Thread.currentThread().getName()+"卖出前:" + (number--)+"剩余：" + number);
       }
   }
}