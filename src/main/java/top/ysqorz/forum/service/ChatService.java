package top.ysqorz.forum.service;

import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.resp.ChatFriendApplyDTO;
import top.ysqorz.forum.dto.resp.ChatListDTO;
import top.ysqorz.forum.dto.resp.ChatUserCardDTO;
import top.ysqorz.forum.po.ChatFriend;
import top.ysqorz.forum.po.ChatFriendApply;

/**
 * @author passerbyYSQ
 * @create 2022-03-26 16:23
 */
public interface ChatService {
    /**
     * 查找用户，用于添加好友
     * @param status    all, online, offline
     */
    PageData<ChatUserCardDTO> getChatUserCards(String keyword, String status, Integer page, Integer count);

    /**
     * 根据两个人的用户id查找好友关系
     */
    ChatFriend getChatFriendByBothIds(Integer friendId);

    /**
     * 添加好友关系
     */
    void addChatFriend(Integer myId, Integer friendId, Integer friendGroupId);

    /**
     * 申请添加好友
     */
    StatusCode applyFriend(Integer receiverId,  Integer friendGroupId, String content);

    /**
     *根据两个人的用户id查找好友申请
     */
    ChatFriendApply getFriendApplyByBothIds(Integer receiverId);

    /**
     * 根据id查询好友申请
     */
    ChatFriendApply getFriendApplyById(Integer friendApplyId);

    /**
     * 添加一条好友申请记录
     */
    void addFriendApply(Integer receiverId, Integer friendGroupId, String content);

    /**
     * 根据id删除好友申请
     */
    void deleteFriendApplyById(Integer friendApplyId);

    /**
     * 根据id修改好友申请的内容
     */
    void updateFriendApplyById(Integer friendApplyId, Integer friendGroupId, String content);

    /**
     * 根据id修改好友申请的状态
     */
    void updateFriendApplyStatusById(Integer friendApplyId, Byte status);

    /**
     * 好友分组的id是否非法
     */
    boolean isInvalidFriendGroup(Integer friendGroupId);

    /**
     * 好友申请的通知
     */
    PageData<ChatFriendApplyDTO> getFriendApplyNotifications(Integer page, Integer count);

    /**
     * 处理好友申请
     * @param action    agree, refuse, ignore
     */
    StatusCode processFriendApply(Integer friendApplyId, Integer friendGroupId, String action);

    /**
     * 签收好友申请的通知
     * @param friendApplyIds    用逗号分隔。形如：1,2,3
     */
    void signFriendApplyNotifications(String friendApplyIds);

    /**
     * 查询好友列表
     */
    ChatListDTO getChatList();
}
