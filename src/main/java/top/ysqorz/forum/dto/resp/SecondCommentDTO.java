package top.ysqorz.forum.dto.resp;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author passerbyYSQ
 * @create 2021-06-22 15:51
 */
@Getter
@Setter
public class SecondCommentDTO {
    private Integer id;
    private SimpleUserDTO creator;
    private String content;
    private LocalDateTime createTime;
    private Boolean isPostCreator; // 是否是楼主

    private Integer quoteSecondCommentId;
    private String repliedCreator; // 被回复的用户的名称
    private Integer repliedCreatorId; // 被回复的用户的名称
}
