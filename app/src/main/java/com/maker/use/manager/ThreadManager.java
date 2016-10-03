package com.maker.use.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 单例模式的线程管理者（线程池）
 * Created by XT on 2016/9/30.
 */
public class ThreadManager {
    private static ThreadPool mThreadPool = null;

    public static ThreadPool getThreadPool() {
        if (mThreadPool == null) {
            synchronized (ThreadManager.class) {
                if (mThreadPool == null) {
                    // 获取cpu数量
                    int cpuCount = Runtime.getRuntime().availableProcessors();

                    int threadCount = cpuCount * 2 + 1;//线程个数
//                    int threadCount = 10;
                    mThreadPool = new ThreadPool(threadCount, threadCount, 1L);
                }
            }
        }
        return mThreadPool;
    }

    public static class ThreadPool {
        private int corePoolSize;// 核心线程数
        private int maximumPoolSize;// 最大线程数
        private long keepAliveTime;// 休息时间

        private ThreadPoolExecutor mExecutor;

        private ThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }

        /**
         * 执行任务
         * @param r
         */
        public void execute(Runnable r) {
            // 参1:核心线程数;参2:最大线程数;参3:线程休眠时间;参4:时间单位;参5:线程队列;参6:生产线程的工厂;参7:线程异常处理策略
            mExecutor = new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(),
                    Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());

            //执行传递进来的Runnable对象
            mExecutor.execute(r);
        }

        /**
         * 取消任务
         * @param r
         */
        public void cancel(Runnable r){
            if (mExecutor != null){
                // 从线程队列中移除对象
                mExecutor.getQueue().remove(r);
            }
        }
    }
}
