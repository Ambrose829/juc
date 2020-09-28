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
