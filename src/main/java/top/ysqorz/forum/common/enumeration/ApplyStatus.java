package top.ysqorz.forum.common.enumeration;

import lombok.Getter;
import top.ysqorz.forum.common.annotation.EnumValue;

/**
 * 申请的状态
 * @author passerbyYSQ
 * @create 2022-08-08 22:45
 */
@Getter
public enum ApplyStatus {
    REFUSE(0, "拒绝"),
    AGREE(1, "同意"),
    IGNORE(2, "忽略");

    @EnumValue
    private Byte code;
    private String desc;

    ApplyStatus(int code, String desc) {
        this.code = (byte) code;
        this.desc = desc;
    }
}
