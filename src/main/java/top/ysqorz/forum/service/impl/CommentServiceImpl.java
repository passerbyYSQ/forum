package top.ysqorz.forum.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;
import top.ysqorz.forum.dao.CommentNotificationMapper;
import top.ysqorz.forum.dao.FirstCommentMapper;
import top.ysqorz.forum.dao.SecondCommentMapper;
import top.ysqorz.forum.dto.FirstCommentDTO;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.SecondCommentDTO;
import top.ysqorz.forum.po.CommentNotification;
import top.ysqorz.forum.po.FirstComment;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.po.SecondComment;
import top.ysqorz.forum.service.CommentService;
import top.ysqorz.forum.service.PostService;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Transactional
    @Override
    public void publishFirstComment(Post post, String content, Integer creatorId) {
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
        // 插入一级评论
        FirstComment comment = new FirstComment();
        comment.setPostId(post.getId())
                .setContent(HtmlUtils.htmlUnescape(content))
                .setUserId(creatorId);
        // comment 会被设置自增长的id
        firstCommentMapper.addFirstCommentUseGeneratedKeys(comment);

        // 更新评论数量（包括一级、二级）
        postService.addCommentCount(post.getId(), 1);

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
        }
    }

    @Transactional
    @Override
    public void publishSecondComment(FirstComment firstComment,
                                     SecondComment quoteComment,
                                     String content, Integer myId) {
        Integer receiverId = firstComment.getUserId();
        Byte commentType = 1;
        Integer repliedId = firstComment.getId();

        // 插入二级评论
        SecondComment comment = new SecondComment();
        comment.setContent(HtmlUtils.htmlUnescape(content))
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
        postService.addCommentCount(firstComment.getPostId(), 1);

        // 更新一级评论下的二级评论的数量
        this.addSecondCommentCount(firstComment.getId(), 1);

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
        }
    }

    @Override
    public PageData<FirstCommentDTO> getFirstCommentList(Post post,
                                                         Integer page, Integer count, Boolean isTimeAsc) {
        PageHelper.startPage(page, count); // 里面会做page的越界纠正

        Map<String, Object> params = new HashMap<>();
        params.put("postId", post.getId());
        params.put("orderByStr", "create_time " + (isTimeAsc ? "asc" : "desc"));
        List<FirstCommentDTO> firstComments = firstCommentMapper.selectFirstCommentList(params);

        for (FirstCommentDTO firstComment : firstComments) {
            boolean isPostCreator = post.getCreatorId().equals(firstComment.getCreator().getId());
            // TODO 计算level
            firstComment.getCreator().setLevel(0);
            firstComment.setIsPostCreator(isPostCreator);
        }

        PageInfo<FirstCommentDTO> pageInfo = new PageInfo<>(firstComments);
        return new PageData<>(pageInfo, firstComments);
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
        for (SecondCommentDTO secondComment : secondCommentList) {
            // TODO 计算level，是否是楼主
            secondComment.getCreator().setLevel(0);
        }
        PageInfo<SecondCommentDTO> pageInfo = new PageInfo<>(secondCommentList);
        return new PageData<>(pageInfo, secondCommentList);
    }

    @Override
    public SecondComment getSecondCommentById(Integer secondCommentId) {
        return secondCommentMapper.selectByPrimaryKey(secondCommentId);
    }

    @Override
    public int addSecondCommentCount(Integer firstCommentId, Integer dif) {
        Map<String, Object> params = new HashMap<>();
        params.put("firstCommentId", firstCommentId);
        params.put("dif", dif);
        return firstCommentMapper.addSecondCommentCount(params);
    }
}
