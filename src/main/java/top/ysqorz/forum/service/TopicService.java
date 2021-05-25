package top.ysqorz.forum.service;

import top.ysqorz.forum.po.Topic;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-05-24 23:19
 */
public interface TopicService {

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


}
