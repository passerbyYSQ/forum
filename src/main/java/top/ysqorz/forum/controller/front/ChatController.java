package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.resp.ChatFriendApplyDTO;
import top.ysqorz.forum.dto.resp.ChatListDTO;
import top.ysqorz.forum.dto.resp.ChatUserCardDTO;
import top.ysqorz.forum.service.ChatService;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
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
     * 消息盒子的页面，显示好友申请的通知
     */
    @GetMapping("/msgbox")
    public String msgBoxPage() {
        return "front/chat/msgbox";
    }

    /**
     * 查找用户，添加好友。暂不支持状态（在线/离线）条件
     * @param keyword   关键词。手机号，邮箱，用户名
     * @param status    状态。all, online, offline
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

    /**
     * 好友申请的消息通知列表
     */
    @ResponseBody
    @GetMapping("/notification")
    public ResultModel<PageData<ChatFriendApplyDTO>> notification(@RequestParam(defaultValue = "1") Integer page,
                                                              @RequestParam(defaultValue = "8") Integer count) {
        return ResultModel.success(chatService.getFriendApplyNotifications(page, count));
    }

    /**
     * 处理好友申请
     * @param action    agree, refuse, ignore
     */
    @ResponseBody
    @PostMapping("/apply/process")
    public ResultModel processFriendApply(@NotNull Integer friendApplyId, Integer friendGroupId, // agree时才需要
                                          @NotNull String action) {
        return ResultModel.wrap(chatService.processFriendApply(friendApplyId, friendGroupId, action));
    }

    /**
     * 签收好友申请的通知
     * @param friendApplyIds    用逗号分隔。形如：1,2,3
     */
    @ResponseBody
    @PostMapping("/apply/sign")
    public ResultModel signNotification(@NotBlank String friendApplyIds) {
        chatService.signFriendApplyNotifications(friendApplyIds);
        return ResultModel.success();
    }

    /**
     * 查询在线聊天相关的列表信息
     */
    @ResponseBody
    @GetMapping("/list")
    public ResultModel<ChatListDTO> chatList() {
        ResultModel<ChatListDTO> result = ResultModel.success(chatService.getChatList());
        result.setCode(0); // 前端LayIM的限制
        return result;
    }
}
