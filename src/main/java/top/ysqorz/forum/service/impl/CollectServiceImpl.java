package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.dao.CollectMapper;
import top.ysqorz.forum.po.Collect;
import top.ysqorz.forum.service.CollectService;

import javax.annotation.Resource;

/**
 * @author passerbyYSQ
 * @create 2021-06-19 13:54
 */
@Service
public class CollectServiceImpl implements CollectService {

    @Resource
    private CollectMapper collectMapper;

    @Override
    public Collect getCollectByUserIdAndPostId(Integer userId, Integer postId) {
        Example example = new Example(Collect.class);
        example.createCriteria()
                .andEqualTo("userId", userId)
                .andEqualTo("postId", postId);
        return collectMapper.selectOneByExample(example);
    }
}
