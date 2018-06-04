package com.example.sell2.quartz.Server;

public interface QuartzUitl {
    Boolean delete(String name, String group) throws Exception;
    Boolean stop(String name, String group, Boolean wait) throws Exception;
    void change(String name, String group, String cron) throws Exception;//只修改循环次数
}
