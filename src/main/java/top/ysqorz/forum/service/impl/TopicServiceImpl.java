package top.ysqorz.forum.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.dao.PostMapper;
import top.ysqorz.forum.dao.TopicMapper;
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


    @Override
    public Topic addTopic(Topic topic) {
        topic.setCreateTime(LocalDateTime.now());
        topic.setCreateId(1);
        topic.setPostCount(0);
        topic.setArchive((byte) 0);
        topicMapper.insertUseGeneratedKeys(topic);
        return topic;
    }

    @Override
    public int archive(Integer id, Integer state) {
        Topic record = new Topic();
        Example example = new Example(Topic.class);
        example.createCriteria().andEqualTo("id", id);
        if (state == 1) {
            record.setArchive((byte) 1);
            return topicMapper.updateByExampleSelective(record, example);
        } else {
            record.setArchive((byte) 0);
            return topicMapper.updateByExampleSelective(record, example);
        }
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
