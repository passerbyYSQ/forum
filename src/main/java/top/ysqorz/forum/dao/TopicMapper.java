package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.vo.TopicVo;

import java.util.List;
import java.util.Map;

public interface TopicMapper extends BaseMapper<Topic> {

    /**
     * （自增或自减）更新某个话题的贴子数（可传入负数）
     * 自增或自减，不能用tk.mapper。自己写SQL
     */
    int updatePostCountById(Map<String, Object> params);

    List<TopicVo> getAllTopic(String topicName);
}
