package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.service.TopicService;
import top.ysqorz.forum.dto.ResultModel;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-05-25 20:53
 */
@Controller
@ResponseBody
@RequestMapping("/topic")
public class TopicController {

    @Resource
    private TopicService topicService;

    /**
     * 不分页获取所有话题
     */
    @GetMapping("/all")
    public ResultModel<List<Topic>> allTopic() {
        return ResultModel.success(topicService.getAllTopic());
    }
}
