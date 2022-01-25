package top.ysqorz.forum.im.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * @author passerbyYSQ
 * @create 2021-02-05 22:31
 */
@Data
public class MsgModel {
    // 消息类型
    private String msgType;
    // 通道类型
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 只用于反序列化(json -> obj)
    private String channelType;
    // 数据
    private Object data;

    public MsgModel(MsgType msgType, Object data) {
        this(msgType.name(), null, data);
    }

    public MsgModel(String msgType, String channelType, Object data) {
        this.msgType = msgType;
        this.channelType = channelType;
        this.data = data;
    }

    // 不要以getXx开头，否则objectMapper在序列化为json的时候会认为是一个属性
    public JsonNode transformToDataNode() {
        return data instanceof JsonNode ? (JsonNode) data : null;
    }
}
