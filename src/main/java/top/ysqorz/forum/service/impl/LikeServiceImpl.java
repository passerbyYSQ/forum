package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.dao.LikeMapper;
import top.ysqorz.forum.po.Like;
import top.ysqorz.forum.service.LikeService;

import javax.annotation.Resource;

/**
 * @author passerbyYSQ
 * @create 2021-06-19 13:46
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Resource
    private LikeMapper likeMapper;

    // 循环依赖
//    @Resource
//    private PostService postService;

    @Override
    public Like getLikeByUserIdAndPostId(Integer userId, Integer postId) {
        Example example = new Example(Like.class);
        example.createCriteria()
                .andEqualTo("userId", userId)
                .andEqualTo("postId", postId);
        return likeMapper.selectOneByExample(example);
    }

}
