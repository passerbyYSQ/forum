package top.ysqorz.forum.dto.resp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import top.ysqorz.forum.common.enumeration.PostVisibility;
import top.ysqorz.forum.po.Label;
import top.ysqorz.forum.po.Post;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 修改帖子
 * @author passerbyYSQ
 * @create 2021-06-12 11:41
 */
@Getter
@Setter
public class UpdatePostDTO {

    private Integer id; // 帖子id
    private String title; // 标题
    private String content; // 内容
    private Integer topicId; // 所属话题id
    private LocalDateTime createTime; // 发帖时间
    private LocalDateTime lastModifyTime; // 上一次编辑时间
    private PostVisibility visibilityType;
    private Byte isLocked; // 是否锁定

    private List<Label> labelList;  // 标签

    public UpdatePostDTO() {
    }

    public UpdatePostDTO(Post post) {
        BeanUtils.copyProperties(post, this);
    }
}
