package com.example.scheduled.task;

import java.util.concurrent.ScheduledFuture;

/**
 * @ClassName : ScheduledTask
 * @Description : 添加ScheduledFuture的包装类。ScheduledFuture是ScheduledExecutorService定时任务线程池的执行结果
 * @Author : 郭兵
 * @Date: 2020-09-28 10:03
 */
public class ScheduledTask {
    volatile ScheduledFuture<?> future;

    /**
     * 取消定时任务
     */
    public void cancel() {
        ScheduledFuture<?> future = this.future;
        if (future != null) {
            future.cancel(true);
        }
    }
}
