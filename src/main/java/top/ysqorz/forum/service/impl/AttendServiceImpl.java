package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.dao.AttendanceMapper;
import top.ysqorz.forum.dto.resp.AttendDTO;
import top.ysqorz.forum.dto.resp.AttendCardDTO;
import top.ysqorz.forum.po.Attendance;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.AttendService;
import top.ysqorz.forum.service.RewardPointsAction;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author passerbyYSQ
 * @create 2021-07-03 23:52
 */
@Service
public class AttendServiceImpl implements AttendService {
    @Resource
    private UserService userService;
    @Resource
    private AttendanceMapper attendanceMapper;
    @Resource
    private RewardPointsAction rewardPointsAction;

    @Override
    public AttendCardDTO getTodayAttendCard() {
        AttendCardDTO attendCard = new AttendCardDTO(); // 空参构造已经赋初始值
        if (ShiroUtils.isAuthenticated()) {
            Integer myId = ShiroUtils.getUserId();
            attendCard.setUserId(myId);

            User self = userService.getUserById(myId);
            LocalDateTime lastAttendTime = self.getLastAttendTime();
            boolean isAttendToday = !ObjectUtils.isEmpty(lastAttendTime) &&
                    LocalDate.now().equals(lastAttendTime.toLocalDate());
            attendCard.setIsAttendToady(isAttendToday);

            if (isAttendToday) { // 今天已签到
                Integer rank = this.attendRankNum(myId, lastAttendTime);
                attendCard.setAttendRank(rank); // 今天的签到排名
            }

            // 由于连续签到的天数在用户下一次签到时才会重置，此处需要做纠正
            // 上一次签到时间是昨天之前，则连续签到的天数为0
            boolean isAttendYesterday = !ObjectUtils.isEmpty(lastAttendTime) &&
                    LocalDate.now().equals(lastAttendTime.toLocalDate());
            if (isAttendYesterday || isAttendToday) {
                attendCard.setConsecutiveAttendDays(self.getConsecutiveAttendDays());
            }
        }
        // 已签到的人数
        Integer attendCount = this.attendedCount(LocalDateTime.now());
        attendCard.setAttendCount(attendCount);
        return attendCard;
    }

    @Override
    public Attendance getMyAttendToday() {
        Example example = new Example(Attendance.class);
        example.createCriteria()
                .andEqualTo("userId", ShiroUtils.getUserId()) // ！！
                .andEqualTo("attendDate", LocalDate.now());
        return attendanceMapper.selectOneByExample(example);
    }

    @Transactional
    @Override
    public AttendDTO attend() {
        Integer myId = ShiroUtils.getUserId();
        LocalDateTime now = LocalDateTime.now();

        // 往 attendance 插入一条记录
        Attendance attend = new Attendance();
        attend.setUserId(myId)
                .setAttendDate(now.toLocalDate())
                .setAttendTime(now.toLocalTime());
        attendanceMapper.insertSelective(attend);


        User user = userService.getUserById(myId);
        User record = new User();
        // 如果昨天签到了，则 consecutiveAttendDays 加1。否则重置为1
        Integer days = 1;
        LocalDateTime lastTime = user.getLastAttendTime();
        if (!ObjectUtils.isEmpty(lastTime) &&
                LocalDate.now().minusDays(1).equals(lastTime.toLocalDate())) {
            days += user.getConsecutiveAttendDays();
        }
        record.setId(myId)
                .setConsecutiveAttendDays(days) // 更新连续签到的天数
                .setLastAttendTime(now); // 更新最后一次签到的时间
                //.setRewardPoints(user.getRewardPoints() + 2); // TODO 积分奖励规则和积分记录之后再说
        userService.updateUserById(record);

        // 奖励积分
        rewardPointsAction.attend(days);

        // 今天第几个签到。一定要放在 insert 和 update 之后
        Integer rank = this.attendRankNum(myId, now);
        Integer attendCount = this.attendedCount(now);

        AttendDTO attendDTO = new AttendDTO();
        attendDTO.setAttendDateTime(now)
                .setRank(rank)
                .setConsecutiveAttendDays(days)
                .setAttendCount(attendCount);
        return attendDTO;
    }

    @Override
    public Integer attendRankNum(Integer userId, LocalDateTime attendDateTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("attendDate", attendDateTime.toLocalDate());
        params.put("attendTime", attendDateTime.toLocalTime());
        return attendanceMapper.attendRankNum(params);
    }

    @Override
    public Integer attendedCount(LocalDateTime attendDateTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("attendDate", attendDateTime.toLocalDate());
        params.put("attendTime", attendDateTime.toLocalTime());
        return attendanceMapper.attendedCount(params);
    }

    @Override
    public List<AttendDTO> rankList(Integer count) {
        Map<String, Object> params = new HashMap<>();
        final LocalDate toady = LocalDate.now();
        params.put("today", toady);
        params.put("count", count);
        return attendanceMapper.rankList(params);
    }

    @Override
    public List<AttendDTO> consecutiveDaysRankList(Integer count) {
        Map<String, Object> params = new HashMap<>();
        LocalDate toady = LocalDate.now();
        // 计算工作尽量不要交给mysql去做
        params.put("today", toady);
        params.put("yesterday", toady.minusDays(1));
        params.put("count", count);
        return attendanceMapper.consecutiveDaysRankList(params);
    }


}
