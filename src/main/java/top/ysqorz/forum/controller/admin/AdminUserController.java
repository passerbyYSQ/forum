package top.ysqorz.forum.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.dto.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    @GetMapping("/table")
    public ResultModel<PageData<UserDTO>> getUserAndRole(@RequestParam(defaultValue = "10") Integer limit,
                                                         @RequestParam(defaultValue = "1") Integer page,
                                                         QueryUserCondition conditions) {
        if (limit <= 0) {
            limit = 10;
        }
        PageHelper.startPage(page, limit);
        //  PageHelper.clearPage(); //不加报错
        List<UserDTO> myUserList = userService.getMyUserList(conditions);
        // List<User> userList = userService.getUserList(conditions);
        PageInfo<UserDTO> pageinfo = new PageInfo<>(myUserList);
        PageData<UserDTO> pageData = new PageData<>();
        pageData.setList(myUserList);
        pageData.setTotal(pageinfo.getTotal());
        pageData.setPage(pageinfo.getPageNum());
        pageData.setCount(pageinfo.getPageSize());

        return ResultModel.success(pageData);
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPsw")
    public ResultModel ResetPsw(@RequestParam("userId") @NotNull Integer userId) {
        User user = userService.getInfoById(userId);
        if (ObjectUtils.isEmpty(user)) {
            ResultModel.failed(StatusCode.USER_NOT_EXIST);
        }
        int i = userService.updatePsw(user.getId());
        return ResultModel.success();
    }

    /**
     * 取消拉黑
     */
    @PostMapping("/cancelBlock")
    public ResultModel cancelBlock(@RequestParam("userId") @NotNull Integer userId) {
        int i = userService.cancelBlock(userId);
        return i == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode.USER_NOT_BLOCK);
    }

    /**
     * 拉黑
     */
    @PostMapping("/block")
    public ResultModel block(@Validated(Blacklist.Add.class) Blacklist blacklist) {
        //System.out.println(blacklist);
        int i = userService.block(blacklist);
        return i == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode.USERNAME_IS_EXIST);

    }

    /**
     * 取消封禁时拉取封禁信息
     */
    @GetMapping("/getBlockInfo")
    public ResultModel<BlackInfoDTO> getBlockInfo(@RequestParam("userId") @NotNull Integer userId) {
        BlackInfoDTO blackInfo = userService.getBlackInfo(userId);
        return ResultModel.success(blackInfo);
    }


    /**
     * 添加角色
     */
    @PostMapping("/addRole")
    public ResultModel addRole(@RequestParam("roleIds[]") @NotEmpty Integer[] roleIds,
                               @RequestParam("userId") @NotNull Integer userId) throws ParameterErrorException {
        if (userService.getInfoById(userId) == null) {
            return ResultModel.failed(StatusCode.USER_NOT_EXIST);
        }
        int i = userService.addRoleForUser(roleIds, userId);
        return i == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode.USERNAME_IS_EXIST);
    }

    /**
     * 查询每个用户所分配的角色
     */
    @GetMapping("/getRoleByUserId")
    public ResultModel<PageData<Role>> getRoleByUserId(@RequestParam(defaultValue = "10") Integer limit,
                                                       @RequestParam(defaultValue = "1") Integer page,
                                                       @RequestParam("userId") @NotNull Integer userId) {

        //System.out.println(conditions);
        if (limit <= 0) {
            limit = 10;
        }
        PageHelper.startPage(page, limit);
        List<Role> role = userService.getRoleByUserId(userId);
        PageInfo<Role> pageInfo = new PageInfo<>(role);
        PageData<Role> pageData = new PageData<>();
        pageData.setList(role);
        pageData.setTotal(pageInfo.getTotal());
        pageData.setPage(pageInfo.getPageNum());
        pageData.setCount(pageInfo.getPageSize());

        return ResultModel.success(pageData);
    }

    /**
     * 删除角色
     */
    @PostMapping("/delRole")
    public ResultModel delRole(@RequestParam("roleIds[]") @NotEmpty Integer[] roleIds,
                               @RequestParam("userId") @NotNull Integer userId) {
        int i = userService.delRoleForUser(roleIds, userId);
        return i == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode.USERNAME_IS_EXIST);
    }

}
