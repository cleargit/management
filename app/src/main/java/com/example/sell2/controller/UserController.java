package com.example.sell2.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.sell2.Dto.UserDto;
import com.example.sell2.Dto.UserEdit;
import com.example.sell2.access.AccessLimit;
import com.example.sell2.emums.BlogEnum;
import com.example.sell2.entity.Blog;
import com.example.sell2.entity.SysPermission;
import com.example.sell2.entity.SysRole;
import com.example.sell2.entity.UserInfo;
import com.example.sell2.hander.BlogException;
import com.example.sell2.redis.BlogKey;
import com.example.sell2.repository.PerRepository;
import com.example.sell2.repository.SysRoleRepsitory;
import com.example.sell2.resultvo.PermissionVo;
import com.example.sell2.resultvo.ResultVo;
import com.example.sell2.resultvo.Uservo;
import com.example.sell2.server.Imp.MailService;

import com.example.sell2.server.Userinfoserver;
import com.example.sell2.util.Conver;
import com.example.sell2.util.RedisUtil;
import com.example.sell2.util.ResultUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private SysRoleRepsitory roleRepsitory;
    @Autowired
    private Userinfoserver userinfoserver;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    MailService mailService;
    @Autowired
    PerRepository perrepository;
    @RequestMapping("add.do")
    public ResultVo adduser(@Valid UserDto userDto)
    {
        Session session = SecurityUtils.getSubject().getSession();
        String v = (String) session.getAttribute("_code");
        session.setAttribute("_code","kkkk");//重置验证码
        String vcode=userDto.getVcode();
        vcode=vcode.toLowerCase();
        if (!vcode.equals(v))
        {
            throw new BlogException(BlogEnum.IDENTIFYING_CODE_FAIL);
        }
        if (userinfoserver.findbyname(userDto.getUsername())!=null)
        {
            throw new BlogException(BlogEnum.USEREXIST);
        }
        if (!userDto.getPassword().equals(userDto.getPassword1()))
        {
            throw new BlogException(BlogEnum.PASSWORD_FAIL);
        }
        String token= UUID.randomUUID().toString();
        redisUtil.set(BlogKey.register,token,userDto);
        mailService.sendhtml(userDto.getUsername(),"sham博客",token);
        return ResultUtil.success("请在你的邮箱点击激活");
    }
    @GetMapping("confirm.do")
    public void confirm(String token, HttpServletResponse response) throws IOException {
        
        UserDto userDto= ((UserDto) redisUtil.get(BlogKey.register,token));
        if (userDto==null)
        {
            response.sendRedirect("/loading?"+"0");
            return;
        }
        redisUtil.remove(token);
        UserInfo userInfo=Conver.adduser(userDto);
        List<Integer> list=new ArrayList<>();
        list.add(3);
        if ( userinfoserver.insert(userInfo)!=null)
        {
            response.sendRedirect("/pages/loading?"+"1");
            return;
        }
        response.sendRedirect("/pages/loading?"+"2");
    }
    @GetMapping("list.do")
    public ResultVo<List<Uservo>> list()
    {
        List<Uservo> list;
        list=(List<Uservo>) redisUtil.get(BlogKey.userlist,"get");
        if (list!=null){
            return ResultUtil.success(list);
        }else {
            list= userinfoserver.findAll();
            redisUtil.set(BlogKey.userlist,"get",list);
            return ResultUtil.success(list);
        }
    }
    @PostMapping("delect.do")
    public ResultVo delete(Integer id)
    {
        if (userinfoserver.delete(id))
        return ResultUtil.success("删除成功");
        return ResultUtil.error(BlogEnum.DECELT_FALSE);
    }
    @GetMapping("401.do")
    public String say()
    {
        return "权限不足";
    }
    @PostMapping("edit.do")
    public ResultVo edit(UserEdit edit)
    {
        UserInfo userInfo= userinfoserver.findone(edit.getId());
        if (userInfo==null) {
            throw new BlogException(BlogEnum.ID_NULL);
        }
        if (edit.getName()==null) {
            throw new BlogException(BlogEnum.NAME_NULL);
        }
        List<SysRole> list=roleRepsitory.findAllById(edit.getList());
        userInfo.setRoleList(list);
        userInfo.setName(edit.getName());
        userinfoserver.insert(userInfo);
        return ResultUtil.success("成功修改");
    }
    @AccessLimit(expireSeconds = 60*5,maxcount = 5)
    @PostMapping("admin.do")
    public void isadmin(String pass){
        try
        {
            UsernamePasswordToken token = new UsernamePasswordToken("admin", pass);
            Subject subject=SecurityUtils.getSubject();
            subject.login(token);
        }catch (Exception e){
            throw new BlogException(BlogEnum.ERROR_ADMIN);
        }
    }

}
