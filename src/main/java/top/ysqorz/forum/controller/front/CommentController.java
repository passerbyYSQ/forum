package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.resp.FirstCommentDTO;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.req.PublishFirstCommentDTO;
import top.ysqorz.forum.dto.resp.SecondCommentDTO;
import top.ysqorz.forum.dto.resp.RecentCommentUserDTO;
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
    public ResultModel<List<RecentCommentUserDTO>> recentCommentUser() {
        return ResultModel.success(commentService.getRecentCommentUsers());
    }

    @PostMapping("/first/publish")
    public ResultModel publishFirstComment(@Validated PublishFirstCommentDTO dto) {
        String correctCaptcha = redisService.getCaptcha(dto.getToken());
        if (ObjectUtils.isEmpty(correctCaptcha)) {
            return ResultModel.failed(StatusCode.CAPTCHA_EXPIRED); // 验证码过期
        }
        if (!dto.getCaptcha().equalsIgnoreCase(correctCaptcha)) {
            return ResultModel.failed(StatusCode.CAPTCHA_INVALID); // 验证码错误
        }
        Post post = postService.getPostById(dto.getPostId());
        if (ObjectUtils.isEmpty(post)) {
            return ResultModel.failed(StatusCode.POST_NOT_EXIST); // 帖子不存在
        }
        if (post.getIsLocked() == 1) {
            return ResultModel.failed(StatusCode.POST_LOCKED); // 帖子锁定，不允许回复
        }
        commentService.publishFirstComment(post, dto.getContent());
        return ResultModel.success();
    }

    @PostMapping("/second/publish")
    public ResultModel publishSecondComment(@Validated SecondComment comment) {
        FirstComment firstComment = commentService.getFirstCommentById(comment.getFirstCommentId());
        if (ObjectUtils.isEmpty(firstComment)) {
            return ResultModel.failed(StatusCode.FIRST_COMMENT_NOT_EXIST);
        }
        Post post = postService.getPostById(firstComment.getPostId());
        if (ObjectUtils.isEmpty(post)) {
            return ResultModel.failed(StatusCode.POST_NOT_EXIST); // 帖子不存在
        }
        if (post.getIsLocked() == 1) {
            return ResultModel.failed(StatusCode.POST_LOCKED); // 帖子锁定，不允许回复
        }

        Integer quoteId = comment.getQuoteSecondCommentId();
        SecondComment quoteComment = null;
        if (!ObjectUtils.isEmpty(quoteId)) {
            quoteComment = commentService.getSecondCommentById(quoteId);
            if (ObjectUtils.isEmpty(quoteComment)) {
                return ResultModel.failed(StatusCode.SECOND_COMMENT_NOT_EXIST);
            }
        }
        commentService.publishSecondComment(firstComment, quoteComment,
                comment.getContent());  // quoteComment 可能为空
        return ResultModel.success();
    }

    @GetMapping("/first/list")
    public ResultModel<PageData<FirstCommentDTO>> firstCommentList(
            @RequestParam Integer postId, //  /detail 和 /detail/sdv 都会404
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer count,
            @RequestParam(defaultValue = "true") Boolean isTimeAsc) {
        Post post = postService.getPostById(postId);
        if (ObjectUtils.isEmpty(post)) {
            return ResultModel.failed(StatusCode.POST_NOT_EXIST); // 帖子存在
        }
        if (count < 1) {
            count = 1;
        }
        PageData<FirstCommentDTO> commentList = commentService.getFirstCommentList(post, page, count, isTimeAsc);
        return ResultModel.success(commentList);
    }

    @GetMapping("/second/list")
    public ResultModel<PageData<SecondCommentDTO>> secondCommentList(
            @RequestParam Integer firstCommentId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer count) {
        FirstComment firstComment = commentService.getFirstCommentById(firstCommentId);
        if (ObjectUtils.isEmpty(firstComment)) {
            return ResultModel.failed(StatusCode.FIRST_COMMENT_NOT_EXIST); // 一级评论不存在
        }
        if (count < 1) {
            count = 1;
        }
        PageData<SecondCommentDTO> secondCommentList = commentService.getSecondCommentList(firstComment, page, count);
        return ResultModel.success(secondCommentList);
    }

    @GetMapping("/first/frontCount")
    public ResultModel<Integer> getFrontFirstCommentCount(@NotNull Integer postId, @NotNull Integer firstCommentId) {
        int count = commentService.getFrontFirstCommentCount(postId, firstCommentId);
        return count != -1 ? ResultModel.success(count) : ResultModel.failed(StatusCode.FIRST_COMMENT_NOT_EXIST);
    }

    @GetMapping("/second/frontCount")
    public ResultModel<Map<String, Integer>> getFrontSecondCommentCount(@NotNull Integer postId, @NotNull Integer secondCommentId) {
        int[] res = commentService.getFrontSecondCommentCount(postId, secondCommentId);
        if (res == null) {
            return ResultModel.failed(StatusCode.SECOND_COMMENT_NOT_EXIST);
        }
        Map<String, Integer> data = new HashMap<>();
        data.put("firstCount", res[0]);
        data.put("secondCount", res[1]);
        data.put("firstCommentId", res[2]);
        return ResultModel.success(data);
    }

    @PostMapping("/delete")
    public ResultModel deleteComment(@NotNull Integer commentId, @NotBlank String type) {
        return ResultModel.wrap(commentService.deleteCommentById(commentId, type));
    }

    @GetMapping("/list/{userId}")
    public ResultModel<PageData<FirstCommentDTO>> getFirstCommentList(@RequestParam(defaultValue = "3") Integer limit,
                                                                      @RequestParam(defaultValue = "1") Integer page,
                                                                      @PathVariable Integer userId) {
        if (limit <= 0) {
            limit = 10;
        }
        PageData<FirstCommentDTO> comments = commentService.getFirstCommentListByCreatorId(userId, page, limit);
        return ResultModel.success(comments);
    }
}
