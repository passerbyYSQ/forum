package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.dao.CollectMapper;
import top.ysqorz.forum.po.Collect;
import top.ysqorz.forum.service.CollectService;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @author passerbyYSQ
 * @create 2021-06-19 13:54
 */
@Service
public class CollectServiceImpl implements CollectService {

    @Resource
    private CollectMapper collectMapper;

    // 循环依赖
//    @Resource
//    private PostService postService;

    @Override
    public Collect getCollectByUserIdAndPostId(Integer userId, Integer postId) {
        Example example = new Example(Collect.class);
        example.createCriteria()
                .andEqualTo("userId", userId)
                .andEqualTo("postId", postId);
        return collectMapper.selectOneByExample(example);
    }

    @Transactional
    @Override
    public Collect addCollect(Integer userId, Integer postId) {
        Collect collect = new Collect();
        collect.setPostId(postId)
                .setUserId(userId)
                .setCreateTime(LocalDateTime.now()); // TODO 在表加上联合唯一键防止并发插入重复数据
        collectMapper.insertUseGeneratedKeys(collect);

        // TODO 收藏数 +1

        return collect;
    }

    @Transactional
    @Override
    public int cancelCollect(Integer collectId) {
        // TODO 收藏数 -1


        return collectMapper.deleteByPrimaryKey(collectId);
    }
}
