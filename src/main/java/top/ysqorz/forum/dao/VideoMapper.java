package top.ysqorz.forum.dao;

import top.ysqorz.forum.common.BaseMapper;
import top.ysqorz.forum.po.Video;

public interface VideoMapper extends BaseMapper<Video> {
    Video selectVideoDetailById(Integer videoId);
}
