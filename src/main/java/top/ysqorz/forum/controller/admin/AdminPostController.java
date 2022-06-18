package top.ysqorz.forum.controller.admin;

import com.github.pagehelper.PageHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.req.QueryPostCondition;
import top.ysqorz.forum.dto.resp.PostDTO;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.service.PostService;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author passerbyYSQ
 * @create 2021-06-07 10:28
 */
@Controller
@ResponseBody
@RequestMapping("/admin/system/post")
public class AdminPostController {
    @Resource
    private PostService postService;

    @RequiresPermissions("post:view")
    @GetMapping("/list")
    public PageData<PostDTO> postList(
            @RequestParam(defaultValue = "1") Integer page, // 当前页
            @RequestParam(defaultValue = "10") Integer count, // 每一页显示条数
            QueryPostCondition condition) { // 查询条件
        if (count < 1) {
            count = 10;
        }
        condition.generateOrderByStr(); // 校验参数，并拼接order by部分的字符串
        PageHelper.startPage(page, count); // 里面会做page的越界纠正
        return new PageData<>(postService.getPostList(condition));
    }

    /**
     * 修改置顶权重
     */
    @RequiresPermissions("post:top")
    @PostMapping("/top")
    public StatusCode updateTopWeight(@NotNull Integer postId,
                                       @NotNull @Min(0) @Max(9999) Integer topWeight) {
        Post post = new Post();
        post.setId(postId).setTopWeight(topWeight);
        int cnt = postService.updatePostById(post);
        return cnt == 1 ? StatusCode.SUCCESS : StatusCode.POST_NOT_EXIST;
    }

    /**
     * 锁定和解锁帖子
     */
    @RequiresPermissions("post:lock")
    @PostMapping("/lock")
    public StatusCode lock(@RequestParam Integer postId, @RequestParam Boolean isLock) {
        Post post = new Post();
        post.setId(postId).setIsLocked((byte) (isLock ? 1 : 0));
        int cnt = postService.updatePostById(post);
        return cnt == 1 ? StatusCode.SUCCESS : StatusCode.POST_NOT_EXIST;
    }

    /**
     * 加精品、取消精品
     */
    @RequiresPermissions("post:quality")
    @PostMapping("/highQuality")
    public StatusCode highQuality(@RequestParam Integer postId,
                                   @RequestParam Boolean isHighQuality) {
        Post post = postService.getPostById(postId);
        if (ObjectUtils.isEmpty(post)) {
            return StatusCode.POST_NOT_EXIST;
        }
        postService.changeHighQuality(post.getCreatorId(), postId, isHighQuality);
        return StatusCode.SUCCESS;
    }
}
