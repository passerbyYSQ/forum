package top.ysqorz.forum.controller.front;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.PostDetailDTO;
import top.ysqorz.forum.dto.PublishPostDTO;
import top.ysqorz.forum.dto.UpdatePostDTO;
import top.ysqorz.forum.po.*;
import top.ysqorz.forum.service.*;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.IpUtils;
import top.ysqorz.forum.utils.RandomUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 帖子相关的接口
 * @author passerbyYSQ
 * @create 2021-05-23 23:42
 */
@Controller
@RequestMapping("/post")
@Validated // 与shiro权限注解冲突，导致页面转发404
public class PostController {

    @Resource
    private TopicService topicService;
    @Resource
    private PostService postService;
    @Resource
    private LabelService labelService;
    @Resource
    private RedisService redisService;
    @Resource
    private LikeService likeService;
    @Resource
    private CollectService collectService;
    @Resource
    private UserService userService;
    @Resource
    private PermManager permManager;

    @GetMapping("/detail/{postId}")
    public String detailPage(@PathVariable Integer postId,
                             Model model, HttpServletRequest request) {
        Post post = postService.getPostById(postId);
        if (ObjectUtils.isEmpty(post)) {
            throw new ParameterErrorException("帖子不存在");
        }

        // 更新访问量。注意放在 getPostById 之前。因为 PostDetailDTO 里面的数据复用post的访问量
        post = postService.addVisitCount(IpUtils.getIpAddress(request), post);

        // 用于验证码缓存和校验。植入到页面的登录页面的隐藏表单元素中
        String token = RandomUtils.generateUUID();
        model.addAttribute("token", token);

        PostDetailDTO postDetailDTO = postService.getPostDetailById(post);
        model.addAttribute("post", postDetailDTO);

        //获取用户信息
        User creator = userService.getUserById(post.getCreatorId());
        model.addAttribute("creator", creator);

        //用于检验用户是否登录
        //判断进入用户界面状态，1：未登录， 2：已登录，身份为本人， 3：已登录，身份为访客
        boolean isLogin = ShiroUtils.isAuthenticated();
        boolean isMyself = isLogin && ShiroUtils.getUserId().equals(creator.getId());
        model.addAttribute("isLogin", isLogin);
        model.addAttribute("isMyself", isMyself);
        boolean isFocus = userService.isFocusOn(creator.getId());
        model.addAttribute("isFocusOn", isFocus);

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
            if (!permManager.allowUpdatePost(post.getCreatorId())) {
                throw new AuthorizationException();
            }

            UpdatePostDTO updatePostDTO = new UpdatePostDTO(post);
            List<Label> labels = labelService.getLabelsByPostId(postId);
            updatePostDTO.setLabelList(labels);

            model.addAttribute("post", updatePostDTO);
        }

        return "front/jie/publish";
    }

    @PostMapping("/delete")
    @ResponseBody
    public ResultModel deletePost(@NotNull Integer postId) {
        return ResultModel.wrap(postService.deletePostById(postId));
    }

    /**
     * 发帖
     */
    @ResponseBody
    @PostMapping("/publish")
    public ResultModel<Post> publish(@Validated(PublishPostDTO.Add.class) PublishPostDTO dto) {
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
        if (topic.getArchive() == 1) {
            return ResultModel.failed(StatusCode.TOPIC_ARCHIVED); // 话题已归档
        }

        Post post = postService.publishPost(dto);
        return ResultModel.success(post);
    }

    @ResponseBody
    @PostMapping("/update")
    public ResultModel<Post> updatePost(@Validated(PublishPostDTO.Update.class) PublishPostDTO dto) {
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
        if (!(post.getCreatorId().equals(ShiroUtils.getUserId())
            || ShiroUtils.hasPerm("post:update"))) {
            throw new AuthorizationException();
        }

        // 校验topicId
        Topic topic = topicService.getTopicById(dto.getTopicId());
        if (ObjectUtils.isEmpty(topic)) {
            return ResultModel.failed(StatusCode.TOPIC_NOT_EXIST); // 话题不存在
        }
        if (topic.getArchive() == 1) {
            return ResultModel.failed(StatusCode.TOPIC_ARCHIVED); // 话题已归档
        }

        // 更新帖子
        post = postService.updatePostAndLabels(dto);

        return ResultModel.success(post);
    }

    @PostMapping("/like")
    @ResponseBody
    public ResultModel likePost(@RequestParam Integer postId,
                                @RequestParam Boolean isLike) {
        Integer myId = ShiroUtils.getUserId();
        Like like = likeService.getLikeByUserIdAndPostId(myId, postId);
        // like != null && isLike = true ：不要重复点赞
        // like == null && isLike = false ：不要重复取消
        if (isLike.equals(!ObjectUtils.isEmpty(like))) { // 勿重复操作
            return ResultModel.failed(StatusCode.DO_NOT_REPEAT_OPERATE);
        }
        if (isLike) {
            postService.addLike(postId);
        } else {
            postService.cancelLike(like.getId(), postId); // likeId是可靠的
        }
        return ResultModel.success();
    }

    @PostMapping("/collect")
    @ResponseBody
    public ResultModel collectPost(@RequestParam Integer postId,
                                   @RequestParam Boolean isCollect) {
        Integer myId = ShiroUtils.getUserId();
        Collect collect = collectService.getCollectByUserIdAndPostId(myId, postId);
        if (isCollect.equals(!ObjectUtils.isEmpty(collect))) {
            return ResultModel.failed(StatusCode.DO_NOT_REPEAT_OPERATE);
        }
        if (isCollect) {
            collectService.addCollect(postId);
        } else {
            collectService.cancelCollect(collect.getId(), postId); // collectId是可靠的
        }
        return ResultModel.success();
    }

    /**
     * 根据标签名模糊匹配标签s，不分页
     */
    @GetMapping("/label/like")
    @ResponseBody
    public ResultModel<List<Label>> getLabelsLikeName(String name,  // 可以不传，在service层判断了
                                                      @RequestParam(defaultValue = "1") Integer maxCount) { // 可以不传，有默认值

        return ResultModel.success(labelService.getLabelsLikeName(name, maxCount));
    }
}
