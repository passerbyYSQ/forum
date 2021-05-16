package top.ysqorz.forum.admin.service;

import top.ysqorz.forum.common.PageData;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.vo.MyUser;
import top.ysqorz.forum.vo.QueryAuthorityCondition;
import top.ysqorz.forum.vo.QueryUserCondition;

import java.util.List;

/**
 * @author 阿灿
 * @create 2021-05-10 16:09
 */
public interface UserService {

    /**
     * 获取所有MyUser信息
     *
     */
    List<MyUser> getMyUserList(QueryUserCondition conditions);

    /**
     * 重置密码
     */
    int updatePsw(User  user);
    /**
     * 根据id查询user信息
     *
     */
    User getinfobyId(Integer id);
    /**
     * 取消拉黑
     */
    int cancelblock(Integer id);

    /**
     * 拉黑
     *
     * @return
     */
    int block(Blacklist blacklist);
}
