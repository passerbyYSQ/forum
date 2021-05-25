package top.ysqorz.forum.service;

import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.vo.TopicVo;

import java.util.List;

/**
 * @author 阿灿
 * @create 2021-05-24 20:12
 */
public interface TopicService {
    /**
     * 获取所有topic
     */
    List<TopicVo> getAllTopic(String topicName);

    /**
     * 修改topic
     */
    int updateTopic(Topic topic);

    /**
     * 删除topic
     */
    void deleteTopic(Integer[] topicIds) throws ParameterErrorException;

    /**
     * 删除topic下的所有帖子
     *
     * @param topicId
     */
    void delTopicAllPosts(Integer topicId);

    /**
     * 添加topic
     */
    Topic addTopic(Topic topic);

    /**
     * 根据id获取Topic信息
     *
     * @param topicId
     * @return
     */

    Topic getTopicById(Integer topicId);
}
