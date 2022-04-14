package top.ysqorz.forum.dto.resp;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 主页右侧的签到情况
 *
 * @author passerbyYSQ
 * @create 2022-04-14 14:29
 */
@Data
@Accessors(chain = true)
public class AttendCardDTO {
    private Integer userId; // 当前登录用户id，未登录为空
    private Boolean isAttendToady; // 今天是否已签到，未登录为false
    private Integer consecutiveAttendDays; // 连续签到的天数
    private Integer attendRank; // 签到排名，未登录为0
    private Integer attendCount; // 截至现在为止的签到人数

    public AttendCardDTO() {
        this.isAttendToady = false;
        this.consecutiveAttendDays = 0;
        this.attendRank = 0;
    }
}
