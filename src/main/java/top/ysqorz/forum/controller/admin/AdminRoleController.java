package top.ysqorz.forum.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.PermZTreeNode;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.RoleResource;
import top.ysqorz.forum.service.AuthorityService;
import top.ysqorz.forum.service.RoleService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 角色管理相关的API
 *
 * @author passerbyYSQ
 * @create 2021-05-13 23:28
 */
@Controller
@ResponseBody
@RequestMapping("/admin/system/role")
@Validated
public class AdminRoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthorityService authorityService;

    @PostMapping("/del")
    public ResultModel delRole(@RequestParam("roleIds[]") @NotEmpty Integer[] roleIds) {
        roleService.delRoleWithPerms(roleIds);
        return ResultModel.success();
    }

    /**
     * 修改角色
     */
    @PostMapping("/update")
    public ResultModel updateRole(@Validated(Role.Update.class) Role role) {
        int cnt = roleService.updateRoleById(role);
        return cnt == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode.ROLE_NOT_EXIST); // 修改失败，可能是角色不存在
    }

    /**
     * 添加角色（没有任何权限）
     */
    @PostMapping("/add")
    public ResultModel<Role> addRole(@Validated(Role.Add.class) Role role) {
        return ResultModel.success(roleService.addRole(role));
    }

    /**
     * 分配权限
     */
    @PostMapping("/assign")
    public ResultModel assignPerms(@NotNull Integer roleId,
           @RequestParam(value = "permIds[]", defaultValue = "") Integer[] permIds) {
        Role role = roleService.getRoleById(roleId);
        if (ObjectUtils.isEmpty(role)) {
            return ResultModel.failed(StatusCode.ROLE_NOT_EXIST); // 角色不存在
        }

        // 删除原本的所有权限
        roleService.delRoleAllPerms(roleId);

        if (!ObjectUtils.isEmpty(permIds)) {
            // 所有的权限id（正确的）
            List<Resource> permList = authorityService.getAuthorityList(null);
            Set<Integer> correctPermIds = permList.stream()
                    .map(Resource::getId).collect(Collectors.toSet());

            // 筛选掉非法的permId，只留下合法的权限id
            List<RoleResource> roleResourceList = Arrays.stream(permIds)
                    .filter(correctPermIds::contains) // true表示留下
                    .map(permId -> new RoleResource(roleId, permId))
                    .collect(Collectors.toList());

            // 分配权限。往role_resource批量插入记录
            roleService.assignPerms(roleResourceList);
        }

        return ResultModel.success();
    }

    /**
     * 某个角色的权限树
     */
    @GetMapping("/perm")
    public ResultModel<List<PermZTreeNode>> rolePermList(@NotNull Integer roleId) {
        Role role = roleService.getRoleById(roleId);
        if (ObjectUtils.isEmpty(role)) {
            return ResultModel.failed(StatusCode.ROLE_NOT_EXIST); // 角色不存在
        }

        // 查询所有权限（用于形成整棵权限树）
        List<Resource> permList = authorityService.getAuthorityList(null);
        // 查询该角色拥有的所有权限的id，用于勾选权限树上对应的已拥有的权限
        Set<Integer> rolePermIds = roleService.getRoleAllPermIds(roleId);
        // 数据转换
        List<PermZTreeNode> nodeList = permList.stream()
                .map(resource -> new PermZTreeNode(resource, rolePermIds))
                .collect(Collectors.toList());

        return ResultModel.success(nodeList);
    }

    /**
     * 角色列表
     */
    @GetMapping("/list")
    public ResultModel<PageData<Role>> roleList(
            @RequestParam(defaultValue = "1") Integer page, // 当前页
            @RequestParam(defaultValue = "10") Integer count, // 每一页显示条数
            String roleName) { // 根据角色名来筛选
        // 参数纠正
        if (count < 1) {
            count = 10;
        }

        // 紧跟在这个方法后的第一个MyBatis 查询方法会被进行分页
        PageHelper.startPage(page, count);
        // 该查询被分页
        List<Role> roleList = roleService.getRoleList(roleName);

        PageInfo<Role> pageInfo = new PageInfo<>(roleList);
        // 获取必需的分页属性
        PageData<Role> pageData = new PageData<>(pageInfo, roleList);

        return ResultModel.success(pageData);
    }

}
