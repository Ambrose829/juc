package com.pika.future;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 异步调用：ajax
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
