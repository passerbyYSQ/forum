package top.ysqorz.forum.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationInfo;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.utils.JwtUtils;
import top.ysqorz.forum.utils.SpringUtils;

/**
 * @author passerbyYSQ
 * @create 2021-06-18 21:17
 */
public class ShiroUtils {

    /**
     * 如果Subject认证成功，此处必定可以取地userId
     */
    public static Integer getUserId() {
        return Integer.valueOf(JwtUtils.getClaimByKey(getToken(), "userId"));
    }

    /**
     * 获取登录用户缓存在Redis中的认证信息
     */
    public static User getLoginUser() {
        JwtRealm jwtRealm = SpringUtils.getBean(JwtRealm.class);
        JwtToken jwtToken = new JwtToken(getToken());
        AuthenticationInfo authenticationInfo = jwtRealm.getAuthenticationInfo(jwtToken);
        return (User) authenticationInfo.getCredentials();
    }

    public static String getToken() {
        return (String) SecurityUtils.getSubject().getPrincipal();
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
