package top.ysqorz.forum.service.impl;

import com.github.pagehelper.PageHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.dao.CommentNotificationMapper;
import top.ysqorz.forum.dao.FirstCommentMapper;
import top.ysqorz.forum.dao.SecondCommentMapper;
import top.ysqorz.forum.dto.MessageListDTO;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.po.CommentNotification;
import top.ysqorz.forum.service.MessageService;
import top.ysqorz.forum.shiro.ShiroUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 阿灿
 * @create 2021-07-02 22:02
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Resource
    private CommentNotificationMapper commentNotificationMapper;
    @Resource
    private SecondCommentMapper secondCommentMapper;
    @Resource
    private FirstCommentMapper firstCommentMapper;

    @Override
    public PageData<MessageListDTO> getMegList(Integer page, Integer count, Integer conditions) {
        PageHelper.startPage(page, count);
        List<MessageListDTO> megList;
        if (conditions == 0) {
            megList = commentNotificationMapper.getMegList(ShiroUtils.getUserId());
        } else {
            megList = commentNotificationMapper.getMegReply(ShiroUtils.getUserId());
            for (MessageListDTO meg : megList) {
                Document doc = Jsoup.parse(meg.getMyContent());
                String content = doc.text();  //获取html文本
                Elements images = doc.select("img[src]");
                if (images.size() > 0) {
                    content = content + "[图片]";
                }
                meg.setMyContent(content);
            }
        }
        return new PageData<>(megList);
    }

    @Override
    public int clearAllMeg() {
        Example example = new Example(CommentNotification.class);
        example.createCriteria().andEqualTo("receiverId", ShiroUtils.getUserId());

        CommentNotification record = new CommentNotification();
        record.setIsRead((byte) 1);
        return commentNotificationMapper.updateByExampleSelective(record, example);
    }


}
