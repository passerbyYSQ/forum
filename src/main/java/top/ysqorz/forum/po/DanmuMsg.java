package top.ysqorz.forum.po;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "danmu_msg")
public class DanmuMsg {
    /**
     * 弹幕的唯一id，由应用层生成
     */
    private String id;

    /**
     * 弹幕的文本内容
     */
    private String content;

    /**
     * 弹幕在视频的哪一毫秒进入
     */
    @Column(name = "start_ms")
    private Long startMs;

    /**
     * 视频的id
     */
    @Column(name = "video_id")
    private Integer videoId;

    /**
     * 发布者的id，发弹幕需要登录
     */
    @Column(name = "creator_id")
    private Integer creatorId;

    private DanmuCreator creator;

    /**
     * 发布时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Data
    @NoArgsConstructor
    public static class DanmuCreator {
        Integer userId;
        String username;
        String photo;

        public DanmuCreator(User user) {
            this.userId = user.getId();
            this.username = user.getUsername();
            this.photo = user.getPhoto();
        }
    }
}
