package top.ysqorz.forum.service;

import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.vo.TopicVo;

import java.util.List;

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
     * 获取所有话题（不分表，不联表）
     */
    List<Topic> getAllTopic();

    /**
     * 根据id获取话题
     */
    Topic getTopicById(Integer topicId);

    /**
     * （自增或自减）更新某个话题的贴子数（可传入负数）
     * 自增或自减，不能用tk.mapper。自己写SQL
     */
    int updatePostCountById(Integer topicId, Integer cnt);


    /*
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
}
