package com.example.sell2.controller;

import com.example.sell2.access.AccessLimit;
import com.example.sell2.emums.BlogEnum;
import com.example.sell2.entity.Blog;
import com.example.sell2.hander.BlogException;
import com.example.sell2.redis.BlogKey;
import com.example.sell2.resultvo.ResultVo;
import com.example.sell2.server.Blogserver;
import com.example.sell2.util.RedisUtil;
import com.example.sell2.util.ResultUtil;
import com.example.sell2.util.BlogUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("blog")
public class blogController {
    @Autowired
    Blogserver blogserver;
    @Autowired
    RedisUtil redisUtil;
    private static String key=BlogKey.bloglist.getPrefix()+"list";
    @PostMapping("add.do")
    public ResultVo add(HttpServletRequest request)
    {
        Integer type=Integer.parseInt(request.getParameter("type"));
        String title=request.getParameter("title");
        String intro=request.getParameter("intro");
        String content=request.getParameter("content");
        String imgurl=request.getParameter("img");
        Blog blog =new Blog(content,intro,title,type,imgurl);
        blog.setTime(BlogUtil.getDate());
        blogserver.sava(blog);
        redisUtil.remove(key);
        return ResultUtil.success("添加成功");
    }
    @GetMapping("list.do")
    public ResultVo findlist( @RequestParam(value = "page",defaultValue ="0") Integer page,
                              @RequestParam(value = "size",defaultValue ="10")Integer size,
                              @RequestParam(value = "bit",defaultValue ="0")Integer bit,
                              HttpServletRequest request)
    {
       if (bit==1){
           page=0;
       }
        Page<Blog> page1=(Page<Blog>) redisUtil.get(key);
        if (page1==null){
            PageRequest pagerequest=new PageRequest(page,size);
            page1=blogserver.getList(pagerequest);
            redisUtil.set(BlogKey.bloglist,"list",page1);
        }
        return ResultUtil.success(page1.getContent(),page1.getTotalPages());
    }
    @AccessLimit(expireSeconds=8,maxcount = 5)
    @GetMapping("findone.do")
    public ResultVo findone(@RequestParam(value = "id") Integer id)
    {
        if (id==null)
        {
           throw new BlogException(BlogEnum.ID_NULL);
        }
        Object o=redisUtil.get(BlogKey.token,String.valueOf(id));
        if (o!=null)
        {
            return ResultUtil.success(o);
        }
        Blog blog =blogserver.findone(id);
        if (blog !=null)
        {
            Integer viewcount= blog.getViewcount()+1;
            blog.setViewcount(viewcount);
            blogserver.sava(blog);
            redisUtil.set(BlogKey.token,String.valueOf(id),blog);
            return ResultUtil.success(blog);
        }
        return ResultUtil.error(BlogEnum.UNFIND_BLOG);

    }

    @PostMapping("delect.do")
    public Map<String,Object> delect(Integer id)
    {
            Map<String,Object> map=new LinkedHashMap<>();
            blogserver.decelt(id);
            map.put("code",0);
            map.put("msg","删除成功");
            redisUtil.remove(key);
            return map;
    }
    @PostMapping("edit.do")
    public ResultVo edit(HttpServletRequest request)
    {
        Integer id=Integer.parseInt(request.getParameter("id"));
        Integer type=Integer.parseInt(request.getParameter("type"));
        String title=request.getParameter("title");
        String intro=request.getParameter("intro");
        String content=request.getParameter("content");
        Blog blog =blogserver.findone(id);
        blog.setTitle(title);
        blog.setIntro(intro);
        blog.setType(type);
        blog.setContent(content);
        blogserver.sava(blog);
        redisUtil.remove(key);
        return ResultUtil.success("修改成功");
    }

    @GetMapping("/logout.do")
    public void logout(HttpServletResponse response) throws IOException {
        SecurityUtils.getSubject().logout();
        response.sendRedirect("index");
    }
}
