package top.ysqorz.forum.im.handler;

import top.ysqorz.forum.dao.DanmuMsgMapper;
import top.ysqorz.forum.im.entity.AsyncInsertTask;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.po.DanmuMsg;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2022-01-15 23:02
 */
public class DanmuMsgHandler extends NonFunctionalMsgHandler<DanmuMsg> {
    public DanmuMsgHandler() {
        super(MsgType.DANMU, ChannelType.DANMU);
    }

    @Override
    protected Set<String> queryServersChannelLocated(DanmuMsg danmu) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        return redisService.getWsServers(this.getChannelType(), danmu.getVideoId().toString());
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
