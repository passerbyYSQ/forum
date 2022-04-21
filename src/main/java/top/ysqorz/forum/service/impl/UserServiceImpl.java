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
import top.ysqorz.forum.dao.*;
import top.ysqorz.forum.dto.req.CheckUserDTO;
import top.ysqorz.forum.dto.req.QueryUserCondition;
import top.ysqorz.forum.dto.req.RegisterDTO;
import top.ysqorz.forum.dto.resp.*;
import top.ysqorz.forum.dto.resp.chat.ChatUserCardDTO;
import top.ysqorz.forum.dto.resp.oauth.BaiduUserDTO;
import top.ysqorz.forum.dto.resp.oauth.GiteeUserDTO;
import top.ysqorz.forum.dto.resp.oauth.QQUserDTO;
import top.ysqorz.forum.oauth.BaiduProvider;
import top.ysqorz.forum.oauth.GiteeProvider;
import top.ysqorz.forum.oauth.QQProvider;
import top.ysqorz.forum.po.*;
import top.ysqorz.forum.service.RoleService;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.JwtToken;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.DateTimeUtils;
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
@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private BlacklistMapper blacklistMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RoleService roleService;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private GiteeProvider giteeProvider;
    @Resource
    private QQProvider qqProvider;
    @Resource
    private BaiduProvider baiduProvider;
    @Resource
    private FollowMapper followMapper;
    @Resource
    private PostMapper postMapper;
    @Resource
    private FirstCommentMapper firstCommentMapper;
    @Resource
    private CommentNotificationMapper commentNotificationMapper;


    @Override
    public User getUserByEmail(String email) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("email", email);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public User getUserByPhone(String phone) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("phone", phone);
        return userMapper.selectOneByExample(example);
    }

    @Override
    public List<User> getUsersLikeUsername(String username, boolean isExcludeMe) {
        Example example = new Example(User.class);
        example.orderBy("lastLoginTime").desc();
        Example.Criteria criteria = example.createCriteria();
        if (isExcludeMe) {
            criteria.andNotEqualTo("id", ShiroUtils.getUserId());
        }
        criteria.andLike("username", "%" + username + "%");
        return userMapper.selectByExample(example);
    }

    @Override
    public List<ChatUserCardDTO> getUserCardsLikeUsername(String username) {
        return userMapper.selectUserCardsLikeUsername(ShiroUtils.getUserId(), username);
    }

    @Override
    public List<UserDTO> getMyUserList(QueryUserCondition conditions) {
        //System.out.println(conditions);
        return userMapper.selectAllUser(conditions);
    }

    @Override
    public int updatePsw(Integer userId, String loginSalt) {
        User record = new User();
        record.setId(userId);
        record.setPassword(this.encryptLoginPwd("123456", loginSalt));
        return userMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateUserById(User user) {
        return userMapper.updateByPrimaryKeySelective(user);
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
    public void addRoleForUser(Integer[] roleIds, Integer userId) {
        for (Integer roleId : roleIds) {
            Role role = roleService.getRoleById(roleId);
            if (role == null) {
                throw new ParameterErrorException(StatusCode.ROLE_NOT_EXIST.getMsg());
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
        // 清除授权缓存
        ShiroUtils.clearUserAuthorizationCache(userId);
    }

    @Override
    public List<Role> getRoleByUserId(Integer userId) {
        return userRoleMapper.getRoleByUserId(userId);
    }

    @Override
    public void delRoleForUser(Integer[] roleIds, Integer userId) {
        for (Integer roleId : roleIds) {
            Example example = new Example(UserRole.class);
            example.createCriteria().andEqualTo("userId", userId)
                    .andEqualTo("roleId", roleId);
            userRoleMapper.deleteByExample(example);
        }
        // 清除授权缓存
        ShiroUtils.clearUserAuthorizationCache(userId);
    }

    @Override
    public void register(RegisterDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setUsername(dto.getUsername().trim());

        // 8个字符的随机字符串，作为加密登录的随机盐。
        String loginSalt = RandomUtils.generateStr(8);
        // 保存到user表，以后每次密码登录的时候，需要使用
        user.setLoginSalt(loginSalt);

        // Md5Hash默认将随机盐拼接到源字符串的前面，然后使用md5加密，再经过x次的哈希散列
        // 第三个参数（hashIterations）：哈希散列的次数
        String encryptPwd = this.encryptLoginPwd(dto.getPassword(), loginSalt);
        // 保存加密后的密码
        user.setPassword(encryptPwd)
                .setRegisterTime(LocalDateTime.now())
                .setModifyTime(LocalDateTime.now())
                .setConsecutiveAttendDays(0)
                .setRewardPoints(0)
                .setFansCount(0)
                .setGender((byte) 2) // 性别保密
                .setJwtSalt("")
                .setPhoto("/admin/assets/images/defaultUserPhoto.jpg");

        userMapper.insertSelective(user);
    }


    @Override
    public User oauth2Gitee(String code) throws IOException {
        GiteeUserDTO giteeUser = giteeProvider.getUser(code);
        User user = giteeProvider.getDbUser(giteeUser.getId());
        if (ObjectUtils.isEmpty(user)) {
            if (ShiroUtils.isAuthenticated()) {
                user = this.getUserById(ShiroUtils.getUserId());
                if (!ObjectUtils.isEmpty(user.getEmail())) { // 未绑定邮箱不允许操作
                    user.setGiteeId(giteeUser.getId());
                    userMapper.updateByPrimaryKeySelective(user);
                }
            } else {
                LocalDateTime now = LocalDateTime.now();
                user = new User();
                user.setGiteeId(giteeUser.getId())
                        .setUsername(giteeUser.getName())
                        .setPhoto(giteeUser.getAvatarUrl())
                        .setEmail(giteeUser.getEmail() != null ? giteeUser.getEmail() : "")
                        .setPassword("")
                        .setRegisterTime(now)
                        .setModifyTime(now)
                        .setConsecutiveAttendDays(0)
                        .setRewardPoints(0)
                        .setFansCount(0)
                        .setGender((byte) 2)
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
        // 第一次判断是否有该第三方授权绑定的用户
        // 有则说明此操作是：通过第三方账号登录或绑定时检测到已绑定该第三方账号
        if (ObjectUtils.isEmpty(user)) {
            // 第二次判断是已登录用户还是要注册用户
            if (ShiroUtils.isAuthenticated()) {
                // 已登录，说明此操作是：绑定第三方账号
                user = this.getUserById(ShiroUtils.getUserId());
                if (!ObjectUtils.isEmpty(user.getEmail())) {
                    user.setQqId(qqUser.getOpenId());
                    userMapper.updateByPrimaryKeySelective(user);
                }
            } else {
                // 未登录，说明此操作是：通过第三方账号授权注册
                LocalDateTime now = LocalDateTime.now();
                user = new User();
                user.setQqId(qqUser.getOpenId())
                        .setUsername(qqUser.getNickname())
                        .setPhoto(qqUser.getFigureurl_qq_1())
                        .setEmail("")
                        .setPassword("")
                        .setRegisterTime(now)
                        .setModifyTime(now)
                        .setConsecutiveAttendDays(0)
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
            if (ShiroUtils.isAuthenticated()) {
                user = this.getUserById(ShiroUtils.getUserId());
                if (!ObjectUtils.isEmpty(user.getEmail())) {
                    user.setBaiduId(baiduUser.getUk());
                    userMapper.updateByPrimaryKeySelective(user);
                }
            } else {
                LocalDateTime now = LocalDateTime.now();
                user = new User();
                user.setBaiduId(baiduUser.getUk())
                        .setUsername(baiduUser.getBaidu_name())
                        .setPhoto(baiduUser.getAvatar_url())
                        .setEmail("")
                        .setPassword("")
                        .setRegisterTime(now)
                        .setModifyTime(now)
                        .setConsecutiveAttendDays(0)
                        .setRewardPoints(0)
                        .setFansCount(0)
                        .setGender((byte) 2)
                        .setJwtSalt("")
                        .setLoginSalt(RandomUtils.generateStr(8));
                userMapper.insertUseGeneratedKeys(user);
            }
        }
        return user;
    }

    @Override
    public SimpleUserDTO getSimpleUser(Integer userId) {
        SimpleUserDTO simpleUserDTO = userMapper.selectSimpleUserById(userId);
        simpleUserDTO.setLevel(6); // TODO 根据积分计算level
        Example example = new Example(CommentNotification.class);
        example.createCriteria().andEqualTo("receiverId", userId).andEqualTo("isRead", 0);
        simpleUserDTO.setNewMeg(commentNotificationMapper.selectCountByExample(example));
        return simpleUserDTO;
    }

    @Override
    public SimpleUserDTO getHomeInformationById(Integer visitId) {
        SimpleUserDTO information = userMapper.selectHomeInformationById(visitId);
        information.setId(visitId);
        information.setLevel(6); // TODO 根据积分计算level
        return information;
    }

    @Override
    public Boolean isFocusOn(Integer visitId) {
        Example example = new Example(Follow.class);
        example.createCriteria()
                .andEqualTo("fromUserId", ShiroUtils.getUserId())
                .andEqualTo("toUserId", visitId);
        Follow follow = followMapper.selectOneByExample(example);
        return follow != null;
    }

    @Transactional
    @Override
    public void addFollow(Integer visitId) {
        Integer myId = ShiroUtils.getUserId();
        //user表对应数据+1粉丝数
        userMapper.updateAndAddFansCount(visitId);
        //follow表添加数据
        Follow follow = new Follow();
        follow.setFromUserId(myId);
        follow.setToUserId(visitId);
        follow.setCreateTime(DateTimeUtils.getFormattedTime());
        followMapper.insert(follow);
    }

    @Transactional
    @Override
    public void deleteFollow(Integer visitId) {
        Integer myId = ShiroUtils.getUserId();
        //user表对应数据-1粉丝数
        userMapper.updateAndReduceFansCount(visitId);
        //follow表删除数据
        Example example = new Example(Follow.class);
        example.createCriteria()
                .andEqualTo("fromUserId", myId)
                .andEqualTo("toUserId", visitId);
        followMapper.deleteByExample(example);
    }

    @Override
    public String login(Integer userId, String loginSalt, HttpServletResponse response) {
        this.updateLastLoginTime(userId);

        // shiro login
        JwtToken jwtToken = this.generateJwtToken(userId, loginSalt);
        Subject subject = SecurityUtils.getSubject();
        subject.login(jwtToken);

        // 将token放置到cookie中
        String token = (String) jwtToken.getCredentials();
        Cookie cookie = new Cookie("token", token);
        cookie.setMaxAge((int) Constant.DURATION_JWT.getSeconds());
        cookie.setPath("/"); // ！！！
        response.addCookie(cookie);
        response.setHeader("token", token);

        return token;
    }

    @Override
    public void logout() {
        this.updateLastLoginTime(ShiroUtils.getUserId());
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
    }

    @Override
    public JwtToken generateJwtToken(Integer userId, String jwtSalt) {
        String jwt = JwtUtils.generateJwt("userId", userId.toString(), jwtSalt, Constant.DURATION_JWT.toMillis());
        return new JwtToken(jwt);
    }

    @Override
    public String encryptLoginPwd(String originPwd, String salt) {
        return new Md5Hash(originPwd, salt, 1024).toHex();
    }

    @Override
    public int updateRewardPoints(Integer userId, Integer num) {
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        param.put("num", num);
        return userMapper.updateRewardPoints(param);
    }

    @Override
    public void updateLastLoginTime(Integer userId) {
        User record = new User();
        record.setId(userId).setLastLoginTime(LocalDateTime.now());
        userMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public SimpleUserDTO getLoginUser() {
        if (ShiroUtils.isAuthenticated()) { // 当前主体已登录
            Integer myId = ShiroUtils.getUserId(); // 当前登录用户的userId
            return this.getSimpleUser(myId);
        }
        return new SimpleUserDTO();
    }

    @Override
    public StatusCode checkUser(CheckUserDTO checkUser) {
        User user = this.getUserByEmail(checkUser.getOldEmail());
        // 验证登录身份
        if (!ObjectUtils.isEmpty(user) && user.getId().equals(ShiroUtils.getUserId())) {
            String encryptPwd = this.encryptLoginPwd(
                    checkUser.getCheckPassword(), user.getLoginSalt());
            if (user.getPassword().equals(encryptPwd)) {
                return StatusCode.SUCCESS;
            }
        }
        return StatusCode.ACCOUNT_OR_PASSWORD_INCORRECT;
    }

    /**
     * @param bindNum  指手机邮箱这类绑定号码
     * @param property 指明检查的第三方账号类型，举例：phone、email
     * @return true代表已有用户绑定，false代表没有用户绑定
     */
    @Override
    public Boolean checkBind(String bindNum, String property) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo(property, bindNum);
        User user = userMapper.selectOneByExample(example);
        return user != null;
    }
}
