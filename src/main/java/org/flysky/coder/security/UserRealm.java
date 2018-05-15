package org.flysky.coder.security;

import org.apache.coyote.http2.ByteUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.flysky.coder.entity.User;
import org.flysky.coder.mapper.UserMapper;
import org.flysky.coder.vo.user.PMConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by hxuhao233 on 2018/4/13.
 * shiro认证类
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;

    /**
     * 权限认证
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user  = (User)principalCollection.getPrimaryPrincipal();


        if(user.getType() == 1){
            // 普通用户
            authorizationInfo.addRole("user");          // 角色权限

            //authorizationInfo.addStringPermission();    // 具体权限

        } else if(user.getType() == 2){
            // 管理员
            authorizationInfo.addRole("manager");          // 角色权限

            //authorizationInfo.addStringPermission();    // 具体权限
        }
        return authorizationInfo;
    }

    /**
     * 登录认证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = String.valueOf(token.getPrincipal());
//        System.out.println(token.getCredentials());
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        User user = userMapper.selectByEmail(username);
//        System.out.println("----->>userInfo="+userInfo);
        if (user == null) {
            return null;
        }
//        if (user.getActivated() == 0) { //账户冻结
//            throw new LockedAccountException();
//        }
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                user, //用户
                user.getPassword(), //密码
                ByteSource.Util.bytes(PMConfig.SALT),//salt
                getName()  //realm name
        );
        return authenticationInfo;
    }

}
