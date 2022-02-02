package top.ysqorz.forum.shiro;

import org.apache.shiro.authc.HostAuthenticationToken;

/**
 * 或者直接实现AuthenticationToken也可以
 *
 * @author passerbyYSQ
 * @create 2020-08-22 10:42
 */
public class JwtToken implements HostAuthenticationToken {

    // JWT字符串
    private String token;

    private String host;

    public JwtToken(String token) {
        this(token, null);
    }

    public JwtToken(String token, String host) {
        this.token = token;
        this.host = host;
    }

    @Override
    public String getHost() {
        return token;
    }

    /**
     * 返回身份信息（相当于username）
     * Jwt里面包含一个访问主体的身份（比如说username）
     * @return
     */
    @Override
    public Object getPrincipal() {
        return token;
    }

    /**
     * 返回凭证信息（相当于password）
     * Jwt本身就是一个令牌凭证，在服务端通过解密校验
     * @return
     */
    @Override
    public Object getCredentials() {
        return token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
