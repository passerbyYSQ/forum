package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用于前台显示的用户小卡片
 *
 * @author passerbyYSQ
 * @create 2021-06-18 23:46
 */
@Getter
@Setter
public class SimpleUserDTO {
    private Integer id;
    private String username;
    private Byte gender;
    private Integer rewardPoints; // 积分
    private Integer level; // 等级
    private String photo; // 以后增加 缩略图和原图

    private Integer blackId; // 当前是否处于封禁
    private Integer consecutiveAttendDays; // 最近一次连续签到的天数
    private LocalDateTime lastAttendTime; // 上一次签到的时间

    private Integer newMeg;//新消息
}
