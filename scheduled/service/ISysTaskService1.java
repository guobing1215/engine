package com.example.scheduled.service;

import com.example.scheduled.domain.SysJobPO;
import com.example.scheduled.task.CronTaskRegistrar;
import com.example.scheduled.task.SchedulingRunnable;
import com.example.scheduled.utils.SysJobStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName : ISysTaskService1
 * @Description :
 * @Author : 郭兵
 * @Date: 2020-09-28 11:15
 */
@Service
public class ISysTaskService1 {
    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;

    /**
      * @Author 郭兵
      * @Description : 新增定时任务
      * @Date  2020/9/28 11:22
     **/
    public Boolean add(SysJobPO sysJob){
        //插入mysql
        boolean success = false;
               // sysJobRepository.addSysJob(sysJob);
        if (!success)
            return false;
        else {
            if (sysJob.getJobStatus().equals(SysJobStatus.NORMAL.ordinal())) {
                SchedulingRunnable task = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
                cronTaskRegistrar.addCronTask(task, sysJob.getCronExpression());
            }
        }
        return true;
    }
    
    /**
      * @Author 郭兵
      * @Description : 修改定时任务，先移除原来的任务，再启动新任务
      * @Date  2020/9/28 11:24
     **/
    public Boolean update(SysJobPO sysJob){
        boolean success =false;
                    // sysJobRepository.editSysJob(sysJob);
        if (!success)
            return false;
        else {
            //先移除再添加
            if (sysJob.getJobStatus().equals(SysJobStatus.NORMAL.ordinal())) {
                SchedulingRunnable task = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
                cronTaskRegistrar.removeCronTask(task);
            }
            if (sysJob.getJobStatus().equals(SysJobStatus.NORMAL.ordinal())) {
                SchedulingRunnable task = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
                cronTaskRegistrar.addCronTask(task, sysJob.getCronExpression());
            }
        }
        return success;
    }
    /**
      * @Author 郭兵
      * @Description : 删除定时任务
      * @Date  2020/9/28 11:25
     **/
    public Boolean delete(SysJobPO sysJob){
        boolean success = true;
                //sysJobRepository.deleteSysJobById(sysJob.getJobId());
        if (!success)
            return false;
        else{
            if (sysJob.getJobStatus().equals(SysJobStatus.NORMAL.ordinal())) {
                SchedulingRunnable task = new SchedulingRunnable(sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
                cronTaskRegistrar.removeCronTask(task);
            }
        }
        return success;
    }

    /**
     * 修改定时任务类状态
     * @param jobVo
     */
    private void changeTaskStatus(SysJobPO jobVo) {
        if (jobVo.getJobStatus().equals(SysJobStatus.NORMAL.ordinal())) {
            SchedulingRunnable task = new SchedulingRunnable(jobVo.getBeanName(), jobVo.getMethodName(), jobVo.getMethodParams());
            cronTaskRegistrar.addCronTask(task, jobVo.getCronExpression());
        } else {
            SchedulingRunnable task = new SchedulingRunnable(jobVo.getBeanName(), jobVo.getMethodName(), jobVo.getMethodParams());
            cronTaskRegistrar.removeCronTask(task);
        }
    }
}
