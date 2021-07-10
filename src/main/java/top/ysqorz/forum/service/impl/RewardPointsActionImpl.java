package top.ysqorz.forum.service.impl;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ysqorz.forum.dao.PointsRecordMapper;
import top.ysqorz.forum.po.PointsRecord;
import top.ysqorz.forum.service.RewardPointsAction;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.DateTimeUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author passerbyYSQ
 * @create 2021-07-09 9:32
 */
@Transactional
@Service
public class RewardPointsActionImpl implements RewardPointsAction {

    // 子类是bean，父类也能注入成功
    @Resource
    private PointsRecordMapper pointsRecordMapper;
    @Resource
    private UserService userService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public void attend(Integer days) {
        int add;
        if (days < 2) { // 1
            add = 1;
        } else if (days < 5) { // [2, 4]
            add = 2;
        } else if (days < 10) { // [5, 9]
            add = 4;
        } else { // >= 10
            add = 5;
        }
        Integer myId = ShiroUtils.getUserId();
        userService.updateRewardPoints(myId, add);
        this.recordPointsChange(myId, add, "签到");
    }

    @Override
    public void publishPost() {
        // setIfAbsent  reward:post:userId --> 5(剩余次数)
        // 过期时长：距离第二天凌晨的时间还有多长时间
        // -1，如果剩余次数仍大于0，则加积分
        Integer myId = ShiroUtils.getUserId();
        String key = "reward:post:" + myId;
        // 如果已经存在就不会重复set
        stringRedisTemplate.opsForValue().setIfAbsent(key, "5", DateTimeUtils.durationToNextDay());
        // 必须使用 stringRedisTemplate，否则无法自增或自减
        Long remained = stringRedisTemplate.opsForValue().decrement(key, 1);
        if (remained >= 0) {
            userService.updateRewardPoints(myId, 4); // 5次 * 4 = 20 每天上限20积分
            this.recordPointsChange(myId, 4, "发表主题帖");
        }
    }

    @Override
    public void publishComment() {
        // reward:comment:userId
        Integer myId = ShiroUtils.getUserId();
        String key = "reward:comment:" + myId;
        Boolean flag = stringRedisTemplate.opsForValue()
                .setIfAbsent(key, "10", DateTimeUtils.durationToNextDay());
        // 必须使用 stringRedisTemplate，否则无法自增或自减
        // https://blog.csdn.net/weixin_42829048/article/details/83989784
        Long remained = stringRedisTemplate.opsForValue().decrement(key, 1);
        if (remained >= 0) {
            userService.updateRewardPoints(myId, 1); // 10次 * 1 = 10 每天上限10积分
            this.recordPointsChange(myId, 1, "发表评论或回复");
        }
    }

    @Override
    public void highQualityPost(Integer userId) {
        userService.updateRewardPoints(userId, 5);
        this.recordPointsChange(userId, 5, "精品帖");
    }

    @Override
    public void visitCountAdded(Integer userId, Integer visitCount) {
        // TODO 需要考虑并发问题下，可能造成积分加多次？
        int add = 0;
        switch (visitCount) {
            case 100:       add = 1;    break;
            case 1000:      add = 4;    break;
            case 10000:     add = 10;   break;
            case 100000:    add = 20;   break;
        }
        if (add > 0) {
            userService.updateRewardPoints(userId, add);
            this.recordPointsChange(userId, add, "帖子访问量达成对应成就");
        }
    }

    @Override
    public void recordPointsChange(Integer userId, Integer dif, String description) {
        PointsRecord record = new PointsRecord();
        record.setUserId(userId)
                .setDif(dif)
                .setDescription(description)
                .setCreateTime(LocalDateTime.now());
        pointsRecordMapper.insertSelective(record);
    }

}
