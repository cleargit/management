package com.example.sell2.quartz.Server;


import com.example.sell2.quartz.entity.CronSchedulerDto;
import com.example.sell2.quartz.entity.SimpleSchedulerDto;

public interface CreateScheduler {
    public final static String JOB_NAME="JOB_";
    public final static String JOB_GROUP="JOB_GROUP_";
    public final static String TRI_NAME="Tri_";
    public final static String TRI_GTOUP="Tri_GROUP_";
    public Boolean simple(SimpleSchedulerDto simpleSchedulerDto);
    public Boolean cro(CronSchedulerDto cronSchedulerDto);
}
