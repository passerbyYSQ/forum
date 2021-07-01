package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.dto.IndexUserDTO;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.PostDTO;
import top.ysqorz.forum.dto.QueryPostCondition;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.service.PostService;
import top.ysqorz.forum.service.TopicService;
import top.ysqorz.forum.service.UserService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-05-22 23:52
 */
@Controller
@Validated
public class IndexController {

    @Resource
    private PostService postService;
    @Resource
    private UserService userService;
    @Resource
    private TopicService topicService;

    // 注意不要这么写 {"/", "/index"}，这样写 /admin 访问不了，要 /admin/ 才能访问
    @GetMapping({"", "/index"})
    public String index(Model model) {
        IndexUserDTO shiroUser = userService.getShiroUser();
        model.addAttribute("myUser", shiroUser);
        List<Topic> allTopic = topicService.getTopicByHot();
        model.addAttribute("Topics", allTopic);
        return "front/index";
    }

    @GetMapping("/head")
    public String head(Model model) {
        IndexUserDTO shiroUser = userService.getShiroUser();
        model.addAttribute("myUser", shiroUser);
        return "front/head";
    }

    /**
     * 获取Post列表
     */
    @ResponseBody
    @GetMapping("/index/list")
    public ResultModel<PageData<PostDTO>> getPostList(@RequestParam(defaultValue = "10") Integer limit,
                                                      @RequestParam(defaultValue = "1") Integer page,
                                                      QueryPostCondition conditions) {
        if (limit <= 0) {
            limit = 10;
        }

        PageData<PostDTO> indexPost = postService.getIndexPost(page, limit, conditions);
        return ResultModel.success(indexPost);
    }

}
