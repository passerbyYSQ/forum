package top.ysqorz.forum.im.entity;

/**
 * 消息的类型
 */
public enum MsgType {

    BIND(true),
    DANMU(true);

    public Boolean isNeedLogin;

    MsgType(Boolean isNeedLogin) {
        this.isNeedLogin = isNeedLogin;
    }
}
