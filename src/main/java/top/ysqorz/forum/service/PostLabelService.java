package top.ysqorz.forum.service;

import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2021-06-12 15:29
 */
public interface PostLabelService {

    /**
     * 删除帖子的所有标签
     */
    int delPostLabelByPostId(Integer postId);


    /**
     * 插入帖子和标签的映射关系
     */
    int addPostLabelList(Integer postId, Set<String> labelSet);

}
