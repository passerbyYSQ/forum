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
    private String Time;
     private String state="0";
    private LocalDateTime starttime;
    private LocalDateTime endtime;

    public void fillDefault() {
        if (ObjectUtils.isEmpty(username)) { // null
            username = "";
        }
        if (ObjectUtils.isEmpty(phone)) {
            phone = "";
        }
        if (ObjectUtils.isEmpty(Time)) {
            Time = "";
        }


    }

    @Override
    public String toString() {
        return "QueryUserCondition{" +
                "username='" + username + '\'' +
                ", phone='" + phone + '\'' +
                ", Time='" + Time + '\'' +
                ", state='" + state + '\'' +
                ", starttime=" + starttime +
                ", endtime=" + endtime +
                '}';
    }
}
