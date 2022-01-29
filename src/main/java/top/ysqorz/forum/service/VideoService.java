package top.ysqorz.forum.service;

import top.ysqorz.forum.po.Video;

/**
 * @author passerbyYSQ
 * @create 2022-01-16 16:23
 */
public interface VideoService {

    Video getVideoDetailById(Integer videoId);

    Video getVideoById(Integer videoId);
}
