package top.ysqorz.forum.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.utils.JwtUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author passerbyYSQ
 * @create 2020-08-23 18:42
 */
public class JwtCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        //  AuthenticationInfo info 是我们在JwtRealm中doGetAuthenticationInfo()返回的那个
        User user = (User) info.getCredentials();

        //String tokenStr = (String) token.getPrincipal();
        //Integer userId = (Integer) info.getPrincipals().getPrimaryPrincipal();

        // 校验失败，会抛出异常，被shiro捕获
        Map<String, String> claims = new HashMap<>();
        claims.put("userId", user.getId().toString());

        return JwtUtils.verifyJwt((String) token.getCredentials(), user.getJwtSalt(), claims);
    }
}
