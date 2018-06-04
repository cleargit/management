package com.example.sell2.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
@DynamicUpdate
public class Blog implements Serializable{
    private static final long serialVersionUID = -6723435629669437811L;
    @Id
    @GeneratedValue
    Integer id;
    String author="sham";
    String content;
    String img;
    String intro;
    String title;
    Integer type;
    String typename="学习";
    Integer viewcount=0;
    String time;

    public Blog() {
    }

    public Blog(String content, String intro, String title, Integer type,String img) {
        this.content = content;
        this.intro = intro;
        this.title = title;
        this.type = type;
        this.img=img;
    }
}
