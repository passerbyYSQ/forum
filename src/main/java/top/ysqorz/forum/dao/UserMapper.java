package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.dto.UserDTO;
import top.ysqorz.forum.dto.QueryUserCondition;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {
    public List<UserDTO> selectAllUser(QueryUserCondition conditions);

}
