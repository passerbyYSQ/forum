package top.ysqorz.forum.im.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import org.springframework.util.ObjectUtils;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.po.User;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 用户和通道的绑定
 *
 * @author passerbyYSQ
 * @create 2022-01-12 0:58
 */
public class UserChannelBindHandler extends MsgHandler<MsgModel> {

    public UserChannelBindHandler(ThreadPoolExecutor dbExecutor) {
        super(MsgType.BIND, dbExecutor, true);
    }

    @Override
    protected MsgModel transformData(MsgModel msg, Integer userId) {
        return msg; // must return msg, or doHandle0 do not invoke
    }

    @Override
    protected boolean doHandle0(MsgModel msg, Channel channel, User loginUser) {
        JsonNode dataNode = msg.transformToDataNode(); // 能来到这，一定是BIND类型且携带了token，dataNode一定不为空
        if (ObjectUtils.isEmpty(msg.getChannelType()) || !dataNode.has("groupId")) {
            return true;
        }
        String groupId = dataNode.get("groupId").asText();
        MsgCenter.getInstance().bind(msg.getChannelType(), loginUser.getId(), groupId, channel);
        // 回送channelId
        Map<String, String> data = new HashMap<>();
        data.put("channelId", channel.id().asLongText());
        channel.writeAndFlush(IMUtils.createTextFrame(MsgType.BIND, data));
        return true; // 消费完成
    }
}
