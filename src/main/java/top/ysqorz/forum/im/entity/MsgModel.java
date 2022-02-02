package top.ysqorz.forum.im.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import top.ysqorz.forum.utils.JsonUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author passerbyYSQ
 * @create 2021-02-05 22:31
 */
@Data
@NoArgsConstructor
public class MsgModel {
    // 消息类型
    @NotEmpty
    private String msgType;

    // 通道类型
    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // 只用于反序列化(json -> obj)
    private String channelType;

    // 数据
    @NotNull
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
        return data instanceof JsonNode ? (JsonNode) data : JsonUtils.objToNode(data);
    }

    public boolean check() {
        return !(ObjectUtils.isEmpty(msgType) || ObjectUtils.isEmpty(channelType) || ObjectUtils.isEmpty(data));
    }
}
