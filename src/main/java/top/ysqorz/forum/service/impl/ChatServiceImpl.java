package top.ysqorz.forum.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.exception.ServiceFailedException;
import top.ysqorz.forum.dao.ChatFriendApplyMapper;
import top.ysqorz.forum.dao.ChatFriendGroupMapper;
import top.ysqorz.forum.dao.ChatFriendMapper;
import top.ysqorz.forum.dao.ChatFriendMsgMapper;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.resp.chat.*;
import top.ysqorz.forum.im.entity.ChannelType;
import top.ysqorz.forum.im.entity.MsgModel;
import top.ysqorz.forum.im.entity.MsgType;
import top.ysqorz.forum.im.handler.MsgCenter;
import top.ysqorz.forum.po.*;
import top.ysqorz.forum.service.ChatService;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.CommonUtils;
import top.ysqorz.forum.utils.RandomUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author passerbyYSQ
 * @create 2022-03-26 16:23
 */
@Service
public class ChatServiceImpl implements ChatService {
    @Resource
    private UserService userService;
    @Resource
    private ChatFriendMapper chatFriendMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private ChatFriendApplyMapper chatFriendApplyMapper;
    @Resource
    private ChatFriendGroupMapper chatFriendGroupMapper;
    @Resource
    private ChatFriendMsgMapper chatFriendMsgMapper;

    @Override
    public Set<Integer> getAllFriendUserIds(Integer userId) {
        Example example = new Example(ChatFriend.class);
        example.selectProperties("friendId")
                .createCriteria().andEqualTo("myId", userId);
        List<ChatFriend> chatFriends = chatFriendMapper.selectByExample(example);
        return chatFriends.stream().map(ChatFriend::getFriendId).collect(Collectors.toSet());
    }

    /**
     * TODO ???????????????????????????????????????status???????????????
     */
    @Override
    public PageData<ChatUserCardDTO> getChatUserCards(String keyword, String status, Integer page, Integer count) {
        User accurateUser = null; // ????????????
        if (keyword.matches(Constant.REGEX_PHONE)) {
            accurateUser = userService.getUserByPhone(keyword);
        } else if (keyword.matches(Constant.REGEX_EMAIL)) {
            accurateUser = userService.getUserByEmail(keyword);
        }

        List<ChatUserCardDTO> userCards = new ArrayList<>();
        Page pageInfo = PageHelper.startPage(page, count); // ????????????
        if (!ObjectUtils.isEmpty(accurateUser)) { // ?????????????????????
            ChatUserCardDTO userCard = new ChatUserCardDTO(accurateUser);
            ChatFriend chatFriend = this.getMyChatFriendById(accurateUser.getId());
            userCard.setIsChatFriend(!ObjectUtils.isEmpty(chatFriend));
            if (!ObjectUtils.isEmpty(chatFriend)) {
                userCard.setAlias(chatFriend.getAlias());
            }
            userCards.add(userCard);
        } else { // ????????????
            userCards = userService.getUserCardsLikeUsername(keyword); // ????????????
        }

        for (ChatUserCardDTO userCard : userCards) {
            boolean isOnline = redisService.isUserOnline(userCard.getUserId());
            userCard.setStatus(isOnline ? "online" : "offline");
        }
        // ?????????userCards??????????????????????????????userList?????????????????????
        // PageData<ChatUserCardDTO> pageData = new PageData<>(userCards);
        return new PageData<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), userCards);
    }

    /*
    @Override
    public PageData<ChatUserCardDTO> getChatUserCards(String keyword, String status, Integer page, Integer count) {
        List<User> userList = new ArrayList<>();
        User accurateUser = null; // ????????????
        if (keyword.matches(Constant.REGEX_PHONE)) {
            accurateUser = userService.getUserByPhone(keyword);
        } else if (keyword.matches(Constant.REGEX_EMAIL)) {
            accurateUser = userService.getUserByEmail(keyword);
        }

        Page pageInfo = PageHelper.startPage(page, count); // ????????????
        if (!ObjectUtils.isEmpty(accurateUser)) { // ????????????????????????????????????
            userList.add(accurateUser);
        } else { // ????????????
            userList = userService.getUsersLikeUsername(keyword, true);
        }

        ChatService chatService = this;
        List<ChatUserCardDTO> userCards = userList.stream().map(user -> {
            ChatUserCardDTO userCard = new ChatUserCardDTO(user);
            ChatFriend chatFriend = chatService.getMyChatFriendById(user.getId());
            boolean isOnline = redisService.isUserOnline(user.getId());
            userCard.setIsChatFriend(!ObjectUtils.isEmpty(chatFriend))
                    .setStatus(isOnline ? "online" : "offline");
            if (!ObjectUtils.isEmpty(chatFriend)) {
                userCard.setAlias(chatFriend.getAlias());
            }
            return userCard;
        }).collect(Collectors.toList());
        // ?????????userCards??????????????????????????????userList?????????????????????
        // PageData<ChatUserCardDTO> pageData = new PageData<>(userCards);
        return new PageData<>(pageInfo.getPageNum(), pageInfo.getPageSize(), pageInfo.getTotal(), userCards);
    }
    */

    @Override
    public ChatFriend getMyChatFriendById(Integer friendId) {
        Example example = new Example(ChatFriend.class);
        example.createCriteria().andEqualTo("myId", ShiroUtils.getUserId())
                .andEqualTo("friendId", friendId);
        return chatFriendMapper.selectOneByExample(example);
    }

    @Override
    public void addChatFriend(Integer myId, Integer friendId, Integer friendGroupId) {
        ChatFriend chatFriend = new ChatFriend();
        chatFriend.setMyId(myId)
                .setFriendId(friendId)
                .setFriendGroupId(friendGroupId)
                .setCreateTime(LocalDateTime.now());
        chatFriendMapper.insert(chatFriend);
    }

    @Override
    public StatusCode applyFriend(Integer receiverId, Integer friendGroupId, String content) {
        if (this.isInvalidFriendGroup(friendGroupId)) { // ??????????????????id????????????
            return StatusCode.CHAT_FRIEND_GROUP_INVALID;
        }
        User receiver = userService.getUserById(receiverId);
        if (ObjectUtils.isEmpty(receiver) || receiverId.equals(ShiroUtils.getUserId())) { // ?????????id???????????????
            return StatusCode.USER_ID_INVALID;
        }
        ChatFriend chatFriend = this.getMyChatFriendById(receiverId);
        if (!ObjectUtils.isEmpty(chatFriend)) { // ????????????????????????????????????
            return StatusCode.CHAT_ALREADY_FRIEND;
        }

        ChatFriendApply friendApply = this.getFriendApplySentBySelf(receiverId);
        if (ObjectUtils.isEmpty(friendApply)) { // ?????????????????????????????????
            this.addFriendApply(receiverId, friendGroupId, content);
        } else {
            // ????????????????????????????????????????????????????????????????????????????????????
            // ???????????????????????????????????????1.?????????????????????????????????????????????????????????2.????????????????????????

            // 1.status??????null?????????????????????????????????????????????????????????????????????????????????????????????return??????
            if (friendApply.getStatus() != null) {
                chatFriendApplyMapper.deleteByPrimaryKey(friendApply.getId());
                return StatusCode.CHAT_FRIEND_APPLY_PROCESSED;
            }
            // 2.??????????????????????????????
            this.updateFriendApplyById(friendApply.getId(), friendGroupId, content);
        }
        this.pushMsgBoxCount(receiverId); // ??????????????????????????????????????????
        return StatusCode.SUCCESS;
    }

    @Override
    public ChatFriendApply getFriendApplySentBySelf(Integer receiverId) {
        Example example = new Example(ChatFriendApply.class);
        example.createCriteria().andEqualTo("senderId", ShiroUtils.getUserId()) // sender is self
                .andEqualTo("receiverId", receiverId);
        return chatFriendApplyMapper.selectOneByExample(example);
    }

    @Override
    public ChatFriendApply getFriendApplyById(Integer friendApplyId) {
        return chatFriendApplyMapper.selectByPrimaryKey(friendApplyId);
    }

    @Override
    public void addFriendApply(Integer receiverId, Integer friendGroupId, String content) {
        ChatFriendApply friendApply = new ChatFriendApply();
        friendApply.setSenderId(ShiroUtils.getUserId())
                .setReceiverId(receiverId)
                .setFriendGroupId(friendGroupId)
                .setContent(content)
                .setApplyTime(LocalDateTime.now());
        // ???????????????status?????????status???null???????????????????????????????????????
        chatFriendApplyMapper.insert(friendApply);
    }

    @Override
    public void updateFriendApplyById(Integer friendApplyId, Integer friendGroupId, String content) {
        ChatFriendApply friendApply = new ChatFriendApply();
        friendApply.setId(friendApplyId)
                .setContent(content)
                .setFriendGroupId(friendGroupId)
                .setApplyTime(LocalDateTime.now());
        chatFriendApplyMapper.updateByPrimaryKeySelective(friendApply); // ?????????Selective??????
    }

    @Override
    public void updateFriendApplyStatusById(Integer friendApplyId, Byte status) {
        ChatFriendApply friendApply = new ChatFriendApply();
        friendApply.setId(friendApplyId)
                .setStatus(status)
                .setApplyTime(LocalDateTime.now()); // ??????????????????
        chatFriendApplyMapper.updateByPrimaryKeySelective(friendApply); // ?????????Selective??????
    }

    @Override
    public PageData<ChatFriendMsg> getChatHistoryWithFriend(Integer friendId, Integer page, Integer count) {
        ChatFriend friend = this.getMyChatFriendById(friendId);
        if (ObjectUtils.isEmpty(friend)) {
            throw new ServiceFailedException(StatusCode.CHAT_FRIEND_NOT_EXIST);
        }
        PageHelper.startPage(page, count); // ????????????
        Example example = new Example(ChatFriendMsg.class);
        Integer myId = ShiroUtils.getUserId();
        example.selectProperties("content", "createTime", "senderId", "receiverId") // ?????????????????????????????????
                .orderBy("createTime").desc(); // ??????
        example.createCriteria()
                .andEqualTo("senderId", myId) // ?????????
                .andEqualTo("receiverId", friendId);
        Example.Criteria sentByFriend = example.createCriteria()
                .andEqualTo("senderId", friendId) // ????????????
                .andEqualTo("receiverId", myId);
        example.or(sentByFriend);
        List<ChatFriendMsg> history = chatFriendMsgMapper.selectByExample(example);
        return new PageData<>(history);
    }

    @Override
    public boolean isInvalidFriendGroup(Integer friendGroupId) {
        if (ObjectUtils.isEmpty(friendGroupId)) {
            return false; // null??????????????????????????????
        }
        ChatFriendGroup friendGroup = chatFriendGroupMapper.selectByPrimaryKey(friendGroupId);
        return ObjectUtils.isEmpty(friendGroup) || !friendGroup.getUserId().equals(ShiroUtils.getUserId());
    }

    @Override
    public PageData<ChatFriendApplyDTO> getFriendApplyNotifications(Integer page, Integer count) {
        PageHelper.startPage(page, count);
        Integer myId = ShiroUtils.getUserId();
        List<ChatFriendApplyDTO> friendApplyList = chatFriendApplyMapper.selectFriendApplyNotifications(myId);
        for (ChatFriendApplyDTO apply : friendApplyList) {
            if (myId.equals(apply.getSenderId())) {
                apply.setSenderId(null); // null????????????????????????
            }
            if (myId.equals(apply.getReceiverId())) {
                apply.setReceiverId(null);
            }
        }
        return new PageData<>(friendApplyList);
    }

    @Override
    public void pushMsgBoxCount(Integer userId) {
        ChatNotificationDTO notification = new ChatNotificationDTO();
        notification.setReceiverId(userId)
                .setSenderId(0) // ????????????
                .setAction("msg_box")
                .addPayload("count", this.getMsgBoxMsgCount(userId));
        MsgModel msgModel = new MsgModel(MsgType.CHAT_NOTIFICATION, ChannelType.CHAT, notification);
        MsgCenter.getInstance().remoteDispatch(msgModel, null, ShiroUtils.getToken());
    }

    @Override
    @Transactional // ??????????????????
    public String processFriendApply(Integer friendApplyId, Integer friendGroupId, String action) {
        ChatFriendApply apply = this.getFriendApplyById(friendApplyId);
        Integer myId = ShiroUtils.getUserId();
        if (ObjectUtils.isEmpty(apply) || !apply.getReceiverId().equals(myId) // ???????????????????????????
                || !ObjectUtils.isEmpty(apply.getStatus())) {
            throw new ServiceFailedException(StatusCode.CHAT_FRIEND_APPLY_INVALID);
        }
        if ("ignore".equalsIgnoreCase(action)) {
            chatFriendApplyMapper.deleteByPrimaryKey(friendApplyId); // ??????????????????
        } else if ("refuse".equalsIgnoreCase(action)) {
            // ?????????????????????????????????????????????
            this.updateFriendApplyStatusById(friendApplyId, (byte) 0);
            this.pushMsgBoxCount(apply.getSenderId());
        } else if ("agree".equalsIgnoreCase(action)) {
            // ?????????????????????????????????????????????
            if (this.isInvalidFriendGroup(friendGroupId)) {
                throw new ServiceFailedException(StatusCode.CHAT_FRIEND_GROUP_INVALID); // ??????????????????
            }
            ChatFriend chatFriend = this.getMyChatFriendById(apply.getSenderId());
            if (!ObjectUtils.isEmpty(chatFriend)) { // ?????????????????????????????????????????????????????????
                chatFriendApplyMapper.deleteByPrimaryKey(friendApplyId);
                throw new ServiceFailedException(StatusCode.CHAT_ALREADY_FRIEND);
            }
            Integer toGroupId = apply.getFriendGroupId();
            this.updateFriendApplyStatusById(friendApplyId, (byte) 1);
            this.addChatFriend(myId, apply.getSenderId(), friendGroupId);
            this.addChatFriend(apply.getSenderId(), myId, toGroupId);
            // ??????????????????????????????????????????????????????
            User self = userService.getUserById(myId);
            ChatNotificationDTO notification = new ChatNotificationDTO();
            notification.setReceiverId(apply.getSenderId())
                    .setSenderId(myId)
                    .setAction("add_friend")
                    .addPayload("username", self.getUsername())
                    .addPayload("avatar", self.getPhoto())
                    .addPayload("sign", self.getDescription())
                    .addPayload("groupid", ObjectUtils.isEmpty(toGroupId) ? -1 : toGroupId)
                    .addPayload("status", "online"); // ??????????????????????????????????????????????????????
            MsgModel msgModel = new MsgModel(MsgType.CHAT_NOTIFICATION, ChannelType.CHAT, notification);
            MsgCenter.getInstance().remoteDispatch(msgModel, null, ShiroUtils.getToken());
            // ??????????????????????????????
            this.pushMsgBoxCount(apply.getSenderId());
            // ???????????????????????????
            boolean iOnline = redisService.isUserOnline(apply.getSenderId());
            return iOnline ? "online" : "offline";
        }
        return "";
    }

    @Override
    public void signFriendApplyNotifications(String friendApplyIds) {
        Set<Integer> applyIdSets = CommonUtils.splitIdStr(friendApplyIds);
        if (!applyIdSets.isEmpty()) {
            Example example = new Example(ChatFriendApply.class);
            // where sender_id = #{myId} and status in (0, 1) and id in (1,2,3)
            example.createCriteria().andEqualTo("senderId", ShiroUtils.getUserId())
                    .andIn("status", Arrays.asList(0, 1))
                    .andIn("id", applyIdSets);
            chatFriendApplyMapper.deleteByExample(example);
        }
    }

    @Override
    public Integer getMsgBoxMsgCount(Integer myId) {
        Example example = new Example(ChatFriendApply.class);
        // ??????????????????????????????
        example.createCriteria()
                .andEqualTo("receiverId", myId)
                .andIsNull("status");
        // ????????????????????????????????????????????????
        Example.Criteria processedByOther = example.createCriteria()
                .andEqualTo("senderId", myId)
                .andIn("status", Arrays.asList(0, 1));
        example.or(processedByOther);
        return chatFriendApplyMapper.selectCountByExample(example);
    }

    @Override
    public ChatListDTO getChatList() {
        ChatListDTO chatListDTO = new ChatListDTO();
        Integer myId = ShiroUtils.getUserId();
        // ????????????
        User selfUser = userService.getUserById(myId);
        ChatListDTO.ChatFriendDTO self = new ChatListDTO.ChatFriendDTO(selfUser);
        Map<String, Object> extra = new HashMap<>();
        extra.put("msgBox", this.getMsgBoxMsgCount(myId)); // ???????????????????????????
        self.setExtra(extra);
        chatListDTO.setMine(self);
        // ????????????
        List<ChatListDTO.ChatFriendGroupDTO> friendGroupList = chatFriendGroupMapper.selectFriendGroupList(myId);
        friendGroupList.add(this.getChatFriendsWithoutGroup()); // ?????????
        for (ChatListDTO.ChatFriendGroupDTO friendGroup : friendGroupList) {
            for (ChatListDTO.ChatFriendDTO friend : friendGroup.getList()) {
                boolean isOnline = redisService.isUserOnline(friend.getId());
                friend.setStatus(isOnline ? "online" : "offline");
            }
        }
        chatListDTO.setFriend(friendGroupList);
        // TODO ????????????
        chatListDTO.setGroup(new ArrayList<>());
        return chatListDTO;
    }

    @Override
    public ChatListDTO.ChatFriendGroupDTO getChatFriendsWithoutGroup() {
        List<ChatListDTO.ChatFriendDTO> friends = chatFriendMapper.selectChatFriendsWithoutGroup(ShiroUtils.getUserId());
        ChatListDTO.ChatFriendGroupDTO friendGroup = new ChatListDTO.ChatFriendGroupDTO();
        friendGroup.setId(-1)
                .setGroupname("?????????")
                .setList(friends);
        return friendGroup;
    }

    @Override
    public ChatFriendGroup createFriendGroup(String friendGroupName) {
        ChatFriendGroup friendGroup = this.getFriendGroupByName(friendGroupName);
        if (!ObjectUtils.isEmpty(friendGroup) || "?????????".equals(friendGroupName)) {
            throw new ServiceFailedException(StatusCode.CHAT_FRIEND_GROUP_EXIST);
        }
        ChatFriendGroup newFriendGroup = new ChatFriendGroup();
        newFriendGroup.setGroupName(friendGroupName.trim())
                .setUserId(ShiroUtils.getUserId());
        chatFriendGroupMapper.insertUseGeneratedKeys(newFriendGroup);
        return newFriendGroup;
    }

    @Override
    public ChatFriendGroup getFriendGroupByName(String friendGroupName) {
        Example example = new Example(ChatFriendGroup.class);
        example.createCriteria().andEqualTo("userId", ShiroUtils.getUserId())
                .andEqualTo("groupName", friendGroupName);
        return chatFriendGroupMapper.selectOneByExample(example);
    }

    @Override
    @Transactional
    public StatusCode deleteFriendGroup(Integer friendGroupId) {
        if (ObjectUtils.isEmpty(friendGroupId) || this.isInvalidFriendGroup(friendGroupId)) { // ??????????????????????????????
            return StatusCode.CHAT_FRIEND_GROUP_INVALID;
        }
        chatFriendGroupMapper.deleteByPrimaryKey(friendGroupId);
        chatFriendMapper.unGroupFriendsByGroupId(friendGroupId, null); // ?????????????????????????????????????????????
        return StatusCode.SUCCESS;
    }

    @Override
    public StatusCode moveFriendToGroup(String friendIds, Integer targetFriendGroupId) {
        if (this.isInvalidFriendGroup(targetFriendGroupId)) {
            return StatusCode.CHAT_FRIEND_GROUP_INVALID;
        }
        Set<Integer> friendIdSet = CommonUtils.splitIdStr(friendIds);
        if (!friendIdSet.isEmpty()) {
            // ?????????????????????????????????SQL???????????????????????????????????????????????????
            chatFriendMapper.updateGroupOfFriends(friendIdSet, targetFriendGroupId, ShiroUtils.getUserId());
        }
        return StatusCode.SUCCESS;
    }

    @Override
    public void deleteChatFriendByBothIds(Integer myId, Integer friendId) {
        Example example = new Example(ChatFriend.class);
        example.createCriteria().andEqualTo("myId", myId)
                .andEqualTo("friendId", friendId);
        chatFriendMapper.deleteByExample(example);
    }

    @Override
    @Transactional
    public StatusCode deleteChatFriend(Integer friendId) {
        ChatFriend friend = this.getMyChatFriendById(friendId);
        if (ObjectUtils.isEmpty(friend)) {
            return StatusCode.CHAT_FRIEND_NOT_EXIST;
        }
        Integer myId = ShiroUtils.getUserId();
        this.deleteChatFriendByBothIds(myId, friendId);
        this.deleteChatFriendByBothIds(friendId, myId); // ?????????????????????????????????????????????
        // ??????????????????????????????????????????????????????
        ChatNotificationDTO notification = new ChatNotificationDTO();
        notification.setReceiverId(friendId)
                .setSenderId(myId)
                .setAction("delete_friend");
        MsgModel msgModel = new MsgModel(MsgType.CHAT_NOTIFICATION, ChannelType.CHAT, notification);
        MsgCenter.getInstance().remoteDispatch(msgModel, null, ShiroUtils.getToken());
        return StatusCode.SUCCESS;
    }

    @Override
    public StatusCode sendChatFriendMsg(Integer friendId, String content, String sourceChannelId) {
        ChatFriend friend = this.getMyChatFriendById(friendId);
        if (ObjectUtils.isEmpty(friend)) {
            return StatusCode.CHAT_FRIEND_NOT_EXIST;
        }
        ChatFriendMsg msg = new ChatFriendMsg();
        msg.setId(RandomUtils.generateUUID())
                .setContent(content)
                .setSenderId(ShiroUtils.getUserId())
                .setReceiverId(friendId)
                .setCreateTime(LocalDateTime.now())
                .setSignFlag((byte) 0);
        MsgModel msgModel = new MsgModel(MsgType.CHAT_FRIEND, ChannelType.CHAT, msg);
        MsgCenter.getInstance().remoteDispatch(msgModel, sourceChannelId, ShiroUtils.getToken());
        return StatusCode.SUCCESS;
    }

    @Override
    public List<NotSignedChatFriendMsg> getNotSignedChatFriendMsg() {
        return chatFriendMsgMapper.selectNotSignedChatFriendMsg(ShiroUtils.getUserId());
    }

    @Override
    public void signChatFriendMsg(String msgIds) {
        Set<String> msgIdSet = new HashSet<>();
        String[] msgIdArr = msgIds.split(",");
        for (String msgId : msgIdArr) {
            if (msgId.trim().isEmpty()) {
                continue;
            }
            msgIdSet.add(msgId.trim());
        }
        Example example = new Example(ChatFriendMsg.class);
        example.createCriteria()
                .andEqualTo("receiverId", ShiroUtils.getUserId()) // ???????????????
                .andEqualTo("signFlag", 0) // ?????????
                .andIn("id", msgIdSet);
        ChatFriendMsg record = new ChatFriendMsg();
        record.setSignFlag((byte) 1);
        chatFriendMsgMapper.updateByExampleSelective(record, example);
    }
}
