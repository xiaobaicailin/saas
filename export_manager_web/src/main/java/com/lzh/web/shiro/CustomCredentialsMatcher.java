package com.lzh.web.shiro;

import com.lzh.common.utils.Encrypt;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class CustomCredentialsMatcher extends SimpleCredentialsMatcher {
    /**
     * This implementation acquires the {@code token}'s credentials
     * (via {@link #getCredentials(AuthenticationToken) getCredentials(token)})
     * and then the {@code account}'s credentials
     * (via {@link #getCredentials(AuthenticationInfo) getCredentials(account)}) and then passes both of
     * them to the {@link #equals(Object, Object) equals(tokenCredentials, accountCredentials)} method for equality
     * comparison.
     *
     * @param token the {@code AuthenticationToken} submitted during the authentication attempt.
     * @param info  the {@code AuthenticationInfo} stored in the system matching the token principal.
     * @return {@code true} if the provided token credentials are equal to the stored account credentials,
     * {@code false} otherwise
     */
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //1.AuthenticationToken强转UsernamePasswordToken
        UsernamePasswordToken tToken = (UsernamePasswordToken) token;
        //2.获取输入表单的明文账号和密码
        String email = tToken.getUsername();
        String password = new String(tToken.getPassword(),0,tToken.getPassword().length);
        //3.使用MD5加密
        String MDpassword = Encrypt.md5(password, email);
        //4.获取数据库的已经加密的密码， 在info参数中储存
        String dbpassword = info.getCredentials().toString();
        //5.比较加密密码
        return super.equals(MDpassword, dbpassword);//返回值为false，抛出异常
    }
}
