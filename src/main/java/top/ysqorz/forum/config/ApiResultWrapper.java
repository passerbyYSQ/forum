package top.ysqorz.forum.config;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.annotation.NotWrapWithResultModel;
import top.ysqorz.forum.utils.JsonUtils;

/**
 * 对Controller层的方法返回值进行统一包装处理
 *
 * @author passerbyYSQ
 * @create 2022-06-18 0:10
 */
@RestControllerAdvice(basePackages = {"top.ysqorz.forum.controller"})
public class ApiResultWrapper implements ResponseBodyAdvice<Object> {

    /**
     * 返回值已经是ResultModel类型或者方法被标注了@NotWrapWithResultModel，则不进行处理
     *
     * 没有排除返回值为String的情况(转发至模板)，不会影响转发到模板
     *
     * 返回值类型为void或者返回值为null，不会进入这个bean
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return  !(returnType.getParameterType().isAssignableFrom(ResultModel.class) // 兼容旧代码
                || returnType.hasMethodAnnotation(NotWrapWithResultModel.class)); // 提供灵活处理的钩子
    }

    @Override
    public Object beforeBodyWrite(Object res, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {
        // 特殊处理返回值为String的情况，虽然selectedContentType为application/json，
        // 但是selectedConverterType却是StringHttpMessageConverter，导致包装成ResultModel后强转String报错
        // 所以需要在此处手动转json，StringHttpMessageConverter将json串强转成String时就不会报错了
        // https://zhuanlan.zhihu.com/p/413133915
        if (String.class.equals(returnType.getGenericParameterType())) {
            return JsonUtils.objToJson(ResultModel.success(res));
        }
        // returnType为void，res为null，也直接包装。但事实上连support()方法也不会进来
        // 所以如果实在不需要返回值，则可以将返回值类型声明为StatusCode，返回StatusCode.SUCCESS
        return res instanceof StatusCode ? ResultModel.wrap((StatusCode) res) : ResultModel.success(res);
    }
}
