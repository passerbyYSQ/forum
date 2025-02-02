package top.ysqorz.forum.service.impl;

import cn.hutool.core.util.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.FakeChannel;
import top.ysqorz.forum.im.handler.MsgCenterImpl;
import top.ysqorz.forum.middleware.ZkConnector;
import top.ysqorz.forum.service.IMService;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.shiro.ShiroUtils;

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
    private ZkConnector<?, ?> zkConnector;
    @Resource
    private RedisService redisService;

    @Override
    public List<String> getIMServerIpList() {
        return zkConnector.getChildrenData(ZkConnector.PATH); // warning ???
    }

    @Override
    public String getRandWsServerUrl() {
        List<String> imServerIps = this.getIMServerIpList();
        int index = RandomUtil.randomInt(imServerIps.size());
        String ip = imServerIps.get(index);
        return zkConnector.getData(ZkConnector.PATH + "/" + ip);
    }

    @Override
    public void handleMsg(MsgModel msg, String sourceChannelId) {
        MsgCenterImpl.getInstance().handle(msg, new FakeChannel(sourceChannelId, ShiroUtils.getToken()));
    }
}
