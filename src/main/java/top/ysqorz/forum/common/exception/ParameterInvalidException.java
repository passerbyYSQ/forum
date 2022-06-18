package top.ysqorz.forum.common.exception;

import lombok.Getter;
import lombok.Setter;
import top.ysqorz.forum.common.StatusCode;

/**
 * @author passerbyYSQ
 * @create 2021-05-15 13:14
 */
@Getter
@Setter
public class ParameterInvalidException extends RuntimeException {
    private StatusCode code;

    public ParameterInvalidException() {
        this(StatusCode.PARAM_INVALID);
    }

    public ParameterInvalidException(StatusCode code) {
        super(code.getMsg());
        this.code = code;
    }

    /**
     * 兼容旧代码
     * @deprecated
     */
    public ParameterInvalidException(String msg) {
        super(msg);
    }
}
