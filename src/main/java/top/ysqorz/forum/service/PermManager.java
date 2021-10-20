package top.ysqorz.forum.service;

/**
 * @author passerbyYSQ
 * @create 2021-10-20 20:29
 */
public interface PermManager {

    boolean allowDelComment(Integer creatorId);

    boolean allowDelPost(Integer creatorId);

    boolean allowUpdatePost(Integer creatorId);
}
