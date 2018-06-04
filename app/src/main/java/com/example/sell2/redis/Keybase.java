package com.example.sell2.redis;

public interface Keybase {
    public int expireSeconds();
    public String getPrefix();
}
