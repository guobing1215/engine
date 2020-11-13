package com.example.scheduled.service;

import org.springframework.stereotype.Component;

/**
 * @ClassName : DemoTest
 * @Description : 定时任务示例类
 * @Author : 郭兵
 * @Date: 2020-09-28 10:11
 */
@Component("demoTest")
public class DemoTest {

    public void taskWithParams(String params) {
        System.out.println(">>>>>执行带参定时任务 params：" + params);
    }

    public void taskNoParams(){
        System.out.println(">>>>>执行无参定时任务");
    }
    public void taskAddOrUpdate(){
        System.out.println(">>>>>taskAddOrUpdate");
    }
}
