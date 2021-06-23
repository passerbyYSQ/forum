package top.ysqorz.forum.service;

import top.ysqorz.forum.po.Like;

/**
 * @author passerbyYSQ
 * @create 2021-06-19 13:43
 */
public interface LikeService {

    // 某用户某帖子的点赞
    Like getLikeByUserIdAndPostId(Integer userId, Integer postId);

}
