package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import top.ysqorz.forum.dao.PostMapper;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.service.PostService;
import top.ysqorz.forum.vo.PublishPostVo;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author passerbyYSQ
 * @create 2021-05-24 23:29
 */
@Service
public class PostServiceImpl implements PostService {

    @Resource
    private PostMapper postMapper;

    @Override
    public Post addPost(PublishPostVo vo, Integer creatorId) {
        Post post = new Post();
        post.setCreatorId(creatorId) // 发帖人id
                .setTopicId(vo.getTopicId())  // 所属话题的id
                .setTitle(vo.getTitle()) // 帖子标题
                // 帖子内容，由于全局XSS防护做了转义，此处需要反转义
                .setContent(HtmlUtils.htmlUnescape(vo.getContent()))
                // [0, 3]：表示4种可见性。其中3表示：帖子需要花积分（[4,99]）购买才可见
                // 所以当VisibilityType为3时，干脆直接用VisibilityType来存积分数x
                .setVisibilityType(vo.getVisibilityType() == 3 ? vo.getPoints() : vo.getVisibilityType())

                .setCreateTime(LocalDateTime.now())
                .setLastModifyTime(LocalDateTime.now())

                .setCollectCount(0) // 收藏数
                .setCommentCount(0) // 评论数（包括一二级评论）
                .setVisitCount(0) // 访问数
                .setLikeCount(0) // 点赞数

                .setIsHightQuality((byte) 0) // 不是精品帖
                .setTopWeight(0); // 置顶权重，0：不置顶

        postMapper.insertUseGeneratedKeys(post);
        return post;
    }

}
