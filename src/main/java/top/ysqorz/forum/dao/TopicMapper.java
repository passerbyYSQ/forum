package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.vo.TopicVo;

import java.util.List;

public interface TopicMapper extends BaseMapper<Topic> {
    List<TopicVo> getAllTopic(String topicName);
}