package top.ysqorz.forum.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.ysqorz.forum.utils.SpringUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 后台帖子列表的查询条件
 * @author passerbyYSQ
 * @create 2021-06-07 10:33
 */
@Getter
@Setter
public class QueryPostCondition {

    private Integer topicId; // 所属话题的id
    private String creator; // 发帖用户名
    private String title; // 标题
    private Byte isHighQuality; //高质量
    private Byte hotDiscussion; //热议
    private String labelsId; //从前台主页获取的标签id拼接
    private List<Integer> labelList; //从labelsId分解出来的list

    private LocalDateTime startPublishTime; // 发帖时间的范围。起始时间
    private LocalDateTime endPublishTime; // 发帖时间的范围。起始时间
    //private String labels; // 标签。需要以","作为分隔符

    private Integer userId;//当前登录用户Id

    private String field; // 排序字段
    private String order; // 排序方式。 desc asc
    private String orderByStr; //

    public void generateOrderByStr() {
        // 前端传过来是驼峰命名法，而数据库中的字段是下划线命名法，需要转换成下划线命名
        field = SpringUtils.snakeCaseFormat(field);
        if (order == null) {
            order = ""; // 空串表示升序
        }
        if (!StringUtils.isEmpty(field) && ("".equals(order) ||
                "asc".equalsIgnoreCase(order) || "desc".equalsIgnoreCase(order))) {
            orderByStr = field + " " + order;
        } else {
            orderByStr = "create_time desc";
        }
    }

    public void splitLabelsStr() {
        if (!ObjectUtils.isEmpty(labelsId)) { // null 或者 空串 都不处理
            String[] labelIds = labelsId.split(",");
            this.labelList = Arrays.stream(labelIds)
                    .filter(s -> !ObjectUtils.isEmpty(s))
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());
        }
    }

}
