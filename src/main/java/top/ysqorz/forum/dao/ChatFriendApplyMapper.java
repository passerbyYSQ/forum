package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.resp.chat.ChatFriendApplyDTO;
import top.ysqorz.forum.po.ChatFriendApply;

import java.util.List;

public interface ChatFriendApplyMapper extends BaseMapper<ChatFriendApply> {
    /**
     * 查询好友申请通知
     */
    List<ChatFriendApplyDTO> selectFriendApplyNotifications(Integer myId);
}
