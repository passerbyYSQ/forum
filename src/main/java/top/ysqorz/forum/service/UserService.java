package top.ysqorz.forum.service;

import top.ysqorz.forum.common.ParameterErrorException;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.Role;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.vo.BlackInfoVo;
import top.ysqorz.forum.vo.QueryUserCondition;
import top.ysqorz.forum.vo.UserVo;

import java.util.List;

/**
 * @author 阿灿
 * @create 2021-05-10 16:09
 */
public interface UserService {

    /**
     * 获取所有MyUser信息
     */
    List<UserVo> getMyUserList(QueryUserCondition conditions);

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
     *
     * @return
     */
    int block(Blacklist blacklist);

    /**
     * 获取封禁信息
     *
     * @param userId
     * @return
     */
    BlackInfoVo getBlackInfo(Integer userId);

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


    void register(User user);

    User login(String username, String password);

    String generateJwt(User user);



}
