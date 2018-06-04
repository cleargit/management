package com.example.sell2.emums;

import lombok.Getter;

@Getter
public enum BlogEnum{
    IDENTIFYING_CODE_FAIL(300, "验证码错误"),
    IDENTIFYING_CODE_NULL(301,"验证码未空"),
    USEREXIST(101,"用户名已存在"),
    PASSWORD_FAIL(102,"密码不一样"),
    ID_NULL(103,"id无效"),
    NAME_NULL(104,"name无效"),
    ACCOUNT_NULL(105,"账号不存在"),
    PASSWORD_ERROR(106,"密码错误"),
    UNAUTHENTICATED(107,"未登录或登陆过期"),
    UNFIND_BBS(109,"未找到评论"),
    ISDZAN_ERROR(108,"你已点过赞了哦(⊙o⊙)"),
    FILE_MAX(110,"图片文件大于1m"),
    DECELT_FALSE(201,"删除失败"),
    UNKONW(1000,"服务器错误"),
    UNKONW_ID_ERROR(1001,"id不一致"),
    BIND_ERROE(2000,"参数校验错误"),
    UNFIND_BLOG(2001,"未找到博客"),
    ERROR_FUCK(30000,"非法请求"),
    ERROR_ADMIN(30001,"你不是管理员"),
    ;
    private Integer code;
    private String message;
    BlogEnum(Integer code,String message)
    {
        this.code = code;
        this.message = message;
    }

}
