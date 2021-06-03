package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.service.PostService;
import top.ysqorz.forum.service.TopicService;
import top.ysqorz.forum.utils.CaptchaUtils;
import top.ysqorz.forum.dto.PublishPostDTO;
import top.ysqorz.forum.dto.ResultModel;
import top.ysqorz.forum.dto.StatusCode;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

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

    /**
     * 发帖页面
     */
    @GetMapping("/publish")
    public String publishPage(Model model) {
        return "front/jie/publish";
    }

    /**
     * 发帖所需要的验证码图片
     */
    @GetMapping("/captcha")
    public void captchaImage(HttpSession session, HttpServletResponse response)
            throws ServletException, IOException {
        // 生成并往网络输出流输出验证码图片，返回正确的验证码
        String correctCaptcha = CaptchaUtils.generateAndOutput(response);
        // TODO 暂时将验证码存到session，后面存到redis
        session.setAttribute("publishCaptcha", correctCaptcha);
    }

    /**
     * 临时的发帖接口，为了方便往数据库插入数据
     */
    @ResponseBody
    @PostMapping("/publish")
    public ResultModel publish(@Validated PublishPostDTO vo, HttpSession session) {
        // 校验验证码
        String correctCaptcha = (String) session.getAttribute("publishCaptcha");
        // 由于验证码是一次性的，访问过立马失效，不管校验正确与否
        session.removeAttribute("publishCaptcha");

        if (!vo.getCaptcha().equalsIgnoreCase(correctCaptcha)) {
            return ResultModel.failed(StatusCode.CAPTCHA_INVALID); // 验证码错误
        }

        // 校验topicId
        Topic topic = topicService.getTopicById(vo.getTopicId());
        if (ObjectUtils.isEmpty(topic)) {
            return ResultModel.failed(StatusCode.TOPIC_NOT_EXIST); // 话题不存在
        }

        // TODO 由于还没做登录，此处传入一个固定的creatorId
        postService.publishPost(vo, 1);

        return ResultModel.success();
    }

}