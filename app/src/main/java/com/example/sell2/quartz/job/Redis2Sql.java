package com.example.sell2.quartz.job;

import com.example.sell2.entity.Log;
import com.example.sell2.repository.Logrepository;
import com.example.sell2.util.RedisUtil;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.List;

public class Redis2Sql extends QuartzJobBean {

    @Autowired
    RedisUtil redisUtil;
    @Autowired
    Logrepository logrepository;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<Log> logs= (List<Log>) redisUtil.getList();
        if (logs!=null){
            for (Log log:logs) {
                logrepository.save(log);
            }
        }else {
            System.out.println("error");
        }

    }
}
