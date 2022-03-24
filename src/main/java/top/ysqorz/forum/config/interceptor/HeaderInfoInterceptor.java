package top.ysqorz.forum.config.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import top.ysqorz.forum.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 给前台所有使用到header的模板注入登录用户的信息
 *
 * @author passerbyYSQ
 * @create 2022-02-19 22:14
 */
@Component
public class HeaderInfoInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private UserService userService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        if (!"GET".equalsIgnoreCase(request.getMethod())) {
            return;
        }
        modelAndView.addObject("loginUser", userService.getLoginUser());
    }
}
