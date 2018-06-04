package com.example.sell2.util;

import com.example.sell2.emums.BlogEnum;
import com.example.sell2.entity.UserInfo;
import com.example.sell2.hander.BlogException;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class SecurityU {
    public static Boolean simpleCheck(UserInfo userInfo)
    {
        Subject subject= SecurityUtils.getSubject();
        UserInfo userInfo1= ((UserInfo) subject.getPrincipal());
        if (userInfo==null)
        {
            throw new BlogException(BlogEnum.ACCOUNT_NULL);
        }
        if (!userInfo1.getUid().equals(userInfo.getUid()))
        {
            throw new BlogException(BlogEnum.UNKONW_ID_ERROR);
        }
        return true;
    }
    public static UserInfo isAuthenticated()
    {
        Subject subject= SecurityUtils.getSubject();
        if (!subject.isAuthenticated()){
           throw new BlogException(BlogEnum.UNAUTHENTICATED);
        }
        return ((UserInfo) subject.getPrincipal());
    }

}
