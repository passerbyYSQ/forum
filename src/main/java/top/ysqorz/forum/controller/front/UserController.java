package top.ysqorz.forum.controller.front;


import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.JwtToken;
import top.ysqorz.forum.vo.ResultModel;
import top.ysqorz.forum.vo.StatusCode;

/**
 * 通过注解实现权限控制
 * 在方法前添加注解 @RequiredRoles 或 @RequiredPermissions
 *
 *
 * @author passerbyYSQ
 * @create 2020-08-20 23:54
 */
@Controller
@RequestMapping("/user")
@ResponseBody
public class UserController {

    @Autowired
    private UserService userService;

    @RequiresRoles({"ysq"})
    @RequestMapping("/info")
    public ResponseEntity<String> index() {
        return ResponseEntity.ok("用户信息！这个接口需要携带有效的token才能访问");
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResultModel register(User user) {
        // 参数判断省略
        // ...
        System.out.println(user);
        userService.register(user);
        return ResultModel.success();
    }

    /**
     * 用户登录，签发token
     */
    @PostMapping("/login")
    public ResultModel<String> login(String username, String password) {
        // 参数校验略
        // ...

        User user = userService.login(username, password);
        if (user == null) {
            return ResultModel.failed(StatusCode.LOGIN_FAILED); // 登录失败
        }

        // 生成jwt
        String jwt = userService.generateJwt(user);

        Subject subject = SecurityUtils.getSubject();
        // 由于jwt是正确的，所以这里的login必定成功。此举是为了将认证信息缓存起来
        subject.login(new JwtToken(jwt));

        // 为什么登录不使用UsernamePasswordToken和定义专门的LoginRealm（Service层的逻辑）来处理UsernamePasswordToken？
        // 由于密码登录只用一次，成功之后都凭借jwt令牌来访问
        // 经过LoginRealm后所缓存起来的认证信息之后多不会被用到。。个人想法

        return ResultModel.success(jwt);
    }

    /**
     * 退出登录
     * 销毁主体的认证信息
     */
    @RequestMapping("/logout")
    public ResultModel logout() {
        Subject subject = SecurityUtils.getSubject();
        // 为什么可以强制转成User？与在Realm中认证方法返回的SimpleAuthenticationInfo()的第一个参数有关
        //User user = (User) subject.getPrincipal();
        //userService.logout(user.getUsername());
        subject.logout();
        return ResultModel.success();
    }

}
