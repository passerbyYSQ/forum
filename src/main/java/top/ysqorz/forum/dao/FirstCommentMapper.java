package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.resp.FirstCommentDTO;
import top.ysqorz.forum.dto.resp.RecentCommentUserDTO;
import top.ysqorz.forum.po.FirstComment;

import java.util.List;
import java.util.Map;

public interface FirstCommentMapper extends BaseMapper<FirstComment> {
    /**
     * 插入一级评论
     */
    int addFirstCommentUseGeneratedKeys(FirstComment comment);

    List<FirstCommentDTO> selectFirstCommentList(Map<String, Object> params);

    int addSecondCommentCount(Map<String, Object> params);

    /**
     *  获取个人主页需要的帖子内容
     */
    List<FirstCommentDTO> selectFirstCommentListByUserId(Integer userId);

    /**
     * 最近发表一级评论的用户
     */
    List<RecentCommentUserDTO> selectRecentCommentUsers(Integer count);
}
