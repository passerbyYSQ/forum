package top.ysqorz.forum.im.netty;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * 在IOC的容器的启动过程，当所有的bean都已经处理完成之后，spring ioc容器会有一个发布事件的动作。
 * 让我们的bean实现ApplicationListener接口，这样当发布事件时，[spring]的ioc容器就会以容器的实例对象作为事件源类，
 * 并从中找到事件的监听者，此时ApplicationListener接口实例中的onApplicationEvent(E event)方法就会被调用，
 */
@Component
public class WsServerBoot implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        // 对于web应用会出现父子容器，这样就会触发两次
        if (event.getApplicationContext().getParent() == null) {
            WebSocketServer.getInstance().start();
        }
    }

}
