package top.ysqorz.forum.service;

import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.vo.PublishPostVo;

/**
 * @author passerbyYSQ
 * @create 2021-05-24 23:28
 */
public interface PostService {

    /**
     * 发帖
     * @param vo            相关参数
     * @param creatorId     发帖者的id
     */
    Post addPost(PublishPostVo vo, Integer creatorId);

}
