package top.ysqorz.forum.admin.service;

import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.vo.QueryAuthorityCondition;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-05-08 11:26
 */
public interface AuthorityService {

    /**
     * 查询权限列表（不需要形成树）
     */
    List<Resource> getAuthorityList(QueryAuthorityCondition conditions);

    /**
     * 根据id查找权限
     */
    Resource getAuthorityByName(String authorityName);

    /**
     * 新增权限。自增长id设置进中
     */
    Resource addAuthority(Resource resource);

    /**
     * 新增权限。自增长id设置进中
     */
    int updateAuthorityById(Resource resource);

    /**
     * 根据id删除权限
     */
    int delAuthorityById(Integer[] authorityIds);

}
