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
public class DanmuMsgHandler extends RemoteMsgHandlerProxy<DanmuMsg> {
    public DanmuMsgHandler() {
        super(MsgType.DANMU, ChannelType.DANMU);
    }

    @Override
    protected Set<String> queryServersChannelLocated(DanmuMsg data) {
        RedisService redisService = SpringUtils.getBean(RedisService.class);
        return redisService.getWsServers(getChannelType(), data.getVideoId().toString());
    }

    @Override
    protected boolean doPush(DanmuMsg data, String sourceChannelId) {  // data is completed
        getChannelMap().pushToGroup(getMsgType(), data, sourceChannelId, data.getVideoId().toString());
        return true;
    }

    @Override
    protected void saveData(DanmuMsg data) {
        DanmuMsgMapper mapper = SpringUtils.getBean(DanmuMsgMapper.class);
        getExecutor().execute(new AsyncInsertTask<>(mapper, data));
    }

    @Override
    public Class<DanmuMsg> getDataClass() {
        return DanmuMsg.class;
    }
}
