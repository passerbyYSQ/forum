package top.ysqorz.forum.common.enumeration;

import lombok.Getter;

/**
 * @author passerbyYSQ
 * @create 2022-10-05 23:59
 */
@Getter
public enum CommentType {
    FIRST_COMMENT("一级评论"),
    SECOND_COMMENT("二级评论");

    private String desc;

    CommentType(String desc) {
        this.desc = desc;
    }
}
