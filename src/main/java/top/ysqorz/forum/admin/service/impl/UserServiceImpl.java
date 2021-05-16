package top.ysqorz.forum.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.admin.service.UserService;
import top.ysqorz.forum.common.PageData;
import top.ysqorz.forum.dao.BlacklistMapper;
import top.ysqorz.forum.dao.ResourceMapper;
import top.ysqorz.forum.dao.UserMapper;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.vo.MyUser;
import top.ysqorz.forum.vo.QueryAuthorityCondition;
import top.ysqorz.forum.vo.QueryUserCondition;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author 阿灿
 * @create 2021-05-10 16:10
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private BlacklistMapper blacklistMapper;


    @Override
    public List<MyUser> getMyUserList(QueryUserCondition conditions) {

        if (conditions != null) {
            conditions.fillDefault(); // 填充默认值
            if(!"".equals(conditions.getTime())){
                String[] time=conditions.getTime().split(" - ");
                // System.out.println(time[0]+time[1]);
                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                conditions.setStarttime(LocalDateTime.parse(time[0],df));
                conditions.setEndtime(LocalDateTime.parse(time[1],df));
                System.out.println(conditions.getEndtime());
                System.out.println(conditions.getStarttime());
            }
        }
        System.out.println(conditions);
        return userMapper.selectAllUser(conditions);
    }

    @Override
    public int updatePsw(User user) {
        user.setPasssword("123456");

        return userMapper.updateByPrimaryKey(user);
    }


    @Override
    public User getinfobyId(Integer id) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("id", id);

        return userMapper.selectOneByExample(example);
    }

    @Override
    public int cancelblock(Integer id) {
        Example example = new Example(Blacklist.class);
        example.createCriteria().andEqualTo("userId", id);

        return blacklistMapper.deleteByExample(example);
    }

    @Override
    public int block(Blacklist blacklist) {
        return blacklistMapper.insert(blacklist);
    }


}
