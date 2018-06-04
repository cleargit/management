package com.example.sell2.access;


import com.alibaba.fastjson.JSON;
import com.example.sell2.emums.BlogEnum;
import com.example.sell2.redis.BlogKey;
import com.example.sell2.resultvo.ResultVo;
import com.example.sell2.util.IpUtil;
import com.example.sell2.util.RedisUtil;
import com.example.sell2.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;

@Service
public class AccessInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    RedisUtil redisUtil;
    /*

     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod)
        {
            HandlerMethod handlerMethod= ((HandlerMethod) handler);
            AccessLimit accessLimit=handlerMethod.getMethodAnnotation(AccessLimit.class);
            if (accessLimit==null){
                return true;
            }
            int expireSeconds = accessLimit.expireSeconds();
            int maxcount = accessLimit.maxcount();
            String ipAddress =IpUtil.getIpFromRequest(request);
            String key = new BlogKey(expireSeconds,"ak").getPrefix() + request.getRequestURI() + ipAddress;
            Integer count = redisUtil.getcount(key);
            if (count == null) {
                redisUtil.inrc(key, 1, true, expireSeconds);
            } else if (count < maxcount) {
                redisUtil.inrc(key, 1, false, expireSeconds);
            } else {
                send(response, ResultUtil.error(BlogEnum.ERROR_FUCK));
                return false;
            }
        }
        return true;
    }

    private void send(HttpServletResponse response, ResultVo errorFuck) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(errorFuck);
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

}
