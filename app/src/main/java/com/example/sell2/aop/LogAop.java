package com.example.sell2.aop;


import com.example.sell2.entity.Log;
import com.example.sell2.util.BlogUtil;
import com.example.sell2.util.IpUtil;
import com.example.sell2.util.RedisUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;


@Aspect
@Component
public class LogAop {
    @Autowired
    RedisUtil redisUtil;
    public final String LOGLIST="logList";

    @Pointcut("execution(public * com.example.sell2.controller.*.*(..))")
    public void pointcut() {
    }



    @Before("pointcut()")
    public void bef(JoinPoint joinPoint)throws Exception {
        ServletRequestAttributes attributes=(ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=attributes.getRequest();
        String url=request.getRequestURI();
        String ip=IpUtil.getIpFromRequest(request);
        String time=BlogUtil.handletime(new Date(),"yyyy-MM-dd HH:mm");
        joinPoint.getSignature().getDeclaringTypeName();
//        Object[] args = joinPoint.getArgs();
        String methodtype=request.getMethod();
        String methodname=joinPoint.getSignature().getName();
        Log log=new Log(url,ip,time,methodname,methodtype);
        redisUtil.setList(log,LOGLIST);
    }



}