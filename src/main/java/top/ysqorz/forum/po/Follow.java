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
@Table(name = "follow")
public class Follow {
    @Id
    private Integer id;

    /**
     * 发起者id
     */
    @Column(name = "from_user_id")
    private Integer fromUserId;

    /**
     * 被关注者
     */
    @Column(name = "to_user_id")
    private Integer toUserId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private String createTime;
}