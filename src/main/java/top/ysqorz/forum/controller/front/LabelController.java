package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.po.Label;
import top.ysqorz.forum.service.LabelService;
import top.ysqorz.forum.vo.ResultModel;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-05-28 21:31
 */
@Controller
@ResponseBody
@RequestMapping("/label")
public class LabelController {

    @Resource
    private LabelService labelService;

    /**
     * 根据标签名模糊匹配标签s，不分页
     */
    @GetMapping("/like")
    public ResultModel<List<Label>> getLabelsLikeName(String name,  // 可以不传，在service层判断了
           @RequestParam(defaultValue = "1") Integer maxCount) { // 可以不传，有默认值
        return ResultModel.success(labelService.getLabelsLikeName(name, maxCount));
    }

}
