package com.pika.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Pika
 * @create 2020/9/28
 * @since 1.0.0
 *
 * 题目：synchronized和lock有什么区别？用新的lock有什么好处？
 * 1.原始构成
 *  synchronized是关键字属于JVM层面
 *      monitorenter（底层是通过monitor对象来完成，其实wait/notify等方法也依赖于monitor对象，只有在同步块或方法中才能调wait/notify等方法
 *      monitorexit
 *  lock是具体类（java.util.concurrent.locks.Lock）是api层面的锁
 *
 * 2.使用方法
 *  synchronized不需要用户去手动释放锁，当synchronized代码执行完毕后系统会自动让线程释放锁的占用
 *  ReentrantLock则需要用户去手动释放锁，如果没有释放锁，就有可能出现死锁现象。需要lock()和unlock()配合try/finally语句块来完成
 *
 * 3.等待是否可中断
 *  synchronized不可中断，除非抛出异常或者正常运行完成
 *  ReentrantLock可中断
 *      1.设置超时方法 tryLock(Long timeout, TimeUnit unit)
 *      2.lockInterruptibilty()放代码块中，调用interrupt()方法可中断
 *
 * 4.加锁是否公平
 *  synchronized是非公平锁
 *  ReentrantLock两者都可以，默认非公平锁，构造方法可以传入boolean值，true为公平锁，flase为非公平锁
 *
 * 5.锁绑定多个条件Condition
 *  synchronized没有
 *  ReentrantLock用来实现分组唤醒需要唤醒的线程们，可以精确唤醒，而不是像synchronized要么随即唤醒一个线程要么唤醒全部
 *
 *
 *  题目：多线程按顺序调用，实现A->B->C三个线程启动，要求如下：
 *  AA打印5次，BB打印10次，CC打印15次
 *  紧接着
 *  AA打印5次，BB打印10次，CC打印15次
 *  。。。
 *  来10轮
 *
 *
 */
public class SyncAndReentrantLock {
    public static void main(String[] args) {
        ShareResource shareResource = new ShareResource();
        new Thread(() -> {
            for (int i = 1; i <= 10 ; i++) {
                shareResource.print("A", 5);
            }
        }, "T1").start();
        new Thread(() -> {
            for (int i = 1; i <= 10 ; i++) {
                shareResource.print("B", 10);
            }
        }, "T2").start();
        new Thread(() -> {
            for (int i = 1; i <= 10 ; i++) {
                shareResource.print("C", 15);
            }
        }, "T3").start();
    }
}

class ShareResource {
    private int number = 1;//A:1, B:2, C:3
    private Lock lock = new ReentrantLock();
    private Condition c1 = lock.newCondition();
    private Condition c2 = lock.newCondition();
    private Condition c3 = lock.newCondition();


    public void print(String info, int times) {
        if (times == 5) {
            lock.lock();
            try {
                //1判断
                if (number != 1) {
                    c1.await();
                }
                //2干活
                for (int i = 1; i <= times; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + info);
                }
                number = 2;
                //3.通知
                c2.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        } else if (times == 10) {
            lock.lock();
            try {
                //判断
                if (number != 2) {
                    c2.await();
                }
                for (int i = 1; i <= times; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + info);
                }
                number = 3;
                c3.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        } else if (times == 15) {
            lock.lock();
            try {
                //判断
                if (number != 3) {
                    c3.await();
                }
                for (int i = 1; i <= times; i++) {
                    System.out.println(Thread.currentThread().getName() + "\t" + info);
                }
                number = 1;
                c1.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }


    }
}
