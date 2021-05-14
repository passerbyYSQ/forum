package top.ysqorz.forum.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.admin.service.RoleService;
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

    @Override
    public Role addRole(Role role) {
        role.setCreateTime(LocalDateTime.now()); // 创建时间
        roleMapper.insertUseGeneratedKeys(role); // 自增长id会被设置到role钟
        return role;
    }

    @Override
    public void assignPerms(List<RoleResource> roleResourceList) {
        roleResourceMapper.insertList(roleResourceList);
    }

    @Override
    public void delRoleAllPerms(Integer roleId) {
        Example example = new Example(RoleResource.class);
        example.createCriteria().andEqualTo("roleId", roleId);
        roleResourceMapper.deleteByExample(example);
    }

    @Override
    public Set<Integer> getRoleAllPerms(Integer roleId) {
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

}
