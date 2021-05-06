package top.ysqorz.forum.po;

import java.time.LocalDateTime;
import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "topic")
public class Topic {
    /**
     * 话题id
     */
    @Id
    private Integer id;

    /**
     * 话题名
     */
    @Column(name = "topic_name")
    private String topicName;

    /**
     * 话题描述
     */
    private String description;

    /**
     * 帖子数
     */
    @Column(name = "post_count")
    private Integer postCount;

    /**
     * 创建者
     */
    @Column(name = "create_id")
    private Integer createId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}