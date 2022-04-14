package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.resp.SecondCommentDTO;
import top.ysqorz.forum.dto.resp.RecentCommentUserDTO;
import top.ysqorz.forum.po.SecondComment;

import java.util.List;

public interface SecondCommentMapper extends BaseMapper<SecondComment> {
    List<SecondCommentDTO> selectSecondCommentList(Integer firstCommentId);

    /**
     * 最近发表二级评论的用户
     */
    List<RecentCommentUserDTO> selectRecentCommentUsers(Integer count);
}
