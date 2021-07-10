package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.dto.SecondCommentDTO;
import top.ysqorz.forum.po.SecondComment;

import java.util.List;

public interface SecondCommentMapper extends BaseMapper<SecondComment> {
    List<SecondCommentDTO> selectSecondCommentList(Integer firstCommentId);

    Integer getSecondPage(Integer firstMegId,Integer secondMegId);
}
