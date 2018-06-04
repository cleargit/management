package com.example.sell2.Dto;

import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class UserDto implements Serializable {

    private static final long serialVersionUID = 6099093527328635430L;
    @NotNull(message = "名称不能为空")
    private String name;
    @NotNull
    @Email(message = "邮箱格式错误")
    private String username;
    @NotNull
    @Length(min=6,message="密码长度不能小于6位")
    private String password;
    private String password1;
    private String salt;
    @NotNull(message = "验证码不能为空")
    private String vcode;
    public String getCredentialsSalt(){
        return this.username+this.salt;
    }
}
