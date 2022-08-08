package top.ysqorz.forum.common.enumeration;

import com.fasterxml.jackson.annotation.JsonValue;
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
    private byte code;
    @JsonValue
    private String value;

    ApplyStatus(int code, String value) {
        this.code = (byte) code;
        this.value = value;
    }
}
