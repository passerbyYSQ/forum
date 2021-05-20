package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.vo.BlackInfoVo;

import java.time.LocalDateTime;

public interface BlacklistMapper extends BaseMapper<Blacklist> {
    public BlackInfoVo getBlockInfo(Integer userId, LocalDateTime localDateTime);
}