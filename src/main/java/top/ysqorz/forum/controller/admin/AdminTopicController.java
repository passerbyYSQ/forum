package top.ysqorz.forum.controller.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.service.TopicService;
import top.ysqorz.forum.vo.PageData;
import top.ysqorz.forum.vo.ResultModel;
import top.ysqorz.forum.vo.StatusCode;
import top.ysqorz.forum.vo.TopicVo;

import javax.validation.constraints.NotEmpty;
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
    public ResultModel<PageData<TopicVo>> getUserAndRole(@RequestParam(defaultValue = "10") Integer limit,
                                                         @RequestParam(defaultValue = "1") Integer page,
                                                         String topicName) {
        if (limit <= 0) {
            limit = 10;
        }
        PageHelper.startPage(page, limit);
        List<TopicVo> allTopic = topicService.getAllTopic(topicName);
        PageInfo<TopicVo> pageinfo = new PageInfo<>(allTopic);
        PageData<TopicVo> pageData = new PageData<>();
        pageData.setList(allTopic);
        pageData.setTotal(pageinfo.getTotal());
        pageData.setPage(pageinfo.getPageNum());
        pageData.setCount(pageinfo.getPageSize());

        return ResultModel.success(pageData);


    }

    /**
     * 删除话题
     */
    @PostMapping("/del")
    public ResultModel delTopic(@RequestParam("topicIds[]") @NotEmpty Integer[] topicIds)
            throws ParameterErrorException {
        topicService.deleteTopic(topicIds);
        return ResultModel.success();
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

}


