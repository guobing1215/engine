package com.example.scheduled.controller;

import com.example.scheduled.domain.SysJobPO;
import com.example.scheduled.service.ISysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName : ScheduleTaskController
 * @Description : 定时任务测试类
 * @Author : 郭兵
 * @Date: 2020-09-28 10:46
 */
@RestController
public class ScheduleTaskController {
    @Autowired
    private ISysTaskService sysTaskService;

    @GetMapping(value = "/list")
    public List<SysJobPO> list(@RequestParam Integer status) {
        return sysTaskService.selectTask(status);
    }

    @PostMapping(value = "/add")
    public void add(@RequestBody SysJobPO jobVo) {
        sysTaskService.addTask(jobVo);
    }

    @PostMapping(value = "upt")
    public void upt(@RequestBody SysJobPO jobVo) {
        sysTaskService.updateTask(jobVo);
    }

    @GetMapping(value = "del")
    public void del(@RequestParam Integer jobId) {
        sysTaskService.deleteTask(jobId);
    }
}
