package top.ysqorz.forum.im.entity;

/**
 * 消息的类型。
 * 长连接建立和所有的消息推送必须登录
 */
public enum MsgType {
    // 功能类型
    BIND,
    PING,
    PONG,
    CLOSE,
    // 业务类型
    DANMU;

    public static boolean isFunctionalType(String type) {
        MsgType[] funcTypes = new MsgType[]{BIND, PING, PONG, CLOSE};
        for (MsgType funcType : funcTypes) {
            if (funcType.name().equals(type)) {
                return true;
            }
        }
        return false;
    }
}
