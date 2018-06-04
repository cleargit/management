package com.example.sell2.shiro.config;

import com.example.sell2.entity.SysPermission;
import com.example.sell2.entity.SysRole;
import com.example.sell2.entity.UserInfo;
import com.example.sell2.server.Userinfoserver;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

//自定义认证 权限配置
public class MyshiroRealm extends AuthorizingRealm {
    @Autowired
    private Userinfoserver userinfoserver;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        UserInfo userInfo = (UserInfo) principalCollection.getPrimaryPrincipal();
        for (SysRole role:userInfo.getRoleList())
        {
            authorizationInfo.addRole(role.getRole());
            for (SysPermission permission:role.getPermissions())
            {
                authorizationInfo.addStringPermission(permission.getPermission());
            }
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()"+token.getPrincipal());

        String name= ((String) token.getPrincipal());
        UserInfo userInfo=userinfoserver.findbyname(name);
//        System.out.println(userInfo.getClass().getClassLoader());
//        System.out.println("----->>userInfo="+userInfo);
        if(userInfo == null){
            return null;
        }
        userinfoserver.incrementAndUpdate(userInfo);
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                userInfo, //用户
                userInfo.getPassword(), //认为真密码
                ByteSource.Util.bytes(userInfo.getCredentialsSalt()),//salt=username+salt加密模式
                getName()  //realm name
        );
        return authenticationInfo;
    }
}
