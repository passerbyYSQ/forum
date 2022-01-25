package top.ysqorz.forum.im.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import org.springframework.util.ObjectUtils;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.utils.JsonUtils;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 用户和通道的绑定
 *
 * @author passerbyYSQ
 * @create 2022-01-12 0:58
 */
public class UserChannelBindHandler extends MsgHandler {

    public UserChannelBindHandler(ThreadPoolExecutor dbExecutor) {
        super(MsgType.BIND, dbExecutor, true);
    }

    @Override
    protected boolean doHandle0(MsgModel msg, Channel channel, User loginUser) {
        JsonNode dataNode = msg.transformToDataNode();
        if (!ObjectUtils.isEmpty(msg.getChannelType())) { // 能来到这，一定是BIND类型且携带了token，dataNode一定不为空
            Object extra = JsonUtils.nodeToObj(dataNode.get("extra"), Object.class);
            String userId = String.valueOf(loginUser.getId().intValue());
            MsgCenter.getInstance().bind(msg.getChannelType(), userId, channel, extra);
        }
        return true; // 消费完成
    }
}
