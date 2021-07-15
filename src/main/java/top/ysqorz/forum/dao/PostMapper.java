package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.QueryPostCondition;
import top.ysqorz.forum.dto.PostDTO;
import top.ysqorz.forum.po.Post;

import java.util.List;
import java.util.Map;

public interface PostMapper extends BaseMapper<Post> {
    Integer selectCountByTodayTopic(Integer topicId);

    List<PostDTO> selectListByConditions(QueryPostCondition condition);

    void addVisitCount(Integer postId);

    int addLikeCount(Map<String, Object> params);

    int updateCommentCountAndLastTime(Map<String, Object> params);

    int addCollectCount(Map<String, Object> params);

    List<PostDTO> selectListByCreatorId(Integer creatorId);
}
