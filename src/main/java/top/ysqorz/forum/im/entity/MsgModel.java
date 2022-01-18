package top.ysqorz.forum.im.entity;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * @author passerbyYSQ
 * @create 2021-02-05 22:31
 */
@Data
public class MsgModel {
    // 消息类型
    private String type;
    // 数据
    private Object data;

    public MsgModel(MsgType type) {
        this(type, null);
    }

    public MsgModel(MsgType type, Object data) {
        this(type.name(), data);
    }

    public MsgModel(String type, Object data) {
        this.type = type;
        this.data = data;
    }

    public JsonNode getDataNode() {
        return data instanceof JsonNode ? (JsonNode) data : null;
    }
}
