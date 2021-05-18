package top.ysqorz.forum.common;

/**
 * @author passerbyYSQ
 * @create 2021-05-15 13:14
 */
public class ParameterErrorException extends Exception {
    public ParameterErrorException() {
    }

    public ParameterErrorException(String message) {
        super(message);
    }

}
