package com.example.sell2.redis;


//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.core.RedisTemplate;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//@Configuration
//@EnableCaching
//public class RedisCacheConfig extends CachingConfigurerSupport {
//    @Bean
//    public CacheManager cacheManager(RedisTemplate redisTemplate) {
//        RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
//
//         rcm.setDefaultExpiration(3600*24*3);
//        return rcm;
//    }
//}
