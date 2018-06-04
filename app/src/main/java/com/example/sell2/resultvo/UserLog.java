package com.example.sell2.resultvo;

import lombok.Data;


import java.io.Serializable;


@Data
public class UserLog implements Serializable {
    Integer userid;
    String name;
    String time;
    String ip;

    public UserLog(Integer userid,String name, String time, String ip) {
        this.userid=userid;
        this.name = name;
        this.time = time;
        this.ip = ip;
    }
}
