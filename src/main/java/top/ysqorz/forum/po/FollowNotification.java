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
@Table(name = "follow_notification")
public class FollowNotification {
    @Id
    private Integer id;

    /**
     * 关注id
     */
    @Column(name = "follow_id")
    private Integer followId;

    /**
     * 是否已读
     */
    @Column(name = "is_read")
    private Byte isRead;
}