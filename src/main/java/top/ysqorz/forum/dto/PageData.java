package top.ysqorz.forum.dto;

import com.github.pagehelper.PageInfo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页数据返回的模型
 *
 * @author passerbyYSQ
 * @create 2021-02-02 23:45
 */
@Data
@NoArgsConstructor
public class PageData<T> {
    private Integer page; // 返回给前端的，被纠正的当前页。可能因为越界而被纠正
    private Integer count; // 每一页显示的记录数。前端不传，会赋默认值
    private Long total; // 总记录数
    private List<T> list; // 当前页的列表数据

    /**
     * 注意是list必须是查询数据库返回的List，否则是没有分页信息的
     */
    public PageData(List<T> list) {
        PageInfo<T> pageInfo = new PageInfo<>(list);
        this.page = pageInfo.getPageNum();
        this.count = pageInfo.getPageSize();
        this.total = pageInfo.getTotal();
        this.list = list;
    }

    public PageData(Integer page, Integer count, Long total, List<T> list) {
        this.page = page;
        this.count = count;
        this.total = total;
        this.list = list;
    }
}
