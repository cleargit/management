package com.example.sell2.controller;

import com.example.sell2.entity.Log;
import com.example.sell2.quartz.Server.CreateScheduler;
import com.example.sell2.quartz.Server.QuartzUitl;
import com.example.sell2.quartz.entity.CronSchedulerDto;
import com.example.sell2.quartz.job.Redis2Sql;
import com.example.sell2.repository.Logrepository;
import com.example.sell2.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("quartz")
public class QuartzController {

    @Autowired
    CreateScheduler createScheduler;
    @Autowired
    QuartzUitl quartzUitl;
    @RequestMapping("init")
    public Boolean init(){
        CronSchedulerDto cronSchedulerDto=new CronSchedulerDto();
        cronSchedulerDto.setName("redis2sql");
        cronSchedulerDto.setGroup("common");
        cronSchedulerDto.setTri_name("redis2sql");
        cronSchedulerDto.setTri_group("common");
        cronSchedulerDto.setClassName(Redis2Sql.class.getName());
        cronSchedulerDto.setCron("0 0 1 * * ?");//每天凌晨1点执行一次：0 0 1 * * ?
        return createScheduler.cro(cronSchedulerDto);
    }
    @RequestMapping("delect")
    public boolean delect(String name,String group) throws Exception {
        String JOB_NAME="JOB_";
        String JOB_GROUP="JOB_GROUP_";
        return quartzUitl.delete(JOB_NAME+name,JOB_GROUP+group);
    }
}
