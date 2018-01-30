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


        //没加shutdown之前这里会有一个 thread group的概念，也就是在同一个group中任何线程只要还没执行完毕，
        //其他线程都会阻塞在那里。


        // blocking task就会shutdown，main thread也可以退出
        //executorService.shutdown();
    }
}
