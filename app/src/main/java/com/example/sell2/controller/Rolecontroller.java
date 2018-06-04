package com.example.sell2.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.sell2.entity.SysPermission;
import com.example.sell2.entity.SysRole;
import com.example.sell2.redis.BlogKey;
import com.example.sell2.repository.PerRepository;
import com.example.sell2.repository.SysRoleRepsitory;
import com.example.sell2.resultvo.PermissionVo;
import com.example.sell2.resultvo.ResultVo;
import com.example.sell2.util.RedisUtil;
import com.example.sell2.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("role")
public class Rolecontroller {
    @Autowired
    PerRepository perrepository;
    @Autowired
    private SysRoleRepsitory roleRepsitory;
    @Autowired
    RedisUtil redisUtil;
    @RequestMapping("role.do")
    public JSONArray role(){
        JSONArray json= ((JSONArray) redisUtil.get(BlogKey.rolelist + "list"));
        if (json!=null){
            return json;
        }
        JSONArray jsonArray=new JSONArray();
        for (SysRole sysRole:roleRepsitory.findAll()) {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("id",sysRole.getId());
            jsonObject.put("name",sysRole.getDescription());

            List<PermissionVo> list=new ArrayList<>();
            for (SysPermission p:
                    sysRole.getPermissions()) {
                PermissionVo permissionVo=new PermissionVo();
                permissionVo.setId(String.valueOf(sysRole.getId())+String.valueOf(p.getId()));
                permissionVo.setName(p.getName());
                list.add(permissionVo);
            }
            jsonObject.put("children",list);
            jsonArray.add(jsonObject);
        }
        redisUtil.set(BlogKey.rolelist,"list",jsonArray);
        return  jsonArray;
    }
    @PostMapping("add.do")
    public ResultVo setrole(String per, Integer id, String description, String rolename){
        SysRole role=new SysRole();
        role.setId(id);
        role.setDescription(description);
        role.setRole(rolename);

        List<Integer> listid=JSONArray.parseArray(per,Integer.class);

        role.setPermissions(perrepository.findAllById(listid));
        roleRepsitory.save(role);
        return ResultUtil.success("添加成功");
    }
    @PostMapping("delect.do")
    public ResultVo delectrole(Integer id){
        roleRepsitory.deleteById(id);
        return ResultUtil.success("删除成功");
    }
}
