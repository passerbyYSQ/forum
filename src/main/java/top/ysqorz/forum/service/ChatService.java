package top.ysqorz.forum.service;

import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.enumeration.ApplyStatus;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.resp.chat.ChatFriendApplyDTO;
import top.ysqorz.forum.dto.resp.chat.ChatListDTO;
import top.ysqorz.forum.dto.resp.chat.ChatUserCardDTO;
import top.ysqorz.forum.dto.resp.chat.NotSignedChatFriendMsg;
import top.ysqorz.forum.po.ChatFriend;
import top.ysqorz.forum.po.ChatFriendApply;
import top.ysqorz.forum.po.ChatFriendGroup;
import top.ysqorz.forum.po.ChatFriendMsg;

import java.util.List;
import java.util.Set;

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
    ChatFriend getMyChatFriendById(Integer friendId);

    /**
     * 获取指定用户所有好友的用户id
     */
    Set<Integer> getAllFriendUserIds(Integer userId);

    /**
     * 根据两个人的用户id删除好友关系
     */
    void deleteChatFriendByBothIds(Integer myId, Integer friendId);

    /**
     * 获取"未分组"
     */
    ChatListDTO.ChatFriendGroupDTO getChatFriendsWithoutGroup();

    /**
     * 添加好友关系
     */
    void addChatFriend(Integer myId, Integer friendId, Integer friendGroupId);

    /**
     * 申请添加好友
     */
    StatusCode applyFriend(Integer receiverId,  Integer friendGroupId, String content);

    /**
     * 查询消息盒子的消息数量
     */
    Integer getMsgBoxMsgCount(Integer myId);

    /**
     *根据两个人的用户id查找好友申请
     */
    ChatFriendApply getFriendApplySentBySelf(Integer receiverId);

    /**
     * 根据id查询好友申请
     */
    ChatFriendApply getFriendApplyById(Integer friendApplyId);

    /**
     * 添加一条好友申请记录
     */
    void addFriendApply(Integer receiverId, Integer friendGroupId, String content);

    /**
     * 根据id修改好友申请的内容
     */
    void updateFriendApplyById(Integer friendApplyId, Integer friendGroupId, String content);

    /**
     * 根据id修改好友申请的状态
     */
    void updateFriendApplyStatusById(Integer friendApplyId, ApplyStatus status);

    /**
     * 好友分组的id是否非法
     */
    boolean isInvalidFriendGroup(Integer friendGroupId);

    /**
     * 根据我的id和分组名查询好友分组
     */
    ChatFriendGroup getFriendGroupByName(String friendGroupName);

    /**
     * 好友申请的通知
     */
    PageData<ChatFriendApplyDTO> getFriendApplyNotifications(Integer page, Integer count);

    /**
     * 处理好友申请
     */
    String processFriendApply(Integer friendApplyId, Integer friendGroupId, ApplyStatus status);

    /**
     * 消息盒子的数量变化后，推送消息
     */
    void pushMsgBoxCount(Integer userId);

    /**
     * 签收好友申请的通知
     * @param friendApplyIds    用逗号分隔。形如：1,2,3
     */
    void signFriendApplyNotifications(String friendApplyIds);

    /**
     * 查询好友列表
     */
    ChatListDTO getChatList();

    /**
     * 创建好友分组
     */
    ChatFriendGroup createFriendGroup(String friendGroupName);

    /**
     * 移动好友至指定分组
     * @param friendIds                 好友的用户id，用逗号分隔。形如：1,2,3
     * @param targetFriendGroupId       如果缺省，则全部移动至"未分组"
     */
    StatusCode moveFriendToGroup(String friendIds, Integer targetFriendGroupId);

    /**
     * 根据id删除好友分组
     */
    StatusCode deleteFriendGroup(Integer friendGroupId);

    /**
     * 删除好友
     */
    StatusCode deleteChatFriend(Integer friendId);

    /**
     * 发送好友私聊消息
     */
    StatusCode sendChatFriendMsg(Integer friendId, String content, String sourceChannelId);

    /**
     * 签收聊天消息
     */
    void signChatFriendMsg(String msgIds);

    /**
     * 获取没有签收的好友聊天消息
     */
    List<NotSignedChatFriendMsg> getNotSignedChatFriendMsg();

    /**
     * 获取跟某个好友的聊天历史记录
     */
    PageData<ChatFriendMsg> getChatHistoryWithFriend(Integer friendId, Integer page, Integer count);
}
