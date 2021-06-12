package top.ysqorz.forum.service;

import top.ysqorz.forum.dto.PublishPostDTO;
import top.ysqorz.forum.dto.QueryPostCondition;
import top.ysqorz.forum.dto.SimplePostDTO;
import top.ysqorz.forum.po.Post;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-05-24 23:28
 */
public interface PostService {

    /**
     * 修改帖子精品状态。涉及积分操作
     */
    void changeHighQuality(Integer userId, Integer postId, Boolean isHighQuality);

    /**
     * 根据id查询帖子
     */
    Post getPostById(Integer postId);

    /**
     * 根据id选择性更新post（只更新post中非null的字段）
     */
    int updatePostById(Post post);

    /**
     * 后台帖子管理查询帖子列表
     * @param condition     查询条件
     */
    List<SimplePostDTO> getPostList(QueryPostCondition condition);

    /**
     * 往post表插入数据
     * @param vo            相关参数
     * @param creatorId     发帖者的id
     */
    Post addPost(PublishPostDTO vo, Integer creatorId);

    /**
     * 发布帖子。包含：Post，Label，PostLabel多表操作
     */
    void publishPost(PublishPostDTO vo, Integer creatorId);


    /**
     * 更新帖子。包括标签
     */
    void updatePostAndLabels(PublishPostDTO vo);
}
