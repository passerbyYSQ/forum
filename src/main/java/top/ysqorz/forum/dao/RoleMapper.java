package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.po.Role;

import java.util.List;

public interface RoleMapper extends BaseMapper<Role> {
    // 查询某个用户的所有角色
    List<Role> getRoleByUserId(Integer userId);

    // 查询某个角色的所有权限
    List<Resource> getRoleAllPerms(Integer roleId);
}
