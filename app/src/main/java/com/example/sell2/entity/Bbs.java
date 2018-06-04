package com.example.sell2.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Bbs implements Serializable {

    private static final long serialVersionUID = 2674167778094601247L;
    @Id
    @GeneratedValue
    Integer id;
    Integer aid;
    String content;
    String name;
    String time;
    String isk;
    Integer uid;
    String img;
    Integer dzan=0;
    String ob;
    String only;
    @Transient
    List<Bbs> list=new ArrayList<>();
    @Transient
    int page;
}
