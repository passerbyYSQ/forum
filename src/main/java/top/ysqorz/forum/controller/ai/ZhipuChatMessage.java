package top.ysqorz.forum.controller.ai;

import lombok.Data;

@Data
public class ZhipuChatMessage {
    private final String role;
    private final String content;

    public ZhipuChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}