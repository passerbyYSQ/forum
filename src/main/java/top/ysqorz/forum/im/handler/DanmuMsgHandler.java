package top.ysqorz.forum.im.handler;

import io.netty.channel.Channel;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;
import top.ysqorz.forum.dao.DanmuMsgMapper;
import top.ysqorz.forum.im.entity.AsyncInsertTask;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.po.DanmuMsg;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.RandomUtils;
import top.ysqorz.forum.utils.SpringUtils;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2022-01-15 23:02
 */
public class DanmuMsgHandler extends MsgHandler<DanmuMsg> {
    public DanmuMsgHandler() {
        super(MsgType.DANMU);
    }

    /**
     * 兼容单机版本
     */
    @Override
    protected boolean doHandle0(DanmuMsg danmu, Channel channel, User loginUser) {
        // 异步将弹幕插入数据库
        this.doSave(danmu);
        // 推送弹幕
        this.doPush(danmu, channel.id().asLongText());
        return true;
    }

    @Override
    protected DanmuMsg transformData(MsgModel msg) {
        return JsonUtils.nodeToObj(msg.transformToDataNode(), DanmuMsg.class);
    }

    @Override
    protected DanmuMsg processData(DanmuMsg danmu, User user) {
        if (danmu == null || ObjectUtils.isEmpty(danmu.getContent()) || danmu.getVideoId() == null) {
            return null;
        }
        int endIndex = Math.min(255, danmu.getContent().length());
        String text = danmu.getContent().substring(0, endIndex); // 如果过长只截取前500个字符
        text = HtmlUtils.htmlEscape(text, "UTF-8"); // 转义，防止XSS攻击
        danmu.setId(RandomUtils.generateUUID())
                .setContent(text)
                .setCreatorId(user.getId())
                .setCreator(new DanmuMsg.DanmuCreator(user))
                .setCreateTime(LocalDateTime.now())
                .setStartMs(Math.max(danmu.getStartMs(), 0)); // 负数时做纠正
        return danmu;
    }

    @Override
    protected Set<String> queryServersChannelLocated(DanmuMsg msg) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        return redisService.getWsServers(this.getType().name(), msg.getVideoId().toString());
    }

    @Override
    protected void doPush(DanmuMsg danmu, String sourceChannelId) {  // data is completed
        this.channelMap.pushToGroup(danmu, sourceChannelId, danmu.getVideoId().toString());
    }

    @Override
    protected AsyncInsertTask createAsyncInsertTask(DanmuMsg danmu) {
        DanmuMsgMapper mapper = SpringUtils.getBean(DanmuMsgMapper.class);
        return new AsyncInsertTask<>(mapper, danmu);
    }
}
