package top.ysqorz.forum.im.entity;

/**
 * @author passerbyYSQ
 * @create 2022-04-05 15:56
 */
public interface OnOffLineAware {

    default void online(String token) {}

    default void offline(String token) {}
}
