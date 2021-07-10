package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.MessageListDTO;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.service.MessageService;

import javax.annotation.Resource;

/**
 * @author 阿灿
 * @create 2021-07-02 16:01
 */
@Controller
@RequestMapping("/meg")
public class MessageController {
    @Resource
    private MessageService messageService;

    /**
     * 跳转到消息页面
     *
     * @param model
     * @return
     */
    @GetMapping("")
    public String index(Model model) {

        return "front/user/message";
    }

    /**
     * 获取消息列表
     *
     * @param limit
     * @param page
     * @param conditions
     */
    @ResponseBody
    @GetMapping("/list")
    public ResultModel<PageData<MessageListDTO>> megList(@RequestParam(defaultValue = "3") Integer limit,
                                                         @RequestParam(defaultValue = "1") Integer page,
                                                         Integer conditions) {
        if (limit <= 0) {
            limit = 3;
        }

        PageData<MessageListDTO> megList = messageService.getMegList(page, limit, conditions);
        return ResultModel.success(megList);
    }

    /**
     * 清空所有消息
     */
    @ResponseBody
    @GetMapping("/clearAll")
    public ResultModel clearAllMeg() {

        int i = messageService.clearAllMeg();
        return i >= 0 ? ResultModel.success() : ResultModel.failed(StatusCode.UNKNOWN_ERROR);
    }


}
