package top.ysqorz.forum.service;

import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.resp.FirstCommentDTO;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.resp.SecondCommentDTO;
import top.ysqorz.forum.dto.resp.RecentCommentUserDTO;
import top.ysqorz.forum.po.FirstComment;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.po.SecondComment;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-06-20 16:41
 */
public interface CommentService {

    /**
     * 该条一级评论前面有多少条评论
     */
    int getFrontFirstCommentCount(Integer firstCommentId);
    /**
     * 该条二级评论前面有多少条评论
     */
    int[] getFrontSecondCommentCount(Integer secondCommentId);

    // 发布一级评论
    void publishFirstComment(Post post, String content);

    // 发布二级评论
    void publishSecondComment(FirstComment firstComment, SecondComment quoteComment,
                              String content);

    /**
     * 某个帖子的一级评论列表
     * @param page      当前页
     * @param count     每一页显示的记录数
     * @param isTimeAsc 是否按照发表时间升序
     */
    PageData<FirstCommentDTO> getFirstCommentList(Post post, Integer page, Integer count, Boolean isTimeAsc);

    FirstComment getFirstCommentById(Integer firstCommentId);

    // 二级评论列表
    PageData<SecondCommentDTO> getSecondCommentList(FirstComment firstComment, Integer page, Integer count);

    SecondComment getSecondCommentById(Integer secondCommentId);

    int addSecondCommentCount(Integer firstCommentId, Integer dif);

    /**
     * 删除一级、二级评论
     */
    StatusCode deleteCommentById(Integer commentId, String type);

    /**
     * 获取最近发表评论的用户
     */
    List<RecentCommentUserDTO> getRecentCommentUsers();

    /**
     * 获取指定用户发布的一级评论，在个人主页中显示
     */
    PageData<FirstCommentDTO> getFirstCommentListByCreatorId(Integer userId, Integer page, Integer count);
}
