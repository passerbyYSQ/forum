package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.UserRole;


import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole> {
    List<Role> getRoleByUserId(Integer userId);
}