package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import top.ysqorz.forum.dao.DanmuMsgMapper;
import top.ysqorz.forum.po.DanmuMsg;
import top.ysqorz.forum.service.DanmuService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2022-01-16 17:08
 */
@Service
public class DanmuServiceImpl implements DanmuService {
    @Resource
    private DanmuMsgMapper danmuMsgMapper;

    @Override
    public List<DanmuMsg> getDanmuListByVideoId(Integer videoId) {
        DanmuMsg record = new DanmuMsg();
        record.setVideoId(videoId);
        return danmuMsgMapper.select(record);
    }
}
