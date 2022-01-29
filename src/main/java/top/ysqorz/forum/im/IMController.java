package top.ysqorz.forum.im;

import io.netty.channel.Channel;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.im.handler.MsgCenter;
import top.ysqorz.forum.shiro.ShiroUtils;

import javax.validation.constraints.NotEmpty;

/**
 * @author passerbyYSQ
 * @create 2022-01-29 22:47
 */
@Controller
@ResponseBody
@Validated
@RequestMapping("/im")
public class IMController {

    /**
     * 1. IM服务之间转发业务类型的消息需要调用此接口
     * 2. 客户端使用API发送业务类型的消息，而不是使用WebSocket
     */
    @PostMapping("/push")
    public ResultModel pushMsg(@Validated MsgModel msg, @NotEmpty String channelId) {
        if (MsgType.isFunctionalType(msg.getMsgType())) {
            return ResultModel.failed(StatusCode.NOT_SUPPORT_FUNC_TYPE);
        }
        Integer userId = ShiroUtils.getUserId();
        Channel channel = MsgCenter.getInstance()
                .findChannel(msg.getChannelType(), userId, channelId);
        if (channel == null) {
            return ResultModel.failed(StatusCode.CHANNEL_NOT_EXIST);
        }
        MsgCenter.getInstance().handle(msg, channel);
        return ResultModel.success();
    }

}
