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
}
