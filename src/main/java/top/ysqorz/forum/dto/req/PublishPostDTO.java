package top.ysqorz.forum.dto.req;

import lombok.Getter;
import lombok.Setter;
import top.ysqorz.forum.common.enumeration.PostVisibility;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 发帖的vo
 *
 * @author passerbyYSQ
 * @create 2021-05-24 21:49
 */
@Getter
@Setter
public class PublishPostDTO {

    public interface Add {}
    public interface Update {}

    @NotNull(groups = Update.class)
    private Integer postId;

    @NotNull
    private Integer topicId; // 所属话题

    @NotEmpty
    @Size(min = 3, max = 64)
    private String title; // 帖子标题

    @NotEmpty
    @Size(min = 5)
    private String content; // 帖子内容

    @NotEmpty
    private String token;

    @NotEmpty
    private String captcha; // 验证码

    @NotNull
    private Boolean isLocked;

    /**
     * 可见策略
     * 0：任何人可见
     * 1：粉丝可见
     * 2：点赞后可见
     * 3：积分购买后可见（积分就是points）
     */
    @NotNull
    private PostVisibility visibilityType;

    private Byte points; // 其他用户查看该贴需要支付的积分

    // 帖子标签
    private String labels;  // 标签，逗号作为分隔符

    public Set<String> splitLabels() {
        return Arrays.stream(labels.split(",")) // 根据逗号分割
                .map(s -> s.replace(" ", "").trim()) // 去除空格
                .filter(s -> !s.isEmpty()) // 只留下非空的
                .limit(5) // 只取前5个
                .collect(Collectors.toSet());
    }

}
