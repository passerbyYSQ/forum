package top.ysqorz.forum;

import org.junit.Test;
import top.ysqorz.forum.im.handler.LinkedListMsgHandlerPipeline;
import top.ysqorz.forum.im.MsgHandler;
import top.ysqorz.forum.im.MsgHandlerPipeline;
import top.ysqorz.forum.im.handler.*;
import top.ysqorz.forum.utils.CommonUtils;

/**
 * ...
 *
 * @author yaoshiquan
 * @date 2025/1/9
 */
public class CommonUtilsTest {

    @Test
    public void testGetLocalHost() {
        String localHostStr = CommonUtils.getLocalHostStr();
        System.out.println(localHostStr);
    }

    @Test
    public void testMsgHandlerPipeline() {
        MsgHandlerPipeline pipeline = new LinkedListMsgHandlerPipeline();
        pipeline.addHandler(new BindMsgHandler(null))
                .addHandler(new PingPongMsgHandler(null))
                .addHandler(new DanmuMsgHandler())
                .addHandler(new ChatFriendMsgHandler())
                .addHandler(new ChatNotificationHandler())
                .addHandler(new TailHandler());
        for (MsgHandler<?> msgHandler : pipeline) {
            System.out.println(msgHandler);
        }
    }

}
