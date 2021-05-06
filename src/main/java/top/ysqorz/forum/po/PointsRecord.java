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
@Table(name = "points_record")
public class PointsRecord {
    @Id
    private Integer id;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 类型id
     */
    @Column(name = "points_type_id")
    private Integer pointsTypeId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}