package top.ysqorz.forum.admin.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.admin.service.UserService;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.utils.PageUtils;
import top.ysqorz.forum.vo.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author 阿灿
 * @create 2021-05-10 16:08
 */
@RestController
@RequestMapping("/admin/system/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取User数据
     */
    @GetMapping("/table")
    public ResultModel<PageData<UserVo>> getUserAndRole(@RequestParam(defaultValue = "10") Integer limit,
                                                        @RequestParam(defaultValue = "1") Integer page,
                                                        QueryUserCondition conditions) {
        if (limit <= 0) {
            limit = 10;
        }
//        PageHelper.startPage(page,limit);
//      //  PageHelper.clearPage(); //不加报错
//        List<UserVo> myUserList = userService.getMyUserList(conditions);
//       // List<User> userList = userService.getUserList(conditions);
//        PageInfo<UserVo> pageinfo = new PageInfo<>(myUserList);
//        PageData<UserVo> pageData=new PageData<>();
//        pageData.setList(myUserList);
//        pageData.setTotal(pageinfo.getTotal());
//        pageData.setPage(pageinfo.getPageNum());
//        pageData.setCount(pageinfo.getPageSize());
//
//        return ResultModel.success(pageData);
        //获取结果集
        List<UserVo> myUserList = userService.getMyUserList(conditions);
        //使用工具类得到分页后的结果集
        List<UserVo> data = PageUtils.getPageSizeDataForRelations(myUserList, limit, page);

        int size = myUserList.size();//总条数
        //总页数
        int totlePage = size / limit;//取整
        int i = size % limit;//取余
        if (i > 0) {
            totlePage += 1;
        }
        PageData<UserVo> pageData = new PageData<>();
        pageData.setList(data);  //传入PageUtil处理后的结果集
        pageData.setTotal((long) size);  //总数
        pageData.setPage(limit);        //每页显示条数
        pageData.setCount(totlePage); //总页数

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
    public ResultModel<BlackInfoVo> getBlockInfo(@RequestParam("userId") @NotNull Integer userId) {
        BlackInfoVo blackInfo = userService.getBlackInfo(userId);
        return ResultModel.success(blackInfo);
    }

    /**
     * 查询所有可分配角色
     *
     * @return
     */
    @GetMapping("/getRole")
    public ResultModel<PageData<Role>> getRole(@RequestParam(defaultValue = "10") Integer limit,
                                               @RequestParam(defaultValue = "1") Integer page) {

        //System.out.println(conditions);
        if (limit <= 0) {
            limit = 10;
        }
        PageHelper.startPage(page, limit);
        List<Role> allRole = userService.getAllRole();
        PageInfo<Role> pageInfo = new PageInfo<>(allRole);
        PageData<Role> pageData = new PageData<>();
        pageData.setList(allRole);
        pageData.setTotal(pageInfo.getTotal());
        pageData.setPage(pageInfo.getPageNum());
        pageData.setCount(pageInfo.getPageSize());

        return ResultModel.success(pageData);


    }

    /**
     * 添加角色
     */
    @PostMapping("/addRole")
    public ResultModel addRole(@RequestParam("roleIds[]") @NotEmpty Integer[] roleIds,
                               @RequestParam("userId") @NotNull Integer userId) {
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
