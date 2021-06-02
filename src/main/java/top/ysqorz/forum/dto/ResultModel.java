package top.ysqorz.forum.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author passerbyYSQ
 * @create 2020-11-01 21:04
 */
@Data
public class ResultModel<T>  {

    // 注意这是业务状态码。业务成功统一为2000，业务失败为其他
    // 比如说登录密码错误，http状态码是200（表示http请求成功），
    // 但是业务状态码为5001（举例）表示虽然http请求成功，但是业务失败
    private Integer code;
    // 状态描述信息
    private String msg;
    // 返回的数据
    private T data;

    private LocalDateTime time = LocalDateTime.now();

    // 全参构造
    public ResultModel(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
    // 没有data的构造
    public ResultModel(Integer code, String msg) {
        this(code, msg, null);
    }

    // 成功。带有结果集
    public static <T> ResultModel<T> success(T data) {
        return success(StatusCode.SUCCESS.getMsg(), data);
    }

    public static <T> ResultModel<T> success(String msg, T data) {
        return new ResultModel<>(StatusCode.SUCCESS.getCode(), msg, data);
    }

    // 成功。没有结果集
    public static <T> ResultModel<T> success() {
        return success(null);
    }

    // 具体错误
    public static <T> ResultModel<T> failed(Integer code, String msg) {
        return new ResultModel<>(code, msg);
    }

    // 具体错误，在枚举类中集中定义，然后传入指定枚举类对象
    public static <T> ResultModel<T> failed(StatusCode code) {
        return failed(code.getCode(), code.getMsg());
    }



}
