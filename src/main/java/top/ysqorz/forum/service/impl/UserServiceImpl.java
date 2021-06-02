package top.ysqorz.forum.service.impl;

import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.dao.BlacklistMapper;
import top.ysqorz.forum.dao.RoleMapper;
import top.ysqorz.forum.dao.UserMapper;
import top.ysqorz.forum.dao.UserRoleMapper;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.po.UserRole;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.utils.JwtUtils;
import top.ysqorz.forum.utils.RandomUtils;
import top.ysqorz.forum.dto.BlackInfoDTO;
import top.ysqorz.forum.dto.QueryUserCondition;
import top.ysqorz.forum.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author 阿灿
 * @create 2021-05-10 16:10
 */
@Service("userService") // 不要忘了
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
    public User getUserByEmail(String email) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("email", email);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public List<UserDTO> getMyUserList(QueryUserCondition conditions) {
        //System.out.println(conditions);
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
        record.setIsRead((byte) 0); // 重置为未读
        record.setEndTime(LocalDateTime.now().minusMinutes(1));  //这里给解封时间设置为当前时间-1分钟
        return blacklistMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int block(Blacklist blacklist) {
        blacklist.setCreateTime(LocalDateTime.now());
        blacklist.setStartTime(LocalDateTime.now());
        blacklist.setIsRead((byte) 0);
        return blacklistMapper.insert(blacklist);
    }

    @Override
    public BlackInfoDTO getBlackInfo(Integer userId) {
        return blacklistMapper.getBlockInfo(userId, LocalDateTime.now());
    }

    @Override
    public List<Role> getAllRole() {
        return roleMapper.selectAll();
    }

    @Transactional // 开启事务操作
    @Override
    public int addRoleForUser(Integer[] roleIds, Integer userId) throws ParameterErrorException {
        for (Integer roleId : roleIds) {
            Example example2 = new Example(Role.class);
            example2.createCriteria().andEqualTo("id", roleId);
            Role role = roleMapper.selectOneByExample(example2);
            if (role == null) {
                throw new ParameterErrorException("角色不存在");
            }
            Example example = new Example(UserRole.class);
            example.createCriteria().andEqualTo("userId", userId)
                    .andEqualTo("roleId", roleId);
            if (userRoleMapper.selectCountByExample(example) == 0) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
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

    @Override
    public void register(User user) {

        // 8个字符的随机字符串，作为加密登录的随机盐。
        String salt = RandomUtils.generateStr(8);
        // 保存到user表，以后每次密码登录的时候，需要使用
        user.setLoginSalt(salt);

        // Md5Hash默认将随机盐拼接到源字符串的前面，然后使用md5加密，再经过x次的哈希散列
        // 第三个参数（hashIterations）：哈希散列的次数
        Md5Hash md5Hash = new Md5Hash(user.getPasssword(), user.getLoginSalt(), 1024);
        // 保存加密后的密码
        user.setPasssword(md5Hash.toHex());

        user.setRegisterTime(LocalDateTime.now());
        user.setModifyTime(LocalDateTime.now());
        user.setRewardPoints(0);
        user.setFansCount(0);

       userMapper.insertUseGeneratedKeys(user);
    }


    @Override
    public String generateJwt(User user) {
        // 8个字符的随机字符串，作为生成jwt的随机盐
        // 保证每次登录成功返回的Token都不一样
        String jwtSecret = RandomUtils.generateStr(8);
        // 将此次登录成功的jwt secret存到数据库，下次携带jwt时解密需要使用
        user.setJwtSalt(jwtSecret);
        userMapper.updateByPrimaryKeySelective(user);
        return JwtUtils.generateJwt("userId", user.getId().toString(),
                jwtSecret, TimeUnit.SECONDS.toMillis(60 * 60)); // 有效期 60s
    }


}
