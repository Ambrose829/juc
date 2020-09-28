package com.pika.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.LongStream;

public class Test {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        test1();
        test2();
        test3();
    }


    public static void test1() {
        Long sum = 0L;
        long start = System.currentTimeMillis();

        for (Long i = 1L; i <= 10_0000_0000L; i++) {
            sum += i;
        }
        System.out.println(sum);
        long end = System.currentTimeMillis();
        System.out.println("花费了: " + (end - start));
    }
    //forkjoin
    public static void test2() throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        ForkJoinTask<Long> forkJoinDemo = new ForkJoinDemo(1L, 10_0000_0000L);
        long start = System.currentTimeMillis();

//        forkJoinPool.execute(forkJoinDemo); //执行，没有结果
        ForkJoinTask<Long> submit = forkJoinPool.submit(forkJoinDemo);//提交任务，有结果
        Long sum = submit.get();
        System.out.println(sum);
        long end = System.currentTimeMillis();
        System.out.println("花费了: " + (end - start));
    }
    public static void test3() {
        long start = System.currentTimeMillis();
//        long sum = LongStream.range(1L, 10_0000_0000L).parallel().reduce(0, Long::sum);
        long sum = LongStream.rangeClosed(1L, 10_0000_0000L).parallel().reduce(1, Long::sum);
        System.out.println(sum);
        long end = System.currentTimeMillis();
        System.out.println("花费了: " + (end - start));
    }

}
