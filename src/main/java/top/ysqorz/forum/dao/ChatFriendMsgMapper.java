package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.resp.chat.NotSignedChatFriendMsg;
import top.ysqorz.forum.po.ChatFriendMsg;

import java.util.List;

public interface ChatFriendMsgMapper extends BaseMapper<ChatFriendMsg> {
    /**
     * 查询当前用户未签收的好友聊天消息
     */
    List<NotSignedChatFriendMsg> selectNotSignedChatFriendMsg(Integer myId);
}
