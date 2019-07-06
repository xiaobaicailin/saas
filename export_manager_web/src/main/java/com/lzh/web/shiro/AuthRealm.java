package com.lzh.web.shiro;

import com.lzh.domain.system.Module;
import com.lzh.domain.system.User;
import com.lzh.service.system.ModuleService;
import com.lzh.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AuthRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;
    /**
     * Retrieves the AuthorizationInfo for the given principals from the underlying data store.  When returning
     * an instance from this method, you might want to consider using an instance of
     * {@link SimpleAuthorizationInfo SimpleAuthorizationInfo}, as it is suitable in most cases.
     *
     * @param principals the primary identifying principals of the AuthorizationInfo that should be retrieved.
     * @return the AuthorizationInfo associated with this principals.
     * @see SimpleAuthorizationInfo
     */
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1.获取成功认证的用户的信息
        User user = (User) principals.fromRealm(this.getName()).iterator().next();
        //2.获取当前认证用户的模块
        List<Module> moduleList = moduleService.showModulesByUser(user);
        //3.创建一个容器存储这些模块的名字，要去重，因为每个用户可能会有重复的模块
        Set<String> hs = new HashSet<>();
        //4.将模块名字存入容器中
        for (Module module : moduleList) {
            hs.add(module.getName());
        }
        SimpleAuthorizationInfo Info = new SimpleAuthorizationInfo();
        Info.setStringPermissions(hs);
        return Info;
    }

    /**
     * Retrieves authentication data from an implementation-specific datasource (RDBMS, LDAP, etc) for the given
     * authentication token.
     * <p/>
     * For most datasources, this means just 'pulling' authentication data for an associated subject/user and nothing
     * more and letting Shiro do the rest.  But in some systems, this method could actually perform EIS specific
     * log-in logic in addition to just retrieving data - it is up to the Realm implementation.
     * <p/>
     * A {@code null} return value means that no account could be associated with the specified token.
     *
     * @param token the authentication token containing the user's principal and credentials.
     * @return an {@link AuthenticationInfo} object containing account data resulting from the
     * authentication ONLY if the lookup is successful (i.e. account exists and is valid, etc.)
     * @throws AuthenticationException if there is an error acquiring data or performing
     *                                 realm-specific authentication logic for the specified <tt>token</tt>
     */
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //1.AuthenticationToken强转UsernamePasswordToken
        UsernamePasswordToken Ttoken = (UsernamePasswordToken) token;
        //2.得到登录信息的用户名
        String username = Ttoken.getUsername();
        //3.得到登录信息的密码
//        String password = new String(Ttoken.getPassword(),0,Ttoken.getPassword().length);

        //4.数据库中获取用户信息
        User user = userService.findUserByemail(username);
        //5.判断用户是否为空
        if (user!=null){
            //获取其认证信息,也就是密码验证
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
            //返回时进入CustomCredentialsMatcher 进行密码加密比较
            return info;
        }
        //当返回为null，shiro抛出异常
        return null;
    }
}
