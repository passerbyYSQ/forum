package top.ysqorz.forum.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.service.TopicService;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.TopicDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

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

    @GetMapping("/table")
    public ResultModel<PageData<TopicDTO>> getUserAndRole(@RequestParam(defaultValue = "10") Integer limit,
                                                          @RequestParam(defaultValue = "1") Integer page,
                                                          String topicName) {
        if (limit <= 0) {
            limit = 10;
        }
        PageHelper.startPage(page, limit);
        List<TopicDTO> allTopic = topicService.getAllTopic(topicName);
        PageInfo<TopicDTO> pageInfo = new PageInfo<>(allTopic);
        PageData<TopicDTO> pageData = new PageData<>();
        pageData.setList(allTopic);
        pageData.setTotal(pageInfo.getTotal());
        pageData.setPage(pageInfo.getPageNum());
        pageData.setCount(pageInfo.getPageSize());

        return ResultModel.success(pageData);
    }


    /**
     * 添加话题
     */
    @PostMapping("/add")
    public ResultModel<Topic> addTopic(@Validated(Topic.Add.class) Topic topic) {
        return ResultModel.success(topicService.addTopic(topic));
    }

    /**
     * 修改话题
     */
    @PostMapping("/update")
    public ResultModel updateTopic(@Validated(Topic.Update.class) Topic topic) {
        int cnt = topicService.updateTopic(topic);
        return cnt == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode.TOPIC_NOT_EXIST);
    }

    /**
     * 是否归档
     *
     * @param id
     * @param state
     * @return
     */
    @PostMapping("/archive")
    public ResultModel archive(@NotNull Integer id, @NotNull Integer state) {
        int cnt = topicService.archive(id, state);
        return cnt == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode.PARAM_NOT_COMPLETED);
    }


}


