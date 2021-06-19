package top.ysqorz.forum.common;

/**
 * @author passerbyYSQ
 * @create 2021-05-15 13:14
 */
public class ParameterErrorException extends RuntimeException {
    public ParameterErrorException() {
    }

    public ParameterErrorException(String message) {
        super(message);
    }

}
