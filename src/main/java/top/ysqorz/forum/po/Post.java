package top.ysqorz.forum.po;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import top.ysqorz.forum.common.enumeration.PostVisibility;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "post")
public class Post {
    /**
     * 帖子id
     */
    @NotNull
    @Id
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 发布者id
     */
    @Column(name = "creator_id")
    private Integer creatorId;


    /**
     * 所属话题
     */
    @Column(name = "topic_id")
    private Integer topicId;

    /**
     * 发布时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 上一次修改时间
     */
    @Column(name = "last_modify_time")
    private LocalDateTime lastModifyTime;

    /**
     * 访问数
     */
    @Column(name = "visit_count")
    private Integer visitCount;

    /**
     * 点赞数
     */
    @Column(name = "like_count")
    private Integer likeCount;

    /**
     * 收藏数
     */
    @Column(name = "collect_count")
    private Integer collectCount;

    /**
     * 总评论数
     */
    @Column(name = "comment_count")
    private Integer commentCount;

    /**
     * 是否为精品
     */
    @Column(name = "is_high_quality")
    private Byte isHighQuality;

    /**
     * 是否锁定。0：未锁定；1：锁定，锁定后不能评论，不能修改
     */
    @Column(name = "is_locked")
    private Byte isLocked;

    /**
     * 置顶权重
     */
    @NotNull
    @Column(name = "top_weight")
    private Integer topWeight;

    /**
     * 最后一次评论时间
     */
    @Column(name = "last_comment_time")
    private LocalDateTime lastCommentTime;

    /**
     * 可见策略（之后再考虑规划）
     */
    @Column(name = "visibility_type")
    private PostVisibility visibilityType;

    /**
     * 内容
     */
    private String content;

}
