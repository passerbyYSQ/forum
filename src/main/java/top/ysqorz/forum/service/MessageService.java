package top.ysqorz.forum.service;

import top.ysqorz.forum.dto.MessageListDTO;
import top.ysqorz.forum.dto.PageData;

/**
 * @author 阿灿
 * @create 2021-07-02 22:01
 */
public interface MessageService {

    /**
     * 获取当前用户消息列表
     */
    PageData<MessageListDTO> getMegList(Integer page, Integer count, Integer conditions);
    /**
     * 获取一级评论前面有多少条评论数
     */
    Integer getFirstMegPage(Integer firstMegId);

    /**
     * 获取二级评论前面有多少条评论数
     */
    Integer getSecondMegPage(Integer firstMegId,Integer secondMegId);

    /**
     * 清空所有消息
     */
    int clearAllMeg();
}
