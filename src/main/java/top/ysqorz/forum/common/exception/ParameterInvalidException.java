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
     * 动态的(包含变量)提示信息
     */
    public ParameterInvalidException(String msg) {
        super(msg);
        this.code = StatusCode.PARAM_INVALID;
        this.code.setMsg(msg);
    }
}
