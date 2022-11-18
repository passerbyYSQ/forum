package top.ysqorz.forum.controller;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
     *
     * @deprecated 弃用，每种业务消息都应该使用不同的接口
     */
    @PostMapping("/send")
    public StatusCode sendMsg(@NotBlank String msgJson, @NotBlank String channelId) {
        MsgModel msg = JsonUtils.jsonToObj(HtmlUtils.htmlUnescape(msgJson), MsgModel.class);
        if (msg == null || !msg.check()) {
            return StatusCode.PARAM_INVALID;
        }
        if (MsgType.isFunctionalType(MsgType.valueOf(msg.getMsgType()))) { // 如果非法type会抛出异常
            return StatusCode.NOT_SUPPORT_FUNC_TYPE;
        }
        MsgCenter.getInstance().remoteDispatch(msg, channelId, ShiroUtils.getToken());
        return StatusCode.SUCCESS;
    }

    /**
     * 转交给 MsgCenter push
     */
    @PostMapping("/push")
    public StatusCode pushMsg(@RequestBody MsgModel msg, String channelId) { // source channel
        MsgCenter.getInstance().push(msg, channelId);
        return StatusCode.SUCCESS;
    }

    @GetMapping("/server")
    public ResultModel<String> wsServers() {
        return ResultModel.success(imService.getRandWsServerUrl());
    }
}
