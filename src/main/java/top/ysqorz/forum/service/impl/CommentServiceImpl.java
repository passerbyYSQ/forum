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
import top.ysqorz.forum.dao.CommentNotificationMapper;
import top.ysqorz.forum.dao.FirstCommentMapper;
import top.ysqorz.forum.dao.SecondCommentMapper;
import top.ysqorz.forum.dto.PageData;
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
            firstCommentDTO.setContent(content);
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
        // ???????????????????????????????????????????????????count???????????????????????????????????????
        // ???????????????????????????????????????count?????????????????????????????????????????????????????????????????????count?????????
        // https://blog.csdn.net/weixin_46244732/article/details/122498257
        Map<String, Optional<RecentCommentUserDTO>> collectedMap = total.stream()
                .collect(
                        Collectors.groupingBy(RecentCommentUserDTO::getUserId, Collectors.maxBy(ascComparator))
                );
        return collectedMap.values().stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                // ??????
                .sorted((user1, user2) -> user2.getLastCommentTime().compareTo(user1.getLastCommentTime()))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public int getFrontFirstCommentCount(Integer postId, Integer firstCommentId) {
        FirstComment firstComment = firstCommentMapper.selectByPrimaryKey(firstCommentId);
        if (ObjectUtils.isEmpty(firstComment) || !postId.equals(firstComment.getPostId())) {
            return -1; // firstCommentId??????
        }
        Example example = new Example(FirstComment.class);
        example.createCriteria().andEqualTo("postId", firstComment.getPostId())
                .andLessThan("createTime", firstComment.getCreateTime());
        return firstCommentMapper.selectCountByExample(example);
    }

    @Override
    public int[] getFrontSecondCommentCount(Integer postId, Integer secondCommentId) {
        SecondComment secondComment = secondCommentMapper.selectByPrimaryKey(secondCommentId);
        if (ObjectUtils.isEmpty(secondComment)) {
            return null;
        }
        Integer firstCommentId = secondComment.getFirstCommentId();
        int firstCount = this.getFrontFirstCommentCount(postId, firstCommentId);
        if (firstCount == -1) {
            return null; // secondCommentId??????
        }
        Example example = new Example(SecondComment.class);
        example.createCriteria().andEqualTo("firstCommentId", firstCommentId)
                .andLessThan("createTime", secondComment.getCreateTime());
        int secondCount = secondCommentMapper.selectCountByExample(example);
        return new int[] {firstCount, secondCount, firstCommentId};
    }


    @Transactional
    @Override
    public void publishFirstComment(Post post, String content) {
        /*
        // ??????????????????????????????FloorNum???????????????????????????????????????????????????FloorNum?????????
        FirstComment comment = new FirstComment();
        comment.setPostId(postId)
                .setContent(HtmlUtils.htmlUnescape(content))
                .setUserId(creatorId)
                //.setFloorNum()
                .setCreateTime(LocalDateTime.now())
                .setSecondCommentCount(0);
         */
        // ?????????????????????????????????creator
        Integer creatorId = ShiroUtils.getUserId();
        // ??????????????????
        FirstComment comment = new FirstComment();
        comment.setPostId(post.getId())
                .setContent(HtmlUtils.htmlUnescape(content))
                .setUserId(creatorId);
        // comment ????????????????????????id
        firstCommentMapper.addFirstCommentUseGeneratedKeys(comment);

        // ?????????????????????????????????????????????
        postService.updateCommentCountAndLastTime(post.getId(), 1);

        if (!creatorId.equals(post.getCreatorId())) {
            // ??????????????????
            CommentNotification notification = new CommentNotification();
            notification.setSenderId(creatorId)
                    .setReceiverId(post.getCreatorId())
                    // ???????????????0????????????????????????1???????????????????????????2????????????????????????
                    .setCommentType((byte) 0) // ?????????????????????????????????
                    // ????????????id????????????????????????????????????????????????????????????????????????????????????
                    .setRepliedId(post.getId())
                    // ?????????????????????????????????????????????????????????????????????
                    .setCommentId(comment.getId())
                    .setCreateTime(LocalDateTime.now())
                    .setIsRead((byte) 0);
            commentNotificationMapper.insertUseGeneratedKeys(notification);

            // ????????????
            rewardPointsAction.publishComment();
        }
    }

    @Transactional
    @Override
    public void publishSecondComment(FirstComment firstComment,
                                     SecondComment quoteComment,
                                     String content) {
        Integer myId = ShiroUtils.getUserId();
        Integer receiverId = firstComment.getUserId();
        byte commentType = 1;
        Integer repliedId = firstComment.getId();

        // ??????????????????
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

        // ???????????????????????????
        postService.updateCommentCountAndLastTime(firstComment.getPostId(), 1);

        // ?????????????????????????????????????????????
        this.addSecondCommentCount(firstComment.getId(), 1);

        // ????????????
        if (!myId.equals(receiverId)) {
            // ??????????????????
            CommentNotification notification = new CommentNotification();
            notification.setSenderId(myId)
                    .setReceiverId(receiverId)
                    // ???????????????0????????????????????????1???????????????????????????2????????????????????????
                    .setCommentType(commentType) // ?????????????????????????????????
                    // ????????????id????????????????????????????????????????????????????????????????????????????????????
                    .setRepliedId(repliedId)
                    // ?????????????????????????????????????????????????????????????????????
                    .setCommentId(comment.getId())
                    .setCreateTime(LocalDateTime.now())
                    .setIsRead((byte) 0);
            commentNotificationMapper.insertUseGeneratedKeys(notification);

            // ????????????
            rewardPointsAction.publishComment();
        }
    }

    @Override
    public PageData<FirstCommentDTO> getFirstCommentList(Post post, Integer page, Integer count, Boolean isTimeAsc) {
        PageHelper.startPage(page, count); // ????????????page???????????????

        Map<String, Object> params = new HashMap<>();
        params.put("postId", post.getId());
        params.put("orderByStr", "create_time " + (isTimeAsc ? "asc" : "desc"));
        List<FirstCommentDTO> firstComments = firstCommentMapper.selectFirstCommentList(params);

        for (FirstCommentDTO firstComment : firstComments) {
            boolean isPostCreator = post.getCreatorId().equals(firstComment.getCreator().getId());
            // TODO ??????level
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
        PageHelper.startPage(page, count); // ????????????page???????????????
        List<SecondCommentDTO> secondCommentList = secondCommentMapper.selectSecondCommentList(firstComment.getId());
        Post post = postService.getPostById(firstComment.getPostId());
        for (SecondCommentDTO secondComment : secondCommentList) {
            // TODO ??????level??????????????????
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
    public int addSecondCommentCount(Integer firstCommentId, Integer dif) {
        Map<String, Object> params = new HashMap<>();
        params.put("firstCommentId", firstCommentId);
        params.put("dif", dif);
        return firstCommentMapper.addSecondCommentCount(params);
    }

    @Transactional
    @Override
    public StatusCode deleteCommentById(Integer commentId, String type) {
        StatusCode code = StatusCode.SUCCESS;
        if ("FIRST_COMMENT".equals(type)) {
            FirstComment firstComment = this.getFirstCommentById(commentId);
            if (ObjectUtils.isEmpty(firstComment)) {
                return StatusCode.FIRST_COMMENT_NOT_EXIST;
            }
            if (!permManager.allowDelComment(firstComment.getUserId())) {
                return StatusCode.NO_PERM;
            }
            firstCommentMapper.deleteByPrimaryKey(commentId);
            postService.updateCommentCountAndLastTime(firstComment.getPostId(), -1);
            // TODO ????????????
        } else if ("SECOND_COMMENT".equals(type)) {
            SecondComment secondComment = this.getSecondCommentById(commentId);
            if (ObjectUtils.isEmpty(secondComment)) {
                return StatusCode.SECOND_COMMENT_NOT_EXIST;
            }
            if (!permManager.allowDelComment(secondComment.getUserId())) {
                return StatusCode.NO_PERM;
            }
            secondCommentMapper.deleteByPrimaryKey(commentId);
            FirstComment firstComment = this.getFirstCommentById(secondComment.getFirstCommentId());
            this.addSecondCommentCount(firstComment.getId(), -1);
            postService.updateCommentCountAndLastTime(firstComment.getPostId(), -1);
            // TODO ????????????
        } else {
            code = StatusCode.COMMENT_TYPE_INVALID; // type??????
        }
        return code;
    }
}
