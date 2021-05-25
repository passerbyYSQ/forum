package top.ysqorz.forum.service;

import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.vo.PublishPostVo;

/**
 * @author passerbyYSQ
 * @create 2021-05-24 23:28
 */
public interface PostService {

    /**
     * 往post表插入数据
     * @param vo            相关参数
     * @param creatorId     发帖者的id
     */
    Post addPost(PublishPostVo vo, Integer creatorId);

    /**
     * 发布帖子。包含：Post，Label，PostLabel多表操作
     */
    void publishPost(PublishPostVo vo, Integer creatorId);


}
