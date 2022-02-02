package top.ysqorz.forum.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.im.handler.MsgCenter;
import top.ysqorz.forum.middleware.ZkConnector;
import top.ysqorz.forum.service.DanmuService;
import top.ysqorz.forum.service.IMService;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.IpUtils;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.OkHttpUtils;
import top.ysqorz.forum.utils.RandomUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2022-02-02 10:07
 */
@Service
@Slf4j
public class IMServiceImpl implements IMService {
    @Resource
    private ZkConnector zkConnector;
    @Resource
    private RedisService redisService;
    @Resource
    private DanmuService danmuService;

    @Override
    public List<String> getIMServerIpList() {
        return zkConnector.getChildrenData(ZkConnector.PATH); // warning ???
    }

    @Override
    public String getRandWsServerUrl() {
        List<String> imServerIps = this.getIMServerIpList();
        int index = RandomUtils.generateInt(imServerIps.size());
        String ip = imServerIps.get(index);
        return zkConnector.getData(ZkConnector.PATH + "/" + ip);
    }

    @Override
    public StatusCode forwardMsg(String msgJson, String channelId) {
        MsgModel msg = JsonUtils.jsonToObj(msgJson, MsgModel.class);
        if (msg == null || !msg.check()) {
            return StatusCode.PARAM_INVALID;
        }
        if (MsgType.isFunctionalType(msg.getMsgType())) {
            return StatusCode.NOT_SUPPORT_FUNC_TYPE;
        }
        Integer userId = ShiroUtils.getUserId();
        String wsServer = redisService.getUserWs(userId);
        String localServer = IpUtils.getLocalIp();
        if (localServer.equals(wsServer)) { // channel在当前服务上
            Channel channel = MsgCenter.getInstance().findChannel(msg.getChannelType(), userId, channelId);
            if (channel == null) {
                return StatusCode.CHANNEL_NOT_EXIST;
            }
            MsgCenter.getInstance().handle(msg, channel);
            return StatusCode.SUCCESS;
        } else {
            try {
                return remoteForwardMsg(wsServer, msgJson, channelId, 1);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return StatusCode.REMOTE_FORWARD_MSG_FAILED;
            }
        }
    }

    private StatusCode remoteForwardMsg(String wsServer, String msgJson, String channelId, int tryCount) throws InterruptedException {
        if (tryCount > 3) { // 重试超过3次，则将消息存到数据库，返回成功
            MsgCenter.getInstance().save(JsonUtils.jsonToObj(msgJson, MsgModel.class), ShiroUtils.getUserId());
            return StatusCode.SUCCESS;
        }
        String api = String.format("http://%s:8080/im/send", wsServer);
        String body = OkHttpUtils.builder().url(api)
                .addHeader("token", ShiroUtils.getToken())
                .addParam("msgJson", msgJson)
                .addParam("channelId", channelId)
                .post(false).sync();
        JsonNode resNode = JsonUtils.jsonToNode(body);
        if (resNode != null && resNode.get("code").asInt() == StatusCode.SUCCESS.getCode()) {
            return StatusCode.SUCCESS;
        } else {
            // 递归重试
            Thread.sleep(1000);
            return remoteForwardMsg(wsServer, msgJson, channelId, tryCount + 1);
        }
    }
}
