package top.ysqorz.forum.common;

import java.util.*;

/**
 * @param <T>   T 是节点中id（parentId）的类型
 * @author passerbyYSQ
 * @create 2021-04-12 14:16
 */
public class TreeBuilder<T> {

    // 所有节点
    private List<? extends TreeNode<T>> nodeList;
    // key：节点id  value：该节点的所有孩子节点
    private Map<T, List<TreeNode<T>>> map = new HashMap<>();
    // 根节点（可能有多个）的parentId的值。根节点的parentId值都指向一个不存在的相同id
    private T rootParentId;
    // 森林
    private TreeNode<T> fakeRootNode;
    // 所有的id
    private Set<T> idSet;

    /**
     * @param nodeList      所有节点
     * @param rootParentId  根节点（可能有多个）的parentId的值
     */
    public TreeBuilder(List<? extends TreeNode<T>> nodeList, T rootParentId) {
        this.nodeList = nodeList;
        this.rootParentId = rootParentId;

        buildTree();
    }

    /**
     * 构建森林
     * @return  返回多棵树的真实根节点
     */
    private void buildTree() {
        // 预处理
        preProcess();

        // 如果有多个根节点，为了方便，创建一个伪头节点
        TreeNode<T> fakeRoot = new TreeNode<>(rootParentId);

        idSet = new HashSet<>();
        // 递归建树
        doBuildTreeByDfs(fakeRoot);
        this.fakeRootNode = fakeRoot;
    }

    private void doBuildTreeByDfs(TreeNode<T> root) {
        List<TreeNode<T>> children = map.get(root.getId());
        root.setChildren(children);
        idSet.add(root.getId());
        // 当前节点没有孩子节点，说明当前节点是叶子节点，回溯
        if (children == null) {
            return;
        }

        for (TreeNode<T> child : children) {
//            child.setParent(root); // 注释掉，否则无法打印测试
            doBuildTreeByDfs(child);
        }
    }

    public void destroy() {
        nodeList.clear();
        map.clear();
        idSet.clear();
        doClearTreeByDfs(fakeRootNode);
    }

    private void doClearTreeByDfs(TreeNode<T> root) {
        if (root == null) {
            return;
        }
        if (root.getChildren() != null) {
            for (TreeNode<T> child : root.getChildren()) {
                doClearTreeByDfs(child);
            }
        }
        // help gc
        root.setChildren(null);
        root.setParentId(null);
        root = null;
    }

    // 判断id是否合法
    public boolean isValidId(T id) {
        return idSet.contains(id);
    }

    // id对应的节点是否叶子节点
    public boolean isLeaf(T id) {
        return isValidId(id) && !map.containsKey(id);
    }

    // 判断key是否是root自己或者它的子孙节点
    public boolean isDescendant(T root, T key) {
        if (root.equals(key)) {
            return true;
        }
        List<TreeNode<T>> children = map.get(root);
        if (children == null) {
            return false;
        }
        for (TreeNode<T> child : children) {
            if (isDescendant(child, key)) {
                return true;
            }
        }
        return false;
    }

    // 判断key是否是root的子孙节点
    private boolean isDescendant(TreeNode<T> root, T key) {
        List<TreeNode<T>> children = root.getChildren();
        if (root.getId().equals(key)) {
            return true;
        }
        if (children == null) {
            return false;
        }
        for (TreeNode<T> child : children) {
            if (isDescendant(child, key)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 预处理所有节点
     * 一次遍历就将各个节点的孩子全部找出，并存储到Map中（根据parentId分类）
     * 避免之后每次找一个节点的孩子都要遍历一遍所有节点
     */
    private void preProcess() {
        for (TreeNode<T> node : nodeList) {
            // T由于要作为Map的key，所以必须重写equals()和HashCode()方法
            T parentId = node.getParentId();
            boolean isContained = map.containsKey(parentId);
            if (isContained) {
                map.get(parentId).add(node);
            } else {
                List<TreeNode<T>> children = new ArrayList<>();
                children.add(node);
                map.put(parentId, children);
            }
        }
    }
}
