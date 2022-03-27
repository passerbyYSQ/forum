package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.resp.ChatUserCardDTO;
import top.ysqorz.forum.service.ChatService;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author passerbyYSQ
 * @create 2022-03-24 17:04
 */
@Validated
@Controller
@RequestMapping("/chat")
public class ChatController {
    @Resource
    private ChatService chatService;

    /**
     * 个人中心私聊页面
     */
    @GetMapping("")
    public String chatPage() {
        return "front/user/chat";
    }

    /**
     * 查找用户和群聊的页面
     */
    @GetMapping("/find")
    public String findPage() {
        return "front/chat/find";
    }

    /**
     * 查找用户，添加好友。暂不支持状态（在线/离线）条件
     * @param keyword   关键词。手机号，邮箱，用户名
     * @param status    状态。all，online，offline
     */
    @ResponseBody
    @GetMapping("/search/user")
    public ResultModel<PageData<ChatUserCardDTO>> searchUser(
            @RequestParam(defaultValue = "") String keyword, @RequestParam(defaultValue = "all") String status,
             @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "8") Integer count) {
        return ResultModel.success(chatService.getChatUserCards(keyword, status, page, count));
    }

    /**
     * 申请添加好友
     */
    @ResponseBody
    @PostMapping("/friend/apply")
    public ResultModel applyFiend(@NotNull Integer receiverId, @NotNull Integer friendGroupId,
                                  @RequestParam(defaultValue = "") String content) {
        return ResultModel.wrap(chatService.applyFriend(receiverId, friendGroupId, content));
    }
}
