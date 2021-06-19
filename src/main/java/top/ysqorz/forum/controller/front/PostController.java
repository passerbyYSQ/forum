package top.ysqorz.forum.controller.front;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.dto.PublishPostDTO;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.UpdatePostDTO;
import top.ysqorz.forum.po.Label;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.service.LabelService;
import top.ysqorz.forum.service.PostService;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.service.TopicService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.RandomUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 帖子相关的接口
 * @author passerbyYSQ
 * @create 2021-05-23 23:42
 */
@Controller
@RequestMapping("/post")
@Validated
public class PostController {

    @Resource
    private TopicService topicService;

    @Resource
    private PostService postService;

    @Resource
    private LabelService labelService;

    @Resource
    private RedisService redisService;

    @GetMapping("/detail/{postId}")
    public String detailPage(@PathVariable Integer postId, Model model) { //  /detail 和 /detail/sdv 都会404
        // 用于验证码缓存和校验。植入到页面的登录页面的隐藏表单元素中
        String token = RandomUtils.generateUUID();
        model.addAttribute("token", token);

        Post post = postService.getPostById(postId);
        if (ObjectUtils.isEmpty(post)) {
            throw new ParameterErrorException("帖子不存在");
        }

        postService.getPostDetailById(post);

        return "front/jie/detail";
    }

    /**
     * 发帖页面
     */
    @GetMapping("/publish")
    public String publishPage(Integer postId, Model model) {
        // 用于验证码缓存和校验。植入到页面的登录页面的隐藏表单元素中
        String token = RandomUtils.generateUUID();
        model.addAttribute("token", token);

        if (!ObjectUtils.isEmpty(postId)) { // 修改的时候需要传值
            Post post = postService.getPostById(postId);
            if (ObjectUtils.isEmpty(post)) {
                throw new ParameterErrorException("帖子不存在");
            }
            // 不能修改其他人的帖子
            if (!post.getCreatorId().equals(ShiroUtils.getUserId())) {
                throw new AuthorizationException();
            }

            UpdatePostDTO updatePostDTO = new UpdatePostDTO(post);
            List<Label> labels = labelService.getLabelsByPostId(postId);
            updatePostDTO.setLabelList(labels);

            model.addAttribute("post", updatePostDTO);
        }

        return "front/jie/publish";
    }

    /**
     * 发帖
     */
    @ResponseBody
    @PostMapping("/publish")
    public ResultModel publish(@Validated(PublishPostDTO.Add.class) PublishPostDTO dto) {
        String correctCaptcha = redisService.getCaptcha(dto.getToken());
        if (ObjectUtils.isEmpty(correctCaptcha)) {
            return ResultModel.failed(StatusCode.CAPTCHA_EXPIRED); // 验证码过期
        }
        if (!dto.getCaptcha().equalsIgnoreCase(correctCaptcha)) {
            return ResultModel.failed(StatusCode.CAPTCHA_INVALID); // 验证码过期
        }

        // 校验topicId
        Topic topic = topicService.getTopicById(dto.getTopicId());
        if (ObjectUtils.isEmpty(topic)) {
            return ResultModel.failed(StatusCode.TOPIC_NOT_EXIST); // 话题不存在
        }

        postService.publishPost(dto, ShiroUtils.getUserId());

        return ResultModel.success();
    }

    /**
     * 临时的发帖接口，为了方便往数据库插入数据
     */
    @ResponseBody
    @PostMapping("/update")
    public ResultModel updatePost(@Validated(PublishPostDTO.Update.class) PublishPostDTO dto) {
        String correctCaptcha = redisService.getCaptcha(dto.getToken());
        if (ObjectUtils.isEmpty(correctCaptcha)) {
            return ResultModel.failed(StatusCode.CAPTCHA_EXPIRED); // 验证码过期
        }
        if (!dto.getCaptcha().equalsIgnoreCase(correctCaptcha)) {
            return ResultModel.failed(StatusCode.CAPTCHA_INVALID); // 验证码过期
        }

        // 校验postId
        Post post = postService.getPostById(dto.getPostId());
        if (ObjectUtils.isEmpty(post)) {
            return ResultModel.failed(StatusCode.POST_NOT_EXIST); // 帖子不存在
        }
        // 不能修改其他人的帖子
        if (!post.getCreatorId().equals(ShiroUtils.getUserId())) {
            throw new AuthorizationException();
        }

        // 校验topicId
        Topic topic = topicService.getTopicById(dto.getTopicId());
        if (ObjectUtils.isEmpty(topic)) {
            return ResultModel.failed(StatusCode.TOPIC_NOT_EXIST); // 话题不存在
        }

        // 更新帖子
        postService.updatePostAndLabels(dto);

        return ResultModel.success();
    }

}
