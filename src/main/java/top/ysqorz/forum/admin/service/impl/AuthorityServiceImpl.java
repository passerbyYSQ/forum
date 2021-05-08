package top.ysqorz.forum.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.admin.service.AuthorityService;
import top.ysqorz.forum.dao.ResourceMapper;
import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.vo.QueryAuthorityCondition;

import java.util.Arrays;
import java.util.List;

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
        conditions.fillDefault(); // 填充默认值
        example.createCriteria()
                .andLike("name", "%" + conditions.getName()  + "%")
                .andLike("url", "%" + conditions.getUrl() + "%")
                .andLike("permission", "%" + conditions.getPermission() + "%");
        return resourceMapper.selectByExample(example);
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
        // parentId为空，说明添加的是根节点
        if (ObjectUtils.isEmpty(resource.getParentId())) {
            resource.setParentId(0);
        }
        return resourceMapper.updateByPrimaryKey(resource); // 全部更新，而不是非空字段更新
    }

    @Override
    public int delAuthorityById(Integer[] authorityIds) {
        Example example = new Example(Resource.class);
        example.createCriteria().andIn("id", Arrays.asList(authorityIds));
        return resourceMapper.deleteByExample(example);
    }
}
