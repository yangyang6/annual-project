package com.cwnu.executors;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

/**
 * @Author：rey
 * @Date：2018/1/30 上午11:51
 */
public class PlayWithDefaultThreadFactory {
    public static void main(String[] args) {
        DefaultThreadFactory factory = new DefaultThreadFactory(); // 这个DefaultThreadFactory会把新的thread创建在main thread所属的group里。

        // 同一个thread group里面的thread执行没结束的时候，main thread不会退出，会被block住。
        Thread t = factory.newThread(() -> {
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // 这个thread跑起来以后，main thread会继续执行，直到block在结束的地方，等待t里面的sleep的3秒钟完成，除非像下面这样给interrupt()。
        t.start();

        t.interrupt();

    }

    /**
     * The default thread factory
     */
    static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        DefaultThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}


