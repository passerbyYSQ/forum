package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;
import top.ysqorz.forum.dao.PostLabelMapper;
import top.ysqorz.forum.dao.PostMapper;
import top.ysqorz.forum.po.Label;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.po.PostLabel;
import top.ysqorz.forum.service.LabelService;
import top.ysqorz.forum.service.PostService;
import top.ysqorz.forum.service.TopicService;
import top.ysqorz.forum.dto.PublishPostDTO;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2021-05-24 23:29
 */
@Service
public class PostServiceImpl implements PostService {

    @Resource
    private PostMapper postMapper;

    @Resource
    private PostLabelMapper postLabelMapper;

    @Resource
    private TopicService topicService;

    @Resource
    private LabelService labelService;

    @Override
    public Post addPost(PublishPostDTO vo, Integer creatorId) {
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

        // 对应的话题下面的帖子数 +1
        topicService.updatePostCountById(vo.getTopicId(), 1);

        return post;
    }

    @Transactional // 开启事务
    @Override
    public void publishPost(PublishPostDTO vo, Integer creatorId) {
        // 注意要用this对象，否则调用的不是被AOP代理后的对象的方法
        Post post = this.addPost(vo, creatorId);

        // postId --- labelId 的映射关系
        List<PostLabel> postLabels = new ArrayList<>();
        Set<String> labelSet = vo.splitLabels();
        for (String name : labelSet) {
            Label label = labelService.getLabelByName(name);
            if (ObjectUtils.isEmpty(label)) { // 标签不存在，就创建
                label = labelService.addLabel(name); // 自增长id被设置进去了
            }
            if (!ObjectUtils.isEmpty(label.getId())) {
                postLabels.add(new PostLabel(post.getId(), label.getId()));
            }
        }

        if (!ObjectUtils.isEmpty(postLabels)) {
            // 批量插入帖子和标签的映射关系
            postLabelMapper.insertList(postLabels);
        }
    }

}
