package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

/** 后台话题管理请求table返回表单
 * @author 阿灿
 * @create 2021-05-24 21:44
 */
@Getter
@Setter
public class TopicDTO {
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
     * 创建者名字
     */

    private String username;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 排序权重
     */
    @Column(name = "sort_weight")
    private Integer sortWeight;

    /**
     * 是否归档。0：没有归档，1：已归档
     */
    private Byte archive;

    /**
     * 今日新增
     */
    private Integer todayCount;

}
