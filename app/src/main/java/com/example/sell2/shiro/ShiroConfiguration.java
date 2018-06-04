package com.example.sell2.shiro;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.sell2.entity.SysPermissionInit;
import com.example.sell2.server.SyspermissionServer;
import com.example.sell2.shiro.config.MyshiroRealm;
import com.example.sell2.shiro.filter.AuthLogfilter;
import com.example.sell2.shiro.filter.KickoutSessionControlFilter;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;


@Configuration
public class ShiroConfiguration {

    @Autowired
    private SyspermissionServer syspermissionServer;
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.password}")
    private String password;


    @Bean
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager){
        System.out.println("ShiroConfiguration.shirFilter()");
        ShiroFilterFactoryBean shiroFilterFactoryBean  = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filtersMap = shiroFilterFactoryBean.getFilters();
//        filtersMap.put("kickout",kickoutSessionControlFilter());
        filtersMap.put("sham",authLogfilter());
        shiroFilterFactoryBean.setFilters(filtersMap);
       List<SysPermissionInit> list=syspermissionServer.findAllpermission();

        //拦截器.a
        Map<String,String> filterChainDefinitionMap = new LinkedHashMap<String,String>();
        for (SysPermissionInit o1:list) {
            filterChainDefinitionMap.put(o1.getUrl(),o1.getPermissionInit());
        }

        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public MyshiroRealm myShiroRealm(){
        MyshiroRealm myShiroRealm = new MyshiroRealm();
        myShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myShiroRealm;
    }

    //总配置  注入
    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager securityManager =  new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(SessionManager());
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    //算法
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher(){
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();

        hashedCredentialsMatcher.setHashAlgorithmName("md5");//散列算法:这里使用MD5算法;
        hashedCredentialsMatcher.setHashIterations(2);//散列的次数，比如散列两次，相当于 md5(md5(""));

        return hashedCredentialsMatcher;

        //session
    }
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    //sessionManager
    public DefaultWebSessionManager SessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setPassword(password);
        redisManager.setExpire(1800);// 配置过期时间
        // redisManager.setTimeout(timeout);
        // redisManager.setPassword(password);
        return redisManager;
    }
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }
    //Aop支持  权限配置需要
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager){
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    //rememberme  cookie
    @Bean
    public SimpleCookie remember()
    {
        System.out.println("ShiroConfiguration.rememberMeCookie()");
        SimpleCookie simpleCookie=new SimpleCookie("rememberme");
        simpleCookie.setMaxAge(10000);
        return simpleCookie;

    }

    //CookieManager
    @Bean
    public CookieRememberMeManager rememberMeManager()
    {
        System.out.println("ShiroConfiguration.rememberMeManager()");
        CookieRememberMeManager manager=new CookieRememberMeManager();
        manager.setCookie(remember());
        return manager;
    }
    public KickoutSessionControlFilter kickoutSessionControlFilter()
    {
        KickoutSessionControlFilter kickoutSessionControlFilter=new KickoutSessionControlFilter();
        kickoutSessionControlFilter.setMaxSession(1);
        kickoutSessionControlFilter.setSessionManager(SessionManager());
        kickoutSessionControlFilter.setCacheManager(cacheManager());
        kickoutSessionControlFilter.setKockouturl("/kickout");
        return kickoutSessionControlFilter;
    }
    public AuthLogfilter authLogfilter(){
        AuthLogfilter authLogfilter=new AuthLogfilter();
        return authLogfilter;
    }
}
