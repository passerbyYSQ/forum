package top.ysqorz.forum.utils;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Description: 提供手动获取被spring管理的bean对象
 */
@Slf4j
@Component // 缺少这个注解，报空指针异常
public class SpringUtils implements ApplicationContextAware {

    // 获取applicationContext
    @Getter
    private static ApplicationContext applicationContext;
	private static PropertyNamingStrategy.PropertyNamingStrategyBase
			snakeCaseStrategy = new PropertyNamingStrategy.SnakeCaseStrategy();

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		if (SpringUtils.applicationContext == null) {
			SpringUtils.applicationContext = applicationContext;
		}
	}

    // 通过name获取 Bean.
	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	// 通过class获取Bean.
	public static <T> T getBean(Class<T> clazz) {
		return getApplicationContext().getBean(clazz);
	}

	// 通过name,以及Clazz返回指定的Bean
	public static <T> T getBean(String name, Class<T> clazz) {
		return getApplicationContext().getBean(name, clazz);
	}

	// 将变量名转成下划线命名方式
	public static String snakeCaseFormat(String variableName) {
		return snakeCaseStrategy.translate(variableName);
	}
}
