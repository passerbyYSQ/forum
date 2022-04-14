package top.ysqorz.forum.dto.resp;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author passerbyYSQ
 * @create 2022-04-14 15:39
 */
@Data
@Accessors(chain = true)
public class RecentCommentUserDTO {
    private String userId;
    private String username;
    private String photo;
    private LocalDateTime lastCommentTime;
}
