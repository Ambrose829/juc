# 1.什么是JUC

java.util.concurrent在并发编程中使用的工具类

java并发编程工具包

# 2.线程和进程



进程：一个程序，QQ.exe、Music.exe程序的集合

一个进程可以包含多个线程，至少包含一个。

java默认有两个线程：main、GC

线程：开了一个Typroa，写、自动保存（线程负责）

java线程方式：Thread、Runnable、Callable

java不可以开启线程

```java
// 最终调用的是一个本地方法，底层的C++，java无法直接操作硬件
private native void start0();
```



> 并发、并行

并发编程：并发、并行

并发（多个线程操作同一个资源）

* CPU 一核，模拟出来多条线程：快速交替

并行（多个线程一起运行）

* CPU 多核，多个线程可以同时运行

```java
package com.pika.dm01;

public class Test1 {
    public static void main(String[] args) {
        // 获取CPU的核数
        // CPU密集型 IO密集型
        System.out.println(Runtime.getRuntime().availableProcessors());
    }
}

```

并发编程的本质：**充分利用CPU的资源**

> 线程有几个状态

```java
public enum State {
    	//新生
        NEW,
		//运行
        RUNNABLE,
		//阻塞
        BLOCKED,
		//等待
        WAITING,
		//超时等待
        TIMED_WAITING,
    	//终止
        TERMINATED;
    }
```



> wait/sleep区别

* 来自不同的类

  wait --> Object

  sleep --> Thread	*TimeUnit*

* 关于锁的释放

  wait会释放锁，sleep不会释放锁！

* 使用的范围不同

  wait：必须在同步代码块中使用

  sleep：可以在任何地方使用

* 是否需要捕获异常

  wait：~~不需要捕获异常~~  需要捕获异常

  sleep：需要捕获异常

  

|            |                             wait                             |                            sleep                            |
| :--------: | :----------------------------------------------------------: | :---------------------------------------------------------: |
|    同步    | 只能在 synchronized 代码块中调用，<br />否则会报 IllegalMonitorStateException <br />非法监控状态异常 |            不需要在同步方法或者同步代码块中调用             |
|  作用对象  |           wait方法定义在Object类中，作用于对象本身           | sleep方法定义在java.lang.Thread方法中，<br />作用于当前线程 |
| 释放锁资源 |                              是                              |                             否                              |
|  唤醒条件  |           其他线程调用notify()或者notifyAll()方法            |                 超时或者调用interrupt()方法                 |
|  方法属性  |                           实例方法                           |                          静态方法                           |



# 3.Lock锁（重点）

> synchronized锁





> Lock接口

```java
Lock l = ...; 
//加锁
l.lock();
try { 
    // access the resource protected by this lock 
} finally { 
    //释放锁
    l.unlock(); 
} 
```

当在不同范围内发生锁定和解锁时，必须注意确保在锁定时执行的所有代码由try-finally或try-catch保护，以确保在必要时释放锁定。 

![image-20200816135511615](.\images\image-20200816135511615.png) 



![image-20200816135055239](.\images\image-20200816135055239.png) 

公平锁：十分公平，先来后到

非公平锁：不公平，可以插队（默认）

```java
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

```

> Synchronized和Lock的区别

* Synchronized是内置Java关键字，Lock是一个Java类
* Synchronized无法判断获取锁的状态，Lock可以判断是否获取到了锁
* Synchronized会自动释放锁，Lock必须要手动释放锁，如果不释放锁会 **死锁**
* Synchronized   线程1（获得锁）线程2（等待），Lock锁就不一定会等待下去（lock.tryLock()）
* Synchronized 可重入锁，不可以中断，非公平；Lock，可重入锁，可以判断锁，非公平（可以自己设置）
* Synchronized适合少量的代码同步问题，Lock适合锁大量的同步代码

> 锁是什么？如何判断锁的是谁？





# 4. 生产者消费者问题

**面试：单例、8种排序算法、生产者消费者、死锁**

## 4.1生产者和消费者问题 Synchronized锁

> 生产者和消费者问题 Synchronized锁



```java
package com.pika.pc;

import java.util.Date;

/**
 * 线程之间的通信问题，生产者和消费者问题
 * 线程交替执行 A B操作同一个变量 num = 0
 * A num + 1
 * B num - 1
 */
public class PCS {
    public static void main(String[] args) {
        Data data = new Data();
        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
    }
}
//等待，业务，通知
class Data { //数字 资源类
    private int num = 0;
    public synchronized void increment() throws InterruptedException {
        if (num != 0) {
            //等待
            this.wait();
        }
        num ++;
        System.out.println(Thread.currentThread().getName() + "---->" + num);
        //通知其他线程，生产了一个
        this.notifyAll();
    }

    public synchronized void decrement() throws InterruptedException {
        if (num == 0) {
            this.wait();
        }
        num --;
        System.out.println(Thread.currentThread().getName() + "---->" + num);
        //通知其它线程，我消费了一个
        this.notifyAll();
    }
}

```

> 问题：ABCD四个线程 存在虚假唤醒

```
线程也可以唤醒，而不会被通知，中断或超时，即所谓的虚假唤醒 。 虽然这在实践中很少会发生，但应用程序必须通过测试应该使线程被唤醒的条件来防范，并且如果条件不满足则继续等待。 换句话说，等待应该总是出现在循环中，就像这样： 

  synchronized (obj) {
         while (<condition does not hold>)
             obj.wait(timeout);
         ... // Perform action appropriate to condition
     } 
```

**解决办法：将if改为while**

防止虚假唤醒的

```java
package com.pika.pc;

import java.util.Date;

/**
 * 线程之间的通信问题，生产者和消费者问题
 * 线程交替执行 A B操作同一个变量 num = 0
 * A num + 1
 * B num - 1
 */
public class PCS {
    public static void main(String[] args) {
        Data data = new Data();
        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();
    }
}
//等待，业务，通知
class Data { //数字 资源类
    private int num = 0;
    public synchronized void increment() throws InterruptedException {
        while (num != 0) {
            //等待
            this.wait();
        }
        num ++;
        System.out.println(Thread.currentThread().getName() + "---->" + num);
        //通知其他线程，生产了一个
        this.notifyAll();
    }

    public synchronized void decrement() throws InterruptedException {
        while (num == 0) {
            this.wait();
        }
        num --;
        System.out.println(Thread.currentThread().getName() + "---->" + num);
        //通知其它线程，我消费了一个
        this.notifyAll();
    }
}

```

## 4.2生产者消费者 JUC

> 生产者消费者JUC

![image-20200816145209340](.\images\image-20200816145209340.png) 

> 代码实现

```java
package com.pika.pc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PCL {
    public static void main(String[] args) {
        Data2 data = new Data2();
        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.increment();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i ++) {
                try {
                    data.decrement();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "D").start();
    }
}
//等待，业务，通知
class Data2 { //数字 资源类
    private int num = 0;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public void increment() throws InterruptedException {
        lock.lock();
        try {
            while (num != 0) {
                //等待
                condition.await();
            }
            num ++;
            condition.signalAll();
            System.out.println(Thread.currentThread().getName() + "---->" + num);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }

    public void decrement() throws InterruptedException {
        lock.lock();
        try {
            while (num == 0) {
                //等待
                condition.await();
            }
            num --;
            condition.signalAll();
            System.out.println(Thread.currentThread().getName() + "---->" + num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

    }
}

```

> 问题，线程执行无序，想要达到 A -> B -> C -> D的效果

```java
package com.pika.pc;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PCL2 {
    public static void main(String[] args) {
        Data3 data = new Data3();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.printA();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A").start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.printB();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    data.printC();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "C").start();


    }
}
//等待，业务，通知
class Data3 { //数字 资源类
    private int num = 1;
    Lock lock = new ReentrantLock();
    Condition condition1 = lock.newCondition();
    Condition condition2 = lock.newCondition();
    Condition condition3 = lock.newCondition();

    public void printA() throws InterruptedException {
        lock.lock();
        try {
            while (num != 1) {
                condition1.await();
            }
            System.out.println(Thread.currentThread().getName() + "printA");
            num = 2;
            condition2.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printB() throws InterruptedException {
        lock.lock();
        try {
            while (num != 2) {
                condition2.await();
            }
            System.out.println(Thread.currentThread().getName() + "printB");
            num = 3;
            condition3.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public void printC() throws InterruptedException {
        lock.lock();
        try {
            while (num != 3) {
                condition3.await();
            }
            System.out.println(Thread.currentThread().getName() + "printC");
            num = 1;
            condition1.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}


```



# 5.关于锁的八个问题

> 如何判断锁的是谁，永远知道什么



## 5.1.标准情况下，两个线程谁先打印，sendMes，还是call

```java
package com.pika.lock8;

import java.util.concurrent.TimeUnit;

public class Test1 {
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(() -> {
            phone.sendMes();
        }, "A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.call();
        },"B").start();
    }
}

class Phone {
    public synchronized void sendMes() {
        System.out.println("sendMes");
    }

    public synchronized void call() {
        System.out.println("call");
    }
}

```

> 答案：先sendMes再call，因为synchronized锁的对象是方法的调用者，这两个方法用的是同一个锁，谁先拿到谁执行



## 5.2.sendmes延迟四秒，两个线程谁先打印，sendMes，还是call

```java
package com.pika.lock8;

import java.util.concurrent.TimeUnit;
public class Test1 {
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(() -> {
            phone.sendMes();
        }, "A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.call();
        },"B").start();
    }
}

class Phone {
    public synchronized void sendMes() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendMes");
    }

    public synchronized void call() {
        System.out.println("call");
    }
}

```



> 答案：先sendMes再call，因为synchronized锁的对象是方法的调用者，这两个方法用的是同一个锁，谁先拿到谁执行



## 5.3. 增加一个普通方法hello，将线程B的call换成hello，先sendMes还是hello



```java
package com.pika.lock8;

import java.util.concurrent.TimeUnit;

public class Test2 {
    public static void main(String[] args) {
        Phone2 phone = new Phone2();
        new Thread(() -> {
            phone.sendMes();
        }, "A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.hello();
        },"B").start();
    }
}

class Phone2 {
    public synchronized void sendMes() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendMes");
    }

    public synchronized void call() {
        System.out.println("call");
    }

    public void hello() {
        System.out.println("hello");
    }
}

```



> 答案：先hello再sendMes， 因为hello()方法是普通方法，不是同步方法（没有锁），不受锁的影响



## 5.4.两个对象，两个线程分别执行不同的对象的sendMes方法、call方法，先sendMes还是call



```java
package com.pika.lock8;

import java.util.concurrent.TimeUnit;

public class Test2 {
    public static void main(String[] args) {
        Phone2 phone1 = new Phone2();
        Phone2 phone2 = new Phone2();
        new Thread(() -> {
            phone1.sendMes();
        }, "A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone2.call();
        },"B").start();
    }
}

class Phone2 {
    public synchronized void sendMes() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendMes");
    }

    public synchronized void call() {
        System.out.println("call");
    }

    public void hello() {
        System.out.println("hello");
    }
}

```



> 答案：先call再sendMes，因为两个方法不是同一个对象的了，不是同一把锁，互不影响



## 5.5.将两个方法变成static静态方法，先sendMes还是call

```java
package com.pika.lock8;

import java.util.concurrent.TimeUnit;

public class Test3 {
    public static void main(String[] args) {
        Phone3 phone = new Phone3();
        new Thread(() -> {
            phone.sendMes();
        }, "A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.call();
        },"B").start();
    }
}

class Phone3 {
    //synchronized 锁的对象是方法的调用者
    //static 静态方法
    //类加载时就有了！Class模板，这里锁的是class对象，class全局唯一
    public static synchronized void sendMes() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendMes");
    }

    public static synchronized void call() {
        System.out.println("call");
    }
}

```

> 答案：先sendMes再call，这时候因为加了static变成了静态方法，而静态方法是在类加载时就加载了，是Class模板，这里锁的就变成了Class对象，Class全局唯一。



## 5.6.将两个方法变成static静态方法，两个对象，两个线程分别执行不同的对象的sendMes方法、call方法，先sendMes还是call

```java
package com.pika.lock8;

import java.util.concurrent.TimeUnit;

public class Test3 {
    public static void main(String[] args) {
        Phone3 phone1 = new Phone3();
        Phone3 phone2 = new Phone3();
        new Thread(() -> {
            phone1.sendMes();
        }, "A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone2.call();
        },"B").start();
    }
}

class Phone3 {
    //synchronized 锁的对象是方法的调用者
    //static 静态方法
    //类加载时就有了！Class模板，这里锁的是class对象，class全局唯一
    public static synchronized void sendMes() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendMes");
    }

    public static synchronized void call() {
        System.out.println("call");
    }
}

```

> 答案：先sendMes再call，这时候因为加了static变成了静态方法，而静态方法是在类加载时就加载了，是Class模板，这里锁的就变成了Class对象，Class全局唯一，不管是几个对象，用的都是同一个Class，索引本质上两个对象用的还是同一把锁

## 5.7.一个静态同步方法sendMes，一个普通同步方法call，一个对象，两个线程谁先打印，sendMes，还是call

```java
package com.pika.lock8;

import java.util.concurrent.TimeUnit;

/**
 * 7.一个静态同步方法sendMes，一个普通同步方法call，一个对象，两个线程谁先打印，sendMes，还是call
 */
public class Test4 {
    public static void main(String[] args) {
        Phone4 phone = new Phone4();

        new Thread(() -> {
            phone.sendMes();
        }, "A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone.call();
        },"B").start();
    }
}

class Phone4 {

    //静态同步方法
    public static synchronized void sendMes() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendMes");
    }
    //普通同步方法
    public synchronized void call() {
        System.out.println("call");
    }
}

```

> 答案：先call再sendMes，因为两个锁锁的东西不一样，静态同步方法sendMes锁的是class类模板，普通同步方法call锁的是方法的调用者，不是同一把锁，所以不受影响



## 5.8.一个静态同步方法sendMes，一个普通同步方法call，两个对象，两个线程谁先打印，sendMes，还是call

```java
package com.pika.lock8;

import java.util.concurrent.TimeUnit;

/**
 * 8.一个静态同步方法sendMes，一个普通同步方法call，两个对象，两个线程谁先打印，sendMes，还是call
 */
public class Test4 {
    public static void main(String[] args) {
        Phone4 phone1 = new Phone4();
        Phone4 phone2 = new Phone4();


        new Thread(() -> {
            phone1.sendMes();
        }, "A").start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(() -> {
            phone2.call();
        },"B").start();
    }
}

class Phone4 {

    //静态同步方法
    public static synchronized void sendMes() {
        try {
            TimeUnit.SECONDS.sleep(4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("sendMes");
    }
    //普通同步方法
    public synchronized void call() {
        System.out.println("call");
    }
}

```

> 答案：先call再sendMes，因为不是一个锁



## 5.9小结

synchronized	锁的是方法的调用者	new	不唯一	每次new出来的对象的锁都是新的

static synchronized	锁的是class	唯一的一个模板 

# 6.集合类不安全

## 6.1.List不安全

```java
package com.pika.unsafe;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

//java.util.ConcurrentModificationException 并发修改异常
public class ListTest {
    public static void main(String[] args) {
//        List<String> list = Arrays.asList("A", "B", "C");
//        list.forEach(System.out::println);
        /**
         * 并发下ArrayList线程不安全
         * 解决方案:
         * 1.List<String> list = new Vector();
         * 2.List<String> list = Collections.synchronizedList(new ArrayList<>());
         * 3.List<String> list = new CopyOnWriteArrayList();
         *
         */
//        List<String> list = new Vector();
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
        //CopyOnWrite 写入时复制 COW 计算机领域的一种优化策略
        //多个线程调用的时候，List，读操作不受影响，读的是原本的数组，写入的时候会加锁以防止数据覆盖，
        //并且是从原本的数组中复制一份新的数组，在新的数组中写入值，所以写操作不影响读，当写入完值之后，用新的数组替换掉原本的数组
        
        //读写分离
        List<String> list = new CopyOnWriteArrayList();
        for (int i = 0; i < 10; i ++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString());
                System.out.println(list);
            }, String.valueOf(i)).start();
        }

    }
}

```

## 6.2.Set不安全

```java
package com.pika.unsafe;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 同理可证：ConcurrentModificationException
 * 解决方案：
 * 1.Set<String> set = Collections.synchronizedSet(new HashSet<>());
 * 2.Set<String> set = new CopyOnWriteArraySet<>();
 */
public class SetTest {
    public static void main(String[] args) {
//        Set<String> set = new HashSet<>();
//        Set<String> set = Collections.synchronizedSet(new HashSet<>());
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 30; i ++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString());
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }
}

```

HashSet的底层是什么？

```java
public HashSet() {
	map = new HashMap<>();
}
//set的本质是map的key，因为map的key不重复
public boolean add(E e) {
    return map.put(e, PRESENT)==null;
}

```

## 6.3.Map不安全

```java
package com.pika.unsafe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 同理：ConcurrentModificationException
 * 解决办法：
 * 1.Map<String, String> map = new Hashtable<>();
 * 2.Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
 * 3.Map<String, String> map = new ConcurrentHashMap<>();
 *
 */
public class MapTest {
    public static void main(String[] args) {

//        Map<String, String> map = new HashMap<>();
//        Map<String, String> map = new Hashtable<>();
//        Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
        Map<String, String> map = new ConcurrentHashMap<>();

        for (int i = 0; i < 30; i++) {
            new Thread(()->{
                map.put(Thread.currentThread().getName().toString(), UUID.randomUUID().toString().substring(0,5));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }
}

```

# 7.Callable

![image-20200816180757638](.\images\image-20200816180757638.png)

* 可以有返回值

* 可以抛出异常
* 方法不同，run() / call()

> 代码测试

![image-20200816183325068](.\images\image-20200816183325068.png)

```java
package com.pika.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CallableTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
//        new Thread(new MyThread()).start();
        /**
         * new Thread(new Runnable()).start();
         * new Thread(new FutureTask<>()).start();
         * new Thread(new FutureTask<>(Callable)).start();
         */

        MyThread myThread = new MyThread();
        FutureTask<Integer> futureTask = new FutureTask<>(myThread);
        //打印几个call，结果会被缓存，效率高
        new Thread(futureTask, "A").start();
        new Thread(futureTask, "B").start();
        //获取callable的返回值
        //get方法可能会导致阻塞，把他放在最后，或者使用异步通信
        Integer integer = futureTask.get();
        System.out.println(integer);

    }
}

//class MyThread implements Runnable {
//
//    @Override
//    public void run() {
//
//    }
//}

class MyThread implements Callable<Integer> {

    @Override
    public Integer call() throws Exception {
        System.out.println("call()");
        return 1024;
    }
}
```



# 8.常用的辅助类

## 8.1.CountDownLatch（闭锁）

减法计数器

```java
package com.pika.add;

import java.util.concurrent.CountDownLatch;
//减法计数器
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        //总数是六, 必须要执行任务的时候使用
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 1; i < 7; i ++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "go out!");
                //数量减一
                countDownLatch.countDown();
            }, String.valueOf(i)).start();
        }


        //等待计数器归零，然后再向下执行，确保全部跑完才进行下面的关门输出
        countDownLatch.await();

        System.out.println("close door！");
    }
}

```

原理：

`countDownLatch.countDown();`//数量减一

`countDownLatch.await();`//等待计数器归零，然后再向下执行

每次有线程调用countDown(),countDownLatch中的数量减一，假设计数器变为0，countDownLatch.await();就会被唤醒，继续执行

## 8.2.CyclicBarrier（线程计数器）

加法计数器

> - 允许一组线程全部等待彼此达到共同屏障点的同步辅助。循环阻塞在涉及固定大小的线程方的程序中很有用，这些线程必须偶尔等待彼此。屏障被称为*循环*  ，因为它可以在等待的线程被释放之后重新使用。
> - A `CyclicBarrier`支持一个可选的[`Runnable`](../../../java/lang/Runnable.html)命令，每个屏障点运行一次，在派对中的最后一个线程到达之后，但在任何线程释放之前。  在任何一方继续进行之前，此*屏障操作*对更新共享状态很有用。 



```java
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

```





## 8.3.Semaphore（信号量）

信号量

> 一个计数信号量。在概念上，信号量维持一组许可证。如果有必要，每个[`acquire()`都会](../../../java/util/concurrent/Semaphore.html#acquire--)阻塞，直到许可证可用，然后才能使用它。每个[`release()`](../../../java/util/concurrent/Semaphore.html#release--)添加许可证，潜在地释放阻塞获取方。但是，没有使用实际的许可证对象;`Semaphore`只保留可用数量的计数，并相应地执行。
>
> 信号量通常用于限制线程数，而不是访问某些（物理或逻辑）资源。  例如，这是一个使用信号量来控制对一个项目池的访问的类： 
>
> ```java
> class Pool { 
> 	private static final int MAX_AVAILABLE = 100; 
>    	private final Semaphore available = new Semaphore(MAX_AVAILABLE, true); 
>    	public Object getItem() throws InterruptedException { 
>    		available.acquire(); 
>    		return getNextAvailableItem(); 
>    	} 
>    	public void putItem(Object x) { 
>    		if (markAsUnused(x)) 
>    			available.release(); 
>    	} 
>    	// Not a particularly efficient data structure; just for demo 
>     protected Object[] items = ... whatever kinds of items being managed 
>     protected boolean[] used = new boolean[MAX_AVAILABLE]; 
>     protected synchronized Object getNextAvailableItem() { 
>         for (int i = 0; i < MAX_AVAILABLE; ++i) { 
>             if (!used[i]) { 
>                 used[i] = true; 
>                 return items[i]; 
>             } 
>         } 
>         return null; // not reached 
>     } 
>     protected synchronized boolean markAsUnused(Object item) { 
>         for (int i = 0; i < MAX_AVAILABLE; ++i) { 
>             if (item == items[i]) {
>                 if (used[i]) { 
>                     used[i] = false; 
>                     return true; 
>                 } else return false; 
>             } 
>         } 
>         return false; 
>     } 
> } 
> ```
>
> 在获得项目之前，每个线程必须从信号量获取许可证，以确保某个项目可用。  当线程完成该项目后，它将返回到池中，并将许可证返回到信号量，允许另一个线程获取该项目。 请注意，当[调用`acquire()`](../../../java/util/concurrent/Semaphore.html#acquire--)时，不会保持同步锁定，因为这将阻止某个项目返回到池中。  信号量封装了限制对池的访问所需的同步，与保持池本身一致性所需的任何同步分开。 
>
> 信号量被初始化为一个，并且被使用，使得它只有至多一个允许可用，可以用作互斥锁。  这通常被称为*二进制信号量* ，因为它只有两个状态：一个许可证可用，或零个许可证可用。  当以这种方式使用时，二进制信号量具有属性（与许多[`Lock`](../../../java/util/concurrent/locks/Lock.html)实现不同），“锁”可以由除所有者之外的线程释放（因为信号量没有所有权概念）。  这在某些专门的上下文中是有用的，例如死锁恢复。 
>
> 此类的构造函数可选择接受*公平*参数。  当设置为false时，此类不会保证线程获取许可的顺序。 特别是，  *闯入*是允许的，也就是说，一个线程调用[`acquire()`](../../../java/util/concurrent/Semaphore.html#acquire--)可以提前已经等待线程分配的许可证-在等待线程队列的头部逻辑新的线程将自己。  当公平设置为真时，信号量保证调用[`acquire`](../../../java/util/concurrent/Semaphore.html#acquire--)方法的线程被选择以按照它们调用这些方法的顺序获得许可（先进先出;  FIFO）。 请注意，FIFO排序必须适用于这些方法中的特定内部执行点。  因此，一个线程可以在另一个线程之前调用`acquire`  ，但是在另一个线程之后到达排序点，并且类似地从方法返回。 另请注意， [未定义的`tryAcquire`](../../../java/util/concurrent/Semaphore.html#tryAcquire--)方法不符合公平性设置，但将采取任何可用的许可证。 
>
> 通常，用于控制资源访问的信号量应该被公平地初始化，以确保线程没有被访问资源。  当使用信号量进行其他类型的同步控制时，非正常排序的吞吐量优势往往超过公平性。 
>
> 本课程还提供了方便的方法， [一次`acquire`](../../../java/util/concurrent/Semaphore.html#acquire-int-)和[`release`](../../../java/util/concurrent/Semaphore.html#release-int-)多个许可证。  当没有公平地使用这些方法时，请注意增加无限期延期的风险。 
>
> 内存一致性效应：在另一个线程中[成功](package-summary.html#MemoryVisibility)执行“获取”方法（如`acquire()`之前，调用“释放”方法之前的线程中的操作，例如`release()`  [*happen-before*](package-summary.html#MemoryVisibility)  。 



```java
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
                    System.out.println(Thread.currentThread().getName() + "抢到车位");
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName() + "离开车位");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //每个release()添加许可证，潜在地释放阻塞获取方
                    semaphore.release();
                }

                
            }, String.valueOf(i)).start();
        }
    }
}

```

原理：

semaphore.acquire(); 获得，假设已经满了，等待，等待被释放为止！

semaphore.release(); 释放，会将当前的信号量释放+1，然后唤醒等待的线程！

作用：多个共享资源互斥的使用！并发限流，控制最大线程数



# 9.ReadWriteLock（读写锁）

> 读的时候可以被多个线程同时去读，写的时候只能有 一个线程去写



> A `ReadWriteLock`维护一对关联的[`locks`](../../../../java/util/concurrent/locks/Lock.html)  ，一个用于只读操作，一个用于写入。[`read  lock`](../../../../java/util/concurrent/locks/ReadWriteLock.html#readLock--)可以由多个阅读器线程同时进行，只要没有作者。[`write  lock`](../../../../java/util/concurrent/locks/ReadWriteLock.html#writeLock--)是独家的。
>
> 所有`ReadWriteLock`实现必须保证的存储器同步效应`writeLock`操作（如在指定[`Lock`](../../../../java/util/concurrent/locks/Lock.html)接口）也保持相对于所述相关联的`readLock`  。 也就是说，一个线程成功获取读锁定将会看到在之前发布的写锁定所做的所有更新。 
>
> 读写锁允许访问共享数据时的并发性高于互斥锁所允许的并发性。 它利用了这样一个事实：一次只有一个线程（  *写入*线程）可以修改共享数据，在许多情况下，任何数量的线程都可以同时读取数据（因此*读取器*线程）。  从理论上讲，通过使用读写锁允许的并发性增加将导致性能改进超过使用互斥锁。  实际上，并发性的增加只能在多处理器上完全实现，然后只有在共享数据的访问模式是合适的时才可以。 
>
> 读写锁是否会提高使用互斥锁的性能取决于数据被读取的频率与被修改的频率相比，读取和写入操作的持续时间以及数据的争用 -  即是，将尝试同时读取或写入数据的线程数。  例如，最初填充数据的集合，然后经常被修改的频繁搜索（例如某种目录）是使用读写锁的理想候选。  然而，如果更新变得频繁，那么数据的大部分时间将被专门锁定，并且并发性增加很少。  此外，如果读取操作太短，则读写锁定实现（其本身比互斥锁更复杂）的开销可以支配执行成本，特别是因为许多读写锁定实现仍将序列化所有线程通过小部分代码。  最终，只有剖析和测量将确定使用读写锁是否适合您的应用程序。 
>
> 虽然读写锁的基本操作是直接的，但是执行必须做出许多策略决策，这可能会影响给定应用程序中读写锁定的有效性。  这些政策的例子包括： 
>
> - 在写入器释放写入锁定时，确定在读取器和写入器都在等待时是否授予读取锁定或写入锁定。  作家偏好是常见的，因为写作预计会很短，很少见。  读者喜好不常见，因为如果读者经常和长期的预期，写作可能导致漫长的延迟。 公平的或“按顺序”的实现也是可能的。 
> - 确定在读卡器处于活动状态并且写入器正在等待时请求读取锁定的读取器是否被授予读取锁定。  读者的偏好可以无限期地拖延作者，而对作者的偏好可以减少并发的潜力。 
> - 确定锁是否可重入：一个具有写锁的线程是否可以重新获取？ 持有写锁可以获取读锁吗？  读锁本身是否可重入？ 
> - 写入锁可以降级到读锁，而不允许插入写者？ 读锁可以升级到写锁，优先于其他等待读者或作者吗？ 
>
> 在评估应用程序的给定实现的适用性时，应考虑所有这些问题。



```java
package com.pika.rw;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 独占锁（写锁 一次只能被一个线程占有）
 * 共享锁（读锁 一次可以被多个线程占有）
 * ReadWriteLock
 * 读读   可以共存
 * 读写   不可以共存
 * 写写   不能共存
 */
public class ReadWriteLockDemo {

    public static void main(String[] args) {
        MyCacheLock myCache = new MyCacheLock();
        for (int i = 1; i <= 5; i++) {
            final int temp = i;
            new Thread(() -> {
                myCache.put(temp+"", temp);
                myCache.get(temp+"");
            }, String.valueOf(i)).start();
        }

//        for (int i = 1; i <= 5; i++) {
//            final int temp = i;
//            new Thread(() -> {
//                myCache.get(temp+"");
//            }, String.valueOf(i)).start();
//        }
    }
}
/*
    自定义缓存
 */
class MyCacheLock {
    private volatile Map<String, Object> map = new HashMap<>();
    //读写锁，更加细粒度的控制
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    // 存，写入的时候希望同时只有一个线程写
    public void put(String key, Object value) {
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "写入" + key);
            map.put(key, value);
            System.out.println(Thread.currentThread().getName() + "写入成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    // 取，读的时候希望所有人都可以读
    public void get(String key) {
        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "读取" + key);
            Object o = map.get(key);
            System.out.println(Thread.currentThread().getName() + "读取成功，数据为" + o);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
```



# 10.BlockingQueue（阻塞队列）

## 10.1概念

队列

​	先进先出的数据结构

阻塞

​	对于写入来说：如果队列满了，必须阻塞等待

​	对于读取来说：如果队列是空的，必须阻塞等待写入

阻塞队列

> All Superinterfaces: 
> Collection <E>， Iterable <E>， Queue <E> 
> All Known Subinterfaces: 
> BlockingDeque <E>， TransferQueue <E> 
> 所有已知实现类： 
> ArrayBlockingQueue ， DelayQueue ， LinkedBlockingDeque ， LinkedBlockingQueue ， LinkedTransferQueue ， PriorityBlockingQueue ， SynchronousQueue

BlockingQueue不是新的东西

![image-20200817111720959](.\images\image-20200817111720959.png)

使用情景：多线程并发处理、线程池

## 10.2.使用

添加、移除

**四组API**

|     方式     | 抛出异常  | 不会抛出异常 | 阻塞等待 |           超时等待            |
| :----------: | :-------: | :----------: | :------: | :---------------------------: |
|     添加     |   add()   |   offer()    |  put()   | offer(o, timeout, TimeUnit.*) |
|     移除     | remove()  |    poll()    |  take()  |   poll(timeout, TimeUnit.*)   |
| 查看队列首位 | element() |    peek()    |    -     |               -               |

* 抛出异常

  ```java
  /**
       * 抛出异常
       */
      public static void test1() {
          //队列大小
          ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);
          System.out.println(blockingQueue.add("a"));
          System.out.println(blockingQueue.add("b"));
          System.out.println(blockingQueue.add("c"));
  
          //java.lang.IllegalStateException: Queue full
  //        System.out.println(blockingQueue.add("d"));
          System.out.println(blockingQueue.remove());
          System.out.println(blockingQueue.remove());
          System.out.println(blockingQueue.remove());
  
          //java.util.NoSuchElementException
          System.out.println(blockingQueue.remove());
  
  
      }
  ```

  

* 不会抛出异常

  ```java
  /**
       * 不抛出异常
       */
      public static void test2() {
          ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);
          System.out.println(blockingQueue.offer("a"));
          System.out.println(blockingQueue.offer("b"));
          System.out.println(blockingQueue.offer("c"));
  
          System.out.println(blockingQueue.offer("d"));
  
  
          System.out.println(blockingQueue.poll());
          System.out.println(blockingQueue.poll());
          System.out.println(blockingQueue.poll());
  
          System.out.println(blockingQueue.poll());
      }
  ```

  

* 阻塞等待

  ```java
  /**
       * 等待，阻塞（一直阻塞）
       */
      public static void test3() throws InterruptedException {
          ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);
          blockingQueue.put("a");
          blockingQueue.put("b");
          blockingQueue.put("c");
  
  //        blockingQueue.put("d");
  
  
          System.out.println(blockingQueue.take());
          System.out.println(blockingQueue.take());
          System.out.println(blockingQueue.take());
  
          System.out.println(blockingQueue.take());
  
  
      }
  ```

  

* 超时等待

  ```java
   /**
       * 等待，阻塞（等待超时）
       */
      public static void test4() throws InterruptedException {
          ArrayBlockingQueue blockingQueue = new ArrayBlockingQueue<>(3);
          System.out.println(blockingQueue.offer("a"));
          System.out.println(blockingQueue.offer("b"));
          System.out.println(blockingQueue.offer("c"));
          //两秒后超时失败
          System.out.println(blockingQueue.offer("d",2, TimeUnit.SECONDS));
  
  
          System.out.println(blockingQueue.poll());
          System.out.println(blockingQueue.poll());
          System.out.println(blockingQueue.poll());
  
          System.out.println(blockingQueue.poll(2, TimeUnit.SECONDS));
  
      }
  ```



##  10.3.SynchronousQueue（同步队列）

没有容量，进去一个元素，必须等待取出来后才能往里面放下一个元素

put()	take()

```java
package com.pika.bq;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * 同步队列
 * 和其它的BlockingQueue不一样，SynchronousQueue 不存储元素
 * 如果向里面put了一个元素，必须从里面取出来，否则不能在put进去值
 */
public class SychronousQueueDemo {
    public static void main(String[] args) {
        //同步队列
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<String>();

        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "put 1");
                synchronousQueue.put("1");
                System.out.println(Thread.currentThread().getName() + "put 2");
                synchronousQueue.put("2");
                System.out.println(Thread.currentThread().getName() + "put 3");
                synchronousQueue.put("3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T1").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName() + "get " + synchronousQueue.take());
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName() + "get " + synchronousQueue.take());
                TimeUnit.SECONDS.sleep(3);
                System.out.println(Thread.currentThread().getName() + "get " + synchronousQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "T2").start();

    }
}

```





# 11.线程池（重要）

## 11.1.池化技术

> 池化技术

程序的运行，本质：占用系统的资源！优化资源的使用 => 池化技术

线程池、连接池、内存池、对象池....	创建和销毁十分浪费资源

> 池化技术：实现准备好一些资源，有人要用，就来我这里拿，用完之后还给我
>
> 默认大小，最大值

**线程池的好处**

* 降低资源的消耗

* 提高响应的速度

* 方便管理

  **线程复用、可以控制最大并发数、管理线程**

## 11.2.Executors三大方法

> ```java
> //创建一个单个线程的线程池
> Executors.newSingleThreadExecutor();
> //创建一个固定的线程数量的线程池
> Executors.newFixedThreadPool(5);
> //创建一个可伸缩的线程池
> Executors.newCachedThreadPool();
> ```



```java
package com.pika.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executors 工具类、三大方法
 * 创建一个单个线程的线程池
 * Executors.newSingleThreadExecutor();
 * 创建一个固定的线程数量的线程池
 * Executors.newFixedThreadPool(5);
 * //创建一个可伸缩的线程池
 * Executors.newCachedThreadPool();
 */
public class Demo01 {
    public static void main(String[] args) {
        //创建一个单个线程的线程池
//        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        //创建一个固定的线程数量的线程池
//        ExecutorService threadPool = Executors.newFixedThreadPool(5);
        //创建一个可伸缩的线程池
        ExecutorService threadPool = Executors.newCachedThreadPool();

        try {
            for (int i = 0; i < 10; i++) {
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"111");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //线程池用完，程序结束，关闭线程池
            threadPool.shutdown();
        }




    }
}

```

## 11.3.Executors7大参数

源码分析：

```java
public static ExecutorService newSingleThreadExecutor() {
        return new FinalizableDelegatedExecutorService
            (new ThreadPoolExecutor(1, 1,
                                    0L, TimeUnit.MILLISECONDS,
                                    new LinkedBlockingQueue<Runnable>()));
    }

public static ExecutorService newFixedThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads,
                                      0L, TimeUnit.MILLISECONDS,
                                      new LinkedBlockingQueue<Runnable>());
    }

public static ExecutorService newCachedThreadPool() {
        return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                      60L, TimeUnit.SECONDS,
                                      new SynchronousQueue<Runnable>());
    }

//本质调用了 ThreadPoolExecutor
public ThreadPoolExecutor(int corePoolSize,//核心线程池大小
                              int maximumPoolSize,// 最大核心线程池大小
                              long keepAliveTime,// 超时了，没有人调用就会释放
                              TimeUnit unit,// 超时单位
                              BlockingQueue<Runnable> workQueue,// 阻塞队列
                              ThreadFactory threadFactory,// 线程工厂，创建线程的，一般不用动
                              RejectedExecutionHandler handler) {//拒绝策略
        if (corePoolSize < 0 ||
            maximumPoolSize <= 0 ||
            maximumPoolSize < corePoolSize ||
            keepAliveTime < 0)
            throw new IllegalArgumentException();
        if (workQueue == null || threadFactory == null || handler == null)
            throw new NullPointerException();
        this.acc = System.getSecurityManager() == null ?
                null :
                AccessController.getContext();
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.workQueue = workQueue;
        this.keepAliveTime = unit.toNanos(keepAliveTime);
        this.threadFactory = threadFactory;
        this.handler = handler;
    }
```



## 11.4.四种拒绝策略

四种拒绝策略

>  * new ThreadPoolExecutor.AbortPolicy()//银行满了，还有人进来不处理这个人，抛出异常
>  * new ThreadPoolExecutor.CallerRunsPolicy()//银行满了，还有人进来，哪来的去哪里，main处理
>  * new ThreadPoolExecutor.DiscardPolicy()//队列满了，丢掉新来的线程，不抛出异常
>  * new ThreadPoolExecutor.DiscardOldestPolicy()//队列满了，尝试去和最早的线程竞争，不成功就丢掉，不抛出异常



**手动写一个线程池**

```java
package com.pika.pool;

import java.util.concurrent.*;

/**
 * 拒绝策略4种
 * new ThreadPoolExecutor.AbortPolicy()//银行满了，还有人进来不处理这个人，抛出异常
 * new ThreadPoolExecutor.CallerRunsPolicy()//银行满了，还有人进来，哪来的去哪里，main处理
 * new ThreadPoolExecutor.DiscardPolicy()//队列满了，丢掉新来的线程，不抛出异常
 * new ThreadPoolExecutor.DiscardOldestPolicy()//队列满了，尝试去和最早的线程竞争，不成功就丢掉，不抛出异常
 */
public class Demo02 {
    public static void main(String[] args) {

        //自定义线程池！工作 ThreadPoolExecutor
        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                5,
                3,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()//队列满了，尝试去和最早的线程竞争，不成功就丢掉，不抛出异常
        );

        try {
            //最大承载，最大值+队列
            for (int i = 0; i < 9; i++) {
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"111");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //线程池用完，程序结束，关闭线程池
            threadPool.shutdown();
        }




    }
}

```



## 11.5.小结

最大线程如何定义？

* CPU密集型	
  * 根据电脑的CPU核数来定义，几核就定义几个
* IO密集型
  * 大于你系统中十分消耗IO的线程数，一般是两倍
  * 假如程序中有15个大型任务，而且他们的IO都非常占用资源，这时最大线程可以设置为30

```java
 /**
         * 自定义线程池！工作 ThreadPoolExecutor
         * * CPU密集型
         *   * 根据电脑的CPU核数来定义，几核就定义几个
         * 
         * * IO密集型
         *   * 大于你系统中十分消耗IO的线程数，一般是两倍
         *   * 假如程序中有15个大型任务，而且他们的IO都非常占用资源，这时最大线程可以设置为30
         */
        //获取当前电脑cpu核数
        System.out.println(Runtime.getRuntime().availableProcessors());

        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                Runtime.getRuntime().availableProcessors(),
                3,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()
        );
```



# 12.四大函数式接口（必须掌握）

新时代程序员：lambda表达式，链式编程，函数式接口，Stream流式计算



> 函数式接口：只有一个方法的接口

```java
@FunctionalInterface
public interface Runnable {
    public abstract void run();
}

//java中存在很多函数式接口
//它可以简化编程模型，在新版本的框架底层大量应用
//foreach()参数就是一个消费者类型的函数式接口
```

<img src=".\images\image-20200817210203465.png" alt="image-20200817210203465" style="zoom:80%;" />

> Function	函数式接口



```java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
```



```java
package com.pika.function;

import java.util.function.Function;

/**
 * Function函数式接口，有一个输入参数，有一个输出参数
 * 只要是函数式接口，就可以使用lambda表达式
 */
public class FunctionDemo {
    public static void main(String[] args) {
//        Function<String, String> function = new Function<String, String>() {
//            @Override
//            public String apply(String str) {
//                return str;
//            }
//        };
        Function<String, String> function = (str)->{ return str; };
        System.out.println(function.apply("131242342"));
    }
}

```



> Predicate	断言式接口



```java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);

```



```java
package com.pika.function;

import java.util.function.Predicate;

/**
 * 断言式接口：只有一个输入参数，返回值必须是布尔类型
 */
public class PredicateDemo {
    public static void main(String[] args) {

//        //假设用断言写一个判断字符串是否为空
//        Predicate<String> predicate = new Predicate<String>() {
//            @Override
//            public boolean test(String str) {
//                return str.isEmpty();
//            }
//        };
        Predicate<String> predicate = (str)->{return str.isEmpty();};
        System.out.println(predicate.test("1"));
    }
}

```



> Supplier	供给式接口



```java
@FunctionalInterface
public interface Supplier<T> {
    T get();
```



```java
package com.pika.function;

import java.util.function.Supplier;

/**
 * 供给式接口：没有参数（输入），只有返回值
 */
public class SupplierDemo {
    public static void main(String[] args) {
//        Supplier<Integer> supplier = new Supplier<Integer>() {
//            @Override
//            public Integer get() {
//                return 1024;
//            }
//        };

        Supplier<Integer> supplier = ()->{return 1024;};
        System.out.println(supplier.get());
    }
}

```













> Consumer	消费式接口



```java
@FunctionalInterface
public interface Consumer<T> {
    void accept(T t);
```



```java
package com.pika.function;

import java.util.function.Consumer;

/**
 * 消费式接口，只有输入（参数），没有返回值
 */
public class ConsumerDemo {
    public static void main(String[] args) {
//        Consumer<String> consumer = new Consumer<String>() {
//            @Override
//            public void accept(String s) {
//                System.out.println(s);
//            }
//        };
        Consumer<String> consumer = (str)->{
            System.out.println(str);
        };

        consumer.accept("sad");

    }
}

```



# 13.Stream流式运算

> 什么是Stream流式运算

大数据：存储+计算

集合、MySQL本质就是存储东西的，计算都应该交给流来做

```java
package com.pika.stream;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;

import java.util.Arrays;
import java.util.List;

/**
 * 题目要求：一分钟内完成此题，只能用一行代码实现
 * 现有5个用户！筛选：
 * 1.ID必须是偶数
 * 2.年龄必须大于20岁
 * 3.用户名转为大写字母
 * 4.用户名字倒着排序
 * 5.只输出一个用户
 */
public class Test {
    public static void main(String[] args) {
        User u1 = new User(1, "a", 21);
        User u2 = new User(2, "b", 22);
        User u3 = new User(3, "c", 23);
        User u4 = new User(4, "d", 24);
        User u5 = new User(5, "e", 25);
        //集合就是存储
        List<User> list = Arrays.asList(u1, u2, u3, u4, u5);

        //计算交给流
        //lambda表达式、链式编程、函数式接口、Stream流式计算
        list.stream()
                .filter(user -> {return user.getId() % 2 == 0;}) //1.ID必须是偶数
                .filter(user -> {return user.getAge() > 20;})  //2.年龄必须大于20岁
                .map(user -> {return user.getName().toUpperCase();}) //3.用户名转为大写字母
                .sorted((user1,user2)-> {return user2.compareTo(user1);})  //4.用户名字倒着排序
                .limit(1)  //5.只输出一个用户
                .forEach(System.out::println);

    }
}

```



# 14.ForkJoin

什么是ForkJoin

ForkJoin在JDL1.7，并行执行任务！提高效率。大数据量。

将大任务划分成多个小任务由不同的线程执行，然后将结果合并为最终结果



>  ForkJoin的特点：工作窃取
>
> 当两个线程执行各自的任务时，一个线程执行完了，另一个线程没执行完，这时，执行完任务的线程会去偷取没执行完任务的线程的任务

里面维护的都是双端队列



forkjoin,必须是大数据量的时候使用

```java
package com.pika.forkjoin;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 求和计算任务
 * 如何使用forkjoin
 * 1.通过forkJoinPool来执行
 * 2.计算任务 forkjoinPool.execute(ForkJoinTask task)
 * 3.计算类要继承ForkJoinTask
 */
public class ForkJoinDemo extends RecursiveTask<Long> {
    private Long start;
    private Long end;
    //临界值
    private Long temp = 10000L;

    public ForkJoinDemo(Long start, Long end) {
        this.start = start;
        this.end = end;
    }


    @Override
    protected Long compute() {
        if ((end - start) < temp) {
            Long sum = 0L;
            for (Long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else { //forkjoin
            long mid = (start + end) / 2;
            ForkJoinDemo task1 = new ForkJoinDemo(start, mid);
            task1.fork(); //拆分任务，把任务压入队列
            ForkJoinDemo task2 = new ForkJoinDemo(mid + 1, end);
            task2.fork(); //拆分任务，把任务压入队列
            return task1.join() + task2.join();
        }
    }
}

```

测试，普通，forkjoin，还有并行流对比

```java
package com.pika.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class Test {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test1();
        test2();
        test3();
    }


    public static void test1() {
        Long sum = 0L;
        long start = System.currentTimeMillis();

        for (Long i = 1L; i <= 10_0000_0000L; i++) {
            sum += i;
        }
        System.out.println(sum);
        long end = System.currentTimeMillis();
        System.out.println("花费了: " + (end - start));
    }
    //forkjoin
    public static void test2() throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> forkJoinDemo = new ForkJoinDemo(1L, 10_0000_0000L);
        long start = System.currentTimeMillis();

//        forkJoinPool.execute(forkJoinDemo); //执行，没有结果
        ForkJoinTask<Long> submit = forkJoinPool.submit(forkJoinDemo);//提交任务，有结果
        Long sum = submit.get();
        System.out.println(sum);
        long end = System.currentTimeMillis();
        System.out.println("花费了: " + (end - start));
    }
    //并行流
    public static void test3() {
        long start = System.currentTimeMillis();
//        long sum = LongStream.range(1L, 10_0000_0000L).parallel().reduce(0, Long::sum);
        long sum = LongStream.rangeClosed(1L, 10_0000_0000L).parallel().reduce(1, Long::sum);
        System.out.println(sum);
        long end = System.currentTimeMillis();
        System.out.println("花费了: " + (end - start));
    }

}

```





# 15.异步回调

> Future 

```java
package com.pika.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 异步调用：类似ajax
 * 1.异步执行
 * 2.成功回调
 * 3.失败回调
 */
public class Demo01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //发起一个请求
        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " runAsync");
        });
        System.out.println(11111111);

        completableFuture.get(); //获取执行结果

        //有返回值supplyAsync,异步回调
        //ajax，成功和失败的回调
        //返回的是错误信息
        CompletableFuture<Integer> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " supplyAsync");
            int a = 10/0;
            return 1024;
        });

        Integer integer = completableFuture1.whenComplete((t, u) -> {
            System.out.println("t=>" + t);
            System.out.println("u=>" + u);
        }).exceptionally((e) -> {
            System.out.println(e.getMessage());
            return 233;
        }).get();
        System.out.println(integer);
    }

}

```

# 16.JMM

> 请你谈谈对Volatile的理解

Volatile是java虚拟机提供轻量级的同步机制

1.保证线程可见

2.不保证原子性

3.禁止指令重排序



> 什么是JMM

JMM（java memory model） ：java内存模型，并不是实际存在的东西，是一个概念（约定）



**关于JMM的一些同步约定**

主线嗅探机制、MESI，缓存行，内存屏障

* 线程解锁前，必须把共享变量立刻刷回主存
* 线程加锁前，必须读取主存中的最新值到工作内存中
* 加锁和解锁是同一把锁

**内存交互操作**

内存交互操作有8种，虚拟机实现必须保证每一个操作都是原子的，不可在分的（对于double和long类型的变量来说，load、store、read和write操作在某些平台上允许例外）

- - lock   （锁定）：作用于主内存的变量，把一个变量标识为线程独占状态
  - unlock （解锁）：作用于主内存的变量，它把一个处于锁定状态的变量释放出来，释放后的变量才可以被其他线程锁定
  - read  （读取）：作用于主内存变量，它把一个变量的值从主内存传输到线程的工作内存中，以便随后的load动作使用
  - load   （载入）：作用于工作内存的变量，它把read操作从主存中变量放入工作内存中
  - use   （使用）：作用于工作内存中的变量，它把工作内存中的变量传输给执行引擎，每当虚拟机遇到一个需要使用到变量的值，就会使用到这个指令
  - assign （赋值）：作用于工作内存中的变量，它把一个从执行引擎中接受到的值放入工作内存的变量副本中
  - store  （存储）：作用于主内存中的变量，它把一个从工作内存中一个变量的值传送到主内存中，以便后续的write使用
  - write 　（写入）：作用于主内存中的变量，它把store操作从工作内存中得到的变量的值放入主内存的变量中

　　JMM对这八种指令的使用，制定了如下规则：

- - 不允许read和load、store和write操作之一单独出现。即使用了read必须load，使用了store必须write
  - 不允许线程丢弃他最近的assign操作，即工作变量的数据改变了之后，必须告知主存
  - 不允许一个线程将没有assign的数据从工作内存同步回主内存
  - 一个新的变量必须在主内存中诞生，不允许工作内存直接使用一个未被初始化的变量。就是怼变量实施use、store操作之前，必须经过assign和load操作
  - 一个变量同一时间只有一个线程能对其进行lock。多次lock后，必须执行相同次数的unlock才能解锁
  - 如果对一个变量进行lock操作，会清空所有工作内存中此变量的值，在执行引擎使用这个变量前，必须重新load或assign操作初始化变量的值
  - 如果一个变量没有被lock，就不能对其进行unlock操作。也不能unlock一个被其他线程锁住的变量
  - 对一个变量进行unlock操作之前，必须把此变量同步回主内存

　　JMM对这八种操作规则和对[volatile的一些特殊规则](https://www.cnblogs.com/null-qige/p/8569131.html)就能确定哪里操作是线程安全，哪些操作是线程不安全的了。但是这些规则实在复杂，很难在实践中直接分析。所以一般我们也不会通过上述规则进行分析。更多的时候，使用java的happen-before规则来进行分析。





# 17.Volatile

> 验证三大特性

## 17.1.保证线程可见

```java
package com.pika.volat;

import java.util.concurrent.TimeUnit;

public class JMMDemo {
    private  static volatile int num = 0;
    public static void main(String[] args) {//main线程

        new Thread(() -> {//线程1 如果不加volitale对主内存的变化不知道
            while (num==0) {

            }
        }).start();

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        num = 1;
        System.out.println(num);
    }
}
```



## 17.2.不保证原子性

原子性：不可分割

​				线程A在执行任务的时候，不能被打扰的，也不能被分割。要么同时成功，要么同时失败。

怎样才能保证原子性呢（不使用synchronized关键字，锁的情况下）

使用原子类		**java.util.concurrent.atomic**	这些类的底层都直接和操作系统挂钩！在内存中修改值！--> Unsafe类是一个很特殊的存在

```java
package com.pika.volat;

import java.util.concurrent.atomic.AtomicInteger;

public class VolatileDemo01 {
    private volatile static AtomicInteger num = new AtomicInteger();

    public static void add() {
//        num ++;
        num.getAndIncrement(); //执行原子类的+1操作   AtomicInteger + 1
    }

    public static void main(String[] args) {
        //理论上num结果是两万
        for (int i = 0; i < 20; i++) {
            new Thread(()->{
                for (int j = 0; j < 1000; j++) {
                    add();
                }
            }).start();
        }

        while (Thread.activeCount()>2) { //main gc
            Thread.yield();
        }

        System.out.println(Thread.currentThread().getName() + "  " + num);

    }
}

```



## 17.3.禁止指令重排序

什么是指令重排序：计算机会优化我们写的程序，并不一定是按照我们写的顺序执行

源代码--->编译器优化的重排--->指令并行也可能会重排--->内存系统也会重排--->执行

处理器在执行指令重排的时候，会考虑到数据之间的依赖关系

假设x,y,a,b默认值都是0

| 线程A | 线程B |
| ----- | ----- |
| x=a   | y=b   |
| b=2   | a=3   |





volatile可以避免指令重排：

​	内存屏障。CPU指令。作用：

​		1.保证特定的操作的执行顺序

​		2.可以保证某些变量的内存可见性（利用这些特性volatile实现了可见性）

![image-20200820182151052](.\images\image-20200820182151052.png)



# 18.彻底玩转单例模式

饿汉式，懒汉式DCL





# 19.深入理解CAS

>  什么是CAS

比较当前工作内存中的值和主内存中的值，如果这个值是期望的，那么执行操作，如果不是就一直循环。

缺点：

* 循环会耗时
* 一次性只能保证一个共享变量的原子性
* ABA问题



> private static final Unsafe unsafe = Unsafe.getUnsafe();
>
> Java无法操作内存
>
> C++可以操作内存
>
> Java 可以调用C++来操作内存  native
>
> Java可以通过这个类来操作内存



> Usafe类

CAS自旋锁

![image-20200820215934209](.\images\image-20200820215934209.png) 



> 涉及到CAS肯定会有ABA问题
>
> A被改为B后又被改为A

Compare And Swap (Compare And Exchange) / 自旋 / 自旋锁 / 无锁 

因为经常配合循环操作，直到完成为止，所以泛指一类操作

cas(v, a, b) ，变量v，期待值a, 修改值b

ABA问题，你的女朋友在离开你的这段儿时间经历了别的人，自旋就是你空转等待，一直等到她接纳你为止

解决办法（版本号 AtomicStampedReference），基础类型简单值不需要版本号

# 20.原子引用

带版本号的原子操作

解决办法（版本号 AtomicStampedReference），基础类型简单值不需要版本号



# 21.各种锁的理解

## 21.1公平锁、非公平锁

* 公平锁：非常公平，不能够插队，必须先来后到
* 非公平锁：非常不公平，可以插队（默认）

## 21.2可重入锁

可重入锁（递归锁）

## 21.3.自旋锁



> 自己使用自旋锁原理写一个简单的锁demo

```java
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

```



> 测试



```java
package com.pika.lock;

import java.util.concurrent.TimeUnit;

public class TestSpinLockDemo {
    public static void main(String[] args) throws InterruptedException {
        SpinLockDemo lock = new SpinLockDemo();

        new Thread(()->{
            lock.myLock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnLock();
            }
        }, "T1").start();


        TimeUnit.SECONDS.sleep(1);

        new Thread(()->{
            lock.myLock();
            try {
//                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.myUnLock();
            }
        }, "T2").start();
    }
}

```



## 21.4.死锁







> 解决问题

* 使用jps定位进程号
  * jsp -l	定位进程号

* 使用jstack进程号查看信息
  * jstack 进程号



日志，堆栈



