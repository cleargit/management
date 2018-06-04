package com.example.sell2.shiro.filter;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class AuthLogfilter extends PathMatchingFilter {

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject=SecurityUtils.getSubject();
        ShiroHttpServletResponse shiroresponse=  (ShiroHttpServletResponse) response;

        if (!subject.isAuthenticated()){
            shiroresponse.setHeader("SESSIONSTATUS","TIMEOUT");
            shiroresponse.setHeader("URL","/pages/login");
            return true;
        }
       return true;
    }
}
