package com.pika.forkjoin;

import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * 求和计算任务
 * 如何使用forkjoin
 * 1.通过forkJoinPool来执行
 * 2.计算任务 forkjoinPool.execute(ForkJoinTask task)
 * 3.计算类要继承ForkJoinTask
 */
public class ForkJoinDemo extends RecursiveTask<Long> {
    private Long start;
    private Long end;
    //临界值
    private Long temp = 10000L;

    public ForkJoinDemo(Long start, Long end) {
        this.start = start;
        this.end = end;
    }


    @Override
    protected Long compute() {
        if ((end - start) < temp) {
            Long sum = 0L;
            for (Long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        } else { //forkjoin
            long mid = (start + end) / 2;
            ForkJoinDemo task1 = new ForkJoinDemo(start, mid);
            task1.fork(); //拆分任务，把任务压入队列
            ForkJoinDemo task2 = new ForkJoinDemo(mid + 1, end);
            task2.fork(); //拆分任务，把任务压入队列
            return task1.join() + task2.join();
        }
    }
}
