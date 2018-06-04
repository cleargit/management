package com.example.sell2.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
public class Log implements Serializable {
    @Id
    @GeneratedValue
    Integer id;
    String api;
    String time;
    String ip;
    String type;
    String methodname;

    public Log(String url, String ip, String time, String methodname,String type) {
        this.api=url;
        this.ip=ip;
        this.time=time;
        this.methodname=methodname;
        this.type=type;
    }
}
