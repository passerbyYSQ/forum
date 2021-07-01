package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.dao.CollectMapper;
import top.ysqorz.forum.dao.PostMapper;
import top.ysqorz.forum.po.Collect;
import top.ysqorz.forum.service.CollectService;
import top.ysqorz.forum.shiro.ShiroUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author passerbyYSQ
 * @create 2021-06-19 13:54
 */
@Service
public class CollectServiceImpl implements CollectService {

    @Resource
    private CollectMapper collectMapper;
    @Resource
    private PostMapper postMapper;

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
    public Collect addCollect(Integer postId) {
        Collect collect = new Collect();
        collect.setPostId(postId)
                .setUserId(ShiroUtils.getUserId())
                .setCreateTime(LocalDateTime.now()); // TODO 在表加上联合唯一键防止并发插入重复数据
        collectMapper.insertUseGeneratedKeys(collect);

        // 收藏数 +1
        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("dif", 1);
        postMapper.addCollectCount(params);

        return collect;
    }

    @Transactional
    @Override
    public int cancelCollect(Integer collectId, Integer postId) {
        // 收藏数 -1
        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("dif", -1);
        postMapper.addCollectCount(params);

        return collectMapper.deleteByPrimaryKey(collectId);
    }
}
