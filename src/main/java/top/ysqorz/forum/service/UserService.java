package top.ysqorz.forum.service;

import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.*;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.shiro.JwtToken;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author 阿灿
 * @create 2021-05-10 16:09
 */
public interface UserService {

    /**
     * 加减积分。注意自增和自减，只能自己写SQL语句，没办法使用tk
     */
    int updateRewardPoints(Integer userId, Integer num);

    /**
     * 退出登录的时候，需要清空Jwt的salt
     */
    int updateJwtSalt(Integer userId, String jwtSalt);

    /**
     * 登录
     */
    String login(Integer userId, HttpServletResponse response);

    /**
     * 退出登录
     */
    void logout();

    /**
     * 生成jwt字符串
     */
    JwtToken generateJwtToken(Integer userId, String jwtSalt);

    /**
     * 根据用户名查询用户
     */
    User getUserByEmail(String email);

    /**
     * 后台用户管理
     */
    List<UserDTO> getMyUserList(QueryUserCondition conditions);

    /**
     * 重置密码
     */
    int updatePsw(Integer userId);

    /**
     * 修改用户
     */
    int updateUserById(User user);

    /**
     * 根据id查询user信息
     */
    User getUserById(Integer userId);

    /**
     * 取消拉黑
     */
    int cancelBlock(Integer id);

    /**
     * 拉黑
     */
    int block(Blacklist blacklist);

    /**
     * 获取封禁信息
     */
    BlackInfoDTO getBlackInfo(Integer userId);

    /**
     * 获取所有角色信息
     */
    List<Role> getAllRole();

    /**
     * 为某一用户添加角色
     */
    int addRoleForUser(Integer[] roleIds, Integer userId);

    /**
     * 查询用户已有角色
     */
    List<Role> getRoleByUserId(Integer userId);

    /**
     * 删除用户已分配角色
     */
    int delRoleForUser(Integer[] roleIds, Integer userId);

    /**
     * 注册
     */
    void register(RegisterDTO vo);

    /**
     * gitee授权
     */
    User oauth2Gitee(String code) throws IOException;

    /**
     * qq授权
     */
    User oauth2QQ(String code) throws IOException;

    /**
     * 百度授权
     */
    User oauth2Baidu(String code) throws IOException;

    /**
     * 清除上一次shiro的认证缓存（实现单点登录）。
     * 我们不直接操作缓存，而是采取先login再logout的方式，
     * 让shiro帮我们清除缓存
     */
    void clearShiroAuthCache(User user);

    SimpleUserDTO getSimpleUser(Integer userId);

    SimpleUserDTO getLoginUser();

    /**
     * 检查用户账号密码是否正确
     */
    StatusCode checkUser(CheckUserDTO checkUser);

    /**
     * 检查所使用的第三方号码是否已绑定
     */
    Boolean checkBind(String bindNum, String property);

    /**
     * 获取主页信息
     */
    SimpleUserDTO getHomeInformationById(Integer visitId);

    /**
     * 判断是否关注
     */
    Boolean isFocusOn(Integer visitId);

    /**
     * 添加关注
     */
    void addFollow(Integer visitId);

    /**
     * 删除关注
     */
    void deleteFollow(Integer visitId);

    /**
     * 查找用户下属帖子信息
     */
    List<PostDTO> getPostInformation(Integer visitId);

    /**
     * 用户主页返回的信息
     */
    PageData<PostDTO> getIndexPost(Integer visitId,Integer page, Integer count);

    /**
     * 获取回复信息
     */
    PageData<FirstCommentDTO> getIndexFirstComment(Integer visitId, Integer page, Integer count);
}
