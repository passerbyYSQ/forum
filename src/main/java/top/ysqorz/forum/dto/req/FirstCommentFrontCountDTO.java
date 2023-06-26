package top.ysqorz.forum.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import top.ysqorz.forum.common.enumeration.OrderMode;

import javax.validation.constraints.NotNull;

/**
 * @author passerbyYSQ
 * @create 2023-06-26 23:31
 */
@Data
@AllArgsConstructor
public class FirstCommentFrontCountDTO {
    @NotNull
    private Integer postId;

    @NotNull
    private Integer firstCommentId;

    private OrderMode firstCommentOrder = OrderMode.ASC;
}
