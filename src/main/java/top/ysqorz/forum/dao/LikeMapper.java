package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.resp.PostDetailDTO;
import top.ysqorz.forum.po.Like;

import java.util.List;

public interface LikeMapper extends BaseMapper<Like> {
    /**
     * 获取帖子的点赞列表
     */
    List<PostDetailDTO.Liker> selectLikerListByPostId(Integer postId, Integer count);
}
