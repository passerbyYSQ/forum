package top.ysqorz.forum.im.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import org.springframework.util.ObjectUtils;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户和通道的绑定
 *
 * @author passerbyYSQ
 * @create 2022-01-12 0:58
 */
public class BindMsgHandler extends AbstractMsgHandler<MsgModel> {
    private final BindEventCallback callback;

    public BindMsgHandler(BindEventCallback callback) {
        super(MsgType.BIND);
        this.callback = callback;
    }

    @Override
    protected boolean doHandle(MsgModel msg, MsgModel data, Channel channel) {
        JsonNode dataNode = data.transform2DataNode(); // 能来到这，一定是BIND类型且携带了token，dataNode一定不为空
        if (ObjectUtils.isEmpty(data.getChannelType()) || !dataNode.has("groupId")) {
            return true;
        }
        String groupId = dataNode.get("groupId").asText();
        ChannelType channelType = ChannelType.valueOf(data.getChannelType());
        String token = dataNode.get("token").asText();
        if (Objects.nonNull(callback)) {
            callback.bind(channelType, token, groupId, channel);
        }
        // 回送channelId
        Map<String, String> result = new HashMap<>();
        result.put("channelId", channel.id().asLongText());
        channel.writeAndFlush(IMUtils.createTextFrame(MsgType.BIND, result));
        return true; // 消费完成
    }

    @Override
    protected boolean doPush(MsgModel data, String sourceChannelId) {
        return false;
    }

    @Override
    public Class<MsgModel> getDataClass() {
        return MsgModel.class;
    }
}
