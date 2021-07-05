package top.ysqorz.forum.service;

import top.ysqorz.forum.dto.AttendDTO;
import top.ysqorz.forum.po.Attendance;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-07-03 23:51
 */
public interface AttendService {
    /**
     * 当前用户今天的签到记录
     */
    Attendance getMyAttendToday();

    /**
     * 签到
     */
    AttendDTO attend();

    /**
     * 签到时间为attendDateTime的排名
     */
    Integer attendRankNum(Integer userId, LocalDateTime attendDateTime);

    /**
     * 截至attendDateTime的已签到的认数
     */
    Integer attendedCount(LocalDateTime attendDateTime);

    /**
     * 截至现在的签到日榜情况
     */
    List<AttendDTO> rankList(Integer count);

    /**
     * 最近一次连续签到的天数 排行榜
     */
    List<AttendDTO> consecutiveDaysRankList(Integer count);
}
