package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;
import top.ysqorz.forum.dao.PostMapper;
import top.ysqorz.forum.dto.*;
import top.ysqorz.forum.po.Label;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.service.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-05-24 23:29
 */
@Service
public class PostServiceImpl implements PostService {

    @Resource
    private PostMapper postMapper;

    @Resource
    private PostLabelService postLabelService;

    @Resource
    private TopicService topicService;

    @Resource
    private LabelService labelService;

    @Resource
    private UserService userService;

    @Transactional
    @Override
    public void changeHighQuality(Integer userId, Integer postId, Boolean isHighQuality) {
        // 奖励积分 或者 撤销积分。TODO 到时候需要从配置中读取奖励的积分值
        userService.updateRewardPoints(userId, isHighQuality ? 5 : -5);
        // 更新精品状态
        Post post = new Post();
        post.setId(postId).setIsHighQuality((byte) (isHighQuality ? 1 : 0));
        this.updatePostById(post);
    }

    @Override
    public Post getPostById(Integer postId) {
        return postMapper.selectByPrimaryKey(postId);
    }

    @Override
    public int updatePostById(Post post) {
        return postMapper.updateByPrimaryKeySelective(post);
    }

    @Override
    public List<PostDTO> getPostList(QueryPostCondition condition) {
        return postMapper.selectListByConditions(condition);
    }

    @Override
    public Post addPost(PublishPostDTO vo, Integer creatorId) {
        Post post = new Post();
        LocalDateTime now = LocalDateTime.now();
        post.setCreatorId(creatorId) // 发帖人id
                .setTopicId(vo.getTopicId())  // 所属话题的id
                .setTitle(vo.getTitle()) // 帖子标题
                // 帖子内容，由于全局XSS防护做了转义，此处需要反转义
                .setContent(HtmlUtils.htmlUnescape(vo.getContent()))
                // [0, 3]：表示4种可见性。其中3表示：帖子需要花积分（[4,99]）购买才可见
                // 所以当VisibilityType为3时，干脆直接用VisibilityType来存积分数x
                .setVisibilityType(vo.getVisibilityType() == 3 ? vo.getPoints() : vo.getVisibilityType())
                .setIsLocked((byte) (vo.getIsLocked() ? 1 : 0))


                .setCreateTime(now)
                .setLastModifyTime(now)

                .setLastCommentTime(now)//默认第一次评论时间等于发布时间！！！

                .setCollectCount(0) // 收藏数
                .setCommentCount(0) // 评论数（包括一二级评论）
                .setVisitCount(0) // 访问数
                .setLikeCount(0) // 点赞数

                .setIsHighQuality((byte) 0) // 不是精品帖
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
        // 批量插入帖子和标签的映射关系
        postLabelService.addPostLabelList(post.getId(), vo.splitLabels());
    }

    @Transactional
    @Override
    public void updatePostAndLabels(PublishPostDTO vo) {
        Post post = new Post();
        post.setId(vo.getPostId())
                .setTopicId(vo.getTopicId())
                .setLastModifyTime(LocalDateTime.now()) // 最后一次的更新时间
                .setTitle(vo.getTitle())
                .setContent(HtmlUtils.htmlUnescape(vo.getContent())) // XSS防护做了转义，此处需要反转义
                .setVisibilityType(vo.getVisibilityType() == 3 ?
                        vo.getPoints() : vo.getVisibilityType())
                .setIsLocked((byte) (vo.getIsLocked() ? 1 : 0));
        this.updatePostById(post);

        // 先把当与前帖子相关标签的映射关系全部删除
        postLabelService.delPostLabelByPostId(vo.getPostId());

        // 批量插入帖子和标签的映射关系
        postLabelService.addPostLabelList(vo.getPostId(), vo.splitLabels());
    }

    @Override
    public PostDetailDTO getPostDetailById(Post post) {
        // 发帖者
        SimpleUserDTO creator = userService.getSimpleUser(post.getCreatorId());
        // 所属话题
        Topic topic = topicService.getTopicById(post.getTopicId());
        // 相关标签
        List<Label> labelList = labelService.getLabelsByPostId(post.getId());
        PostDTO postDTO = new PostDTO(post, creator, topic, labelList);

        // 我是否已经点赞

        // 我是否已经收藏

        PostDetailDTO postDetailDTO = new PostDetailDTO();
        postDetailDTO.setDetail(postDTO);


        return null;
    }

}
