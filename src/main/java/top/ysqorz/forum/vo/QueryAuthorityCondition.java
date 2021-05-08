package top.ysqorz.forum.vo;

import lombok.Getter;
import lombok.Setter;

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

}
