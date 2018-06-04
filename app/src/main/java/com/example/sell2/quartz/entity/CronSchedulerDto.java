package com.example.sell2.quartz.entity;

import lombok.Data;

@Data
public class CronSchedulerDto extends BaseSchedulerDto {
    String cron;
}
