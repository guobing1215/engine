package com.example.scheduled.service;

import com.alibaba.fastjson.JSONObject;
import com.example.scheduled.domain.SysJobPO;
import com.example.scheduled.task.CronTaskRegistrar;
import com.example.scheduled.task.SchedulingRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName : SysJobRunner
 * @Description : 初始化数据库中的定时任务
 * @Author : 郭兵
 * @Date: 2020-09-28 10:18
 */
@Service
public class SysJobRunner implements CommandLineRunner {
    private final static Logger log = LoggerFactory.getLogger(SysJobRunner.class);

    @Autowired
    private ISysTaskService sysTaskService;

    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    @Override
    public void run(String... args) throws Exception {
        List<SysJobPO> list = sysTaskService.selectTask(0);
        log.info(">>>>初始化定时任务 list={}", JSONObject.toJSONString(list));
        for (SysJobPO jobVo : list) {
            SchedulingRunnable task = new SchedulingRunnable(jobVo.getBeanName(), jobVo.getMethodName(), jobVo.getMethodParams());
            cronTaskRegistrar.addCronTask(task, jobVo.getCronExpression());
        }
        log.info(">>>>>定时任务初始化完毕<<<<<");
    }
}
