package com.example.scheduled.service;

import com.example.scheduled.domain.SysJobPO;
import com.example.scheduled.task.CronTaskRegistrar;
import com.example.scheduled.task.SchedulingRunnable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName : ISysTaskService
 * @Description : 定时任务实现类
 * @Author : 郭兵
 * @Date: 2020-09-28 10:21
 */
@Service
public class ISysTaskService {
    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    private static List<SysJobPO> jobList;

    static {
        jobList = new ArrayList<>();
        SysJobPO vo = new SysJobPO();
        vo.setJobId(1);
        vo.setBeanName("demoTest");
        vo.setMethodName("taskNoParams");
        vo.setCronExpression("0/10 * * * * *");
        vo.setJobStatus(0);
        jobList.add(vo);

        vo = new SysJobPO();
        vo.setJobId(2);
        vo.setBeanName("demoTest");
        vo.setMethodName("taskNoParams");
        vo.setCronExpression("0/10 * * * * *");
        vo.setJobStatus(1);
        jobList.add(vo);

        vo = new SysJobPO();
        vo.setJobId(3);
        vo.setBeanName("demoTest");
        vo.setMethodName("taskWithParams");
        vo.setMethodParams("我是一号测试");
        vo.setCronExpression("0/10 * * * * *");
        vo.setJobStatus(0);
        jobList.add(vo);

        vo = new SysJobPO();
        vo.setJobId(4);
        vo.setBeanName("demoTest");
        vo.setMethodName("taskWithParams");
        vo.setMethodParams("我是二号测试");
        vo.setCronExpression("0/10 * * * * *");
        vo.setJobStatus(1);
        jobList.add(vo);
    }

    /**
     * 查询进行中的定时任务类
     *
     * @param status
     */
    public List<SysJobPO> selectTask(int status) {
        List<SysJobPO> list = jobList.stream()
                .filter(s -> Objects.equals(s.getJobStatus(), status))
                .collect(Collectors.toList());
        return list;
    }

    /**
     * 添加定时任务
     *
     * @param jobVo
     */
    public void addTask(SysJobPO jobVo) {
        // 处理数据 插入数据库
        jobList.add(jobVo);

        // 判断定时任务是否开启
        Integer jobStatus = jobVo.getJobStatus();
        if (Objects.equals(jobStatus, 0)) {
            this.changeTaskStatus(Boolean.TRUE, jobVo);
        }

    }

    /**
     * 修改定时任务
     *
     * @param jobVo
     */
    public void updateTask(SysJobPO jobVo) {
        // 获取数据库中已存在的数据
        SysJobPO existJob = jobList.stream()
                .filter(s -> Objects.equals(jobVo.getJobId(), s.getJobId()))
                .findFirst().orElse(null);
        if (existJob == null) {
            return;
        }

        // 判断 原来的定时任务是否开启，如果开启，则先停止
        if (Objects.equals(existJob.getJobStatus(), 0)) {
            this.changeTaskStatus(Boolean.FALSE, existJob);
        }

        // 处理数据 插入数据库
        for (SysJobPO sysJobVo : jobList) {
            Integer jobId = sysJobVo.getJobId();
            if (Objects.equals(jobId,jobVo.getJobId())) {
               // BeanUtils.copyPropertiesIgnoreNullValue(jobVo, sysJobVo);
                break;
            }
        }
        // 判断定时任务是否开启
        if (Objects.equals(jobVo.getJobStatus(), 0)) {
            this.changeTaskStatus(Boolean.TRUE, jobVo);
        }
    }

    /**
     * 删除定时任务
     *
     * @param jobId
     */
    public void deleteTask(Integer jobId) {
        // 获取数据库中已存在的数据
        SysJobPO existJob = jobList.stream()
                .filter(s -> Objects.equals(jobId, s.getJobId()))
                .findFirst().orElse(null);
        if (existJob == null) {
            return;
        }

        // 判断定时任务是否开启
        if (Objects.equals(existJob.getJobStatus(), 0)) {
            this.changeTaskStatus(Boolean.FALSE, existJob);
        }

        // 处理数据 插入数据库
        for (int i = 0; i < jobList.size(); i++) {
            Integer jobId2 = jobList.get(i).getJobId();
            if (Objects.equals(jobId2,jobId)) {
                jobList.remove(i);
                break;
            }
        }
    }

    /**
     * 修改定时任务类状态
     *
     * @param add
     * @param jobVo
     */
    private void changeTaskStatus(boolean add, SysJobPO jobVo) {
        if (add) {
            SchedulingRunnable task = new SchedulingRunnable(jobVo.getBeanName(), jobVo.getMethodName(), jobVo.getMethodParams());
            cronTaskRegistrar.addCronTask(task, jobVo.getCronExpression());
        } else {
            SchedulingRunnable task = new SchedulingRunnable(jobVo.getBeanName(), jobVo.getMethodName(), jobVo.getMethodParams());
            cronTaskRegistrar.removeCronTask(task);
        }
    }


}
