package com.example.sell2.util;

import com.example.sell2.Dto.UserDto;
import com.example.sell2.entity.UserInfo;
import com.example.sell2.resultvo.Uservo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class Conver {
    public static Uservo conver(UserInfo userInfo)
    {
        Uservo uservo=new Uservo();
        BeanUtils.copyProperties(userInfo,uservo);
        List<String> list=userInfo.getRoleList().stream().map(e->e.getDescription()).collect(Collectors.toList());
        uservo.setRole(list);
        return uservo;
    }
    public static List<Uservo> uservos(List<UserInfo> userInfos)
    {

        return userInfos.stream().map(e->conver(e)).collect(Collectors.toList());
    }

    public static UserInfo adduser( UserDto UserDto)
    {
        try {
            UserDto.setSalt("8d78869f470951332959580424d4bf4f");
            String password=UserDto.getPassword();
            String hex=new SimpleHash("md5",password, ByteSource.Util.bytes(UserDto.getCredentialsSalt()),2).toHex();
            UserDto.setPassword(hex);
            UserInfo userInfo=new UserInfo();
            BeanUtils.copyProperties(UserDto,userInfo);

            return userInfo;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
