package top.ysqorz.forum.common.enumeration;

import lombok.Getter;
import top.ysqorz.forum.common.annotation.EnumValue;

/**
 * @author passerbyYSQ
 * @create 2022-10-06 1:14
 */
@Getter
public enum Gender {
    MALE(0, "男"),
    FEMALE(1, "女"),
    SECRET(2, "保密");

    @EnumValue
    private byte code;
    private String desc;

    Gender(int code, String desc) {
        this.code = (byte) code;
        this.desc = desc;
    }
}
