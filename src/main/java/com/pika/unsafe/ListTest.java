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
