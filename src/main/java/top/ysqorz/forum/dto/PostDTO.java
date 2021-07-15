package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import top.ysqorz.forum.po.Label;
import top.ysqorz.forum.po.Post;
import top.ysqorz.forum.po.Topic;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 后台帖子管理
 *
 * @author passerbyYSQ
 * @create 2021-06-07 10:54
 */
@Getter
@Setter
public class PostDTO {

    private Integer id; // 帖子id
    private String title; // 标题
    private String content; // 内容
    private SimpleUserDTO creator; // 发布者的用户名
    private String topic; // 所属话题
    private Integer topicId;
    private LocalDateTime createTime;//创建时间
    private Integer visitCount; // 访问量
    private Integer likeCount; // 点赞数
    private Integer collectCount; // 收藏数
    private Integer commentCount; // 评论数
    private Byte isHighQuality; // 是否精品帖
    private Byte isLocked; // 是否锁定
    private Integer topWeight; // 置顶权重
    private String timeDifference; //当前时间与创建时间的时间差

    private List<Label> labelList;  // 标签
    private List<String>  imagesList; //图片列表
    private LocalDateTime lastCommentTime;

    // 当前登录的用户是否点赞 这里是isLike 到前端就变成like了 原因不详
    // 因为：PostMapper.xml resultMap 没有做映射
    private Boolean isLike;
    private Boolean isHot;

    public PostDTO() {
    }

    public PostDTO(Post post, SimpleUserDTO creator, Topic topic, List<Label> labelList) {
        BeanUtils.copyProperties(post, this);
        this.creator = creator;
        this.topicId = topic.getId();
        this.topic = topic.getTopicName();
        this.labelList = labelList;
    }

    public PostDTO(Post post){
        BeanUtils.copyProperties(post, this);
    }




}
