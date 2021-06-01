package top.ysqorz.forum.shiro;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.CredentialsMatcher;

/**
 * @author passerbyYSQ
 * @create 2020-08-23 18:42
 */
public class JwtCredentialsMatcher implements CredentialsMatcher {
    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
//        //  AuthenticationInfo info 是我们在JwtRealm中doGetAuthenticationInfo()返回的那个
//        User user = (User) info.getCredentials();
//
//        //String tokenStr = (String) token.getPrincipal();
//        String tokenStr = (String) info.getPrincipals().getPrimaryPrincipal();
//
//        // 校验失败，会抛出异常，被shiro捕获
//        Map<String, String> claims = new HashMap<>();
//        claims.put("userId", user.getId().toString());
//        try {
//            JwtUtils.verifyJwt(tokenStr, user.getJwtSalt(), claims);
//            return true;
//        } catch (JWTVerificationException e) {
//            e.printStackTrace();
//            return false;
//        }
        return true;
    }
}
