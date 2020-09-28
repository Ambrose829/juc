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
