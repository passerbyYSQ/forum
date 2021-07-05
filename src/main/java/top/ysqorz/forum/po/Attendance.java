package top.ysqorz.forum.po;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "attendance")
public class Attendance {
    @Id
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    /**
     * 签到的日期，独立出来方便查询
     */
    @Column(name = "attend_date")
    private LocalDate attendDate;

    /**
     * 签到的时间
     */
    @Column(name = "attend_time")
    private LocalTime attendTime;
}
