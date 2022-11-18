package top.ysqorz.forum.middleware;


import org.apache.zookeeper.CreateMode;

import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2022-01-28 0:11
 */
public interface ZkConnector<T, Callback extends ZkConnector.NodeChangedCallback<T>> {

    String PATH = "/im/ws"; // 最后不能加 /

    default void create(String path) {
        create(path, "");
    }

    /**
     * 默认创建持久化节点，支持递归创建
     */
    default void create(String path, String data) {
        create(path, data, CreateMode.PERSISTENT);
    }

    void create(String path, String data, CreateMode mode);

    void deleteLeafNode(String path);

    void deleteRecursively(String path);

    String getData(String path);

    List<String> getChildrenData(String path);

    void update(String path, String data);

    default void addListener(String path, Callback callback) {
        addListener(path, callback, true);
    }

    void addListener(String path, Callback callback, boolean isIncludeCurrNodeOnAdded);

    interface NodeChangedCallback<Node> {
        /**
         * 子孙节点新增时
         *
         * @param node 新增的节点
         */
        default void nodeAdded(Node node) {
        }

        /**
         * 子孙节点删除时
         *
         * @param node 删除的节点
         */
        default void nodeDeleted(Node node) {
        }

        /**
         * 数据更改时
         *
         * @param oldNode 更改前的旧节点信息
         * @param newNode 更改后的新节点信息
         */
        default void dataChanged(Node oldNode, Node newNode) {
        }
    }

}
