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
