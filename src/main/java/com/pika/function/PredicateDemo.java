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
