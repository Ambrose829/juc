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
