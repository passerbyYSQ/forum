package top.ysqorz.forum.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 * @author 阿灿
 * @create 2021-05-10 16:17
 */
@Getter
@Setter
public class QueryUserCondition {
    private String username; // 名字关键字
    private String phone; // 手机号关键字
    private byte state;  //state 1代表查询注册时间 2代表查询上一次登录时间
    private LocalDateTime startTime;//起始时间
    private LocalDateTime endTime; //结束时间
    private LocalDateTime now = LocalDateTime.now(); //当前时间


    @Override
    public String toString() {
        return "QueryUserCondition{" +
                "username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", state=" + state +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
