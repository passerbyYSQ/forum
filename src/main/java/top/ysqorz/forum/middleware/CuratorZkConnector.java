package top.ysqorz.forum.middleware;

import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.CuratorCache;
import org.apache.curator.framework.recipes.cache.CuratorCacheListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import top.ysqorz.forum.im.IMUtils;
import top.ysqorz.forum.utils.CommonUtils;
import top.ysqorz.forum.utils.SpringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author passerbyYSQ
 * @create 2022-01-28 13:20
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "zookeeper.curator")
public class CuratorZkConnector implements ZkConnector<ChildData, CuratorZkConnector.NodeChangedCallback>, ConnectionStateListener {
    private String namespace;
    // ACL digest方式
    private String user;
    private String password;
    // zk的服务器地址
    private String servers; // 南京 "119.45.164.115:2181"
    // 会话的超时时间
    private int sessionTimeout; //  1000 * 20
    // 连接的超时时间
    private int connectionTimeout; // 1000 * 15

    private List<ACL> aclList = new ArrayList<>();
    private CuratorFramework client;
    private Set<CuratorCache> curatorCaches = new HashSet<>();

    @PostConstruct
    public void postConstruct() {
        String plainUserPwd = user + ":" + password;
        String userPwdDigest = IMUtils.generateAuthDigest(plainUserPwd);
        Id userId = new Id("digest", userPwdDigest);
        aclList.add(new ACL(ZooDefs.Perms.ALL, userId));
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        this.client = CuratorFrameworkFactory.builder()
                .namespace(namespace)
                .authorization("digest", plainUserPwd.getBytes(StandardCharsets.UTF_8))
                .connectString(servers)
                .sessionTimeoutMs(sessionTimeout)
                .connectionTimeoutMs(connectionTimeout)
                .retryPolicy(retryPolicy)
                .build();
        this.client.getConnectionStateListenable()
                .addListener(this);
        log.info("开始连接Zookeeper");
        this.client.start();
        // this.client.blockUntilConnected(); // 不要在这里阻塞，否则会拖长初始化bean的时间，从而导致应用启动时间变长
    }

    @PreDestroy
    public void preDestroy() {
        this.client.close();
        for (CuratorCache curatorCache : curatorCaches) {
            curatorCache.close();
        }
        log.info("成功关闭CuratorFramework和所有的CuratorCache");
    }

    @Override
    public void create(String path, String data, CreateMode mode) {
        try {
            client.create()
                    .orSetData()
                    .creatingParentsIfNeeded()
                    .withMode(mode)
                    .withACL(aclList, true)
                    .forPath(path, data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteLeafNode(String path) {
        try {
            client.delete()
                    .guaranteed()
                    .forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRecursively(String path) {
        try {
            client.delete()
                    .guaranteed()
                    .deletingChildrenIfNeeded()
                    .forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getData(String path) {
        try {
            byte[] bytes = client.getData().forPath(path);
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> getChildrenData(String path) {
        try {
            return client.getChildren().forPath(path);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update(String path, String data) {
        try {
            client.setData()
                    .forPath(path, data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addListener(String path, NodeChangedCallback callback, boolean isIncludeCurrNodeOnAdded) {
        CuratorCache curatorCache = CuratorCache.build(client, path);
        curatorCaches.add(curatorCache);
        CuratorCacheListener listener = CuratorCacheListener.builder()
                .forCreates(node -> {
                    if (node.getPath().equals(path) && !isIncludeCurrNodeOnAdded) {
                        // 如果因当前节点不存在而创建，则不回调
                        return;
                    }
                    callback.nodeAdded(node);
                })
                .forDeletes(callback::nodeDeleted)
                .forChanges(callback::dataChanged)
                .build();
        curatorCache.listenable().addListener(listener);
        curatorCache.start();
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        if (ConnectionState.CONNECTED == newState || ConnectionState.RECONNECTED == newState) {
            log.info("连接Zookeeper成功");
            boolean failed = true;
            do {
                try {
                    // 将当前服务器注册到zookeeper中，作为临时节点
                    String path = ZkConnector.PATH + "/" + IMUtils.getWebServer();
                    this.create(path, IMUtils.getWsServer(), CreateMode.EPHEMERAL);
                    log.warn("往Zookeeper注册当前服务成功");
                    failed = false;
                } catch (Exception ignored) {
                    log.info("服务IOC容器尚未初始化，往Zookeeper注册当前服务失败");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } while (failed);
        }
    }

    interface NodeChangedCallback extends ZkConnector.NodeChangedCallback<ChildData> {
    }
}
