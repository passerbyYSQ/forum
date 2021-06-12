package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;
import top.ysqorz.forum.po.Label;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台帖子管理
 *
 * @author passerbyYSQ
 * @create 2021-06-07 10:54
 */
@Getter
@Setter
public class SimplePostDTO {

    private Integer id; // 帖子id
    private String title; // 标题
    private String creator; // 发布者的用户名
    private String topic; // 所属话题
    private LocalDateTime createTime;
    private Integer visitCount; // 访问量
    private Integer likeCount; // 点赞数
    private Integer collectCount; // 收藏数
    private Integer commentCount; // 评论数
    private Byte isHighQuality; // 是否精品帖
    private Byte isLocked; // 是否锁定
    private Integer topWeight; // 置顶权重

    private List<Label> labelList;  // 标签
}
