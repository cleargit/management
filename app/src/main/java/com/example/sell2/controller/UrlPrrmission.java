package com.example.sell2.controller;

import com.example.sell2.entity.SysPermission;
import com.example.sell2.redis.BlogKey;
import com.example.sell2.repository.PerRepository;
import com.example.sell2.resultvo.ResultVo;
import com.example.sell2.util.RedisUtil;
import com.example.sell2.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("per")
public class UrlPrrmission {
    @Autowired
    PerRepository repository;
    @Autowired
    RedisUtil redisUtil;
    @GetMapping("list.do")
    public ResultVo get(){
        ResultVo resultVo= ((ResultVo) redisUtil.get(BlogKey.perlist, "per"));
        if (resultVo!=null){
            return resultVo;
        }
        List<SysPermission> list=repository.findAll();
        redisUtil.set(BlogKey.perlist,"per",ResultUtil.success(list));
        return ResultUtil.success(list);
    }
    @GetMapping("demo")
    public String demo(){
        return  Thread.currentThread().getName();
    }
}
