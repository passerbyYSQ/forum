package top.ysqorz.forum.admin.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.admin.service.UserService;
import top.ysqorz.forum.dao.*;

import top.ysqorz.forum.po.*;

import top.ysqorz.forum.dao.UserMapper;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.vo.BlackInfoVo;
import top.ysqorz.forum.vo.UserVo;
import top.ysqorz.forum.vo.QueryUserCondition;

import java.time.LocalDateTime;
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
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;


    @Override
    public List<UserVo> getMyUserList(QueryUserCondition conditions) {

//        if (conditions != null) {
//            conditions.fillDefault(); // 填充默认值
////            if(!"".equals(conditions.getTime())){
////                String[] time=conditions.getTime().split(" - ");
////                // System.out.println(time[0]+time[1]);
////                DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
////                conditions.setStarttime(LocalDateTime.parse(time[0],df));
////                conditions.setEndtime(LocalDateTime.parse(time[1],df));
////                System.out.println(conditions.getEndtime());
////                System.out.println(conditions.getStarttime());
////            }
//        }
        System.out.println(conditions);
        return userMapper.selectAllUser(conditions);
    }

    @Override
    public int updatePsw(Integer userId) {

        User record = new User();
        record.setId(userId);
        record.setPasssword("123456");
        return userMapper.updateByPrimaryKeySelective(record);

    }


    @Override
    public User getInfoById(Integer id) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("id", id);

        return userMapper.selectOneByExample(example);
    }

    @Override
    public int cancelBlock(Integer id) {
        Example example = new Example(Blacklist.class);
        example.createCriteria().andEqualTo("userId", id)
                .andGreaterThan("endTime", LocalDateTime.now());

        Blacklist record = new Blacklist();
        record.setEndTime(LocalDateTime.now().minusMinutes(1));  //这里给解封时间设置为当前时间-1分钟
        return blacklistMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int block(Blacklist blacklist) {
        return blacklistMapper.insert(blacklist);
    }

    @Override
    public BlackInfoVo getBlackInfo(Integer userId) {

        return blacklistMapper.getBlockInfo(userId, LocalDateTime.now());
    }

    @Override
    public List<Role> getAllRole() {
        return roleMapper.selectAll();
    }

    @Override
    public int addRoleForUser(Integer[] roleIds, Integer userId) {
        for (int i = 0; i < roleIds.length; i++) {
            Example example = new Example(UserRole.class);
            example.createCriteria().andEqualTo("userId", userId)
                    .andEqualTo("roleId", roleIds[i]);
            if (userRoleMapper.selectCountByExample(example) == 0) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleIds[i]);
                userRole.setUserId(userId);
                userRole.setCreateTime(LocalDateTime.now());
                userRoleMapper.insert(userRole);
            }

        }
        return 1;
    }

    @Override
    public List<Role> getRoleByUserId(Integer userId) {
        return userRoleMapper.getRoleByUserId(userId);
    }

    @Override
    public int delRoleForUser(Integer[] roleIds, Integer userId) {
        for (int i = 0; i < roleIds.length; i++) {
            Example example = new Example(UserRole.class);
            example.createCriteria().andEqualTo("userId", userId)
                    .andEqualTo("roleId", roleIds[i]);
            userRoleMapper.deleteByExample(example);

        }
        return 1;
    }


}
