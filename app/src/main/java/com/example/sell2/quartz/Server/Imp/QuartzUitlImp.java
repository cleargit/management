package com.example.sell2.quartz.Server.Imp;


import com.example.sell2.quartz.Server.QuartzUitl;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuartzUitlImp implements QuartzUitl {
    @Autowired
    private Scheduler scheduler;
    private Logger logger=LoggerFactory.getLogger(QuartzUitlImp.class);
    @Override
    public Boolean delete(String name, String group)throws Exception{
        JobKey jobKey=JobKey.jobKey(name,group);
        return scheduler.deleteJob(jobKey);
    }

    @Override
    public Boolean stop(String name, String group,Boolean wait) throws Exception {
        scheduler.shutdown(wait);
        return true;
    }

    @Override
    public void change(String name, String group,String cron) throws Exception {
        TriggerKey triggerKey=TriggerKey.triggerKey(name,group);
        if (triggerKey==null){
            return;
        }
        CronScheduleBuilder cronScheduleBuilder=CronScheduleBuilder.cronSchedule(cron);
        Trigger  trigger= scheduler.getTrigger(triggerKey);
        Trigger changes=TriggerBuilder.newTrigger().withSchedule(cronScheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey,changes);
    }
}
