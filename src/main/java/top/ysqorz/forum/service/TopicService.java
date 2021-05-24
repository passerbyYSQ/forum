package top.ysqorz.forum.service;

import top.ysqorz.forum.po.Topic;

/**
 * @author passerbyYSQ
 * @create 2021-05-24 23:19
 */
public interface TopicService {

    Topic getTopicById(Integer topicId);

}
