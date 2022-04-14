package top.ysqorz.forum.dto.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 发布一级评论的dto
 * 回复post
 *
 * @author passerbyYSQ
 * @create 2021-06-20 16:30
 */
@Getter
@Setter
public class PublishFirstCommentDTO {
    @NotNull
    private Integer postId;
    @NotEmpty
    private String content;
    @NotEmpty
    private String token;
    @NotEmpty
    private String captcha;
}
