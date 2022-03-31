package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.resp.ChatListDTO;
import top.ysqorz.forum.po.ChatFriend;

import java.util.List;

public interface ChatFriendMapper extends BaseMapper<ChatFriend> {
    /**
     * 获取未分组的好友
     */
    List<ChatListDTO.ChatFriendDTO> selectChatFriendsWithoutGroup(Integer myId);
}
