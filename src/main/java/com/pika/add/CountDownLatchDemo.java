package com.pika.add;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.CountDownLatch;
//减法计数器
public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(6);

        for (int i = 1; i <= 6; i++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "\t 国，被灭！");
                countDownLatch.countDown();
            }, CountryEnum.forEach_CountryEnum(i).getCountryName()).start();
        }
        countDownLatch.await();
        System.out.println(Thread.currentThread().getName() + "\t 秦国一统天下。");



    }

    private static void closeDoor() throws InterruptedException {
        //总数是六, 必须要执行任务的时候使用
        CountDownLatch countDownLatch = new CountDownLatch(6);
        for (int i = 1; i < 7; i ++) {
            new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "go out!");
                //数量减一
                countDownLatch.countDown();
            }, String.valueOf(i)).start();


        }


        //等待计数器归零，然后再向下执行，确保全部跑完才进行下面的关门输出
        countDownLatch.await();

        System.out.println("close door！");
    }
}
@AllArgsConstructor
@Getter
enum CountryEnum {
    ONE(1, "齐"), TWO(2, "楚"), THREE(3, "燕"),
    FOUR(4, "赵"), FIVE(5, "魏"), SIX(6, "韩");

    private Integer id;
    private String countryName;

    public static CountryEnum forEach_CountryEnum(int index) {
        CountryEnum[] values = CountryEnum.values();
        for (CountryEnum value : values) {
            if (index == value.getId()) {
                return value;
            }
        }
        return null;
    }




}
