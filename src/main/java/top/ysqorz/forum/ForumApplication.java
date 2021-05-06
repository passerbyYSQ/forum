package top.ysqorz.forum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
// 扫描Mapper接口
@MapperScan("net.ysq.webchat.dao") // 注解不要导入导错包！是tk下的，不是mybatis包下
public class ForumApplication {

	public static void main(String[] args) {
		SpringApplication.run(ForumApplication.class, args);
	}

}
