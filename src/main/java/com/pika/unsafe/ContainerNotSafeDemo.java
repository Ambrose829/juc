package com.pika.unsafe;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Pika
 * @create 2020/9/27
 * @since 1.0.0
 * 集合类不安全问题
 *  ArrayList：lisNotSafe
 *  HashSet：
 *
 */
public class ContainerNotSafeDemo {
    public static void main(String[] args) {
        mapNotSafe();


    }

    private static void mapNotSafe() {
        //        Map<String, String> map = new HashMap<>();
//        Map<String, String> map = new Hashtable<>();
//        Map<String, String> map = Collections.synchronizedMap(new HashMap<>());
        Map<String, String> map = new ConcurrentHashMap<>();

        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                map.put(Thread.currentThread().getName(), UUID.randomUUID().toString().substring(0, 8));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }

    private static void setNotSafe() {
        //        Set<String> set = new HashSet<>();
//        Set<String> set = Collections.synchronizedSet(new HashSet<>());
        Set<String> set = new CopyOnWriteArraySet<>();

        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(0, 8));
                System.out.println(set);
            }, String.valueOf(i)).start();

        }
    }

    private static void listNotSafe() {
//        List<String> list = new ArrayList<>();
//        List<String> list = new Vector<>();
//        List<String> list = Collections.synchronizedList(new ArrayList<>());

        List<String> list = new CopyOnWriteArrayList<>();


        for (int i = 1; i <= 30; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(0,8));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
        /**
         * 故障现象
         * java.util.ConcurrentModificationException
         *
         * 导致原因
         *  并发争抢修改导致的：一个线程正在修改，另一个线程也想要修改并过来抢夺，导致数据不一致，并发修改异常。
         * 解决方案
         *  1.List<String> list = new Vector();
         *
         *  2.List<String> list = Collections.synchronizedList(new ArrayList<>());
         *
         *  3.List<String> list = new CopyOnWriteArrayList();
         *      写时复制
         *      CopyOnWrite容器即写时复制容器。往一个容器添加元素的时候，不直接往当前容器Object[]中添加，而是先拷贝当前容器Object[]，
         *      复制出一个新的容器Object[] newElements，然后在新的新的容器newElements中添加元素，添加完元素后，
         *      将原容器的引用指向新的容器setArray(newElements); 这样做的好处是可以对CopyOnWrite进行并发的读，
         *      而不需要加锁，因为当前容器不会添加任何元素。所以CopyOnWrite也是一种读写分离的思想，读和写在不同的容器中。
         *
         *      public boolean add(E e) {
         *         final ReentrantLock lock = this.lock;
         *         lock.lock();
         *         try {
         *             Object[] elements = getArray();
         *             int len = elements.length;
         *             Object[] newElements = Arrays.copyOf(elements, len + 1);
         *             newElements[len] = e;
         *             setArray(newElements);
         *             return true;
         *         } finally {
         *             lock.unlock();
         *         }
         *     }
         * 优化建议
         */
    }

}
