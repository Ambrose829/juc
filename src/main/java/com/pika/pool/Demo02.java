package com.pika.pool;

import java.util.concurrent.*;


public class Demo02 {
    public static void main(String[] args) {


        /**
         * 自定义线程池！工作 ThreadPoolExecutor
         * * CPU密集型
         *   * 根据电脑的CPU核数来定义，几核就定义几个
         *
         * * IO密集型
         *   * 大于你系统中十分消耗IO的线程数，一般是两倍
         *   * 假如程序中有15个大型任务，而且他们的IO都非常占用资源，这时最大线程可以设置为30
         */
        //获取当前电脑cpu核数
        System.out.println(Runtime.getRuntime().availableProcessors());

        ExecutorService threadPool = new ThreadPoolExecutor(
                2,
                Runtime.getRuntime().availableProcessors(),
                3,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardOldestPolicy()//队列满了，尝试去和最早的线程竞争，不成功就丢掉，不抛出异常
        );

        try {
            //最大承载，最大值+队列
            for (int i = 0; i < 9; i++) {
                threadPool.execute(()->{
                    System.out.println(Thread.currentThread().getName()+"111");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //线程池用完，程序结束，关闭线程池
            threadPool.shutdown();
        }




    }
}
