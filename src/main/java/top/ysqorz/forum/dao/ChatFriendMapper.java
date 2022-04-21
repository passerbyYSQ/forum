package top.ysqorz.forum.dao;

import org.apache.ibatis.annotations.Param;
import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.resp.chat.ChatListDTO;
import top.ysqorz.forum.po.ChatFriend;

import java.util.List;
import java.util.Set;

public interface ChatFriendMapper extends BaseMapper<ChatFriend> {
    /**
     * 获取未分组的好友
     */
    List<ChatListDTO.ChatFriendDTO> selectChatFriendsWithoutGroup(Integer myId);

    /**
     * 将指定分组的好友移动至未分组
     */
    void unGroupFriendsByGroupId(@Param("sourceFriendGroupId") Integer sourceFriendGroupId,
                                 @Param("targetFriendGroupId") Integer targetFriendGroupId);

    /**
     * 更改部分好友的分组
     */
    void updateGroupOfFriends(@Param("friendIdSet") Set<Integer> friendIdSet,
                              @Param("targetFriendGroupId") Integer targetFriendGroupId,
                              @Param("myId") Integer myId);
}
