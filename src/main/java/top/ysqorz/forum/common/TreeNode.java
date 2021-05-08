package top.ysqorz.forum.common;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 树节点
 * @param <T>   parentId的类型
 */
@Data
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode
public class TreeNode<T> {

    private T id;
    private T parentId;
    // 注释掉，否则无法打印测试
//    private TreeNode<T> parent;
    private List<TreeNode<T>> children;

    public TreeNode(T id) {
        this.id = id;
    }

    public TreeNode(T id, T parentId) {
        this.id = id;
        this.parentId = parentId;
    }
}
