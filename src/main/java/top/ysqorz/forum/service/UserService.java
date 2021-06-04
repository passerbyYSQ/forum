package top.ysqorz.forum.service;

import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.dto.*;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.shiro.JwtToken;

import java.util.List;

/**
 * @author 阿灿
 * @create 2021-05-10 16:09
 */
public interface UserService {

    /**
     * 退出登录的时候，需要清空Jwt的salt
     */
    int updateJwtSalt(Integer userId, String jwtSalt);

    /**
     * 登录
     */
    String login(Integer userId);

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
     * 根据id查询user信息
     */
    User getInfoById(Integer id);

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
    int addRoleForUser(Integer[] roleIds, Integer userId) throws ParameterErrorException;

    /**
     * 查询用户已有角色
     */
    List<Role> getRoleByUserId(Integer userId);

    /**
     * 删除用户已分配角色
     */
    int delRoleForUser(Integer[] roleIds, Integer userId);


    void register(RegisterDTO vo);

}
