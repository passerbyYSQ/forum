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
@Table(name = "system_notification")
public class SystemNotification {
    /**
     * 通知id
     */
    @Id
    private Integer id;

    /**
     * 内容
     */
    private String content;

    /**
     * 是否紧急
     */
    @Column(name = "is_urgent")
    private Byte isUrgent;

    /**
     * 时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;
}