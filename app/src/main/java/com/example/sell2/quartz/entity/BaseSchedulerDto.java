package com.example.sell2.quartz.entity;

import lombok.Data;

import java.util.Map;

@Data
public class BaseSchedulerDto {
    String name;
    String group;
    String tri_name;
    String tri_group;
    String className;
    Long startAt;
    Long endAt;
    Map<String,Object> mapData;
}
