package top.ysqorz.forum.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import top.ysqorz.forum.utils.SpringUtils;

/**
 * https://www.jianshu.com/p/0b1131be7ace
 * @author passerbyYSQ
 * @create 2021-06-18 21:17
 */
public class ShiroUtils {

    /**
     * 清除某个用户的授权缓存
     */
    public static void clearUserAuthorizationCache(Integer userId){
        Subject subject = SecurityUtils.getSubject();
        JwtRealm jwtRealm = SpringUtils.getBean(JwtRealm.class);
//        String realmName = subject.getPrincipals().getRealmNames().iterator().next();
        SimplePrincipalCollection principals = new SimplePrincipalCollection(userId, jwtRealm.getName());
        subject.runAs(principals); // 不能使用noSessionCreation完全禁止使用session，否则无法切换Subject
        jwtRealm.getAuthorizationCache().remove(subject.getPrincipals());
        subject.releaseRunAs();
    }

    /**
     * 如果Subject认证成功，此处必定可以取地userId
     */
    public static Integer getUserId() {
        return (Integer) SecurityUtils.getSubject().getPrincipal();
    }

    /**
     * 获取登录用户缓存在Redis中的认证信息
     */
    public static LoginUser getLoginUser() {
        JwtRealm jwtRealm = SpringUtils.getBean(JwtRealm.class);
        AuthenticationInfo authenticationInfo = jwtRealm.getAuthenticationCache()
                .get(SecurityUtils.getSubject().getPrincipals());
        return (LoginUser) authenticationInfo.getCredentials();
    }

    public static String getToken() {
        return ShiroUtils.getLoginUser().getToken();
    }

    public static boolean isAuthenticated() {
        return SecurityUtils.getSubject().isAuthenticated();
    }

    /**
     * 是否有权限
     */
    public static boolean hasPerm(String perm) {
        return SecurityUtils.getSubject().isPermitted(perm);
    }
}
