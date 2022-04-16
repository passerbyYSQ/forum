package top.ysqorz.forum.service;

import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.*;
import top.ysqorz.forum.dto.req.PublishPostDTO;
import top.ysqorz.forum.dto.req.QueryPostCondition;
import top.ysqorz.forum.dto.resp.PostDTO;
import top.ysqorz.forum.dto.resp.PostDetailDTO;
import top.ysqorz.forum.po.Like;
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
     * 删除帖子，帖子下面相关的其他内容也会删除
     */
    StatusCode deletePostById(Integer postId);

    /**
     * 后台帖子管理查询帖子列表
     * @param condition     查询条件
     */
    List<PostDTO> getPostList(QueryPostCondition condition);

    /**
     * 往post表插入数据
     * @param vo            相关参数
     */
    Post addPost(PublishPostDTO vo);

    /**
     * 发布帖子。包含：Post，Label，PostLabel多表操作
     */
    Post publishPost(PublishPostDTO vo);

    /**
     * 更新帖子。包括标签
     */
    Post updatePostAndLabels(PublishPostDTO vo);

    PostDetailDTO getPostDetailById(Post post);

    /**
     * 帖子访问量 +1。考虑刷访问量
     * @param ipAddress
     */
    Post addVisitCount(String ipAddress, Post post);

    int updateCommentCountAndLastTime(Integer postId, Integer dif);

    //
    Like addLike(Integer postId);

    int cancelLike(Integer likeId, Integer postId);

    int addLikeCount(Integer postId, Integer dif);

    /**
     * index返回的帖子信息
     */
    PageData<PostDTO> getIndexPost(Integer page, Integer count, QueryPostCondition conditions);

    /**
     * 查询指定用户发布的帖子，在个人主页中显示
     */
    PageData<PostDTO> getPostListByCreatorId(Integer userId, Integer page, Integer count);
}
