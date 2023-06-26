package top.ysqorz.forum.service.impl;

import com.github.pagehelper.PageHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.enumeration.CommentType;
import top.ysqorz.forum.common.enumeration.OrderMode;
import top.ysqorz.forum.common.exception.ServiceFailedException;
import top.ysqorz.forum.dao.CommentNotificationMapper;
import top.ysqorz.forum.dao.FirstCommentMapper;
import top.ysqorz.forum.dao.SecondCommentMapper;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.req.FirstCommentFrontCountDTO;
import top.ysqorz.forum.dto.req.SecondCommentFrontCountDTO;
import top.ysqorz.forum.dto.resp.FirstCommentDTO;
import top.ysqorz.forum.dto.resp.RecentCommentUserDTO;
import top.ysqorz.forum.dto.resp.SecondCommentDTO;
import top.ysqorz.forum.dto.resp.SimpleUserDTO;
import top.ysqorz.forum.po.CommentNotification;
import top.ysqorz.forum.po.FirstComment;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.po.SecondComment;
import top.ysqorz.forum.service.CommentService;
import top.ysqorz.forum.service.PermManager;
import top.ysqorz.forum.service.PostService;
import top.ysqorz.forum.service.RewardPointsAction;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.CommonUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author passerbyYSQ
 * @create 2021-06-20 16:42
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private FirstCommentMapper firstCommentMapper;
    @Resource
    private SecondCommentMapper secondCommentMapper;
    @Resource
    private CommentNotificationMapper commentNotificationMapper;
    @Resource
    private PostService postService;
    @Resource
    private RewardPointsAction rewardPointsAction;
    @Resource
    private PermManager permManager;

    @Override
    public PageData<FirstCommentDTO> getFirstCommentListByCreatorId(Integer userId, Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<FirstCommentDTO> firstCommentDTOList = firstCommentMapper.selectFirstCommentListByUserId(userId);
        for (FirstCommentDTO firstCommentDTO : firstCommentDTOList) {
            Document doc = Jsoup.parse(firstCommentDTO.getContent());
            String content = doc.text();
            if (content.length() >= 100) {
                content = content.substring(0, 100);
            }
            firstCommentDTO.setContent(HtmlUtils.htmlEscape(content));
        }
        return new PageData<>(firstCommentDTOList);
    }

    @Override
    public List<RecentCommentUserDTO> getRecentCommentUsers() {
        int count = 12;
        List<RecentCommentUserDTO> firstCommentUsers = firstCommentMapper.selectRecentCommentUsers(count);
        List<RecentCommentUserDTO> secondCommentUsers = secondCommentMapper.selectRecentCommentUsers(count);
        List<RecentCommentUserDTO> total = new ArrayList<>(firstCommentUsers);
        total.addAll(secondCommentUsers);
        Comparator<RecentCommentUserDTO> ascComparator = Comparator.comparing(RecentCommentUserDTO::getLastCommentTime);
        // 由于评论分布到两张表中，极端情况下count条记录全部来源与其中一张表
        // 所以两张表分别聚合降序后取count条记录，在内存中合并后再做一次聚合降序，取出前count条记录
        // https://blog.csdn.net/weixin_46244732/article/details/122498257
        Map<String, Optional<RecentCommentUserDTO>> collectedMap = total.stream()
                .collect(
                        Collectors.groupingBy(RecentCommentUserDTO::getUserId, Collectors.maxBy(ascComparator))
                );
        return collectedMap.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                // 降序
                .sorted((user1, user2) -> user2.getLastCommentTime().compareTo(user1.getLastCommentTime()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public int getFrontFirstCommentCount(FirstCommentFrontCountDTO dto) {
        FirstComment firstComment = firstCommentMapper.selectByPrimaryKey(dto.getFirstCommentId());
        if (ObjectUtils.isEmpty(firstComment) || !dto.getPostId().equals(firstComment.getPostId())) {
            throw new ServiceFailedException(StatusCode.FIRST_COMMENT_NOT_EXIST);
        }
        Example example = new Example(FirstComment.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("postId", firstComment.getPostId());
        if (OrderMode.ASC.equals(dto.getFirstCommentOrder())) { // 升序
            criteria.andLessThan("createTime", firstComment.getCreateTime());
        } else { // 降序
            criteria.andGreaterThan("createTime", firstComment.getCreateTime());
        }
        return firstCommentMapper.selectCountByExample(example);
    }

    @Override
    public int[] getFrontSecondCommentCount(SecondCommentFrontCountDTO dto) {
        SecondComment secondComment = secondCommentMapper.selectByPrimaryKey(dto.getSecondCommentId());
        if (ObjectUtils.isEmpty(secondComment)) {
            throw new ServiceFailedException(StatusCode.SECOND_COMMENT_NOT_EXIST);
        }
        Integer firstCommentId = secondComment.getFirstCommentId();
        int firstCount = this.getFrontFirstCommentCount(new FirstCommentFrontCountDTO(dto.getPostId(), firstCommentId, dto.getFirstCommentOrder()));

        Example example = new Example(SecondComment.class);
        Example.Criteria criteria = example.createCriteria().andEqualTo("firstCommentId", firstCommentId);
        if (OrderMode.ASC.equals(dto.getSecondCommentOrder())) {
            criteria.andLessThan("createTime", secondComment.getCreateTime());
        } else {
            criteria.andGreaterThan("createTime", secondComment.getCreateTime());
        }
        int secondCount = secondCommentMapper.selectCountByExample(example);
        return new int[]{firstCount, secondCount, firstCommentId};
    }


    @Transactional
    @Override
    public void publishFirstComment(Post post, String content) {
        /*
        // 这种方式，先必须查出FloorNum，再插入记录。并发情况下，可能导致FloorNum不正确
        FirstComment comment = new FirstComment();
        comment.setPostId(postId)
                .setContent(HtmlUtils.htmlUnescape(content))
                .setUserId(creatorId)
                //.setFloorNum()
                .setCreateTime(LocalDateTime.now())
                .setSecondCommentCount(0);
         */
        // 当前用户就是一级评论的creator
        Integer creatorId = ShiroUtils.getUserId();
        // 富文本被转义了，此处需要还原，前端编辑器一次，全局转换器一次
        String unEscapedText = HtmlUtils.htmlUnescape(HtmlUtils.htmlUnescape(content));
        String cleanContent = CommonUtils.cleanRichText(unEscapedText);
        // 插入一级评论
        FirstComment comment = new FirstComment();
        comment.setPostId(post.getId())
                .setContent(cleanContent)
                .setUserId(creatorId);
        // comment 会被设置自增长的id
        firstCommentMapper.addFirstCommentUseGeneratedKeys(comment);

        // 更新评论数量（包括一级、二级）
        postService.updateCommentCountAndLastTime(post.getId(), 1);

        if (!creatorId.equals(post.getCreatorId())) {
            // 插入评论通知
            CommentNotification notification = new CommentNotification();
            notification.setSenderId(creatorId)
                    .setReceiverId(post.getCreatorId())
                    // 通知类型。0：主题帖被回复，1：一级评论被回复，2：二级评论被回复
                    .setCommentType((byte) 0) // 一级评论是回复主题帖的
                    // 被回复的id（可能是主题帖、一级评论、二级评论，根据评论类型来判断）
                    .setRepliedId(post.getId())
                    // 通知来自于哪条评论（可能是一级评论、二级评论）
                    .setCommentId(comment.getId())
                    .setCreateTime(LocalDateTime.now())
                    .setIsRead((byte) 0);
            commentNotificationMapper.insertUseGeneratedKeys(notification);

            // 奖励积分
            rewardPointsAction.publishComment();
        }
    }

    @Transactional
    @Override
    public void publishSecondComment(FirstComment firstComment, SecondComment quoteComment, String content) {
        Integer myId = ShiroUtils.getUserId();
        Integer receiverId = firstComment.getUserId();
        byte commentType = 1;
        Integer repliedId = firstComment.getId();
        // 富文本被转义了，此处需要还原，前端编辑器一次，全局转换器一次
        String unEscapedText = HtmlUtils.htmlUnescape(HtmlUtils.htmlUnescape(content));
        String cleanContent = CommonUtils.cleanRichText(unEscapedText);
        // 插入二级评论
        SecondComment comment = new SecondComment();
        comment.setContent(cleanContent)
                .setCreateTime(LocalDateTime.now())
                .setFirstCommentId(firstComment.getId())
                .setUserId(myId);
        if (!ObjectUtils.isEmpty(quoteComment)) {
            comment.setQuoteSecondCommentId(quoteComment.getId());
            receiverId = quoteComment.getUserId();
            commentType = 2;
            repliedId = quoteComment.getId();
        }
        secondCommentMapper.insertUseGeneratedKeys(comment);

        // 更新帖子的评论数量
        postService.updateCommentCountAndLastTime(firstComment.getPostId(), 1);

        // 更新一级评论下的二级评论的数量
        this.updateSecondCommentCount(firstComment.getId(), 1);

        // 评论通知
        if (!myId.equals(receiverId)) {
            // 插入评论通知
            CommentNotification notification = new CommentNotification();
            notification.setSenderId(myId)
                    .setReceiverId(receiverId)
                    // 通知类型。0：主题帖被回复，1：一级评论被回复，2：二级评论被回复
                    .setCommentType(commentType) // 一级评论是回复主题帖的
                    // 被回复的id（可能是主题帖、一级评论、二级评论，根据评论类型来判断）
                    .setRepliedId(repliedId)
                    // 通知来自于哪条评论（可能是一级评论、二级评论）
                    .setCommentId(comment.getId())
                    .setCreateTime(LocalDateTime.now())
                    .setIsRead((byte) 0);
            commentNotificationMapper.insertUseGeneratedKeys(notification);

            // 奖励积分
            rewardPointsAction.publishComment();
        }
    }

    @Override
    public PageData<FirstCommentDTO> getFirstCommentList(Post post, Integer page, Integer count, Boolean isTimeAsc) {
        PageHelper.startPage(page, count); // 里面会做page的越界纠正

        Map<String, Object> params = new HashMap<>();
        params.put("postId", post.getId());
        params.put("orderByStr", "create_time " + (isTimeAsc ? "asc" : "desc"));
        List<FirstCommentDTO> firstComments = firstCommentMapper.selectFirstCommentList(params);

        for (FirstCommentDTO firstComment : firstComments) {
            boolean isPostCreator = post.getCreatorId().equals(firstComment.getCreator().getId());
            // TODO 计算level
            firstComment.getCreator().setLevel(6);
            firstComment.setIsPostCreator(isPostCreator);
        }

        return new PageData<>(firstComments);
    }


    @Override
    public FirstComment getFirstCommentById(Integer firstCommentId) {
        return firstCommentMapper.selectByPrimaryKey(firstCommentId);
    }

    @Override
    public PageData<SecondCommentDTO> getSecondCommentList(FirstComment firstComment,
                                                           Integer page, Integer count) {
        PageHelper.startPage(page, count); // 里面会做page的越界纠正
        List<SecondCommentDTO> secondCommentList = secondCommentMapper.selectSecondCommentList(firstComment.getId());
        Post post = postService.getPostById(firstComment.getPostId());
        for (SecondCommentDTO secondComment : secondCommentList) {
            // TODO 计算level，是否是楼主
            SimpleUserDTO secondCreator = secondComment.getCreator();
            secondCreator.setLevel(6);
            secondComment.setIsPostCreator(secondCreator.getId().equals(post.getCreatorId()));
        }
        return new PageData<>(secondCommentList);
    }

    @Override
    public SecondComment getSecondCommentById(Integer secondCommentId) {
        return secondCommentMapper.selectByPrimaryKey(secondCommentId);
    }

    @Override
    public int updateSecondCommentCount(Integer firstCommentId, Integer dif) {
        Map<String, Object> params = new HashMap<>();
        params.put("firstCommentId", firstCommentId);
        params.put("dif", dif);
        return firstCommentMapper.addSecondCommentCount(params);
    }

    @Transactional
    @Override
    public StatusCode deleteCommentById(Integer commentId, CommentType type) {
        StatusCode code = StatusCode.SUCCESS;
        switch (type) {
            case FIRST_COMMENT: {
                FirstComment firstComment = this.getFirstCommentById(commentId);
                if (ObjectUtils.isEmpty(firstComment)) {
                    return StatusCode.FIRST_COMMENT_NOT_EXIST;
                }
                if (!permManager.allowDelComment(firstComment.getUserId())) {
                    return StatusCode.NO_PERM;
                }
                firstCommentMapper.deleteByPrimaryKey(commentId);
                postService.updateCommentCountAndLastTime(firstComment.getPostId(), -1);
                // TODO 积分回退
                break;
            }
            case SECOND_COMMENT: {
                SecondComment secondComment = this.getSecondCommentById(commentId);
                if (ObjectUtils.isEmpty(secondComment)) {
                    return StatusCode.SECOND_COMMENT_NOT_EXIST;
                }
                if (!permManager.allowDelComment(secondComment.getUserId())) {
                    return StatusCode.NO_PERM;
                }
                secondCommentMapper.deleteByPrimaryKey(commentId);
                FirstComment firstComment = this.getFirstCommentById(secondComment.getFirstCommentId());
                this.updateSecondCommentCount(firstComment.getId(), -1);
                postService.updateCommentCountAndLastTime(firstComment.getPostId(), -1);
                // TODO 积分回退
                break;
            }
            default: {
                code = StatusCode.COMMENT_TYPE_INVALID; // type错误
            }
        }
        return code;
    }
}
