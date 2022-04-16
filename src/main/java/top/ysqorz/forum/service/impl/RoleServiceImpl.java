package top.ysqorz.forum.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.service.RoleService;
import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.dao.RoleMapper;
import top.ysqorz.forum.dao.RoleResourceMapper;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.RoleResource;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author passerbyYSQ
 * @create 2021-05-13 23:54
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleResourceMapper roleResourceMapper;

    @Transactional // 开启事务操作
    @Override
    public void delRoleWithPerms(Integer[] roleIds) {
        for (Integer roleId : roleIds) {
            // 注意删除的先后顺序
            // 先删除role_resource表中该角色相关的记录
            this.delPermsByRoleId(roleId);
            // 再删除角色
            int cnt = roleMapper.deleteByPrimaryKey(roleId);
            if (cnt != 1) { // 只要有一个角色不存在，抛出异常回滚所有操作
                throw new ParameterErrorException(StatusCode.ROLE_NOT_EXIST.getMsg());
            }
        }
    }

    @Override
    public int updateRoleById(Role role) {
        return roleMapper.updateByPrimaryKeySelective(role); // 注意：选择更新
    }

    @Override
    public Role addRole(Role role) {
        role.setCreateTime(LocalDateTime.now()); // 创建时间
        roleMapper.insertUseGeneratedKeys(role); // 自增长id会被设置到role中
        return role;
    }

    @Override
    public void assignPerms(List<RoleResource> roleResourceList) {
        roleResourceMapper.insertList(roleResourceList);
    }

    @Override
    public void delPermsByRoleId(Integer roleId) {
        Example example = new Example(RoleResource.class);
        example.createCriteria().andEqualTo("roleId", roleId);
        roleResourceMapper.deleteByExample(example);
    }

    @Override
    public Set<Integer> getRoleAllPermIds(Integer roleId) {
        Example example = new Example(RoleResource.class);
        example.selectProperties("resourceId")
                .createCriteria().andEqualTo("roleId", roleId);
        List<RoleResource> rolePerms = roleResourceMapper.selectByExample(example);
        return rolePerms.stream()
                .map(RoleResource::getResourceId)  // 取出resourceId
                .collect(Collectors.toSet());
    }

    @Override
    public Role getRoleById(Integer roleId) {
        return roleMapper.selectByPrimaryKey(roleId);
    }

    @Override
    public List<Role> getRoleList(String roleName) {
        Example example = new Example(Role.class);
        example.orderBy("createTime").desc();
        if (!StringUtils.isEmpty(roleName)) {
            example.createCriteria().andLike("roleName", "%" + roleName + "%");
        }
        return roleMapper.selectByExample(example);
    }

    @Override
    public List<Role> getRoleByUserId(Integer userId) {
        return roleMapper.getRoleByUserId(userId); // 需要联表查询
    }

    @Override
    public List<Resource> getRoleAllPerms(Integer roleId) {
        return roleMapper.getRoleAllPerms(roleId); // 需要联表查询
    }

}
