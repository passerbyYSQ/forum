package top.ysqorz.forum.service;

import top.ysqorz.forum.dto.resp.WeekTopPostDTO;
import top.ysqorz.forum.im.entity.ChannelType;

import java.security.KeyPair;
import java.time.Duration;
import java.util.List;
import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2021-06-02 13:18
 */
public interface RedisService {

    /**
     * 保存验证码
     *
     * @param key
     * @param captcha 验证码字符串
     */
    void saveCaptcha(String key, String captcha);

    /**
     * 取出验证码
     */
    String getCaptcha(String key);

    /**
     * 第三方授权回调时防止CSRF
     *
     * @param key
     * @param state
     */
    void saveOauthState(String key, String state);

    String getOauthState(String key);

    // 访问帖子的ip
    boolean tryAddPostVisitIp(String ipAddress, Integer postId);

    // 往热帖排行榜上对应的帖子增加访问量
    void addHotPostDayRankScore(Integer postId);

    // 获取热帖日榜的前count个帖子id
    Set<Integer> hostPostDayRankTop(Integer count);

    // 往热帖排行周榜上对应的帖子增加访问量
    void addHotPostWeekRankScore(Integer postId);

    // 获取热帖周榜的前count个帖子信息
    List<WeekTopPostDTO> hostPostWeekRankTop(Integer count);

    // 获取热帖周榜是否有信息
    Boolean isHaveWeekHotPost();

    // 记录用户的ws连接在哪个web服务上，以便转发消息时调用接口
    void bindWsServer(ChannelType channelType, String groupId);

    void removeWsServer(ChannelType channelType, String groupId);

    Set<String> getWsServers(ChannelType channelType, String groupId);

    /**
     * 通过Redis缓存IP访问接口的记录。如果频率过高，则限制一段时间内不允许访问
     * @param key       API:接口的路径:客户端ip
     * @param maxCount  duration时长内允许的最大次数
     * @param duration  时长
     * @param blackDuration 封禁时长
     * @return true：放行；false：拦截
     */
    Boolean recordIpAccessAPI(String key, Integer maxCount, Duration duration, Duration blackDuration);

    /**
     * 判断用户是否在线。判断该用户是否有WebSocket通道
     */
    boolean isUserOnline(Integer userId);


    void saveRSAKeyPair();

    KeyPair getRSAKeyPair();
}
