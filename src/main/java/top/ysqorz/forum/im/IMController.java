package top.ysqorz.forum.im;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.im.handler.MsgCenter;
import top.ysqorz.forum.service.IMService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.JsonUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;

/**
 * @author passerbyYSQ
 * @create 2022-01-29 22:47
 */
@Controller
@ResponseBody
@Validated
@RequestMapping("/im")
public class IMController {
    @Resource
    private IMService imService;

    /**
     * 1. IM服务之间转发业务类型的消息需要调用此接口
     * 2. 客户端使用API发送业务类型的消息，而不是使用WebSocket
     * @deprecated  弃用，每种业务消息都应该使用不同的接口
     */
    @PostMapping("/send")
    public ResultModel sendMsg(@NotBlank String msgJson, @NotBlank  String channelId) {
        MsgModel msg = JsonUtils.jsonToObj(HtmlUtils.htmlUnescape(msgJson), MsgModel.class);
        if (msg == null || !msg.check()) {
            return ResultModel.failed(StatusCode.PARAM_INVALID);
        }
        if (MsgType.isFunctionalType(MsgType.valueOf(msg.getMsgType()))) { // 如果非法type会抛出异常
            return ResultModel.failed(StatusCode.NOT_SUPPORT_FUNC_TYPE);
        }
        MsgCenter.getInstance().remoteDispatch(msg, channelId, ShiroUtils.getToken());
        return ResultModel.success();
    }

    /**
     * 转交给 MsgCenter push
     */
    @PostMapping("/push")
    public ResultModel pushMsg(@NotBlank String msgJson, String channelId) { // source channel
        MsgModel msg = JsonUtils.jsonToObj(HtmlUtils.htmlUnescape(msgJson), MsgModel.class);
        MsgCenter.getInstance().push(msg, channelId);
        return ResultModel.success();
    }

    @GetMapping("/server")
    public ResultModel<String> wsServers() {
        return ResultModel.success(imService.getRandWsServerUrl());
    }
}
