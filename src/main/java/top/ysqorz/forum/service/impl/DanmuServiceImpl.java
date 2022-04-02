package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dao.DanmuMsgMapper;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.im.handler.MsgCenter;
import top.ysqorz.forum.po.DanmuMsg;
import top.ysqorz.forum.po.Video;
import top.ysqorz.forum.service.DanmuService;
import top.ysqorz.forum.service.VideoService;
import top.ysqorz.forum.shiro.LoginUser;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.RandomUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2022-01-16 17:08
 */
@Service
public class DanmuServiceImpl implements DanmuService {
    @Resource
    private VideoService videoService;
    @Resource
    private DanmuMsgMapper danmuMsgMapper;

    @Override
    public List<DanmuMsg> getDanmuListByVideoId(Integer videoId) {
        DanmuMsg record = new DanmuMsg();
        record.setVideoId(videoId);
        return danmuMsgMapper.select(record);
    }

    @Override
    public StatusCode sendDanmu(Integer videoId, String content, Long startMs, String sourceChannelId) {
        Video video = videoService.getVideoById(videoId);
        if (ObjectUtils.isEmpty(video)) {
            return StatusCode.VIDEO_NOT_EXIST;
        }
        int endIndex = Math.min(255, content.length());
        String text = content.substring(0, endIndex); // 如果过长只截取前255个字符
        LoginUser loginUser = ShiroUtils.getLoginUser();
        DanmuMsg danmu = new DanmuMsg();
        danmu.setId(RandomUtils.generateUUID())
                .setVideoId(videoId)
                .setContent(text)
                .setCreatorId(ShiroUtils.getUserId())
                .setCreator(new DanmuMsg.DanmuCreator(loginUser))
                .setCreateTime(LocalDateTime.now())
                .setStartMs(Math.max(startMs, 0)); // 负数时做纠正
        MsgModel msg = new MsgModel(MsgType.DANMU, ChannelType.DANMU, danmu);
        MsgCenter.getInstance().remoteDispatch(msg, sourceChannelId);
        return StatusCode.SUCCESS;
    }
}
