package top.ysqorz.forum.controller.admin;

import com.github.pagehelper.PageHelper;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.resp.TopicDTO;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.service.TopicService;

import javax.validation.constraints.NotNull;

/**
 * @author 阿灿
 * @create 2021-05-24 20:11
 */
@RestController
@RequestMapping("/admin/system/topic")
public class AdminTopicController {
    @Autowired
    private TopicService topicService;

    /**
     * 获取Topic数据
     */
    @RequiresPermissions("topic:view")
    @GetMapping("/table")
    public PageData<TopicDTO> getUserAndRole(@RequestParam(defaultValue = "10") Integer limit,
                                             @RequestParam(defaultValue = "1") Integer page,
                                             String topicName) {
        PageHelper.startPage(page, Math.max(1, limit));
        return new PageData<>(topicService.getAllTopic(topicName));
    }


    /**
     * 添加话题
     */
    @RequiresPermissions("topic:add")
    @PostMapping("/add")
    public Topic addTopic(@Validated(Topic.Add.class) Topic topic) {
        return topicService.addTopic(topic);
    }

    /**
     * 修改话题
     */
    @RequiresPermissions("topic:update")
    @PostMapping("/update")
    public StatusCode updateTopic(@Validated(Topic.Update.class) Topic topic) {
        int cnt = topicService.updateTopic(topic);
        return cnt == 1 ? StatusCode.SUCCESS : StatusCode.TOPIC_NOT_EXIST;
    }

    /**
     * 是否归档
     *
     * @param id
     * @param state
     * @return
     */
    @RequiresPermissions("topic:archive")
    @PostMapping("/archive")
    public StatusCode archive(@NotNull Integer id, @NotNull Integer state) {
        int cnt = topicService.archive(id, state);
        return cnt == 1 ? StatusCode.SUCCESS : StatusCode.PARAM_NOT_COMPLETED;
    }
}
