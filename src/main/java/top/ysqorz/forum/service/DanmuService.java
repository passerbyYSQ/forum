package top.ysqorz.forum.service;

import top.ysqorz.forum.po.DanmuMsg;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2022-01-16 1:00
 */
public interface DanmuService {

    List<DanmuMsg> getDanmuListByVideoId(Integer videoId);
}
