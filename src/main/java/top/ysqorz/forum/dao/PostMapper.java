package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.QueryPostCondition;
import top.ysqorz.forum.dto.SimplePostDTO;
import top.ysqorz.forum.po.Post;

import java.util.List;

public interface PostMapper extends BaseMapper<Post> {
    Integer selectCountByTodayTopic(Integer topicId);

    List<SimplePostDTO> selectListByConditions(QueryPostCondition condition);

}
