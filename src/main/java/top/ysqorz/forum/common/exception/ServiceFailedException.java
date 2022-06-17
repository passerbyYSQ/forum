package top.ysqorz.forum.common.exception;

import lombok.Getter;
import lombok.Setter;
import top.ysqorz.forum.common.StatusCode;

/**
 * @author passerbyYSQ
 * @create 2022-06-17 23:51
 */
@Getter
@Setter
public class ServiceFailedException extends RuntimeException {
    private StatusCode code;

    public ServiceFailedException(StatusCode code) {
        super(code.getMsg());
        this.code = code;
    }
}
