package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.vo.BlackInfoVo;
import top.ysqorz.forum.vo.UserVo;
import top.ysqorz.forum.vo.QueryUserCondition;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    public List<UserVo> selectAllUser(QueryUserCondition conditions);

}