package com.example.sell2.controller;

import com.example.sell2.access.AccessLimit;
import com.example.sell2.emums.BlogEnum;
import com.example.sell2.entity.UserInfo;
import com.example.sell2.hander.BlogException;
import com.example.sell2.resultvo.ResultVo;
import com.example.sell2.server.Userinfoserver;
import com.example.sell2.util.ResultUtil;
import com.example.sell2.util.SecurityU;
import com.example.sell2.vcode.Captcha;
import com.example.sell2.vcode.GifCaptcha;
import com.example.sell2.vcode.NewC;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;

import org.apache.shiro.subject.Subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    Userinfoserver userinfoserver;
    @AccessLimit(expireSeconds = 5,maxcount = 5)
    @RequestMapping(value="ajaxLogin.do",method=RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> submitLogin(String username, String password, String vcode,String flag) {
        Map<String, Object> resultMap = new LinkedHashMap<String, Object>();
        String url;
        if (flag!=null && flag.equals("on")){
            url="/index";
        }else {
            url="/blog/index";
        }
        if(vcode==null||vcode==""){
            throw new BlogException(BlogEnum.IDENTIFYING_CODE_NULL);
        }
        Session session = SecurityUtils.getSubject().getSession();
        vcode = vcode.toLowerCase();
        String v = (String) session.getAttribute("_code");
        session.removeAttribute("_code");
        if(!vcode.equals(v)){
           throw new BlogException(BlogEnum.IDENTIFYING_CODE_FAIL);
        }
        try {
            UsernamePasswordToken token = new UsernamePasswordToken(username, password,true);
            Subject subject=SecurityUtils.getSubject();
            subject.login(token);
            UserInfo userInfo= ((UserInfo) subject.getPrincipal());
            if (userInfo.getState()==2){
                subject.logout();
                resultMap.put("status", 500);
                resultMap.put("message", "账号已被禁");
            }else {
                resultMap.put("status", 200);
                resultMap.put("url",url);
                resultMap.put("message", "登录成功");
            }

        } catch (Exception e) {
            if (UnknownAccountException.class.isInstance(e)) {
                throw new BlogException(BlogEnum.ACCOUNT_NULL);
            } else if (IncorrectCredentialsException.class.isInstance(e)) {
               throw new BlogException(BlogEnum.PASSWORD_ERROR);
            }
        }
        return resultMap;
    }


    @RequestMapping(value="getGifCode.do",method=RequestMethod.GET)
    public void getGifCode(HttpServletResponse response, HttpServletRequest request){
        try {
            String referer=request.getHeader("referer");

            HttpSession session = request.getSession(true);
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/gif");
            NewC newC=new NewC();
            String v=newC.text();
            NewC.outputImage(130,40,response.getOutputStream(),v,"mixGIF");
            session.setAttribute("_code",v.toLowerCase());

        } catch (Exception e) {
            System.err.println("获取验证码异常："+e.getMessage());
        }
    }
    @RequestMapping(value = "logout.do")
    public void logout(HttpServletResponse response) throws IOException {
        Subject subject= SecurityUtils.getSubject();
        subject.logout();
        response.sendRedirect("/pages/login");
    }
}

