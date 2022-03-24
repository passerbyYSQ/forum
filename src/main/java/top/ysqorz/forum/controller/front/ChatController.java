package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author passerbyYSQ
 * @create 2022-03-24 17:04
 */
@Controller
@RequestMapping("/user/chat")
public class ChatController {

    @GetMapping("")
    public String chatPage() {
        return "front/user/chat";
    }
}
