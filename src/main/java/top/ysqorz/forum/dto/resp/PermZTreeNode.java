package top.ysqorz.forum.dto.resp;

import lombok.Getter;
import lombok.Setter;
import top.ysqorz.forum.po.Resource;

import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2021-05-14 22:32
 */
@Getter
@Setter
public class PermZTreeNode {

    private String name;
    private Integer id; // 权限id
    private Integer parentId; // 父权限的id
    private Boolean checked; // 是否勾选。勾选代表角色拥有该权限

    public PermZTreeNode(Resource res, Set<Integer> rolePermIds) {
        name = res.getName() + " " + res.getPermission();
        id = res.getId();
        parentId = res.getParentId();
        checked = rolePermIds.contains(id);
    }

}
