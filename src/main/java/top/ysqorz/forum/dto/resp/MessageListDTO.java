package top.ysqorz.forum.dto.resp;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author 阿灿
 * @create 2021-07-02 22:04
 */
@Getter
@Setter
public class MessageListDTO {
    private String username;  //评论or回复者信息
    private String photo;   //评论or回复者信息
    private String title;  //被评论的帖子
    private String hisContent; //被评论or回复的内容
    private String myContent;  //自己的评论
    private LocalDateTime createTime; //评论or回复时间
    private Integer postID;//帖子id
    private Integer firstConId; //用于定位的一级id
    private Integer secondConId; //用于定位的二级id 不一定有
//
//    private Integer leaf;  //定位页数
//
//    private Integer secondLeaf;  //二级评论页数



}
