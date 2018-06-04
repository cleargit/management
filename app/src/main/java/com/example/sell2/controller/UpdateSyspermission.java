package com.example.sell2.controller;

import com.example.sell2.shiro.Shiroserver;
import com.example.sell2.util.BlogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class UpdateSyspermission {
    @Autowired
    Shiroserver shiroserver;
    @GetMapping("update.do")
    public String update()
    {
        shiroserver.updatePermission();
        return "ok";
    }
    @GetMapping("demo.do")
    public String demo()
    {

        return BlogUtil.handletime(new Date(),"yyyy-MM-dd HH:mm:ss");
    }
}
