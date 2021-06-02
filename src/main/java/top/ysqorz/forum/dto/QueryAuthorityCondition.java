package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;

/**
 * @author passerbyYSQ
 * @create 2021-05-08 22:44
 */
@Getter
@Setter
public class QueryAuthorityCondition {
    private String name; // 权限名关键字
    private String url; // url关键字
    private String permission; // 权限标识关键字

    // 拼接条件
    public void joinConditions(Example example) {
        Example.Criteria criteria = example.createCriteria();
        if (!ObjectUtils.isEmpty(name)) {
            criteria.andLike("name", "%" + name  + "%");
        }
        if (!ObjectUtils.isEmpty(url)) {
            criteria.andLike("url", "%" + url  + "%");
        }
        if (!ObjectUtils.isEmpty(permission)) {
            criteria.andLike("permission", "%" + permission  + "%");
        }
    }

}
