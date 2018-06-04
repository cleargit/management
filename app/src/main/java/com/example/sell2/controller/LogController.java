package com.example.sell2.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.sell2.entity.UserInfo;
import com.example.sell2.resultvo.UserLog;
import com.example.sell2.redis.BlogKey;
import com.example.sell2.resultvo.ResultVo;
import com.example.sell2.util.BlogUtil;
import com.example.sell2.util.IpUtil;
import com.example.sell2.util.RedisUtil;
import com.example.sell2.util.ResultUtil;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.web.subject.support.DefaultWebSubjectContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("log")
public class LogController {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    SessionDAO sessionDAO;

    @GetMapping("list.do")
    public JSONObject getlog(@RequestParam("page") int page, @RequestParam(value = "limit",defaultValue = "20") int size ){
        return redisUtil.getlist(page,size);
    }
    @GetMapping("userlist.do")
    public ResultVo getUserlist(@RequestParam(value = "page",defaultValue = "0") int page, @RequestParam(value = "limit",defaultValue = "10") int size , HttpServletRequest request){
        Object object=redisUtil.get(BlogKey.userlog,"log");
        if (object!=null){
            return (ResultVo) object;
        }
        List<UserLog> userLogs=new ArrayList<>();
        Collection<Session> sessions= sessionDAO.getActiveSessions();
        for (Session session:sessions) {
            SimplePrincipalCollection simplePrincipalCollection= (SimplePrincipalCollection) session.getAttribute(DefaultWebSubjectContext.PRINCIPALS_SESSION_KEY);
            UserInfo userInfo= (UserInfo) simplePrincipalCollection.getPrimaryPrincipal();
            if (userInfo!=null){
                String name=userInfo.getName();
                String date=BlogUtil.handletime(session.getLastAccessTime(),"yyyy-MM-dd HH:mm");
                String ip=IpUtil.getIpFromRequest(request);
                Integer userid=userInfo.getUid();
                userLogs.add(new UserLog(userid,name,date,ip));
            }
        }
        if (userLogs.size()!=0){
            redisUtil.set(BlogKey.userlog,"log",ResultUtil.success(userLogs));
        }

        return ResultUtil.success(userLogs);
    }
}
