package top.ysqorz.forum.dto.resp;

import lombok.Getter;
import lombok.Setter;

/**
 * @author passerbyYSQ
 * @create 2021-06-18 23:36
 */
@Getter
@Setter
public class PostDetailDTO {

    private PostDTO detail;

    private Integer likeId; // 如果我点过赞，则不为空
    private Integer collectId; // 如果我收藏过，则不为空
    private Boolean isHot;

    private Boolean isShowEdit;
    private Boolean isShowDelete;
    private Boolean isShowTop;
    private Boolean isShowQuality;

    //private PageData<FirstCommentDTO> comments;
}
