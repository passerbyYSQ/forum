package top.ysqorz.forum.controller.admin;

import com.github.pagehelper.PageHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.exception.ServiceFailedException;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.resp.PermZTreeNode;
import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.service.AuthorityService;
import top.ysqorz.forum.service.RoleService;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @RequiresPermissions("role:delete")
    @PostMapping("/del")
    public StatusCode delRole(@RequestParam("roleIds[]") @NotEmpty Integer[] roleIds) {
        roleService.delRoleWithPerms(roleIds);
        return StatusCode.SUCCESS;
    }

    /**
     * 修改角色
     */
    @RequiresPermissions("role:update")
    @PostMapping("/update")
    public StatusCode updateRole(@Validated(Role.Update.class) Role role) {
        int cnt = roleService.updateRoleById(role);
        return cnt == 1 ? StatusCode.SUCCESS : StatusCode.ROLE_NOT_EXIST;
    }

    /**
     * 添加角色（没有任何权限）
     */
    @RequiresPermissions("role:add")
    @PostMapping("/add")
    public Role addRole(@Validated(Role.Add.class) Role role) {
        return roleService.addRole(role);
    }

    /**
     * 分配权限
     */
    @RequiresPermissions("role:allotPerm")
    @PostMapping("/assign")
    public StatusCode assignPerms(@NotNull Integer roleId,
           @RequestParam(value = "permIds[]", defaultValue = "") Integer[] permIds) {
        return roleService.assignPerms(roleId, permIds);
    }

    /**
     * 某个角色的权限树
     */
    @RequiresPermissions("perm:view")
    @GetMapping("/perm")
    public List<PermZTreeNode> rolePermList(@NotNull Integer roleId) {
        Role role = roleService.getRoleById(roleId);
        if (ObjectUtils.isEmpty(role)) {
            throw new ServiceFailedException(StatusCode.ROLE_NOT_EXIST);
        }
        // 查询所有权限（用于形成整棵权限树）
        List<Resource> permList = authorityService.getAuthorityList(null);
        // 查询该角色拥有的所有权限的id，用于勾选权限树上对应的已拥有的权限
        Set<Integer> rolePermIds = roleService.getRoleAllPermIds(roleId);
        // 数据转换
        return permList.stream()
                .map(resource -> new PermZTreeNode(resource, rolePermIds))
                .collect(Collectors.toList());
    }

    /**
     * 角色列表
     */
    @RequiresPermissions("role:view")
    @GetMapping("/list")
    public PageData<Role> roleList(
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
        return new PageData<>(roleService.getRoleList(roleName));
    }
}
