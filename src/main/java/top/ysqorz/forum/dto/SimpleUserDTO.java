package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;
import top.ysqorz.forum.po.Role;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用于前台显示的用户小卡片
 *
 * @author passerbyYSQ
 * @create 2021-06-18 23:46
 */
@Getter
@Setter
public class SimpleUserDTO {
    private Integer id;
    private String username;
    private Byte gender;
    private String photo; // 以后增加 缩略图和原图
    private LocalDateTime lastLoginTime;
    private LocalDateTime registerTime;

    private List<Role> roles;
    private Integer blackId; // 当前是否处于封禁
}
