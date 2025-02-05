package top.ysqorz.forum.controller.ai;

import lombok.Data;

import java.util.List;

/**
 * 通用的AI对话请求参数
 *
 * @author yaoshiquan
 * @date 2025/2/5
 */
@Data
public class ZhiChatReq {
    // 模型ID
    private String model;
    // 对话信息
    private List<ZhipuChatMessage> messages;
    // 是否是流式对话
    private boolean stream = false;
    // 控制生成的响应的最大 token 数量，默认值：动态计算（默认情况下，max_tokens的值会根据上下文长度减去输入长度来自动计算）
    private int max_tokens = 2048;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ZhiChatReq req = new ZhiChatReq();

        public Builder model(String model) {
            req.model = model;
            return this;
        }

        public Builder messages(List<ZhipuChatMessage> messages) {
            req.messages = messages;
            return this;
        }

        public Builder stream(boolean stream) {
            req.stream = stream;
            return this;
        }

        public Builder max_tokens(int max_tokens) {
            req.max_tokens = max_tokens;
            return this;
        }

        public ZhiChatReq build() {
            assert req.model != null;
            assert req.messages != null && !req.messages.isEmpty();
            return req;
        }
    }
}
