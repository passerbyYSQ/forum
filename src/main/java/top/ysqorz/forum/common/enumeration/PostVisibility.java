package top.ysqorz.forum.common.enumeration;

import lombok.Getter;
import top.ysqorz.forum.common.annotation.EnumValue;

/**
 * @author passerbyYSQ
 * @create 2022-10-06 0:07
 */
@Getter
public enum PostVisibility {
    EVERYONE_VISIBLE(0, "任何人可见"),
    FOLLOWER_VISIBLE(1, "粉丝可见"),
    LIKED_VISIBLE(2, "点赞后可见"),
    PURCHASED_VISIBLE(3, "积分购买后可见");

    @EnumValue
    private byte code;
    private String desc;

    PostVisibility(int code, String desc) {
        this.code = (byte) code;
        this.desc = desc;
    }
}
