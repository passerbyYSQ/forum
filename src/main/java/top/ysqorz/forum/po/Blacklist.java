package top.ysqorz.forum.po;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    @NotNull
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 操作人
     */
    @NotNull
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
    @NotNull
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 操作时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 封禁原因
     */
    @NotEmpty
    private String reason;

    /**
     * 是否已被用户阅读
     */
    @Column(name = "is_read")
    private Byte isRead;

    @Override
    public String toString() {
        return "Blacklist{" +
                "id=" + id +
                ", userId=" + userId +
                ", adminId=" + adminId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", reason='" + reason + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
