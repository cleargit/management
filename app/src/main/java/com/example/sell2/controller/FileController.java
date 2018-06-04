package com.example.sell2.controller;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.example.sell2.entity.UserInfo;
import com.example.sell2.server.Userinfoserver;
import com.example.sell2.util.SecurityU;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;

@RestController
public class FileController
{
    @Autowired
    Userinfoserver userinfoserver;
    public static final Integer MAX=1048576;
    @PostMapping("/upload.do")
    public Map<String,String> upload(@RequestParam(value = "file")MultipartFile file, @RequestParam(value = "id") Integer id)
    {
        Map<String,String> map=new HashMap<>();
        try {
            int size=file.getBytes().length;
            if(size>MAX)
            {
                map.put("code","0");
                map.put("msg","图片超过1mb");
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        UserInfo userInfo=userinfoserver.findone(id);
        if (!SecurityU.simpleCheck(userInfo)){
            map.put("code","0");
            map.put("msg","错误");
            return map;
        }
        if(file.isEmpty())
        {
            return null;
        }
        String endPoint="oss-cn-shenzhen.aliyuncs.com";
        String accessKeyId="xxxxxxx";
        String accessKeySecret="xxxxxxxxxxx";
        OSSClient ossClient=new OSSClient(endPoint,accessKeyId,accessKeySecret);
        String name=file.getOriginalFilename();
        String last=name.substring(name.lastIndexOf("."));
        String key=String.valueOf(id)+last;
        try {
            ossClient.putObject("mybucket99", key, new ByteArrayInputStream(file.getBytes()));
        }catch (IOException e)
        {
            map.put("code","0");
            map.put("msg","上传失败");
            return map;
        }finally {
            ossClient.shutdown();
        }
        userInfo.setImg("http://mybucket99.oss-cn-shenzhen.aliyuncs.com/"+key);
        userinfoserver.insert(userInfo);
        map.put("img","http://mybucket99.oss-cn-shenzhen.aliyuncs.com/"+key);
        map.put("msg","上传成功");
        return map;
    }
    @PostMapping("/myupload.do")
    public Object upload(@RequestParam(value = "file")MultipartFile file)
    {
        JSONObject jsonObject=new JSONObject();
        String myword="myblogimg/";
        String name=file.getOriginalFilename();
        String begin=name.substring(0,name.lastIndexOf("."));
        String last=name.substring(name.lastIndexOf("."));
        String save=begin+"_"+UUID.randomUUID().toString()+last;
        String result=myword+save;
        List<String> list=new ArrayList<>();
        String endPoint="oss-cn-shenzhen.aliyuncs.com";
        String accessKeyId="LTAIVUF2Krgp1u4Z";
        String accessKeySecret="k78gbDqJxvZ1ovhnZzeKq8vvgJBqlm";
        OSSClient ossClient=new OSSClient(endPoint,accessKeyId,accessKeySecret);
        try {
            ossClient.putObject("mybucket99", result, new ByteArrayInputStream(file.getBytes()));
            list.add("http://mybucket99.oss-cn-shenzhen.aliyuncs.com/"+result);
            jsonObject.put("errno",0);
            jsonObject.put("data",list);
        }catch (IOException e)
        {
            jsonObject.put("errno",1);
        }finally {
            ossClient.shutdown();
        }
        return jsonObject;
    }
}
