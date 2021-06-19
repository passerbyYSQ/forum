package top.ysqorz.forum.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.*;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.service.PostService;

import javax.annotation.Resource;
import java.util.List;

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

    @GetMapping("/list")
    public ResultModel<PageData<PostDTO>> postList(
            @RequestParam(defaultValue = "1") Integer page, // 当前页
            @RequestParam(defaultValue = "10") Integer count, // 每一页显示条数
            QueryPostCondition condition) { // 查询条件
        if (count < 1) {
            count = 10;
        }
        condition.generateOrderByStr(); // 校验参数，并拼接order by部分的字符串
        PageHelper.startPage(page, count); // 里面会做page的越界纠正
        List<PostDTO> postList = postService.getPostList(condition);
        PageInfo<PostDTO> pageInfo = new PageInfo<>(postList);
        PageData<PostDTO> pageData = new PageData<>(pageInfo, postList);
        return ResultModel.success(pageData);
    }

    /**
     * 修改置顶权重
     */
    @PostMapping("/top")
    public ResultModel updateTopWeight(@RequestParam("id") Integer postId,
                                       @RequestParam(defaultValue = "0") Integer topWeight) {
        Post post = new Post();
        post.setId(postId).setTopWeight(topWeight);
        int cnt = postService.updatePostById(post);
        return cnt == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode.POST_NOT_EXIST); // 帖子不存在
    }

    /**
     * 锁定和解锁帖子
     */
    @PostMapping("/lock")
    public ResultModel lock(@RequestParam Integer postId, @RequestParam Boolean isLock) {
        Post post = new Post();
        post.setId(postId).setIsLocked((byte) (isLock ? 1 : 0));
        int cnt = postService.updatePostById(post);
        return cnt == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode.POST_NOT_EXIST); // 帖子不存在
    }

    /**
     * 加精品、取消精品
     */
    @PostMapping("/highQuality")
    public ResultModel highQuality(@RequestParam Integer postId,
                                   @RequestParam Boolean isHighQuality) {
        Post post = postService.getPostById(postId);
        if (ObjectUtils.isEmpty(post)) {
            return ResultModel.failed(StatusCode.POST_NOT_EXIST); // 帖子不存在
        }
        postService.changeHighQuality(post.getCreatorId(), postId, isHighQuality);
        return ResultModel.success();
    }

}
