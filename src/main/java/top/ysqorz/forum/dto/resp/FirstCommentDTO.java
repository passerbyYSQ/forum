package top.ysqorz.forum.dto.resp;

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
    private Integer secondCommentCount;
    private LocalDateTime createTime;

    private Integer postId;         //回复的帖子ID
    private String postTitle;     //回复的帖子内容
    private String timeDifference;  //回复的时间

    private Boolean isPostCreator; // 是否是楼主

}
