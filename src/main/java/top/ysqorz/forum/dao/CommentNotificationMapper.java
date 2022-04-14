package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.resp.MessageListDTO;
import top.ysqorz.forum.po.CommentNotification;

import java.util.List;

public interface CommentNotificationMapper extends BaseMapper<CommentNotification> {
    /**
     * 获取所有评论
     * @param id
     * @param
     * @return
     */
    List<MessageListDTO> getMegList(Integer id);

    /**
     * 获取所有回复
     * @param id
     * @return
     */

    List<MessageListDTO> getMegReply(Integer id);


}
