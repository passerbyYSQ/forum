package top.ysqorz.forum.shiro;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.ObjectUtils;
import top.ysqorz.forum.common.TreeBuilder;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.AuthorityService;
import top.ysqorz.forum.service.RoleService;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.utils.JwtUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author passerbyYSQ
 * @create 2020-08-23 18:24
 */
public class JwtRealm extends AuthorizingRealm {

    @Resource
    private UserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private AuthorityService authorityService;

    /**
     * 当前Realm支持处理哪些Token
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Integer userId = (Integer) principals.getPrimaryPrincipal();
        List<top.ysqorz.forum.po.Resource> resourceList =
                authorityService.getAuthorityList(null);
        List<Role> roles = roleService.getRoleByUserId(userId);
        // 将所有权限形成一棵树
        TreeBuilder<Integer> builder = new TreeBuilder<>(resourceList, 0);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        for (Role role : roles) {
            authorizationInfo.addRole(role.getRoleName());
            // 当前角色的所有权限
            List<String> leafPermList = roleService.getRoleAllPerms(role.getId())
                    // 只筛选出叶子节点权限
                    .stream().filter(resource -> builder.isLeaf(resource.getId()))
                    .map(top.ysqorz.forum.po.Resource::getPermission)
                    .collect(Collectors.toList());
            authorizationInfo.addStringPermissions(leafPermList);
        }

        builder.destroy();
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
//        JwtToken jwtToken = (JwtToken) token;
        // 取决于JwtToken的getPrincipal()
        String tokenStr = (String) token.getCredentials();
        Integer userId = Integer.valueOf(JwtUtils.getClaimByKey(tokenStr, "userId"));

        // 根据token中的username去数据库查询用户信息，并封装成SimpleAuthenticationInfo（认证信息）给Matcher去校验
        User user = userService.getUserById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return null;
        }
        /*
        注意第一个参数必须是与token.getPrincipal()，否则在remove缓存时的key，会与put的时候的key不一样
        从而导致退出的时候无法remove掉缓存。

        principle是身份信息，简单的可以放username，网上很多人将User对象作为身份信息。
        不管是用username还是user对象作为principle，必须保证这里返回的SimpleAuthenticationInfo()的第一个参数
        和token.getPrincipal()一致，否则就会出现上面说的问题。详情看源码：AuthenticatingRealm的【两个】
        getAuthenticationCacheKey()方法

        但是将user作为principle（存入redis时会作为key），会遇到redis key的序列化和反序列化的问题，而且
        一个对象作为redis key不合适。一般redis key我们都尽量使用string，value对象可以使用jdk序列化或者
        json序列化。但我个人不建议使用json，用json无非是为了可视化，意义不大，但是由于java是强类型语言，
        反序列化的时候可能就会出问题
         */
        LoginUser loginUser = new LoginUser(user, tokenStr);
        return new SimpleAuthenticationInfo(token.getPrincipal(), loginUser, this.getName()); // loginUser必须实现序列化接口
    }
}
