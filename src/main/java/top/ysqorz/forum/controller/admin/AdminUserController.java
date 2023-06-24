package top.ysqorz.forum.controller.admin;

import com.github.pagehelper.PageHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.req.QueryUserCondition;
import top.ysqorz.forum.dto.resp.BlackInfoDTO;
import top.ysqorz.forum.dto.resp.UserDTO;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.Permission;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author 阿灿
 * @create 2021-05-10 16:08
 */
@Controller
@ResponseBody
@RequestMapping("/admin/system/user")
@Validated
public class AdminUserController {
    @Autowired
    private UserService userService;

    /**
     * 获取User数据
     */
    @RequiresPermissions(Permission.USER_VIEW)
    @GetMapping("/table")
    public PageData<UserDTO> getUserAndRole(@RequestParam(defaultValue = "10") Integer limit,
                                            @RequestParam(defaultValue = "1") Integer page,
                                            QueryUserCondition conditions) {
        PageHelper.startPage(page, Math.max(1, limit));
        //  PageHelper.clearPage(); //不加报错
        return new PageData<>(userService.getMyUserList(conditions));
    }

    /**
     * 重置密码
     */
    @RequiresPermissions(Permission.USER_RESET_PWD)
    @PostMapping("/resetPsw")
    public StatusCode resetPsw(@RequestParam("userId") @NotNull Integer userId) {
        User user = userService.getUserById(userId);
        if (ObjectUtils.isEmpty(user)) {
            return StatusCode.USER_NOT_EXIST;
        }
        int cnt = userService.updatePsw(user.getId(), user.getLoginSalt());
        return StatusCode.SUCCESS;
    }

    /**
     * 取消拉黑
     */
    @RequiresPermissions(Permission.USER_BLACKLIST)
    @PostMapping("/cancelBlock")
    public StatusCode cancelBlock(@RequestParam("userId") @NotNull Integer userId) {
        int cnt = userService.cancelBlock(userId);
        return cnt == 1 ? StatusCode.SUCCESS : StatusCode.USER_NOT_BLOCK;
    }

    /**
     * 拉黑
     */
    @RequiresPermissions(Permission.USER_BLACKLIST)
    @PostMapping("/block")
    public StatusCode block(@Validated Blacklist blacklist) {
        int cnt = userService.block(blacklist);
        return cnt == 1 ? StatusCode.SUCCESS : StatusCode.USERNAME_EXIST;
    }

    /**
     * 取消封禁时拉取封禁信息
     */
    @RequiresPermissions(Permission.USER_BLACKLIST)
    @GetMapping("/getBlockInfo")
    public BlackInfoDTO getBlockInfo(@RequestParam("userId") @NotNull Integer userId) {
        return userService.getBlackInfo(userId);
    }


    /**
     * 添加角色
     */
    @RequiresPermissions(Permission.USER_ALLOT_ROLE)
    @PostMapping("/addRole")
    public StatusCode addRole(@RequestParam("roleIds[]") @NotEmpty Integer[] roleIds,
                               @RequestParam("userId") @NotNull Integer userId) {
        if (userService.getUserById(userId) == null) {
            return StatusCode.USER_NOT_EXIST;
        }
        userService.addRoleForUser(roleIds, userId);
        return StatusCode.SUCCESS;
    }

    /**
     * 查询每个用户所分配的角色
     */
    @RequiresPermissions(Permission.ROLE_VIEW)
    @GetMapping("/getRoleByUserId")
    public PageData<Role> getRoleByUserId(@RequestParam(defaultValue = "10") Integer limit,
                                          @RequestParam(defaultValue = "1") Integer page,
                                          @RequestParam("userId") @NotNull Integer userId) {
        PageHelper.startPage(page, Math.max(1, limit));
        return new PageData<>(userService.getRoleByUserId(userId));
    }

    /**
     * 删除角色
     */
    @RequiresPermissions(Permission.USER_DELETE_ROLE)
    @PostMapping("/delRole")
    public StatusCode delRole(@RequestParam("roleIds[]") @NotEmpty Integer[] roleIds,
                              @RequestParam("userId") @NotNull Integer userId) {
        if (userService.getUserById(userId) == null) {
            return StatusCode.USER_NOT_EXIST;
        }
        userService.delRoleForUser(roleIds, userId);
        return StatusCode.SUCCESS;
    }
}
