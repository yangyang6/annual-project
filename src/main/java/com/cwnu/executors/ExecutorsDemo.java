package com.cwnu.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author：rey
 * @Date：2018/1/30 上午11:38
 */
public class ExecutorsDemo {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> {
            try {
                Thread.currentThread().wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });



        // blocking task就会shutdown，main thread也可以退出
        executorService.shutdown();
    }
}
