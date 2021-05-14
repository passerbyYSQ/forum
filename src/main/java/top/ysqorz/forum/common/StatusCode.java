package top.ysqorz.forum.common;

/**
 * 2000 - 成功处理请求
 * 3*** - 重定向，需要进一步的操作已完成请求
 * 4*** - 客户端错误，请求参数错误，语法错误等等
 * 5*** - 服务器内部错误
 * ...
 *
 * @author passerbyYSQ
 * @create 2020-11-02 16:26
 */
// 不加上此注解，Jackson将对象序列化为json时，直接将枚举类转成它的名字
//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum StatusCode {
    SUCCESS(2000, "成功"),

    // 服务器内部错误
    UNKNOWN_ERROR(5000, "未知错误"),
    NO_PERM(5001, "无权限操作"),

    // 参数相关
    PARAM_NOT_COMPLETED(6001, "参数缺失"),
    PARAM_IS_INVALID(6002, "参数无效"),
    FILE_TYPE_INVALID(6003, "非法文件类型"),
    FILE_SIZE_EXCEEDED(6004, "文件大小超出限制"),

    // 用户相关
    USERNAME_IS_EXIST(6101, "用户名已存在"),
    PASSWORD_INCORRECT(6102, "密码错误"),
    USER_NOT_EXIST(6103, "用户不存在"), // 可能是userId错误

    // 账户相关
    TOKEN_IS_MISSING(6200, "token缺失"),
    FORCED_OFFLINE(6201, "当前账号在异地登录，您已被挤下线"),
    TOKEN_IS_EXPIRED(6202, "token已过期，请重新登录"),
    TOKEN_IS_INVALID(6203, "无效token"),

    // 权限相关
    AUTHORITY_NAME_EXIST(6300, "权限名称已存在"),
    AUTHORITY_NOT_EXIST(6301, "权限不存在"),
    AUTHORITY_UPDATE_FAILED(6302, "您未做任何更新"),
    AUTHORITY_PID_NOT_VALID(6303, "新的父权限不能为当前权限及子孙权限"),

    // 角色相关的
    ROLE_NOT_EXIST(6400, "角色不存在")

    ;


    // 状态码数值
    private Integer code;
    // 状态码描述信息
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据业务状态码获取对应的描述信息
     * @param code      业务状态码
     * @return
     */
    public static String getMsgByCode(Integer code) {
        for (StatusCode status : StatusCode.values()) {
            if (status.code.equals(code)) {
                return status.msg;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
