package top.ysqorz.forum.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dao.ChatFriendApplyMapper;
import top.ysqorz.forum.dao.ChatFriendMapper;
import top.ysqorz.forum.dto.resp.ChatUserCardDTO;
import top.ysqorz.forum.po.ChatFriend;
import top.ysqorz.forum.po.ChatFriendApply;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.ChatService;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * 暂不支持状态条件的筛选，故status暂时没用到
     */
    @Override
    public List<ChatUserCardDTO> getChatUserCards(String keyword, String status, Integer page, Integer count) {
        List<User> userList = new ArrayList<>();
        User accurateUser = null; // 精确结果
        if (keyword.matches(Constant.REGEX_PHONE)) {
            accurateUser = userService.getUserByPhone(keyword);
        } else if (keyword.matches(Constant.REGEX_EMAIL)) {
            accurateUser = userService.getUserByEmail(keyword);
        }
        if (!ObjectUtils.isEmpty(accurateUser)) { // 能够精确查找到，直接返回
            userList.add(accurateUser);
        } else { // 模糊匹配
            PageHelper.startPage(page, count); // 分页
            userList = userService.getUsersLikeUsername(keyword);
        }

        ChatService chatService = this;
        return userList.stream().map(user -> {
            ChatUserCardDTO userCard = new ChatUserCardDTO(user);
            ChatFriend chatFriend = chatService.getChatFriendByBothIds(user.getId());
            boolean isOnline = redisService.isUserOnline(user.getId());
            userCard.setIsChatFriend(!ObjectUtils.isEmpty(chatFriend))
                    .setAlias(chatFriend.getAlias())
                    .setStatus(isOnline ? "online" : "offline");
            return userCard;
        }).collect(Collectors.toList());
    }

    @Override
    public ChatFriend getChatFriendByBothIds(Integer friendId) {
        Example example = new Example(ChatFriend.class);
        example.createCriteria().andEqualTo("myId", ShiroUtils.getUserId())
                .andEqualTo("friendId", friendId);
        return chatFriendMapper.selectOneByExample(example);
    }

    @Override
    public StatusCode applyFriend(Integer receiverId, String content) {
        ChatFriend chatFriend = this.getChatFriendByBothIds(receiverId);
        if (!ObjectUtils.isEmpty(chatFriend)) { // 已经是好友，不能发送申请
            return StatusCode.CHAT_ALREADY_FRIEND;
        }
        User receiver = userService.getUserById(receiverId);
        if (ObjectUtils.isEmpty(receiver)) { // 确保插入到数据库的receiverId是正确的
            return StatusCode.USER_NOT_EXIST;
        }

        ChatFriendApply friendApply = this.getFriendApplyByBothIds(receiverId);
        if (ObjectUtils.isEmpty(friendApply)) { // 不存在，则插入新的记录
            this.addFriendApply(receiverId, content);
        } else {
            // 好友同意和拒绝后，对方签收后才删除对应；忽略后，立即删除
            // 已经存在申请记录，可能是：1.接收者同意或者拒绝，但发送者还没签收；2.接收者尚未处理；

            // 1.status不为null，代表接收者尚已经处理该申请（拒绝，因为如果是好友在前面已经被return了）
            if (friendApply.getStatus() != null) {
                this.deleteFriendApplyById(friendApply.getId());
                return StatusCode.CHAT_FRIEND_APPLY_REFUSED;
            }
            // 2.接收者尚未处理该申请
            this.updateFriendApplyById(friendApply.getId(), content);
        }
        return StatusCode.SUCCESS;
    }

    @Override
    public ChatFriendApply getFriendApplyByBothIds(Integer receiverId) {
        Example example = new Example(ChatFriendApply.class);
        example.createCriteria().andEqualTo("senderId", ShiroUtils.getUserId()) // sender is me
                .andEqualTo("receiverId", receiverId);
        return chatFriendApplyMapper.selectOneByExample(example);
    }

    @Override
    public void addFriendApply(Integer receiverId, String content) {
        ChatFriendApply friendApply = new ChatFriendApply();
        friendApply.setSenderId(ShiroUtils.getUserId())
                .setReceiverId(receiverId)
                .setContent(content)
                .setApplyTime(LocalDateTime.now());
        // 不需要设置status，因为status为null，代表接收者尚未处理该申请
        chatFriendApplyMapper.insert(friendApply);
    }

    @Override
    public void deleteFriendApplyById(Integer friendApplyId) {
        chatFriendApplyMapper.deleteByPrimaryKey(friendApplyId);
    }

    @Override
    public void updateFriendApplyById(Integer friendApplyId, String content) {
        ChatFriendApply friendApply = new ChatFriendApply();
        friendApply.setId(friendApplyId)
                .setContent(content)
                .setApplyTime(LocalDateTime.now());
        chatFriendApplyMapper.updateByPrimaryKeySelective(friendApply); // 注意，Selective更新
    }
}
