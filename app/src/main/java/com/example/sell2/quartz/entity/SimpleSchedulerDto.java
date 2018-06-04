package com.example.sell2.quartz.entity;

import lombok.Data;

@Data
public class SimpleSchedulerDto extends BaseSchedulerDto {
    Integer repeatCount;
    Integer repeatInterval;
    Boolean repeatForever;
    String frag;//标志 hours   Minutes  Seconds

}
