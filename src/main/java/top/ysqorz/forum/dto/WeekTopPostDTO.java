package top.ysqorz.forum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 帖子热议周榜管理
 *
 * @author ligouzi
 * @create 2021-07-11 22:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeekTopPostDTO {

    private Integer postId;    // 帖子id号
    private String title;       // 帖子标题
    private Integer views;      // 帖子浏览数

}
