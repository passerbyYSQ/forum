package top.ysqorz.forum.im.entity;


import io.netty.channel.Channel;

/**
 * @author passerbyYSQ
 * @create 2022-04-05 15:56
 */
public interface OnOffLineAware {

    default void online(Channel channel) {
    }

    default void offline(Channel channel) {
    }
}
