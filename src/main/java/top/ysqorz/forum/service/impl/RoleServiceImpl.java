package top.ysqorz.forum.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dao.RoleMapper;
import top.ysqorz.forum.dao.RoleResourceMapper;
import top.ysqorz.forum.dao.UserRoleMapper;
import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.RoleResource;
import top.ysqorz.forum.po.UserRole;
import top.ysqorz.forum.service.AuthorityService;
import top.ysqorz.forum.service.RoleService;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private AuthorityService authorityService;

    @Override
    public void delPermsByRoleIds(List<Integer> roleIds) {
        Example example = new Example(RoleResource.class);
        example.createCriteria().andIn("roleId", roleIds);
        roleResourceMapper.deleteByExample(example);
    }

    @Override
    public void delUserRoleRelationsByRoleIds(List<Integer> roleIds) {
        Example example = new Example(UserRole.class);
        example.createCriteria().andIn("roleId", roleIds);
        userRoleMapper.deleteByExample(example);
    }

    @Override
    public void delRolesByRoleIds(List<Integer> roleIds) {
        Example example = new Example(Role.class);
        example.createCriteria().andIn("id", roleIds);
        roleMapper.deleteByExample(example);
    }

    @Transactional // 开启事务操作
    @Override
    public void delRoleWithPerms(Integer[] roleIds) {
        // 删除role_resource表中该角色相的关记录
        List<Integer> roleIdList = Arrays.asList(roleIds);
        this.delPermsByRoleIds(roleIdList);
        // 删除user_role表中该角色的相关记录
        this.delUserRoleRelationsByRoleIds(roleIdList);
        // 删除role表的相关记录
        this.delRolesByRoleIds(roleIdList);
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
    @Transactional
    public StatusCode assignPerms(Integer roleId, Integer[] permIds) {
        Role role = this.getRoleById(roleId);
        if (ObjectUtils.isEmpty(role)) {
            return StatusCode.ROLE_NOT_EXIST; // 角色不存在
        }

        // 删除原本的所有权限
        this.delPermsByRoleId(roleId);

        if (!ObjectUtils.isEmpty(permIds)) {
            // 所有的权限id（正确的）
            List<Resource> permList = authorityService.getAuthorityList(null);
            Set<Integer> allPermIds = permList.stream()
                    .map(Resource::getId).collect(Collectors.toSet());

            // 筛选掉非法的permId，只留下合法的权限id
            List<RoleResource> roleResourceList = Arrays.stream(permIds)
                    .filter(allPermIds::contains) // true表示留下
                    .map(permId -> new RoleResource(roleId, permId))
                    .collect(Collectors.toList());

            // 分配权限。往role_resource批量插入记录
            roleResourceMapper.insertList(roleResourceList);
        }

        return StatusCode.SUCCESS;
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
