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
@Table(name = "blacklist")
public class Blacklist {
    @Id
    private Integer id;

    /**
     * 被封禁用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 操作人
     */
    @Column(name = "admin_id")
    private Integer adminId;

    /**
     * 起始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 封禁原因
     */
    private String reason;
}