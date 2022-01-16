package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.po.DanmuMsg;
import top.ysqorz.forum.service.DanmuService;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2022-01-16 16:14
 */
@Controller
@RequestMapping("/danmu")
@ResponseBody
@Validated
public class DanmuController {
    @Resource
    private DanmuService danmuService;

    @GetMapping("/{videoId}")
    public ResultModel<List<DanmuMsg>> getDanmuList(@PathVariable @NotNull Integer videoId) {
        // TODO check video id
        List<DanmuMsg> danmuList = danmuService.getDanmuListByVideoId(videoId);
        return ResultModel.success(danmuList);
    }
}
