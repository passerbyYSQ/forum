package top.ysqorz.forum.service;

/**
 * 积分奖励的行为，暂不考虑积分撤销
 *
 * @author passerbyYSQ
 * @create 2021-07-09 9:26
 */
public interface RewardPointsAction {

    // 当前用户签到
    void attend(Integer days);

    void publishPost();

    // 当前用户发表一级评论。评论或回复自己不奖励积分
    void publishComment();

    // 被设置为精品帖
    void highQualityPost(Integer userId);

    // 帖子访问量
    void visitCountAdded(Integer userId,Integer visitCount);

    // 记录积分变化
    void recordPointsChange(Integer userId, Integer dif, String description);

}
