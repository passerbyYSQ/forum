package top.ysqorz.forum.dto.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.ysqorz.forum.shiro.LoginUser;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-06-18 23:36
 */
@Data
public class PostDetailDTO {

    private PostDTO detail;

    private Integer likeId; // 如果我点过赞，则不为空
    private Integer collectId; // 如果我收藏过，则不为空
    private Boolean isHot;
    private List<Liker> likerList;

    private Boolean isShowEdit;
    private Boolean isShowDelete;
    private Boolean isShowTop;
    private Boolean isShowQuality;

    @Data
    @NoArgsConstructor
    public static class Liker {
        private Integer userId;
        private String username;
        private String photo;

        public Liker(LoginUser user) {
            this.userId = user.getId();
            this.username = user.getUsername();
            this.photo = user.getPhoto();
        }
    }
}
