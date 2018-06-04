package com.example.sell2.server;

import com.example.sell2.entity.UserInfo;
import com.example.sell2.resultvo.Uservo;

import java.util.List;

public interface Userinfoserver {
    UserInfo findone(Integer id);
    UserInfo insert(UserInfo userInfo);
    List<Uservo> findAll();
    List<UserInfo> finall(List<Integer> list);
    Boolean  delete(Integer id);
    void incrementAndUpdate(UserInfo userInfo);
    UserInfo findbyname(String name);
}
