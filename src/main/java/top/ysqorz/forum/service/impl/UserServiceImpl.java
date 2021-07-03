package top.ysqorz.forum.service.impl;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.common.Constant;
import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dao.BlacklistMapper;
import top.ysqorz.forum.dao.RoleMapper;
import top.ysqorz.forum.dao.UserMapper;
import top.ysqorz.forum.dao.UserRoleMapper;
import top.ysqorz.forum.dto.*;
import top.ysqorz.forum.oauth.BaiduProvider;
import top.ysqorz.forum.oauth.GiteeProvider;
import top.ysqorz.forum.oauth.QQProvider;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.po.UserRole;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.JwtToken;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.JwtUtils;
import top.ysqorz.forum.utils.RandomUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 阿灿
 * @create 2021-05-10 16:10
 */
@Service // 不要忘了
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;
    @Resource
    private BlacklistMapper blacklistMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private GiteeProvider giteeProvider;
    @Resource
    private QQProvider qqProvider;
	@Resource
    private BaiduProvider baiduProvider;

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
        record.setPassword("123456");
        return userMapper.updateByPrimaryKeySelective(record);
    }


    @Override
    public User getUserById(Integer userId) {
        return userMapper.selectByPrimaryKey(userId);
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
    public int addRoleForUser(Integer[] roleIds, Integer userId) {
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
        for (Integer roleId : roleIds) {
            Example example = new Example(UserRole.class);
            example.createCriteria().andEqualTo("userId", userId)
                    .andEqualTo("roleId", roleId);
            userRoleMapper.deleteByExample(example);
        }
        return 1;
    }

    @Override
    public void register(RegisterDTO vo) {

        User user = new User();
        user.setEmail(vo.getEmail());
        user.setUsername(vo.getUsername());

        // 8个字符的随机字符串，作为加密登录的随机盐。
        String salt = RandomUtils.generateStr(8);
        // 保存到user表，以后每次密码登录的时候，需要使用
        user.setLoginSalt(salt);

        // Md5Hash默认将随机盐拼接到源字符串的前面，然后使用md5加密，再经过x次的哈希散列
        // 第三个参数（hashIterations）：哈希散列的次数
        Md5Hash md5Hash = new Md5Hash(vo.getPassword(), user.getLoginSalt(), 1024);
        // 保存加密后的密码
        user.setPassword(md5Hash.toHex())
                .setRegisterTime(LocalDateTime.now())
                .setModifyTime(LocalDateTime.now())
                .setRewardPoints(0).setFansCount(0)
                .setGender((byte) 3) // 性别保密
                .setJwtSalt("")
                .setPhoto("/admin/assets/images/defaultUserPhoto.jpg");

        userMapper.insertSelective(user);
    }


    @Override
    public User oauth2Gitee(String code) throws IOException {
        GiteeUserDTO giteeUser = giteeProvider.getUser(code);
        User user = giteeProvider.getDbUser(giteeUser.getId());
        //第一次查找是否有该第三方授权绑定的用户，没有则查找是否已经有登录用户
        if (ObjectUtils.isEmpty(user)) {
            user = new User();
            if (ShiroUtils.isAuthenticated()) {
                user.setGiteeId(giteeUser.getId());
                userMapper.updateByPrimaryKey(user);
            } else {
                LocalDateTime now = LocalDateTime.now();
                user.setGiteeId(giteeUser.getId())
                        .setUsername(giteeUser.getName())
                        .setPhoto(giteeUser.getAvatarUrl())
                        .setEmail(giteeUser.getEmail() != null ? giteeUser.getEmail() : "")
                        .setPassword("")
                        .setRegisterTime(now)
                        .setModifyTime(now)
                        .setRewardPoints(0)
                        .setFansCount(0)
                        .setGender((byte) 3)
                        .setJwtSalt("")
                        .setLoginSalt(RandomUtils.generateStr(8));
                userMapper.insertUseGeneratedKeys(user); // 填充了主键
            }
        }
        return user;
    }

    @Override
    public User oauth2QQ(String code) throws IOException {
        QQUserDTO qqUser = qqProvider.getUser(code);
        User user = qqProvider.getDbUser(qqUser.getOpenId());
        //第一次查找是否有该第三方授权绑定的用户，没有则找到则判断当前是否已经登录
        if (ObjectUtils.isEmpty(user)) {
            user = new User();
            if (ShiroUtils.isAuthenticated()) {
                // 已经登录，说明此操作是：绑定第三方账号
                user.setQqId(qqUser.getOpenId());
                userMapper.updateByPrimaryKeySelective(user);
            } else {
                // 未登录，说明此操作是：通过第三方账号授权注册
                LocalDateTime now = LocalDateTime.now();
                user.setQqId(qqUser.getOpenId())
                        .setUsername(qqUser.getNickname())
                        .setPhoto(qqUser.getFigureurl_qq_1())
                        .setEmail("")
                        .setPassword("")
                        .setRegisterTime(now)
                        .setModifyTime(now)
                        .setRewardPoints(0)
                        .setFansCount(0)
                        .setGender((byte) ("男".equals(qqUser.getGender()) ? 0 : 1))
                        .setJwtSalt("")
                        .setLoginSalt(RandomUtils.generateStr(8));
                userMapper.insertUseGeneratedKeys(user);
            }
        }
        return user;
    }

	@Override
    public User oauth2Baidu(String code) throws IOException {
        BaiduUserDTO baiduUser = baiduProvider.getUser(code);
        User user = baiduProvider.getDbUser(baiduUser.getUk());
        //第一次查找是否有该第三方授权绑定的用户，没有则查找是否已经有登录用户
        if (ObjectUtils.isEmpty(user)) {
            user = new User();
            if (ShiroUtils.isAuthenticated()) {
                user.setBaiduId(baiduUser.getUk());
                userMapper.updateByPrimaryKey(user);
            } else {
                LocalDateTime now = LocalDateTime.now();
                user.setBaiduId(baiduUser.getUk())
                        .setUsername(baiduUser.getBaidu_name())
                        .setPhoto(baiduUser.getAvatar_url())
                        .setEmail("")
                        .setPassword("")
                        .setRegisterTime(now)
                        .setModifyTime(now)
                        .setRewardPoints(0)
                        .setFansCount(0)
                        .setGender((byte) 3)
                        .setJwtSalt("")
                        .setLoginSalt(RandomUtils.generateStr(8));
                userMapper.insertUseGeneratedKeys(user);
            }
        }
        return user;
    }

    @Override
    public void clearShiroAuthCache(User user) {
        Subject subject = SecurityUtils.getSubject();
        // 旧的盐未被清空说明，已经登录尚未退出
        if (!ObjectUtils.isEmpty(user.getJwtSalt())) {
            // 根据旧盐再一次生成旧的token
            JwtToken oldToken = this.generateJwtToken(user.getId(), user.getJwtSalt());
            subject.login(oldToken);
            this.logout(); // 清除旧token的缓存
        }
    }

    @Override
    public SimpleUserDTO getSimpleUser(Integer userId) {
        SimpleUserDTO simpleUserDTO = userMapper.selectSimpleUserById(userId);
        simpleUserDTO.setLevel(6); // TODO 根据积分计算level
        return simpleUserDTO;
    }


    @Override
    public String login(Integer userId, HttpServletResponse response) {
        String jwtSecret = RandomUtils.generateStr(8);
        // 更新数据库中的jwt salt
        this.updateJwtSalt(userId, jwtSecret);

        // shiro login
        JwtToken jwtToken = this.generateJwtToken(userId, jwtSecret);
        Subject subject = SecurityUtils.getSubject();
        subject.login(jwtToken);

        // 将token放置到cookie中
        String token = (String) jwtToken.getCredentials();
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge((int) Constant.DURATION_JWT.getSeconds());
        cookie.setPath("/"); // ！！！
        response.addCookie(cookie);

        return token;
    }

    @Override
    public void logout() {
        Subject subject = SecurityUtils.getSubject();
        // 为什么可以强制转成User？与在Realm中认证方法返回的SimpleAuthenticationInfo()的第一个参数有关
        Integer userId = (Integer) subject.getPrincipal();
        this.updateJwtSalt(userId, "");
        subject.logout();

    }


    @Override
    public JwtToken generateJwtToken(Integer userId, String jwtSalt) {
        String jwt = JwtUtils.generateJwt("userId", userId.toString(),
                jwtSalt, Constant.DURATION_JWT.toMillis());
        return new JwtToken(jwt);
    }

    @Override
    public int updateRewardPoints(Integer userId, Integer num) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("num", num);
        return userMapper.updateRewardPoints(param);
    }

    @Override
    public int updateJwtSalt(Integer userId, String jwtSalt) {
        User record = new User();
        record.setId(userId);
        record.setJwtSalt(jwtSalt);
        record.setLastLoginTime(LocalDateTime.now());
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public SimpleUserDTO getLoginUser() {
        if (ShiroUtils.isAuthenticated()) { // 当前主体已登录
            Integer myId = ShiroUtils.getUserId(); // 当前登录用户的userId
            return this.getSimpleUser(myId);
        }
        return new SimpleUserDTO();
    }

    /**
     * @param status:
     * 1代表手机绑定
     * 2代表邮箱绑定
     * 3代表QQ解绑
     * 4代表Gitee解绑
     * 5代表百度解绑
     */
    @Override
    public int changeUser(CheckUserDTO checkUser, int status) {
        User user = getUserByEmail(checkUser.getOldEmail());
        //检查账号密码是否错误、手机号或邮箱是否已经被绑定以及是否为当前用户，无错则绑定新手机号码并返回true
        if (user != null && user.getId().equals(ShiroUtils.getUserId())) {
            Md5Hash md5Hash = new Md5Hash(checkUser.getCheckPassword(), user.getLoginSalt(), 1024);
            if (user.getPassword().equals(md5Hash.toHex())) {
                User user1 = null;
                //检验手机号是否已经被绑定
                Example example = new Example(User.class);
                if (status == 1 && checkUser.getNewPhone() != null) {
                    example.createCriteria().andEqualTo("phone", checkUser.getNewPhone());
                    user1 = userMapper.selectOneByExample(example);
                    if (user1 == null) {
                        user.setPhone(checkUser.getNewPhone());
                        userMapper.updateByPrimaryKey(user);
                        return StatusCode.SUCCESS.getCode();
                    } else {
                        return StatusCode.PHONE_IS_EXIST.getCode();
                    }
                }
                //检验邮箱是否已经被绑定
                if (status == 2 && checkUser.getNewEmail() != null) {
                    example.createCriteria().andEqualTo("email", checkUser.getNewEmail());
                    user1 = userMapper.selectOneByExample(example);
                    if (user1 == null) {
                        user.setEmail(checkUser.getNewEmail());
                        userMapper.updateByPrimaryKey(user);
                        return StatusCode.SUCCESS.getCode();
                    } else {
                        return StatusCode.EMAIL_IS_EXIST.getCode();
                    }
                }
                if (status == 3) {
                    user.setQqId(null);
                    userMapper.updateByPrimaryKey(user);
                    return StatusCode.SUCCESS.getCode();
                }
                if (status == 4) {
                    user.setGiteeId(null);
                    userMapper.updateByPrimaryKey(user);
                    return StatusCode.SUCCESS.getCode();
                }
                if (status == 5) {
                    user.setBaiduId(null);
                    userMapper.updateByPrimaryKey(user);
                    return StatusCode.SUCCESS.getCode();
                }
            }
        }
        return StatusCode.ACCOUNT_OR_PASSWORD_INCORRECT.getCode();
    }

}
