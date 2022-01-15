package top.ysqorz.forum.netty.entity;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author passerbyYSQ
 * @create 2021-02-05 22:31
 */
@Data
@AllArgsConstructor
public class MsgModel {
    // 消息类型
    private String type;
    // 数据
    private Object data;

    public JsonNode getDataNode() {
        return data instanceof JsonNode ? (JsonNode) data : null;
    }
}
