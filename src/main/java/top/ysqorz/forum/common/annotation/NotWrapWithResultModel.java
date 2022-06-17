package top.ysqorz.forum.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 该注解标注于Controller类的方法上，表明该方法的返回值不需要包装成ResultModel
 *
 * @author passerbyYSQ
 * @create 2022-06-18 0:07
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotWrapWithResultModel {
}
