package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import top.ysqorz.forum.dao.TopicMapper;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.service.TopicService;

import javax.annotation.Resource;

/**
 * @author passerbyYSQ
 * @create 2021-05-24 23:23
 */
@Service
public class TopicServiceImpl implements TopicService {

    @Resource
    private TopicMapper topicMapper;

    @Override
    public Topic getTopicById(Integer topicId) {
        return topicMapper.selectByPrimaryKey(topicId);
    }
}
