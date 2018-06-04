package com.example.sell2.shiro.filter;

import com.example.sell2.entity.UserInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.DefaultSessionKey;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;
import java.util.Deque;
import java.util.LinkedList;

public class KickoutSessionControlFilter extends AccessControlFilter {

    private String kockouturl;
    private int maxSession=1;
    private SessionManager sessionManager;
    private Cache<String, Deque<Serializable>> cache;
    private boolean kickoutAfter = true;

    public void setKockouturl(String kockouturl) {
        this.kockouturl = kockouturl;
    }

    public void setMaxSession(int maxSession) {
        this.maxSession = maxSession;
    }

    public void setSessionManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void setCacheManager(CacheManager cacheManager) {
        this.cache = cacheManager.getCache("shiro_redis_cache");
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject=getSubject(servletRequest,servletResponse);
        Session session=subject.getSession();
        UserInfo userInfo= ((UserInfo) subject.getPrincipal());
        String username=userInfo.getUsername();
        Serializable sessionid=session.getId();
        Deque<Serializable> deque = cache.get(username);
        if (deque==null)
        {
            deque=new LinkedList<>();
        }
        if (!deque.contains(sessionid)&&session.getAttribute("kickout")==null)
        {
            deque.push(sessionid);
            cache.put(username, deque);
        }
        //开踢
        while (deque.size()>maxSession)
        {
            Serializable kickoutSessionId = null;
            if (kickoutAfter)
            {
                kickoutSessionId = deque.removeFirst();
            }else {
                kickoutSessionId = deque.removeLast();
            }
            cache.put(username, deque);
            try
            {
                Session kickout=sessionManager.getSession(new DefaultSessionKey(kickoutSessionId));
                if (kickout!=null)
                {
                    kickout.setAttribute("kickout",true);
                }
            }catch (Exception x)
            {

            }

            if ((Boolean)session.getAttribute("kickout")!=null&&(Boolean) session.getAttribute("kickout")==true)
            {
                try {
                    subject.logout();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

            }
            saveRequest(servletRequest);
            WebUtils.issueRedirect(servletRequest, servletResponse, kockouturl);
            return false;
        }


        return true;
    }
}
