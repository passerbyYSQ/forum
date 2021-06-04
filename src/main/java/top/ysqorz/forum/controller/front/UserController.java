package top.ysqorz.forum.controller.front;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.dto.*;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.JwtToken;
import top.ysqorz.forum.utils.CaptchaUtils;
import top.ysqorz.forum.utils.RandomUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 通过注解实现权限控制
 * 在方法前添加注解 @RequiredRoles 或 @RequiredPermissions
 *
 * @author passerbyYSQ
 * @create 2020-08-20 23:54
 */
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;


//    @RequiresRoles({"ysq"})
    @RequestMapping("/info")
    @ResponseBody
    public ResultModel<String> index() {
        return ResultModel.success("用户信息！这个接口需要携带有效的token才能访问");
    }

    /**
     * 用户注册
     */
    @PostMapping("/reg")
    @ResponseBody
    public ResultModel register(@Validated RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getRePassword())) {
            return ResultModel.failed(StatusCode.PASSWORD_NOT_EQUAL); // 两次输入密码不一致
        }
        String correctCaptcha = redisService.getCaptcha(dto.getToken());
        if (ObjectUtils.isEmpty(correctCaptcha)) {
            return ResultModel.failed(StatusCode.CAPTCHA_EXPIRED); // 验证码过期
        }
        if (!dto.getCaptcha().equalsIgnoreCase(correctCaptcha)) {
            return ResultModel.failed(StatusCode.CAPTCHA_INVALID); // 验证码错误
        }

        User user = userService.getUserByEmail(dto.getEmail());
        if (!ObjectUtils.isEmpty(user)) {
            return ResultModel.failed(StatusCode.EMAIL_ISEXIST); // 该邮箱已注册
        }

        userService.register(dto);

        return ResultModel.success();
    }

    /**
     * 跳转到注册页面
     */
    @GetMapping("/reg")
    public String registerPage(Model model) {
        // 用于验证码缓存和校验。植入到注册的登录页面的隐藏表单元素中
        String token = RandomUtils.generateUUID();
        model.addAttribute("token", token);
        return "front/user/reg";
    }

    /**
     * 跳转到登录页面
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        // 用于验证码缓存和校验。植入到页面的登录页面的隐藏表单元素中
        String token = RandomUtils.generateUUID();
        model.addAttribute("token", token);
        return "front/user/login";
    }

    /**
     * 登录页面的验证码图片(注册页面也用这个接口)
     */
    @GetMapping("/captcha")
    public void captchaImage(@RequestParam String token, HttpServletResponse response)
            throws ServletException, IOException {
        String captcha = CaptchaUtils.generateAndOutput(response);
        redisService.saveCaptcha(token, captcha);
    }

    /**
     * 用户登录的API
     */
    @PostMapping("/login")
    @ResponseBody
    public ResultModel<UserLoginInfo> login(@Validated LoginDTO dto, HttpServletResponse response) {
        String correctCaptcha = redisService.getCaptcha(dto.getToken());
        if (ObjectUtils.isEmpty(correctCaptcha)) {
            return ResultModel.failed(StatusCode.CAPTCHA_EXPIRED); // 验证码过期
        }
        if (!dto.getCaptcha().equalsIgnoreCase(correctCaptcha)) {
            return ResultModel.failed(StatusCode.CAPTCHA_INVALID); // 验证码过期
        }

        User user = userService.getUserByEmail(dto.getEmail());
        if (ObjectUtils.isEmpty(user)) {
            return ResultModel.failed(StatusCode.EMAIL_INCORRECT); // 邮箱错误
        }

        // 以注册时的相同规则，加密用户输入的密码
        Md5Hash md5Hash = new Md5Hash(dto.getPassword(), user.getLoginSalt(), 1024);
        if (!user.getPasssword().equals(md5Hash.toHex())) {
            return ResultModel.failed(StatusCode.PASSWORD_INCORRECT); // 密码错误
        }
        Subject subject = SecurityUtils.getSubject();
        // 旧的盐未被清空说明，已经登录尚未退出
        if (!ObjectUtils.isEmpty(user.getJwtSalt())) {
            // 根据旧盐再一次生成旧的token
            JwtToken oldToken = userService.generateJwtToken(user.getId(), user.getJwtSalt());
            subject.login(oldToken);
            userService.logout(); // 清除旧token的缓存
        }

        UserLoginInfo loginInfo = userService.login(user);

        // 为什么登录不使用UsernamePasswordToken和定义专门的LoginRealm（Service层的逻辑）来处理UsernamePasswordToken？
        // 由于密码登录只用一次，成功之后都凭借jwt令牌来访问
        // 经过LoginRealm后所缓存起来的认证信息之后多不会被用到。。个人想法

        // 将token放到请求头中，方便前端判断有无token刷新
        // 将常显的数据返回给前端缓存
        return ResultModel.success(loginInfo);
    }

    /**
     * 销毁主体的认证信息
     */
    @RequestMapping("/logout")
    @ResponseBody
    public ResultModel logout() {
        userService.logout();
        return ResultModel.success();
    }

}
