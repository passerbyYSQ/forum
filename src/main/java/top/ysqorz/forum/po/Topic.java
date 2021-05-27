package top.ysqorz.forum.po;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "topic")
public class Topic {

    public interface Add {
    }

    public interface Update {
    }

    /**
     * 话题id
     */
    @NotNull(groups = Update.class) // 分组校验
    @Min(value = 0, groups = Role.Update.class)
    @Id
    private Integer id;

    /**
     * 话题名
     */
    @NotEmpty
    @Size(min = 2, max = 32)
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

	/**
     * 排序权重
     */
    @NotNull
    @Min(value = 0)
    @Column(name = "sort_weight")
    private Integer sortWeight;

    /**
     * 是否归档。0：没有归档，1：已归档
     */
    private Byte archive;
}
