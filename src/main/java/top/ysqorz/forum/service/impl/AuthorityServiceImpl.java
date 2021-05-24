package top.ysqorz.forum.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.service.AuthorityService;
import top.ysqorz.forum.common.TreeBuilder;
import top.ysqorz.forum.dao.ResourceMapper;
import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.vo.QueryAuthorityCondition;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author passerbyYSQ
 * @create 2021-05-08 11:28
 */
@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public List<Resource> getAuthorityList(QueryAuthorityCondition conditions) {
        Example example = new Example(Resource.class);
        example.orderBy("sortWeight").desc();
        if (conditions != null) { // 条件筛选
            conditions.joinConditions(example); // 拼接条件
        }
        return resourceMapper.selectByExample(example);
    }

    @Override
    public Resource getAuthorityById(Integer authorityId) {
        return resourceMapper.selectByPrimaryKey(authorityId);
    }

    @Override
    public Resource getAuthorityByName(String authorityName) {
        Example example = new Example(Resource.class);
        example.createCriteria()
                .andEqualTo("name", authorityName);
        return resourceMapper.selectOneByExample(example);
    }

    @Override
    public Resource addAuthority(Resource resource) {
        // parentId为空，说明添加的是根节点
        if (ObjectUtils.isEmpty(resource.getParentId())) {
            resource.setParentId(0);
        }
        // 自增长生成的id会被设置进resource中
        resourceMapper.insertUseGeneratedKeys(resource);
        return resource;
    }

    @Override
    public int updateAuthorityById(Resource resource) {
        return resourceMapper.updateByPrimaryKey(resource); // 全部更新，而不是非空字段更新
    }

    @Override
    public int delAuthorityById(Integer[] authorityIds) {
        List<Resource> resourceList = getAuthorityList(null);
        TreeBuilder<Integer> builder = new TreeBuilder<>(resourceList, 0);
        // 筛选出合法id，只有是叶子节点才删。非叶子节点不删除
        List<Integer> ids = Arrays.stream(authorityIds)
                .filter(builder::isLeaf)
                .collect(Collectors.toList());

        if (ids.isEmpty()) {
            return 0;
        }

        Example example = new Example(Resource.class);
        example.createCriteria().andIn("id", ids); // 不能传入空的List，前面需要判断
        return resourceMapper.deleteByExample(example);
    }
}
