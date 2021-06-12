package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.dao.PostLabelMapper;
import top.ysqorz.forum.po.Label;
import top.ysqorz.forum.po.PostLabel;
import top.ysqorz.forum.service.LabelService;
import top.ysqorz.forum.service.PostLabelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2021-06-12 15:29
 */
@Service
public class PostLabelServiceImpl implements PostLabelService {

    @Resource
    private PostLabelMapper postLabelMapper;

    @Resource
    private LabelService labelService;

    @Override
    public int delPostLabelByPostId(Integer postId) {
        Example example = new Example(PostLabel.class);
        example.createCriteria().andEqualTo("postId", postId);
        return postLabelMapper.deleteByExample(example);
    }

    @Override
    public int addPostLabelList(Integer postId, Set<String> labelSet) {
        // postId --- labelId 的映射关系
        List<PostLabel> postLabels = new ArrayList<>();
        for (String name : labelSet) {
            Label label = labelService.getLabelByName(name);
            if (ObjectUtils.isEmpty(label)) { // 标签不存在，就创建
                label = labelService.addLabel(name); // 自增长id被设置进去了
            }
            if (!ObjectUtils.isEmpty(label.getId())) {
                postLabels.add(new PostLabel(postId, label.getId()));
            }
        }

        if (!ObjectUtils.isEmpty(postLabels)) { // 空集合，insertList会抛出异常
            // 批量插入帖子和标签的映射关系
            return postLabelMapper.insertList(postLabels);
        }
        return 0;
    }
}
