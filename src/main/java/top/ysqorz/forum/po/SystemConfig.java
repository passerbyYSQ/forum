package top.ysqorz.forum.po;

import javax.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
@Table(name = "system_config")
public class SystemConfig {
    @Id
    private Integer id;

    /**
     * 参数名
     */
    private String key;

    /**
     * 参数值
     */
    private String value;
}