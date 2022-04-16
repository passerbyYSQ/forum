package top.ysqorz.forum.service;

import top.ysqorz.forum.dto.resp.PostDetailDTO;
import top.ysqorz.forum.po.Like;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-06-19 13:43
 */
public interface LikeService {

    // 某用户某帖子的点赞
    Like getLikeByUserIdAndPostId(Integer userId, Integer postId);

    /**
     * 获取帖子的点赞列表
     */
    List<PostDetailDTO.Liker> getLikerListByPostId(Integer postId, Integer count);
}
