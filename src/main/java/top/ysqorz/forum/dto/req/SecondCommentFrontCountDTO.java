package top.ysqorz.forum.dto.req;

import lombok.Data;
import top.ysqorz.forum.common.enumeration.OrderMode;

import javax.validation.constraints.NotNull;

/**
 * @author passerbyYSQ
 * @create 2023-06-26 23:41
 */
@Data
public class SecondCommentFrontCountDTO {
    @NotNull
    private Integer postId;

    @NotNull
    private Integer secondCommentId;

    private OrderMode firstCommentOrder = OrderMode.ASC;

    private OrderMode secondCommentOrder = OrderMode.DESC;
}
