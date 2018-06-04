package com.example.sell2.util;

import com.alibaba.fastjson.JSONObject;
import com.example.sell2.entity.Log;
import com.example.sell2.redis.BlogKey;
import com.example.sell2.redis.Keybase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    RedisTemplate redisTemplate;
    @Autowired
    StringRedisTemplate stringRedisTemplate;


    public boolean set(Keybase keybase, String key, Object value) {
        boolean result = false;
        try {
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();

            int expireSecvonds=keybase.expireSeconds();
            if (expireSecvonds<0)
            {
                operations.set(keybase.getPrefix()+key, value);
            }else {
                operations.set(keybase.getPrefix()+key, value,keybase.expireSeconds(),TimeUnit.SECONDS);
            }
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public Object get(final String key) {
        Object result = null;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        result = operations.get(key);
        return result;
    }
    public Object get(BlogKey basekey,String fix){
        String key=basekey.getPrefix()+fix;
        return get(key);
    }
    public Integer getcount(final String key) {
        String result = null;
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        result = operations.get(key);
        if(result==null)
        {
            return null;
        }
        Integer i=Integer.parseInt(result);
        return i;
    }
    public void remove (Keybase keybase,String keyfix){
        String key=keybase.getPrefix()+keyfix;
        remove(key);
    }
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }
    public void inrc(String key,long length,Boolean is,Integer expire){
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.increment(key,length);
        if (is){
            stringRedisTemplate.expire(key,expire,TimeUnit.SECONDS);
        }
    }

    public void setList(Log log,String name) {
        redisTemplate.opsForList().leftPush(name,log);
    }

    public List<Log> getList() {
        ListOperations listOperations = redisTemplate.opsForList();
        List<Log> logList= listOperations.range("logList",0,-1);;
        return logList;
    }
    public JSONObject getlist(int page,int size){
        ListOperations listOperations = redisTemplate.opsForList();
        List<Log> result= listOperations.range("logList",0,-1);//表示开始为0 结束为-1
        int index=(page-1)*size;
        int num=result.size()-index;
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code","0");
        jsonObject.put("msg","");
        jsonObject.put("count","1000");
        Collections.reverse(result);//倒序
        Object data=(result.subList(index,num));
        jsonObject.put("data", data);
        return jsonObject;
    }
    public JSONObject getUserlist(int page, int size) {
        return null;
    }
}
