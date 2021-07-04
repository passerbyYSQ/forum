package top.ysqorz.forum.controller.front;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.CheckUserDTO;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;

import javax.annotation.Resource;

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
        User user = alterUser();
        model.addAttribute("user", user);
        return "front/user/set";
    }

    /**
     * 跳转到基本设置前的User数据处理
     */
    public User alterUser() {
        int myId = ShiroUtils.getUserId();
        User user = userService.getUserById(myId);
        if (user.getEmail().equals("")) {
            user.setEmail(null);
        } else {
            int len1 = user.getEmail().length();
            String email = encryption(user.getEmail(), len1);
            user.setEmail(email);
        }
        if (user.getPhone() != null) {
            int len2 = user.getPhone().length();
            String phone = encryption(user.getPhone(), len2);
            user.setPhone(phone);
        }
        if (user.getQqId() != null && user.getQqId().equals("")) {
            user.setQqId(null);
        }
        if (user.getGiteeId() != null && user.getGiteeId().equals("")) {
            user.setGiteeId(null);
        }
        if (user.getBaiduId() != null && user.getBaiduId().equals("")) {
            user.setBaiduId(null);
        }
        return user;
    }


    /**
     * 邮箱、手机加密显示
     */
    public String encryption(String str, int len) {
        StringBuffer str1 = new StringBuffer();
        if (len > 6) {
            str1.append(str.substring(0, 3));
            for (int i = 0; i < len - 6; i++) {
                str1.append("*");
            }
            str1.append(str.substring(len - 3, len));
            return str1.toString();
        }
        return str;
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
        //检查用户账号密码是否正确
        StatusCode check = userService.checkUser(checkUser);
        if (!check.equals(StatusCode.SUCCESS)) {
            return ResultModel.failed(check);
        }
        //确认是11位的正确手机号码
        if (checkUser.getNewPhone().length() != 11) {
            return ResultModel.failed(StatusCode.PHONE_INCORRECT);
        }
        //检查手机是否已被绑定
        if (userService.checkBind(checkUser.getNewPhone(), "phone")) {
            return ResultModel.failed(StatusCode.PHONE_IS_EXIST);
        }
        User user = userService.getUserByEmail(checkUser.getOldEmail());
        user.setPhone(checkUser.getNewPhone());
        userService.changeUser(user);
        return ResultModel.success();
    }

    /**
     * 邮箱绑定检验
     */
    @ResponseBody
    @PostMapping("changeUserEmail")
    public ResultModel changeUserEmail(CheckUserDTO checkUser) {
        int myId = ShiroUtils.getUserId();
        User user = userService.getUserById(myId);
        //判断用户是否已经有邮箱绑定，0代表没有
        if (userService.getUserById(myId).getEmail().equals("")) {
            //检查两次输入的密码是否一致
            if (!checkUser.getCheckPassword().equals(checkUser.getRePassword())) {
                return ResultModel.failed(StatusCode.PASSWORD_NOT_EQUAL);
            }
            //检查邮箱是否已被绑定
            if (userService.checkBind(checkUser.getOldEmail(), "email")) {
                return ResultModel.failed(StatusCode.EMAIL_IS_EXIST);
            }
            Md5Hash md5Hash = new Md5Hash(checkUser.getCheckPassword(), user.getLoginSalt(), 1024);
            user.setEmail(checkUser.getOldEmail())
                    .setPassword(md5Hash.toHex());

        } else {
            //检查用户账号密码是否正确
            StatusCode check = userService.checkUser(checkUser);
            if (!check.equals(StatusCode.SUCCESS)) {
                return ResultModel.failed(check);
            }
            //检查邮箱是否已被绑定
            if (userService.checkBind(checkUser.getNewEmail(), "email")) {
                return ResultModel.failed(StatusCode.EMAIL_IS_EXIST);
            }
            user.setEmail(checkUser.getNewEmail());
        }
        userService.changeUser(user);
        return ResultModel.success();
    }

    /**
     * QQ、Gitee、百度解绑检验
     */
    @ResponseBody
    @PostMapping("Unbundling")
    public ResultModel Unbundling(CheckUserDTO checkUser) {
        //检查用户账号密码是否正确
        StatusCode check = userService.checkUser(checkUser);
        if (!check.equals(StatusCode.SUCCESS)) {
            return ResultModel.failed(check);
        }
        User user = userService.getUserByEmail(checkUser.getOldEmail());
        if (checkUser.getPoFile().equals("qq")) {
            user.setQqId("");
        }
        if (checkUser.getPoFile().equals("gitee")) {
            user.setGiteeId("");
        }
        if (checkUser.getPoFile().equals("baidu")) {
            user.setBaiduId("");
        }
        userService.changeUser(user);
        User user1 = userService.getUserByEmail(checkUser.getOldEmail());
        return ResultModel.success();
    }

}
