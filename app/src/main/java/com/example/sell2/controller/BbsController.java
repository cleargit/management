package com.example.sell2.controller;

import com.example.sell2.emums.BlogEnum;
import com.example.sell2.entity.Bbs;
import com.example.sell2.entity.UserInfo;
import com.example.sell2.redis.BlogKey;
import com.example.sell2.resultvo.ResultVo;
import com.example.sell2.server.BbsServer;
import com.example.sell2.server.Userinfoserver;
import com.example.sell2.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("bbs")
public class BbsController {
    @Autowired
    BbsServer server;
    @Autowired
    Userinfoserver userinfoserver;
    private Set set=new LinkedHashSet();
    @PostMapping("save.do")
    public ResultVo save(@RequestParam(value = "aid")Integer aid, @RequestParam(value = "content") String content, @RequestParam(value = "isk") String isk)
    {
        UserInfo userInfo= SecurityU.isAuthenticated();
        Bbs bbs=new Bbs();
        bbs.setAid(aid);
        bbs.setContent(content);
        bbs.setImg(userInfo.getImg());
        bbs.setTime(BlogUtil.getDate());
        bbs.setName(userInfo.getName());
        bbs.setUid(userInfo.getUid());
        if (isk==null||isk.isEmpty()) {
            bbs.setIsk(IsKeyUtil.key());
            bbs.setOb("main");
        }
        else {
            bbs.setIsk(isk);
            bbs.setOb("chick");
        }
        set.remove(aid);
        Bbs re=server.sava(bbs);
        return ResultUtil.success(re);
    }
    @GetMapping("get.do")
    public ResultVo get(@RequestParam(value ="page",defaultValue = "0")Integer page,
                        @RequestParam(value = "size",defaultValue = "10") Integer size,
                        @RequestParam(value = "aid")Integer aid)
    {
        List<Bbs> bslist;
        Page<Bbs> dopage;
        Sort sort=new Sort(Sort.Direction.DESC,"dzan");
        PageRequest request=new PageRequest(page,size,sort);
            dopage=server.findByObAndAid("main",aid,request);
            if (dopage.getContent().size()==0) {
                set.add(aid);
                return ResultUtil.success("",0);
            }
            bslist=dopage.getContent();
            bslist.get(0).setPage(dopage.getTotalPages());
        for (int i = 0; i <bslist.size() ; i++) {
            String isk=bslist.get(i).getIsk();
            if (isk==""||isk.isEmpty()) {
                continue;
            }
            List<Bbs> list=server.findByIsk(isk);
            if (list.size()>0){
                bslist.get(i).setList(list);
            }
        }
        return ResultUtil.success(bslist,dopage.getTotalPages());
}
    @PostMapping("dzan.do")
    public ResultVo dzan(@RequestParam(value = "id") Integer id)
    {
        UserInfo userInfo=SecurityU.isAuthenticated();
        if (server.isDzan(userInfo.getUid(),id)) {
            return ResultUtil.success("点赞成功");
        }else {
            return ResultUtil.error(BlogEnum.ISDZAN_ERROR);
        }
    }
    @GetMapping(value = "list.do")
    public ResultVo getall(@RequestParam(value = "page",defaultValue = "0") Integer page,@RequestParam(value = "size",defaultValue = "10") Integer size )
    {
        page-=1;

        PageRequest request=new PageRequest(page,size);
        Page<Bbs> page1=server.getlist(request);
        return ResultUtil.success(page1.getContent(),page1.getTotalPages());
    }
    @PostMapping(value = "delect.do")
    public ResultVo delect(@RequestParam(value = "id")Integer id)
    {
        if (id==null) {
            return ResultUtil.error(BlogEnum.ID_NULL);
        }
        Bbs bbs=server.findone(id);
        if (bbs==null) {
            return ResultUtil.error(BlogEnum.ID_NULL);
        }
        server.delect(bbs);
        return ResultUtil.success("删除成功");
    }
}
