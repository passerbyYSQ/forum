package top.ysqorz.forum.config;


import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.mgt.SessionStorageEvaluator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSessionStorageEvaluator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.ysqorz.forum.shiro.JwtAuthenticatingFilter;
import top.ysqorz.forum.shiro.JwtCredentialsMatcher;
import top.ysqorz.forum.shiro.ShiroCacheManager;
import top.ysqorz.forum.shiro.JwtRealm;

import javax.servlet.Filter;
import java.util.Collections;
import java.util.Map;

/**
 * 整合Shiro框架的配置类
 *
 * @author passerbyYSQ
 * @create 2020-08-20 23:10
 */
@Configuration
public class ShiroConfig {

    /**
     * 创建Web环境的安全管理器
     */
//    @Bean
//    public DefaultWebSecurityManager getDefaultWebSecurityManager(Realm realm) {
//        DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
//        // 给安全管理器设置Realm
//        defaultWebSecurityManager.setRealm(realm);
//        return defaultWebSecurityManager;
//    }
    /*
    引入shiro的starter会自动开启注解，详情见：ShiroAnnotationProcessorAutoConfiguration
     */

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(
            DefaultWebSecurityManager securityManager, ShiroFilterChainDefinition chainDefinition) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();

        // 必须的设置。我们自定义的Realm此时已经被设置到securityManager中了
        factoryBean.setSecurityManager(securityManager);

        // 注册我们写的过滤器
        Map<String, Filter> filters = factoryBean.getFilters();
        filters.put("jwtAuth", new JwtAuthenticatingFilter(true));

        factoryBean.setFilters(filters);

        // 设置请求的过滤规则。其中过滤规则中用到了我们注册的过滤器：jwtAuth
        factoryBean.setFilterChainDefinitionMap(chainDefinition.getFilterChainMap());

        return factoryBean;
    }

    @Bean
    public DefaultWebSecurityManager shiroSecurityManager(@Qualifier("jwtRealm") Realm jwtRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 所有的Realm都用这个全局缓存。不生效，需要在realm中设置缓存。原因暂时搞不懂。
//        securityManager.setCacheManager(new EhCacheManager());
        securityManager.setRealms(Collections.singletonList(jwtRealm));
        return securityManager;
    }

    /**
     * 设置请求的过滤规则
     * @return
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition chainDefinition = new DefaultShiroFilterChainDefinition();

        // 除了浏览帖子特殊（不登录也行，只是数据不一样），发帖、修改帖子等其他操作都需要登录
        //chainDefinition.addPathDefinition("/post/**", "noSessionCreation,jwtAuth");

        chainDefinition.addPathDefinition("/user/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/test/**", "noSessionCreation,anon");  //login不做认证，noSessionCreation的作用是用户在操作session时会抛异常
        chainDefinition.addPathDefinition("/index", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/", "noSessionCreation,anon");

        // 放行静态资源。但是admin也把后台给放行了。之后再做修正
        chainDefinition.addPathDefinition("/front/**", "noSessionCreation,anon");
        chainDefinition.addPathDefinition("/admin/**", "noSessionCreation,anon");


//        // 注意第2个参数的"jwtAuth"需要与上面的 filters.put("jwtAuth", new JwtAuthenticatingFilter()); 一致
//        // 做用户认证，permissive参数的作用是当token无效时也允许请求访问，不会返回鉴权未通过的错误
//        chainDefinition.addPathDefinition("/user/logout", "noSessionCreation,jwtAuth[permissive]");
//        chainDefinition.addPathDefinition("/**", "noSessionCreation,jwtAuth"); // 默认进行用户鉴权
        return chainDefinition;
    }

    /**
     * 初始化Authenticator，将我们需要的Realm设置进去
     * Shiro会将Authenticator设置到SecurityManager里面
     */
    /*
    @Bean
    public Authenticator authenticator(@Qualifier("jwtRealm") Realm jwtRealm) {
        ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
        // ！！！只对认证器有效，对授权器无效。所以直接通过SecurityManager来设置
        authenticator.setRealms(Collections.singletonList(jwtRealm));
        //设置多个realm认证策略，一个成功即跳过其它的
        authenticator.setAuthenticationStrategy(new FirstSuccessfulStrategy());
        return authenticator;
    }
    */

    /**
     * 返回我们自定义的Realm
     */
    /*
    @Bean("loginRealm") // 自动配置类中有同名组件，如果只写@Bean，会出现歧义
    public Realm loginRealm(CacheManager cacheManager) {
        LoginRealm loginRealm = new LoginRealm();

        // 匹配器。需要与密码加密规则一致
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        // 设置匹配器的加密算法
        hashedCredentialsMatcher.setHashAlgorithmName(Md5Hash.ALGORITHM_NAME);
        // 设置匹配器的哈希散列次数
        hashedCredentialsMatcher.setHashIterations(1024);
        // 将对应的匹配器设置到Realm中
        loginRealm.setCredentialsMatcher(hashedCredentialsMatcher);

        // AuthenticatingRealm里面的isAuthenticationCachingEnabled()
        loginRealm.setCacheManager(cacheManager);
        loginRealm.setCachingEnabled(true); // 这句话不能少！！！
        loginRealm.setAuthenticationCachingEnabled(true); // 认证缓存
        loginRealm.setAuthorizationCachingEnabled(true); // 授权缓存
        // 可以设置认证和授权的名称
        // ...

        return loginRealm;
    }
    */

    @Bean("jwtRealm")
    public Realm jwtRealm(CacheManager cacheManager) {
        JwtRealm jwtRealm = new JwtRealm();
        // 使用我们自定义的CredentialsMatcher
        jwtRealm.setCredentialsMatcher(new JwtCredentialsMatcher());
        jwtRealm.setCacheManager(cacheManager);
        jwtRealm.setCachingEnabled(true);  // 这句话不能少！！！
        jwtRealm.setAuthenticationCachingEnabled(true); // 认证缓存
        jwtRealm.setAuthorizationCachingEnabled(true); // 授权缓存

        return jwtRealm;
    }

    /**
     * 禁用session, 不保存用户登录状态。保证每次请求都重新认证。
     * 需要注意的是，如果用户代码里调用Subject.getSession()还是可以用session，
     * 如果要完全禁用，要配合上过滤规则的noSessionCreation的Filter来实现
     */
    @Bean
    protected SessionStorageEvaluator sessionStorageEvaluator(){
        DefaultWebSessionStorageEvaluator sessionStorageEvaluator = new DefaultWebSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        return sessionStorageEvaluator;
    }

    /**
     * shiro的全局缓存管理器
     * @return
     */

//    @Bean
//    public CacheManager ehCacheManager() {
//        return new EhCacheManager();
//    }

    @Bean
    public CacheManager shiroCacheManager() {
        return new ShiroCacheManager(); // org.springframework.cache.CacheManager springCacheManager
    }

}
