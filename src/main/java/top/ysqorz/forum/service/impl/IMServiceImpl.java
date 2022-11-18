package top.ysqorz.forum.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.ysqorz.forum.middleware.ZkConnector;
import top.ysqorz.forum.service.IMService;
import top.ysqorz.forum.service.RedisService;
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
        int index = RandomUtils.generateInt(imServerIps.size());
        String ip = imServerIps.get(index);
        return zkConnector.getData(ZkConnector.PATH + "/" + ip);
    }
}
