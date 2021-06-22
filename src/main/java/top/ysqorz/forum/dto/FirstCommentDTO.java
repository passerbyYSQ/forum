package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 帖子下的一级评论
 *
 * @author passerbyYSQ
 * @create 2021-06-21 20:03
 */
@Getter
@Setter
public class FirstCommentDTO {
    private Integer id;
    private SimpleUserDTO creator;
    private String content;
    private Integer floorNum;
    private LocalDateTime createTime;

    private Boolean isPostCreator; // 是否是楼主
}
