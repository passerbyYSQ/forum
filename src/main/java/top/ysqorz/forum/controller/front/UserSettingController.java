package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.CheckUserDTO;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * 用户信息管理的API
 *
 * @author ligouzi
 * @create 2021-06-30 13:12
 */
@Controller
@RequestMapping("/user/setting")
public class UserSettingController {

    @Resource
    private UserService userService;

    /**
     * 跳转到我的主页
     */
    @GetMapping("/home")
    public String homePage() {
        return "front/user/home";
    }

    /**
     * 跳转到用户中心
     */
    @GetMapping("/index")
    public String indexPage() {
        return "front/user/index";
    }

    /**
     * 跳转到基本设置
     */
    @GetMapping("/set")
    public String setPage(Model model) {
        User user = userService.getInfoById(ShiroUtils.getUserId());
        model.addAttribute("user", user);
        return "front/user/set";
    }

    /**
     * 跳转到我的消息
     */
    @GetMapping("/message")
    public String messagePage() {
        return "front/user/message";
    }

    /**
     * 手机绑定检验
     */
    @ResponseBody
    @PostMapping("changeUserPhone")
    public ResultModel changeUserPhone(CheckUserDTO checkUser) {
        int result = userService.ChangeUser(checkUser, 1);
        return result == StatusCode.SUCCESS.getCode() ?
                ResultModel.success() : ResultModel.failed(result ,StatusCode.getMsgByCode(result));
    }

    /**
     * 邮箱绑定检验
     */
    @ResponseBody
    @PostMapping("changeUserEmail")
    public ResultModel changeUserEmail(CheckUserDTO checkUser) {
        int result = userService.ChangeUser(checkUser, 2);
        return result == StatusCode.SUCCESS.getCode() ?
                ResultModel.success() : ResultModel.failed(result ,StatusCode.getMsgByCode(result));
    }

    /**
     * QQ解绑检验
     */
    @ResponseBody
    @PostMapping("UnbundlingQQ")
    public ResultModel UnbundlingQQ(CheckUserDTO checkUser) {
        int result = userService.ChangeUser(checkUser, 3);
        return result == StatusCode.SUCCESS.getCode() ?
                ResultModel.success() : ResultModel.failed(result ,StatusCode.getMsgByCode(result));
    }

    /**
     * Gitee解绑检验
     */
    @ResponseBody
    @PostMapping("UnbundlingGitee")
    public ResultModel UnbundlingGitee(CheckUserDTO checkUser) {
        int result = userService.ChangeUser(checkUser, 4);
        return result == StatusCode.SUCCESS.getCode() ?
                ResultModel.success() : ResultModel.failed(result ,StatusCode.getMsgByCode(result));
    }

    /**
     * 百度解绑检验
     */
    @ResponseBody
    @PostMapping("UnbundlingBaidu")
    public ResultModel UnbundlingBaidu(CheckUserDTO checkUser) {
        int result = userService.ChangeUser(checkUser, 5);
        return result == StatusCode.SUCCESS.getCode() ?
                ResultModel.success() : ResultModel.failed(result ,StatusCode.getMsgByCode(result));
    }

}
