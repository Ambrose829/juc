package com.pika.cas;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Pika
 * @create 2020/9/24
 * @since 1.0.0
 */
public class AtomicReferenceDemo {
    public static void main(String[] args) {
        AtomicReference<User> userAtomicReference = new AtomicReference<>();

        User z3 = new User("z3", 22);
        User l4 = new User("l4", 25);

        userAtomicReference.set(z3);

        System.out.println(userAtomicReference.compareAndSet(z3, l4) + "\t" + userAtomicReference.get().toString());
        System.out.println(userAtomicReference.compareAndSet(z3, l4) + "\t" + userAtomicReference.get().toString());


    }
}
@Data
@AllArgsConstructor
class User {
    String userName;
    int age;
}
