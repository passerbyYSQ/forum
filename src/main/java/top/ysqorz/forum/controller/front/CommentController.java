package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.exception.ServiceFailedException;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.req.PublishFirstCommentDTO;
import top.ysqorz.forum.dto.resp.FirstCommentDTO;
import top.ysqorz.forum.dto.resp.RecentCommentUserDTO;
import top.ysqorz.forum.dto.resp.SecondCommentDTO;
import top.ysqorz.forum.po.FirstComment;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.po.SecondComment;
import top.ysqorz.forum.service.CommentService;
import top.ysqorz.forum.service.PostService;
import top.ysqorz.forum.service.RedisService;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author passerbyYSQ
 * @create 2021-06-20 16:26
 */
@Validated
@Controller
@ResponseBody
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private RedisService redisService;
    @Resource
    private CommentService commentService;
    @Resource
    private PostService postService;

    /**
     * 获取最近发表评论的用户
     */
    @GetMapping("/user/recent")
    public List<RecentCommentUserDTO> recentCommentUser() {
        return commentService.getRecentCommentUsers();
    }

    @PostMapping("/first/publish")
    public StatusCode publishFirstComment(@Validated PublishFirstCommentDTO dto) {
        String correctCaptcha = redisService.getCaptcha(dto.getToken());
        if (ObjectUtils.isEmpty(correctCaptcha)) {
            return StatusCode.CAPTCHA_EXPIRED; // 验证码过期
        }
        if (!dto.getCaptcha().equalsIgnoreCase(correctCaptcha)) {
            return StatusCode.CAPTCHA_INVALID; // 验证码错误
        }
        Post post = postService.getPostById(dto.getPostId());
        if (ObjectUtils.isEmpty(post)) {
            return StatusCode.POST_NOT_EXIST; // 帖子不存在
        }
        if (post.getIsLocked() == 1) {
            return StatusCode.POST_LOCKED; // 帖子锁定，不允许回复
        }
        commentService.publishFirstComment(post, dto.getContent());
        return StatusCode.SUCCESS;
    }

    @PostMapping("/second/publish")
    public StatusCode publishSecondComment(@Validated SecondComment comment) {
        FirstComment firstComment = commentService.getFirstCommentById(comment.getFirstCommentId());
        if (ObjectUtils.isEmpty(firstComment)) {
            return StatusCode.FIRST_COMMENT_NOT_EXIST;
        }
        Post post = postService.getPostById(firstComment.getPostId());
        if (ObjectUtils.isEmpty(post)) {
            return StatusCode.POST_NOT_EXIST; // 帖子不存在
        }
        if (post.getIsLocked() == 1) {
            return StatusCode.POST_LOCKED; // 帖子锁定，不允许回复
        }

        Integer quoteId = comment.getQuoteSecondCommentId();
        SecondComment quoteComment = null;
        if (!ObjectUtils.isEmpty(quoteId)) {
            quoteComment = commentService.getSecondCommentById(quoteId);
            if (ObjectUtils.isEmpty(quoteComment)) {
                return StatusCode.SECOND_COMMENT_NOT_EXIST;
            }
        }
        commentService.publishSecondComment(firstComment, quoteComment, comment.getContent()); // quoteComment 可能为空
        return StatusCode.SUCCESS;
    }

    @GetMapping("/first/list")
    public PageData<FirstCommentDTO> firstCommentList(
            @RequestParam Integer postId, //  /detail 和 /detail/sdv 都会404
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer count,
            @RequestParam(defaultValue = "true") Boolean isTimeAsc) {
        Post post = postService.getPostById(postId);
        if (ObjectUtils.isEmpty(post)) {
            throw new ServiceFailedException(StatusCode.POST_NOT_EXIST);
        }
        return commentService.getFirstCommentList(post, page, Math.max(1, count), isTimeAsc);
    }

    @GetMapping("/second/list")
    public PageData<SecondCommentDTO> secondCommentList(
            @RequestParam Integer firstCommentId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer count) {
        FirstComment firstComment = commentService.getFirstCommentById(firstCommentId);
        if (ObjectUtils.isEmpty(firstComment)) {
            throw new ServiceFailedException(StatusCode.FIRST_COMMENT_NOT_EXIST);
        }
        return commentService.getSecondCommentList(firstComment, page, Math.max(1, count));
    }

    @GetMapping("/first/frontCount")
    public Integer getFrontFirstCommentCount(@NotNull Integer postId, @NotNull Integer firstCommentId) {
        int count = commentService.getFrontFirstCommentCount(postId, firstCommentId);
        if (count == -1) {
            throw new ServiceFailedException(StatusCode.FIRST_COMMENT_NOT_EXIST);
        }
        return count;
    }

    @GetMapping("/second/frontCount")
    public Map<String, Integer> getFrontSecondCommentCount(@NotNull Integer postId, @NotNull Integer secondCommentId) {
        int[] res = commentService.getFrontSecondCommentCount(postId, secondCommentId);
        if (res == null) {
            throw new ServiceFailedException(StatusCode.SECOND_COMMENT_NOT_EXIST);
        }
        Map<String, Integer> data = new HashMap<>();
        data.put("firstCount", res[0]);
        data.put("secondCount", res[1]);
        data.put("firstCommentId", res[2]);
        return data;
    }

    @PostMapping("/delete")
    public StatusCode deleteComment(@NotNull Integer commentId, @NotBlank String type) {
        return commentService.deleteCommentById(commentId, type);
    }

    @GetMapping("/list/{userId}")
    public PageData<FirstCommentDTO> getFirstCommentList(@RequestParam(defaultValue = "3") Integer limit,
                                                         @RequestParam(defaultValue = "1") Integer page,
                                                         @PathVariable Integer userId) {
        return commentService.getFirstCommentListByCreatorId(userId, page, Math.max(1, limit));
    }
}
