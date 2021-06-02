package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * @author 阿灿
 * @create 2021-05-19 16:11
 */
@Getter
@Setter
public class BlackInfoDTO {


    /**
     * 被封禁用户名
     */

    private String username;

    /**
     * 操作人
     */
    private String adminname;

    /**
     * 起始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;


    /**
     * 封禁原因
     */
    private String reason;


}
