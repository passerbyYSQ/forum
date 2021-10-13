package top.ysqorz.forum.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.utils.DateTimeUtils;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.JwtUtils;
import top.ysqorz.forum.utils.SpringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
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
        } catch (IllegalStateException e) { //not found any token
            log.info("Not found any token");
        } catch (Exception e) {
            log.info("Error occurs when login");
        }
        //return allowed;
        return allowed || super.isPermissive(mappedValue);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        if (httpRequest.getHeader("Accept") == null ||
                !httpRequest.getHeader("Accept").contains("text/html")) { // api
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.setContentType("application/json;charset=UTF-8");
            //httpResponse.setStatus(HttpStatus.FORBIDDEN.value()); // ajax 提示 "请求失败"，无法提示业务失败的提示信息
            PrintWriter writer = response.getWriter();
            String json = JsonUtils.objectToJson(ResultModel.failed(StatusCode.AUTHENTICATION_FAILED));
            writer.print(json);
        } else { // html
            httpResponse.sendRedirect("/user/login"); // 重定向到登录页面
        }
        return false;
    }

    /**
     * 父类executeLogin()首先会createToken()，然后调用shiro的Subject.login()方法。
     * executeLogin()的逻辑是不是跟UserController里面的密码登录逻辑很像？
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 从请求头中的Authorization字段尝试获取jwt token
        String token = httpRequest.getHeader("Authorization");
        // 从请求头中的token字段（自定义字段）尝试获取jwt token
        if (StringUtils.isEmpty(token)) {
            token = httpRequest.getHeader("token");
        }
        // 从请求参数中尝试获取jwt token
//        if (StringUtils.isEmpty(token)) {
//            token = httpRequest.getParameter("token");
//        }
        // 从cookie中尝试获取token
        if (StringUtils.isEmpty(token)) {
            Cookie[] cookies = httpRequest.getCookies();
            for (Cookie cookie : cookies) {
                if ("token".equalsIgnoreCase(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }
        return !StringUtils.isEmpty(token) ? new JwtToken(token) : null;
    }

    /**
     * 登录成功后判断是否需要刷新token
     * 登录成功说明：jwt有效，尚未过期。当离过期时间不足一天时，往响应头中放入新的token返回给前端
     */
    @Override
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject,
                                     ServletRequest request, ServletResponse response) {

        String oldToken = (String) token.getCredentials();
        LocalDateTime expireAt = DateTimeUtils.toLocalDateTime(JwtUtils.getExpireAt(oldToken));

        if (shouldRefreshToken &&
                DateTimeUtils.dif(LocalDateTime.now(), expireAt, ChronoUnit.DAYS) < 1) {
            // 刷新token
            HttpServletResponse httpResponse = WebUtils.toHttp(response);
            UserService userService = (UserService) SpringUtils.getBean("userServiceImpl");
            userService.logout();
            String jwt = userService.login((Integer) token.getPrincipal(), httpResponse);
            httpResponse.setHeader("token", jwt);
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
