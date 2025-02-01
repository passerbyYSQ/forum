package top.ysqorz.forum.im.handler;

import cn.hutool.json.JSONObject;
import io.netty.channel.Channel;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;
import top.ysqorz.forum.common.RestRequest;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.service.IMService;
import top.ysqorz.forum.utils.SpringUtils;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2025-02-02 0:44
 */
public abstract class RemoteMsgHandlerProxy<DataType> extends AbstractMsgHandler<DataType> {
    public RemoteMsgHandlerProxy(MsgType msgType, ChannelType channelType) {
        super(msgType, channelType);
    }

    @Override
    protected boolean doHandle(MsgModel msg, DataType data, Channel channel) {
        String token = IMUtils.getTokenFromChannel(channel);
        return remoteDispatch(msg, channel, token);
    }

    protected boolean remoteDispatch(MsgModel msg, Channel sourceChannel, String token) {
        DataType data = getDataClass().cast(msg.getData());
        Set<String> servers = queryServersChannelLocated(data);
        if (ObjectUtils.isEmpty(servers)) {
            return true;
        }
        IMService imService = SpringUtils.getBean(IMService.class);
        Set<String> imServers = new HashSet<>(imService.getIMServerIpList()); // web servers
        // 分发到各个服务器，然后进行推送
        //log.info("Msg: {}", JsonUtils.objToJson(msg));
        //log.info("Channel Located Servers: {}", servers);
        //log.info("IM Servers: {}", imServers);
        for (String server : servers) {
            if (imServers.contains(server)) { // 如果服务是正常，才转发
                String api = String.format("http://%s/im/push%s", server, IMUtils.getWebContextPath());
                RestRequest restRequest = RestRequest.builder().url(api)
                        .addHeader(HttpHeaders.AUTHORIZATION, token)
                        .body(msg);
                if (Objects.nonNull(sourceChannel)) {
                    String sourceChannelId = sourceChannel.id().asLongText();
                    restRequest.addParam("channelId", sourceChannelId);
                }
                restRequest.post(JSONObject.class);
            }
        }
        return true;
    }

    protected abstract Set<String> queryServersChannelLocated(DataType data);
}
