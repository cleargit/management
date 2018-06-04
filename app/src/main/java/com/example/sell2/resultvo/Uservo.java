package com.example.sell2.resultvo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Uservo implements Serializable {

    private static final long serialVersionUID = -2618544369088878939L;
    private Integer uid;
    private String username;
    private String name;
    private List<String> role;
    private byte state;
    private Integer num;
    private Integer lastlogin;
    public Uservo() {

    }
}
