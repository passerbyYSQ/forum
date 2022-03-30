package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.resp.ChatListDTO;
import top.ysqorz.forum.po.ChatFriendGroup;

import java.util.List;

public interface ChatFriendGroupMapper extends BaseMapper<ChatFriendGroup> {
    /**
     * 查询好友列表
     */
    List<ChatListDTO.ChatFriendGroupDTO> selectChatFriendList(Integer myId);
}
