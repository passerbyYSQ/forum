package top.ysqorz.forum.dto;

import lombok.Data;
import org.springframework.beans.BeanUtils;
import top.ysqorz.forum.po.User;

/**
 * @author 阿灿
 * @create 2021-06-29 16:48
 * 主页显示的用户信息
 */
@Data
public class IndexUserDTO {
    private Integer id;
    private String username;
    private String email;
    private String phone;
    private String photo;

    public IndexUserDTO(){}

    public IndexUserDTO(User user){
        BeanUtils.copyProperties(user, this);
    }


}

