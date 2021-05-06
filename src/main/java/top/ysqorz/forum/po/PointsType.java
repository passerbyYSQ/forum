package top.ysqorz.forum.po;

import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "points_type")
public class PointsType {
    @Id
    private Integer id;

    /**
     * 描述
     */
    private String description;

    /**
     * 加减操作的分值
     */
    private Integer points;
}