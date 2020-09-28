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
