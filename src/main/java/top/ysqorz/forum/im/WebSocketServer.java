package top.ysqorz.forum.im;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.ysqorz.forum.im.handler.DanmuMsgHandler;
import top.ysqorz.forum.im.handler.MsgCenter;

/**
 * netty和springboot的整合启动
 * Spring在创建bean的时候会调用WebSocketServer的空参构造，装载进IOC容器
 *
 * @author passerbyYSQ
 * @create 2021-02-05 20:59
 */
@Component  // 注意不要忘了！！！
@Slf4j
public class WebSocketServer {

    /**
     * 静态内部类方式实现懒汉式单例模式
     */
    private static class SingletonWSServer {
        private static final WebSocketServer instance = new WebSocketServer();
    }

    // 获取单例的方法
    public static WebSocketServer getInstance() {
        return SingletonWSServer.instance; // 此时才会加载内部类，所以是懒汉式
    }

    private ServerBootstrap server;

    /**
     *
     */
    private WebSocketServer() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                // 设置其他一些参数
                .childHandler(new WsChannelInitializer());
    }

    public void start() {
        ChannelFuture future = server.bind(8081);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (future.isSuccess()) {
                    log.info("netty websocket server 异步启动成功");
                    initMsgHandlers();
                } else {
                    log.error("netty websocket server 异步启动失败");
                }
            }
        });
    }

    private void initMsgHandlers() {
        MsgCenter.getInstance()
                .addLast(new DanmuMsgHandler());
        log.info("消息处理器初始化成功");
    }
}

