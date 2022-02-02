package top.ysqorz.forum.shiro;

import org.apache.shiro.SecurityUtils;
import top.ysqorz.forum.utils.JwtUtils;

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
