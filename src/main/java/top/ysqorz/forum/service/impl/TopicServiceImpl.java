package top.ysqorz.forum.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.dao.PostMapper;
import top.ysqorz.forum.dao.TopicMapper;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.service.TopicService;
import top.ysqorz.forum.vo.TopicVo;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author 阿灿
 * @create 2021-05-24 20:13
 */
@Service
public class TopicServiceImpl implements TopicService {

    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private PostMapper postMapper;

    @Override
    public List<TopicVo> getAllTopic(String topicName) {
        return topicMapper.getAllTopic(topicName);
    }

    @Override
    public int updateTopic(Topic topic) {
        topic.setCreateId(1);
        return topicMapper.updateByPrimaryKeySelective(topic);
    }

    @Transactional // 开启事务操作
    @Override
    public void deleteTopic(Integer[] topicIds) throws ParameterErrorException {
        for (Integer topicId : topicIds) {
            // 注意删除的先后顺序
            // 先删除topic下所有帖子
            this.delTopicAllPosts(topicId);
            // 再删除topic
            int cnt = topicMapper.deleteByPrimaryKey(topicId);
            if (cnt != 1) { // 只要有一个topic不存在，抛出异常回滚所有操作
                throw new ParameterErrorException("话题不存在");
            }
        }
    }

    @Override
    public void delTopicAllPosts(Integer topicId) {
        Example example = new Example(Post.class);
        example.createCriteria().andEqualTo("topicId", topicId);
        postMapper.deleteByExample(example);
    }

    @Override
    public Topic addTopic(Topic topic) {
        topic.setCreateTime(LocalDateTime.now());
        topic.setCreateId(1);
        topic.setPostCount(0);
        topicMapper.insertUseGeneratedKeys(topic);
        return topic;
    }

    @Override
    public List<Topic> getAllTopic() {
        return topicMapper.selectAll();
    }

    @Override
    public Topic getTopicById(Integer topicId) {
        return topicMapper.selectByPrimaryKey(topicId);
    }
    @Override
    public int updatePostCountById(Integer topicId, Integer cnt) {
        Map<String, Object> params = new HashMap<>();
        params.put("topicId", topicId);
        params.put("cnt", cnt);
        return topicMapper.updatePostCountById(params);
    }
}
