package com.example.scheduled.task;

import com.example.scheduled.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @ClassName : SchedulingRunnable
 * @Description : 实现Runnable接口实现类，被定时任务线程池调用，用来执行指定bean里面的方法
 * @Author : 郭兵
 * @Date: 2020-09-28 10:04
 */
public class SchedulingRunnable  implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(SchedulingRunnable.class);

    /**
     * Bean 名称
     */
    private String beanName;

    /**
     * 方法 名称
     */
    private String methodName;

    /**
     * 参数
     */
    private String params;

    public SchedulingRunnable(String beanName, String methodName) {
        this.beanName = beanName;
        this.methodName = methodName;
    }

    public SchedulingRunnable(String beanName, String methodName, String params) {
        this.beanName = beanName;
        this.methodName = methodName;
        this.params = params;
    }

    @Override
    public void run() {
        log.info("定时任务开始执行：beanName:{},methodName:{},params:{}", beanName, methodName, params);

        // 开始执行时间
        long startTime = System.currentTimeMillis();

        try {
            // 获取到bean
            Object target = SpringContextUtil.getBean(beanName);

            // 获取到bean里面的方法
            Method method = null;
            if (isBlank(params)) {
                method = target.getClass().getDeclaredMethod(methodName);
            } else {
                method = target.getClass().getDeclaredMethod(methodName, String.class);
            }
            ReflectionUtils.makeAccessible(method);
            if (isBlank(params)) {
                method.invoke(target);
            } else {
                method.invoke(target, params);
            }
        } catch (Exception e) {
            log.error(String.format("定时任务执行异常 - bean：%s，方法：%s，参数：%s ", beanName, methodName, params), e);
        }
        long times = System.currentTimeMillis() - startTime;
        log.info("定时任务执行结束 - bean：{}，方法：{}，参数：{}，耗时：{} 毫秒", beanName, methodName, params, times);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SchedulingRunnable)) return false;
        SchedulingRunnable that = (SchedulingRunnable) o;
        if (params == null) {
            return Objects.equals(beanName, that.beanName) &&
                    Objects.equals(methodName, that.methodName) &&
                    Objects.equals(null, that.params);
        }
        return Objects.equals(beanName, that.beanName) &&
                Objects.equals(methodName, that.methodName) &&
                Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        if (params == null) {
            return Objects.hash(beanName, methodName);
        }
        return Objects.hash(beanName, methodName, params);
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 参数
     * @return true 为空
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}
