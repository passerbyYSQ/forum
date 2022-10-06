package top.ysqorz.forum.common.enumeration;

import lombok.Getter;

/**
 * @author passerbyYSQ
 * @create 2022-10-06 1:01
 */
@Getter
public enum Oauth2App {
    QQ("QQ"),
    GITEE("Gitee"),
    BAIDU("Baidu");

    private String desc;

    Oauth2App(String desc) {
        this.desc = desc;
    }
}
