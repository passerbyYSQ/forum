package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author passerbyYSQ
 * @create 2022-03-24 17:04
 */
@Controller
@RequestMapping("/chat")
public class ChatController {

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
}
