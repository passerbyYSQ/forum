package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.vo.MyUser;
import top.ysqorz.forum.vo.QueryUserCondition;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    public List<MyUser> selectAllUser(QueryUserCondition conditions);
}