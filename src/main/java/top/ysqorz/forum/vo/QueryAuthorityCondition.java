package top.ysqorz.forum.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

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

    public void fillDefault() {
        if (ObjectUtils.isEmpty(name)) { // null
            name = "";
        }
        if (ObjectUtils.isEmpty(url)) {
            url = "";
        }
        if (ObjectUtils.isEmpty(permission)) {
            permission = "";
        }
    }

}
