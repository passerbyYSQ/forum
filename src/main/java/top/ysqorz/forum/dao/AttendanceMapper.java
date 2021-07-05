package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.AttendDTO;
import top.ysqorz.forum.po.Attendance;

import java.util.List;
import java.util.Map;

public interface AttendanceMapper extends BaseMapper<Attendance> {
    // 今天第几个签到
    Integer attendRankNum(Map<String, Object> params);

    // 截至现在为止的签到日榜
    List<AttendDTO> rankList(Map<String, Object> params);

    // 最近一次连续签到的天数 排行榜
    List<AttendDTO> consecutiveDaysRankList(Map<String, Object> params);

    Integer attendedCount(Map<String, Object> params);
}
