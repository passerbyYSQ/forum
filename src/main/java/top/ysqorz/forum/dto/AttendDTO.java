package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @author passerbyYSQ
 * @create 2021-07-03 20:47
 */
@Getter
@Setter
public class AttendDTO {
    private Integer userId;
    private String username;
    private String photo;

    private Integer rank; // 第几个签到
    private Integer consecutiveAttendDays; // 最近一次连续签到的天数
    private LocalDateTime attendDateTime; // 签到时间
    private LocalDate attendDate; // 签到时间
    private LocalTime attendTime; // 签到时间

}
