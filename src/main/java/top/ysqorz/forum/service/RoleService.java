package top.ysqorz.forum.service;

import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.RoleResource;

import java.util.List;
import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2021-05-13 23:54
 */
public interface RoleService {
    /**
     * 根据id删除角色
     */
    void delRoleWithPerms(Integer[] roleIds);

    /**
     * 根据id更新角色
     */
    int updateRoleById(Role role);

    /**
     * 添加角色（没有任何权限）
     */
    Role addRole(Role role);

    /**
     * 分配权限。往role_resource批量插入记录
     */
    void assignPerms(List<RoleResource> roleResourceList);

    /**
     * 删除角色的所有权限
     */
    void delRoleAllPerms(Integer roleId);

    /**
     * 某个角色拥有的所有权限的id
     */
    Set<Integer> getRoleAllPermIds(Integer roleId);

    /**
     * 根据id查询角色
     */
    Role getRoleById(Integer roleId);

    /**
     * 查询角色列表
     */
    List<Role> getRoleList(String roleName);

    /**
     * 查询某个用户的所有角色
     */
    List<Role> getRoleByUserId(Integer userId);

    /**
     * 查询某个用户的所有权限
     */
    List<Resource> getRoleAllPerms(Integer roleId);
}
