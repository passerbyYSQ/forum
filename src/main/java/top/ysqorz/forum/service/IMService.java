package top.ysqorz.forum.service;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2022-02-02 10:05
 */
public interface IMService {

    List<String> getIMServerIpList();

    String getRandWsServerUrl();

}
