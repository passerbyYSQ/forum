package top.ysqorz.forum.service.impl;

import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.dao.LabelMapper;
import top.ysqorz.forum.po.Label;
import top.ysqorz.forum.service.LabelService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author passerbyYSQ
 * @create 2021-05-25 11:26
 */
@Service
public class LabelServiceImpl implements LabelService {

    @Resource
    private LabelMapper labelMapper;

    @Override
    public List<Label> getLabelsByPostId(Integer postId) {
        return labelMapper.selectLabelsByPostId(postId);
    }

    @Override
    public List<Label> getLabelsLikeName(String labelName, Integer maxCount) {
        Example example = new Example(Label.class);
        if (!ObjectUtils.isEmpty(labelName)) {
            example.createCriteria().andLike("labelName", "%" + labelName + "%");
        }
        return labelMapper.selectByExampleAndRowBounds(example, new RowBounds(0, maxCount));
    }

    @Override
    public Label getLabelByName(String labelName) {
        Example example = new Example(Label.class);
        example.createCriteria().andEqualTo("labelName", labelName); // labelName是唯一键
        return labelMapper.selectOneByExample(example);
    }

    @Override
    public Label addLabel(String labelName) {
        Label label = new Label();
        label.setLabelName(labelName)
                .setDescription("")
                .setPostCount(1);
        labelMapper.insertUseGeneratedKeys(label); // 自动填充自增长主键
        return label;
    }

    @Override
    public List<Label> achieveRandomLabels(Integer total) {
        List<Label> labelList = new ArrayList<>();
        // 如果label的总个数小于total，则返回所有label
        if (labelMapper.selectCountOfLabels() <= total) {
            return labelMapper.selectAll();
        }
        // len为label的最大id值
        int len = labelMapper.selectLastIdOfLabels();
        int[] temp = new int[len];
        for (int i = 0; i < len; i++) {
            temp[i] = i + 1;
        }
        int num = total;
        // k为被淘汰或被使用的label，被淘汰是指label的id在len的范围内，但label已经被删除
        int k = 0;
        while (num > 0) {
            int i = (int)(Math.random() * (len - k) + 1);
            Label label = labelMapper.selectByPrimaryKey(temp[i]);
            if (label != null) {
                labelList.add(label);
                num--;
            }
            k++;
            // 将已经被使用或被淘汰的temp换成第len - k个temp，保证不会被重复使用
            temp[i] = temp[len - k - 1];
        }
        return labelList;
    }
}
