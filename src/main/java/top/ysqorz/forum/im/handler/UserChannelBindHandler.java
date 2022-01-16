package top.ysqorz.forum.im.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.po.User;

/**
 * 用户和通道的绑定
 *
 * @author passerbyYSQ
 * @create 2022-01-12 0:58
 */
class UserChannelBindHandler extends MsgHandler { // 包权限

    public UserChannelBindHandler() {
        super(MsgType.BIND);
    }

    @Override
    protected boolean doHandle(MsgModel msg, Channel channel, User loginUser) {
        JsonNode dataNode = msg.getDataNode();
        if (dataNode.has("channelType")) {
            String channelType = dataNode.get("channelType").asText();
            String userId = String.valueOf(loginUser.getId().intValue());
            MsgCenter.getInstance().bind(channelType, userId, channel);
        }
        return true; // 消费完成
    }
}
