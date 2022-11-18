package top.ysqorz.forum.controller.front;


import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.exception.ParameterInvalidException;
import top.ysqorz.forum.common.exception.ServiceFailedException;
import top.ysqorz.forum.dto.req.LoginDTO;
import top.ysqorz.forum.dto.req.RegisterDTO;
import top.ysqorz.forum.dto.resp.SimpleUserDTO;
import top.ysqorz.forum.oauth.OauthProvider;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.RSAService;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.CommonUtils;
import top.ysqorz.forum.utils.RandomUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.util.Map;

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
    @Resource
    private UserService userService;
    @Resource
    private RedisService redisService;
    @Resource
    private Map<String, OauthProvider<?>> oauthProviderMap;
    @Resource
    private RSAService rsaService;

    @RequiresRoles({"ysq"})
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
    public StatusCode register(@Validated(RegisterDTO.Register.class) RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getRePassword())) {
            return StatusCode.PASSWORD_NOT_EQUAL; // 两次输入密码不一致
        }
        String correctCaptcha = redisService.getCaptcha(dto.getToken());
        if (ObjectUtils.isEmpty(correctCaptcha)) {
            return StatusCode.CAPTCHA_EXPIRED; // 验证码过期
        }
        if (!dto.getCaptcha().equalsIgnoreCase(correctCaptcha)) {
            return StatusCode.CAPTCHA_INVALID; // 验证码错误
        }
        User user = userService.getUserByEmail(dto.getEmail());
        if (!ObjectUtils.isEmpty(user)) {
            return StatusCode.EMAIL_IS_EXIST; // 该邮箱已注册
        }
        String decryptedPwd = rsaService.decryptByPrivateKey(dto.getPassword());
        if (ObjectUtils.isEmpty(decryptedPwd)) {
            return StatusCode.DECRYPT_FAILED; // 解密失败
        }
        dto.setPassword(decryptedPwd);
        dto.setRePassword(decryptedPwd);
        userService.register(dto);
        return StatusCode.SUCCESS;
    }

    /**
     * 跳转到注册页面
     */
    @GetMapping("/reg")
    public String registerPage(Model model) {
        // 用于验证码缓存和校验。植入到注册的登录页面的隐藏表单元素中
        model.addAttribute("token", RandomUtils.generateUUID());
        // 植入加密密码的公钥
        model.addAttribute("publicKey", rsaService.getPublicKey());
        return "front/user/reg";
    }

    /**
     * 跳转到登录页面
     */
    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        if (ObjectUtils.isEmpty(request.getParameter("referer")) &&
                !ObjectUtils.isEmpty(request.getHeader("referer"))) {
            String referer = request.getHeader("referer");
            if (referer.endsWith("/reg")) {
                referer = "/";
            }
            return "redirect:/user/login?referer=" + referer;
        } else {
            // 用于验证码缓存和校验。植入到页面的登录页面的隐藏表单元素中
            model.addAttribute("token", RandomUtils.generateUUID());
            // 植入加密密码的公钥
            model.addAttribute("publicKey", rsaService.getPublicKey());
            return "front/user/login";
        }
    }

    /**
     * 用户登录的API
     */
    @PostMapping("/login")
    @ResponseBody
    public String login(@Validated LoginDTO dto, @RequestHeader(defaultValue = "") String referer,
                                            HttpServletResponse response) {
        String correctCaptcha = redisService.getCaptcha(dto.getToken());
        if (ObjectUtils.isEmpty(correctCaptcha)) {
            throw new ServiceFailedException(StatusCode.CAPTCHA_EXPIRED); // 验证码过期
        }
        if (!dto.getCaptcha().equalsIgnoreCase(correctCaptcha)) {
            throw new ServiceFailedException(StatusCode.CAPTCHA_INVALID); // 验证码无效
        }

        User user = userService.getUserByEmail(dto.getEmail());
        if (ObjectUtils.isEmpty(user)) {
            throw new ServiceFailedException(StatusCode.EMAIL_INCORRECT);
        }

        String decryptedPwd = rsaService.decryptByPrivateKey(dto.getPassword());
        if (ObjectUtils.isEmpty(decryptedPwd)) {
            throw new ServiceFailedException(StatusCode.DECRYPT_FAILED); // 解密失败
        }
        dto.setPassword(decryptedPwd);

        // 以注册时的相同规则，加密用户输入的密码
        String encryptPwd = userService.encryptLoginPwd(dto.getPassword(), user.getLoginSalt());
        if (!user.getPassword().equals(encryptPwd)) {
            throw new ServiceFailedException(StatusCode.PASSWORD_INCORRECT); // 密码错误
        }

        String token = userService.login(user.getId(), user.getLoginSalt(), response);

        String originReferer = CommonUtils.getUrlParam(referer, "referer");
        if (ObjectUtils.isEmpty(originReferer)) {
            originReferer = "/";
        }

        // 为什么登录不使用UsernamePasswordToken和定义专门的LoginRealm（Service层的逻辑）来处理UsernamePasswordToken？
        // 由于密码登录只用一次，成功之后都凭借jwt令牌来访问
        // 经过LoginRealm后所缓存起来的认证信息之后多不会被用到。。个人想法

        // 将token放到请求头中，方便前端判断有无token刷新
        // 将常显的数据返回给前端缓存
        return originReferer;
    }

    /**
     * 销毁主体的认证信息
     */
    @RequestMapping("/logout")
    public String logout(@RequestHeader(defaultValue = "/") String referer, HttpServletResponse response) {
        userService.logout();
        Cookie cookie = new Cookie("token", "");
        cookie.setMaxAge(0);
        cookie.setPath("/"); // ！！！
        response.addCookie(cookie);
        return "redirect:" + referer;
    }

    @GetMapping("/oauth/{provider}/authorize")
    public String oauthAuthorize(@PathVariable @NotBlank String provider,
                                 @RequestHeader(defaultValue = "") String referer) {
        OauthProvider<?> oauthProvider = oauthProviderMap.get(provider);
        if (ObjectUtils.isEmpty(oauthProvider)) {
            throw new ServiceFailedException(StatusCode.UNSUPPORTED_OAUTH_AUTHORIZATION);
        }
        return oauthProvider.redirectAuthorize(referer);
    }

    /**
     * 第三方授权回调地址
     */
    @GetMapping("/oauth/{provider}/callback")
    public String giteeCallback(@PathVariable @NotBlank String provider, @RequestParam(defaultValue = "") String state,
                                String code, HttpServletResponse response) {
        OauthProvider<?> oauthProvider = oauthProviderMap.get(provider);
        if (ObjectUtils.isEmpty(oauthProvider)) {
            throw new ServiceFailedException(StatusCode.UNSUPPORTED_OAUTH_AUTHORIZATION);
        }
        return oauthProvider.oauthCallback(state, code, response);
    }

    /**
     * 个人主页界面
     * 获取用户登录页面状态
     * 获取个人信息
     * 获取主页信息
     */
    @GetMapping("/home/{visitId}")
    public String personalHomePage(@PathVariable Integer visitId, Model model) {
        User user = userService.getUserById(visitId);
        //确认用户是否存在
        if (ObjectUtils.isEmpty(user)) {
            throw new ParameterInvalidException(StatusCode.USER_NOT_EXIST);
        }
        SimpleUserDTO information = userService.getHomeInformationById(visitId);
        model.addAttribute("information", information);
        //判断进入用户界面状态，1：未登录， 2：已登录，身份为本人， 3：已登录，身份为访客
        boolean isLogin = ShiroUtils.isAuthenticated();
        boolean isMyself = isLogin && ShiroUtils.getUserId().equals(visitId);
        boolean isFocus = userService.isFocusOn(visitId);
        model.addAttribute("isLogin", isLogin);
        model.addAttribute("isMyself", isMyself);
        model.addAttribute("isFocusOn", isFocus);
        return "front/user/home";
    }

    /**
     * 关注和取关方法
     */
    @PostMapping("/home/{visitId}/changeFocus")
    @ResponseBody
    public StatusCode changeFollowOther(boolean isFocusOn, @PathVariable Integer visitId) {
        if (userService.isFocusOn(visitId) == isFocusOn) {
            return StatusCode.DO_NOT_REPEAT_OPERATE;
        }
        if (isFocusOn) {
            userService.addFollow(visitId);
        } else {
            userService.deleteFollow(visitId);
        }
        return StatusCode.SUCCESS;
    }
}
