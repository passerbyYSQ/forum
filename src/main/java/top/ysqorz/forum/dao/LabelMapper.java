package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.po.Label;

import java.util.List;

public interface LabelMapper extends BaseMapper<Label> {

    List<Label> selectLabelsByPostId(Integer postId);
}
