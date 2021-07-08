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
    public List<Label> achieveRandomLabelList(Integer total) {
        List<Label> list = new ArrayList<>();
        // size为label的总个数
        int size = labelMapper.selectCountOfLabels();
        // 若label的总个数小于total个则返回所有的label
        if (size <= total) {
            list = labelMapper.selectAll();
            return list;
        }
        // len为label的id的最大值
        int len = labelMapper.selectMaxIdFromLabels();
        int[] ran = new int[len];
        for (int i = 0; i < len; i++) {
            ran[i] = i + 1;
        }
        int num = total;
        // n为被淘汰或者被使用的label的id个数，被淘汰是指id在length内但无此label的情况
        int n = 0;
        while (num > 0) {
            // k表示正在获取的是第k个label
            int k = total - num;
            // myIndex是从ran中获取id的位置，每次
            int myIndex = (int)(Math.random()*(len - k));
            // 每次取label从ran中抽取id
            Label label = labelMapper.selectByPrimaryKey(ran[myIndex]);
            if (label != null) {
                list.add(label);
                num--;
            }
            // 将当前使用过的id换为第len - n - 1个id，保证用过的id不会再出现
            ran[myIndex] = ran[len - n - 1];
            n++;
        }
        return list;
    }

}
