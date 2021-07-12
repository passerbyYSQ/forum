package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.*;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.MessageService;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.upload.UploadRepository;
import top.ysqorz.forum.upload.uploader.ImageUploader;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.io.IOException;

/**
 *
 * 用户个人信息修改
 *
 * @author ligouzi
 * @create 2021-06-30 13:12
 */
@Controller
@RequestMapping("/user/center")
@Validated
public class UserCenterController {

    @Resource
    private UserService userService;
    // 为了方便不同组员开发，使用阿里云OSS
    @Resource
    private UploadRepository aliyunOssRepository;

    @Resource
    private MessageService messageService;

    /**
     * 跳转到我的主页
     */
    @GetMapping("/home")
    public String personalHomePage() {

        return "forward:/user/home/" + ShiroUtils.getUserId();
    }

    /**
     * 跳转到用户中心
     */
    @GetMapping("/index")
    public String indexPage() {
        return "front/user/index";
    }

    /**
     * 跳转到第三方账号绑定页面，此页面必须登录才能进
     */
    @GetMapping("/set")
    public String bindOauthAccountPage(Model model) {
        User user = userService.getUserById(ShiroUtils.getUserId());
        user.setEmail(encryption(user.getEmail()));
        user.setPhone(encryption(user.getPhone()));
        model.addAttribute("user", user);
        return "front/user/set";
    }

    @PostMapping("/updatePassword")
    @ResponseBody
    public ResultModel updatePassword(@Validated(RegisterDTO.UpdatePassword.class)
                                                  RegisterDTO dto) {
        if (!dto.getNewPassword().equals(dto.getRePassword())) {
            return ResultModel.failed(StatusCode.PASSWORD_NOT_EQUAL); // 两次密码不一致
        }
        User me = userService.getUserById(ShiroUtils.getUserId());

        String salt = me.getLoginSalt();
        String encryptPwd = userService.encryptLoginPwd(dto.getPassword(), salt);
        if (!me.getEmail().equals(dto.getEmail()) ||
                !me.getPassword().equals(encryptPwd)) {
            return ResultModel.failed(StatusCode.ACCOUNT_OR_PASSWORD_INCORRECT); // 邮箱或密码错误
        }

        String newEncryptPwd = userService.encryptLoginPwd(dto.getNewPassword(), salt);
        User record = new User();
        record.setId(me.getId())
                .setPassword(newEncryptPwd);
        userService.updateUserById(record);
        return ResultModel.success();
    }

    @PostMapping("/updateInfo")
    @ResponseBody
    public ResultModel updatePersonalInfo(User user) {
        user.setId(ShiroUtils.getUserId());
        int cnt = userService.updateUserById(user);
        return cnt == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode.INFO_UPDATE_FAILED);
    }

    @PostMapping("/uploadFaceImage")
    @ResponseBody
    public ResultModel uploadFaceImage(@NotBlank String imageBase64) throws IOException {
        ImageUploader imageUploader = new ImageUploader(imageBase64, aliyunOssRepository);
        UploadResult uploadResult = imageUploader.uploadBase64();
        User record = new User();
        record.setId(ShiroUtils.getUserId())
                .setPhoto(uploadResult.getUrl()[0]);
        userService.updateUserById(record);
        return ResultModel.success(uploadResult);
    }

    /**
     * 邮箱、手机加密显示
     */
    private String encryption(String str) {
        int len;
        if (ObjectUtils.isEmpty(str) || (len = str.length()) < 6) {
            return str;
        }
        StringBuilder sbd = new StringBuilder(str);
        StringBuilder mid = new StringBuilder();
        for (int i = 0; i < len - 6; i++) {
            mid.append("*");
        }
        return sbd.replace(3, len - 3, mid.toString()).toString();
    }

    /**
     * 跳转到我的消息
     */
    @GetMapping("/message")
    public String messagePage() {
        return "front/user/message";
    }


    /**
     * 获取消息列表
     *
     * @param limit
     * @param page
     * @param conditions
     */
    @ResponseBody
    @GetMapping("/message/list")
    public ResultModel<PageData<MessageListDTO>> megList(@RequestParam(defaultValue = "3") Integer limit,
                                                         @RequestParam(defaultValue = "1") Integer page,
                                                         Integer conditions) {
        if (limit <= 0) {
            limit = 3;
        }

        PageData<MessageListDTO> megList = messageService.getMegList(page, limit, conditions);
        return ResultModel.success(megList);
    }

    /**
     * 清空所有消息
     */
    @ResponseBody
    @GetMapping("/message/clearAll")
    public ResultModel clearAllMeg() {

        int i = messageService.clearAllMeg();
        return i >= 0 ? ResultModel.success() : ResultModel.failed(StatusCode.UNKNOWN_ERROR);
    }


    /**
     * 手机绑定检验
     */
    @ResponseBody
    @PostMapping("changeUserPhone")
    public ResultModel changeUserPhone(@Validated CheckUserDTO checkUser) {
        //检查用户账号密码是否正确
        StatusCode check = userService.checkUser(checkUser);
        if (!check.equals(StatusCode.SUCCESS)) {
            return ResultModel.failed(check);
        }
        //手机格式不正确
        if (!checkUser.getNewPhone().matches(Constant.REGEX_PHONE)) {
            return ResultModel.failed(StatusCode.PHONE_INCORRECT);
        }
        //检查手机是否已被绑定
        if (userService.checkBind(checkUser.getNewPhone(), "phone")) {
            return ResultModel.failed(StatusCode.PHONE_IS_EXIST);
        }
        User record = new User();
        record.setId(ShiroUtils.getUserId())
            .setPhone(checkUser.getNewPhone());
        userService.updateUserById(record);
        return ResultModel.success();
    }

    /**
     * 邮箱绑定检验
     */
    @ResponseBody
    @PostMapping("changeUserEmail")
    public ResultModel changeUserEmail(@Validated CheckUserDTO checkUser) {
        int myId = ShiroUtils.getUserId();
        User me = userService.getUserById(myId);
        User record = new User();
        record.setId(myId); // ！！
        //判断用户是否已经有邮箱绑定
        if (ObjectUtils.isEmpty(me.getEmail())) { // 设置邮箱和密码
            //检查两次输入的密码是否一致
            if (!checkUser.getCheckPassword().equals(checkUser.getRePassword())) {
                return ResultModel.failed(StatusCode.PASSWORD_NOT_EQUAL);
            }
            //检查邮箱是否已被绑定
            if (userService.checkBind(checkUser.getOldEmail(), "email")) {
                return ResultModel.failed(StatusCode.EMAIL_IS_EXIST);
            }
            record.setEmail(checkUser.getOldEmail())
                    .setPassword(userService.encryptLoginPwd(
                            checkUser.getCheckPassword(),  me.getLoginSalt()));

        } else { // 修改邮箱
            //检查用户账号密码是否正确
            StatusCode code = userService.checkUser(checkUser);
            if (!code.equals(StatusCode.SUCCESS)) {
                return ResultModel.failed(code);
            }
            //检查邮箱是否已被绑定
            if (userService.checkBind(checkUser.getNewEmail(), "email")) {
                return ResultModel.failed(StatusCode.EMAIL_IS_EXIST);
            }
            record.setEmail(checkUser.getNewEmail());
        }
        userService.updateUserById(record);
        return ResultModel.success();
    }

    /**
     * QQ、Gitee、百度解绑检验
     */
    @ResponseBody
    @PostMapping("Unbundling")
    public ResultModel unbind(@Validated CheckUserDTO checkUser) {
        //检查用户账号密码是否正确
        StatusCode check = userService.checkUser(checkUser);
        if (!check.equals(StatusCode.SUCCESS)) {
            return ResultModel.failed(check);
        }
        User record = new User();
        record.setId(ShiroUtils.getUserId());
        if (checkUser.getPoFile() != null) {
            switch (checkUser.getPoFile()) {
                case "qq":      record.setQqId("");     break;
                case "gitee":   record.setGiteeId("");  break;
                case "baidu":   record.setBaiduId("");  break;
            }
            userService.updateUserById(record);
        }
        return ResultModel.success();
    }

}
