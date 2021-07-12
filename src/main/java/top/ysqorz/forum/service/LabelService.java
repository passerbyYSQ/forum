package top.ysqorz.forum.service;

import top.ysqorz.forum.po.Label;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-05-25 11:26
 */
public interface LabelService {

    /**
     * 查询帖子的相关的标签
     */
    List<Label> getLabelsByPostId(Integer postId);

    /**
     * 根据名字模糊匹配标签
     */
    List<Label> getLabelsLikeName(String labelName, Integer maxCount);

    /**
     * 根据名字查找标签
     */
    Label getLabelByName(String labelName);

    /**
     * 添加一个标签
     */
    Label addLabel(String labelName);

    /**
     * 随机获取指定数量个标签
     */
    List<Label> achieveRandomLabels(Integer total);
}
