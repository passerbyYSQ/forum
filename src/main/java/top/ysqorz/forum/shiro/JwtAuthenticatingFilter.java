package top.ysqorz.forum.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.utils.CommonUtils;
import top.ysqorz.forum.utils.DateTimeUtils;
import top.ysqorz.forum.utils.JwtUtils;
import top.ysqorz.forum.utils.SpringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author passerbyYSQ
 * @create 2020-08-22 12:06
 */
@Slf4j
public class JwtAuthenticatingFilter extends BasicHttpAuthenticationFilter {

    // 是否刷新token
    private boolean shouldRefreshToken;

    public JwtAuthenticatingFilter() {
        this.shouldRefreshToken = false;
    }

    public JwtAuthenticatingFilter(Boolean shouldRefreshToken) {
        this.shouldRefreshToken = shouldRefreshToken;
    }

    /**
     * AccessControlFilter的onPreHandle：isAccessAllowed || onAccessDenied
     * 请求是否允许放行
     * 父类会在请求进入拦截器后调用该方法，返回true则继续，返回false则会调用onAccessDenied()。这里在不通过时，还调用了isPermissive()方法，我们后面解释。
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        boolean allowed = false;
        try {
            allowed = executeLogin(request, response);
        } catch (IllegalStateException ignored) { //not found any token
        } catch (Exception e) {
            log.error("Error occurs when login", e);
        }
        //return allowed;
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        if (CommonUtils.isApiRequest(httpRequest)) { // api
            CommonUtils.writeJson(httpResponse, ResultModel.failed(StatusCode.AUTHENTICATION_FAILED));
        } else { // html
            httpResponse.sendRedirect("/user/login?referer=" + httpRequest.getRequestURI()); // 重定向到登录页面
        }
        return false;
    }

    /**
     * 父类executeLogin()首先会createToken()，然后调用shiro的Subject.login()方法。
     * executeLogin()的逻辑是不是跟UserController里面的密码登录逻辑很像？
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        // 从请求头中的Authorization字段尝试获取jwt token
        String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
        // 从请求头中的token字段（自定义字段）尝试获取jwt token
        if (ObjectUtils.isEmpty(token)) {
            token = httpRequest.getHeader(Constant.token);
        }
        // 从请求参数中尝试获取jwt token。TODO 与验证码的token冲突，需要调整
        /*
        if (StringUtils.isEmpty(token)) {
            token = httpRequest.getParameter(Constant.token);
        }
        */
        // 从cookie中尝试获取token
        if (ObjectUtils.isEmpty(token)) {
            token = getTokenFromCookie(httpRequest);
        }
        return ObjectUtils.isEmpty(token) ? null: new JwtToken(token);
    }

    protected String getTokenFromCookie(HttpServletRequest httpRequest) {
        Cookie[] cookies = httpRequest.getCookies();
        if (ObjectUtils.isEmpty(cookies)) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (Constant.token.equalsIgnoreCase(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * 登录成功后判断是否需要刷新token
     * 登录成功说明：jwt有效，尚未过期。当离过期时间不足一天时，往响应头中放入新的token返回给前端
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) {
        HttpServletResponse httpResponse = WebUtils.toHttp(response);

        String oldToken = (String) token.getCredentials();
        LocalDateTime expireAt = DateTimeUtils.toLocalDateTime(JwtUtils.getExpireAt(oldToken));
        // 如果token过期前一天内登录，则签发新的token给用户
        if (shouldRefreshToken && DateTimeUtils.dif(LocalDateTime.now(), expireAt, ChronoUnit.DAYS) < 1) {
            LoginUser loginUser = ShiroUtils.getLoginUser();
            UserService userService = SpringUtils.getBean(UserService.class);
            userService.logout();
            userService.login(loginUser.getId(), loginUser.getLoginSalt(), httpResponse);
        }

        return true;
    }

    public boolean isShouldRefreshToken() {
        return shouldRefreshToken;
    }

    public void setShouldRefreshToken(boolean shouldRefreshToken) {
        this.shouldRefreshToken = shouldRefreshToken;
    }
}
