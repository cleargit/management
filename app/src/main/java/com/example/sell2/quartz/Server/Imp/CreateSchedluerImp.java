package com.example.sell2.quartz.Server.Imp;

import com.example.sell2.hander.BlogException;
import com.example.sell2.quartz.entity.CronSchedulerDto;
import com.example.sell2.quartz.Server.CreateScheduler;
import com.example.sell2.quartz.entity.SimpleSchedulerDto;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Map;

@Service
public class CreateSchedluerImp implements CreateScheduler {
    @Autowired
    Scheduler scheduler;
    private Logger logger=LoggerFactory.getLogger(CreateSchedluerImp.class);
    private static final String Hour="hours";
    private static final String Minutes="Minutes";
    private static final String Seconds="Seconds";


    @Override
    public Boolean simple(SimpleSchedulerDto simpleSchedulerDto)  {
        Boolean success=false;
        try {
            String name =JOB_NAME+ simpleSchedulerDto.getName();
            String group=JOB_GROUP+ simpleSchedulerDto.getGroup();
            String tri_Name=TRI_NAME+ simpleSchedulerDto.getTri_name();
            String tri_group=TRI_GTOUP+ simpleSchedulerDto.getTri_group();
            String job_class= simpleSchedulerDto.getClassName();
            String frag=simpleSchedulerDto.getFrag();
            Map<String,Object> map= simpleSchedulerDto.getMapData();
            Integer repeatCount= simpleSchedulerDto.getRepeatCount();
            Integer repeatInterval= simpleSchedulerDto.getRepeatInterval();
            Boolean repeatForever=simpleSchedulerDto.getRepeatForever();//如果为true会一直执行到结束时间 没结束就会一直执行
            Long startAt= simpleSchedulerDto.getStartAt();
            Long endAt= simpleSchedulerDto.getEndAt();
            JobDetail jobDetail=createJob(name,group,job_class,map);
            if(jobExist(name,group)) return false;
            if (triExist(tri_Name,tri_group)) return false;
            Trigger trigger=createSimple(tri_Name,tri_group,repeatCount,repeatInterval,repeatForever,frag,startAt,endAt);
            scheduler.scheduleJob(jobDetail,trigger);
            success=true;
            return success;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }



    private Trigger createSimple(String tri_name, String tri_group, Integer repeatCount, Integer repeatInterval,Boolean repeatForever,String frag, Long startAt, Long endAt) {
        TriggerBuilder triggerBuilder=TriggerBuilder.newTrigger().withIdentity(tri_name,tri_group);
        SimpleScheduleBuilder simpleSchedule=SimpleScheduleBuilder.simpleSchedule();
        if (startAt<System.currentTimeMillis()){
            throw new BlogException(1000,"开始时间小于现在");
        }
        if (startAt<0 && startAt==null){
            throw new BlogException(1001,"开始时间错误");
        }
        triggerBuilder.startAt(new Date(startAt));
        if (endAt>0 && endAt!=null){
            triggerBuilder.endAt(new Date(endAt));
        }
        if (repeatForever!=null &&repeatForever){
            simpleSchedule.repeatForever();
        }

        if (frag.equals(Hour)){
            simpleSchedule.withIntervalInHours(repeatInterval);
        }else if(frag.equals(Minutes)) {
            simpleSchedule.withIntervalInMinutes(repeatInterval);
        }else{
            simpleSchedule.withIntervalInSeconds(repeatInterval);
        }
        if (repeatCount!=null){
            simpleSchedule.withRepeatCount(repeatCount);
        }
        return triggerBuilder.withSchedule(simpleSchedule).build();
    }


    private JobDetail createJob(String name, String group, String job_class,Map map) throws Exception {
         Class clazz=Class.forName(job_class);
        JobBuilder jobBuilder=JobBuilder.newJob(clazz).withIdentity(name,group);
         if (!StringUtils.isEmpty(map)){
             JobDataMap jobDataMap=new JobDataMap(map);
             jobBuilder.setJobData(jobDataMap);
         }
       JobDetail jobDetail=jobBuilder.build();
        return jobDetail;
    }


    @Override
    public Boolean cro(CronSchedulerDto cronSchedulerDto) {
        Boolean success=false;
        try {
            String name =JOB_NAME+ cronSchedulerDto.getName();
            String group=JOB_GROUP+ cronSchedulerDto.getGroup();
            String tri_Name=TRI_NAME+ cronSchedulerDto.getTri_name();
            String tri_group=TRI_GTOUP+ cronSchedulerDto.getTri_group();
            Long startat=cronSchedulerDto.getStartAt();
            Long endat=cronSchedulerDto.getEndAt();
            String cron= cronSchedulerDto.getCron();
            String job_class= cronSchedulerDto.getClassName();
            Map<String,Object> map= cronSchedulerDto.getMapData();
            if(jobExist(name,group)) return false;
            if (triExist(tri_Name,tri_group)) return false;
            JobDetail jobDetail=createJob(name,group,job_class,map);
            CronScheduleBuilder cronScheduleBuilder=CronScheduleBuilder.cronSchedule(cron);
            Trigger trigger;
            if (endat!=null){//表示没有结束时间
                trigger=TriggerBuilder.newTrigger().startAt(new Date(startat)).endAt(new Date(endat)).withIdentity(tri_Name,tri_group).withSchedule(cronScheduleBuilder).build();
            }else {
                trigger=TriggerBuilder.newTrigger().withIdentity(tri_Name,tri_group).withSchedule(cronScheduleBuilder).build();
            }

            scheduler.scheduleJob(jobDetail,trigger);
            success=true;
            return success;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
    private boolean jobExist(String name, String group) throws SchedulerException {
        JobDetail jobDetail=scheduler.getJobDetail(JobKey.jobKey(name,group));
        if (jobDetail!=null){
            return true;
        }else {
            return false;
        }
    }

    private boolean triExist(String tri_name, String tri_group) throws SchedulerException {
        Trigger trigger=scheduler.getTrigger(TriggerKey.triggerKey(tri_name,tri_group));
        if (trigger!=null){
            return true;
        }else {
            return false;
        }
    }
}
