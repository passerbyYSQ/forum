package top.ysqorz.forum.netty.handler;

import com.fasterxml.jackson.databind.JsonNode;
import io.netty.channel.Channel;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;
import top.ysqorz.forum.dao.DanmuMsgMapper;
import top.ysqorz.forum.netty.entity.AsyncInsertTask;
import top.ysqorz.forum.netty.entity.MsgModel;
import top.ysqorz.forum.netty.entity.MsgType;
import top.ysqorz.forum.po.DanmuMsg;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.utils.JsonUtils;
import top.ysqorz.forum.utils.RandomUtils;
import top.ysqorz.forum.utils.SpringUtils;

import java.time.LocalDateTime;

/**
 * @author passerbyYSQ
 * @create 2022-01-15 23:02
 */
public class DanmuMsgHandler extends MsgHandler {
    public DanmuMsgHandler() {
        super(MsgType.DANMU);
    }

    @Override
    protected boolean doHandle(MsgModel msg, Channel channel, User loginUser) {
        DanmuMsg danmu = JsonUtils.nodeToObj((JsonNode) msg.getData(), DanmuMsg.class);
        if (danmu == null || ObjectUtils.isEmpty(danmu.getContent())) {
            return true;
        }
        // TODO check video
        String text = danmu.getContent().substring(0, 255); // 如果过长只截取前500个字符
        text = HtmlUtils.htmlEscape(text, "UTF-8"); // 转义，防止XSS攻击
        danmu.setId(RandomUtils.generateUUID())
                .setContent(text)
                .setCreatorId(loginUser.getId())
                .setCreateTime(LocalDateTime.now())
                .setStartMs(Math.max(danmu.getStartMs(), 0)); // 负数时做纠正
        // 推送弹幕
        this.channelMap.pushExceptMe(danmu, channel);
        // 异步将弹幕插入数据库
        DanmuMsgMapper mapper = SpringUtils.getBean(DanmuMsgMapper.class);
        AsyncInsertTask<DanmuMsg> insertTask = new AsyncInsertTask<>(mapper, danmu);
        this.dbExecutor.execute(insertTask);
        return true;
    }

}
