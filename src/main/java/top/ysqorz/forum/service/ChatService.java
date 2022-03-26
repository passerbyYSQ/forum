package top.ysqorz.forum.service;

import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.resp.ChatUserCardDTO;
import top.ysqorz.forum.po.ChatFriend;
import top.ysqorz.forum.po.ChatFriendApply;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2022-03-26 16:23
 */
public interface ChatService {
    /**
     * 查找用户，用于添加好友
     */
    List<ChatUserCardDTO> getChatUserCards(String keyword, String status, Integer page, Integer count);

    /**
     * 根据两个人的用户id查找好友关系
     */
    ChatFriend getChatFriendByBothIds(Integer friendId);

    /**
     * 申请添加好友
     */
    StatusCode applyFriend(Integer receiverId, String content);

    /**
     *根据两个人的用户id查找好友申请
     */
    ChatFriendApply getFriendApplyByBothIds(Integer receiverId);

    /**
     * 添加一条好友申请记录
     */
    void addFriendApply(Integer receiverId, String content);

    /**
     * 根据id删除好友申请
     */
    void deleteFriendApplyById(Integer friendApplyId);

    /**
     * 根据id修改好友申请的内容
     */
    void updateFriendApplyById(Integer friendApplyId, String content);
}
