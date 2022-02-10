package top.ysqorz.forum.im.entity;

/**
 * 消息的类型。
 * 长连接建立和所有的消息推送必须登录
 */
public enum MsgType {
    // 功能类型的消息
    BIND,
    PING,
    PONG,
    CLOSE,
    // 业务类型的消息
    DANMU,
    SINGLE_CHAT,
    GROUP_CHAT;

    public static boolean isFunctionalType(MsgType type) {
        MsgType[] funcTypes = new MsgType[]{BIND, PING, PONG, CLOSE};
        for (MsgType funcType : funcTypes) {
            if (funcType.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
