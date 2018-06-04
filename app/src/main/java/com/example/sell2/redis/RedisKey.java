package com.example.sell2.redis;


import lombok.Getter;

public abstract class RedisKey implements Keybase{
    private int expireSeconds;
    private String prefix;
    public RedisKey(String prefix){
      this(0,prefix);//默认0代表永不过期
    }
    public RedisKey(int expireSeconds,String prefix){
        this.expireSeconds=expireSeconds;
        this.prefix=prefix;
    }
    public int expireSeconds() {
        return expireSeconds;
    }

    public String getPrefix() {
        String className = getClass().getSimpleName();
        return className+":" + prefix;
    }
}
