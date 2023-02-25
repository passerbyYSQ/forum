package top.ysqorz.forum.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.exception.ParamInvalidException;
import top.ysqorz.forum.common.exception.ServiceFailedException;
import top.ysqorz.forum.utils.CommonUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * 全局异常捕获
 *
 * @author passerbyYSQ
 * @create 2020-11-02 23:27
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @Resource
    private MappingJackson2JsonView mappingJackson2JsonView; // 被定制过

    // 参数错误
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ModelAndView handleBindException(BindException e, HttpServletRequest request) {
        return wrapModelAndView(StatusCode.PARAM_INVALID, joinErrorMsg(e.getBindingResult()), request);
    }

    // 参数错误
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        String errorMsg = e.getConstraintViolations().stream()
                .map(cvl -> {
                    String attr = cvl.getPropertyPath().toString().split("\\.")[1];
                    return attr + cvl.getMessage();
                })
                .collect(Collectors.joining("; "));
        return wrapModelAndView(StatusCode.PARAM_INVALID, errorMsg, request);
    }

    // 参数错误
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        return wrapModelAndView(StatusCode.PARAM_INVALID, joinErrorMsg(e.getBindingResult()), request);
    }

    // 参数错误
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParamInvalidException.class)
    public ModelAndView handleParameterInvalidException(ParamInvalidException e, HttpServletRequest request) {
        return wrapModelAndView(e.getCode(), e.getMessage(), request);
    }

    /**
     * 对于接口业务失败的情况(如：密码错误，验证码错误等)，这些不属于异常(http响应码仍为200)，但属于业务失败。
     * 为了对接口返回值进行统一处理，业务失败的情况通过异常抛出，在此处做统一包装处理
     */
    @ExceptionHandler(ServiceFailedException.class)
    public ModelAndView handleServiceFailedException(ServiceFailedException e, HttpServletRequest request) {
        return wrapModelAndView(e.getCode(), e.getMessage(), request);
    }

    // jwt校验错误
//    @ResponseStatus(HttpStatus.FORBIDDEN) // 无登录权限。Jwt之后会结合shiro，之后再改
//    @ExceptionHandler(JWTVerificationException.class)
//    public ModelAndView handlerJwtVerificationException(JWTVerificationException e, HttpServletRequest request) {
//        // 主要分为两类错误
//        ResultModel res = ResultModel.failed(e instanceof TokenExpiredException ?
//                        StatusCode.TOKEN_IS_EXPIRED : StatusCode.TOKEN_IS_INVALID); // token过期，登录状态过期
//        return wrapModelAndView(res, request);
//    }

    // 授权异常
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthorizationException.class)
    public ModelAndView handlerAuthorizationException(AuthorizationException e, HttpServletRequest request) {
        return wrapModelAndView(StatusCode.AUTHORIZATION_FAILED, e.getMessage(), request);
    }

    // 前后端联调时和正式上线后开启
    // 后端编码时，为了方便测试，先注释掉
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 非业务层面的异常，表示出现了服务端错误。
    @ExceptionHandler(Exception.class)
    public ModelAndView handleOtherException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        return wrapModelAndView(StatusCode.SERVER_ERROR, ExceptionUtil.stacktraceToString(e), request);
    }

    private ModelAndView wrapModelAndView(StatusCode code, String errorMsg, HttpServletRequest request) {
        ResultModel<Object> res = ObjectUtils.isEmpty(errorMsg) ?
                ResultModel.failed(code) :
                ResultModel.failed(code.getCode(), errorMsg); // errorMsg覆盖code的msg
        ModelAndView modelAndView = CommonUtils.isApiRequest(request) ?
                new ModelAndView(mappingJackson2JsonView) : // new MappingJackson2JsonView()
                new ModelAndView("error/500"); // 需要配置500页面的模板
        modelAndView.addObject("code", res.getCode())
                .addObject("msg", res.getMsg())
                .addObject("data", res.getData())
                .addObject("time", res.getTime());
        return modelAndView;
    }

    // 拼接错误信息
    private String joinErrorMsg(BindingResult bindingRes) {
        List<FieldError> fieldErrors = new ArrayList<>();
        List<ObjectError> otherErrors = new ArrayList<>();
        List<ObjectError> allErrors = bindingRes.getAllErrors();
        for (ObjectError objError : allErrors) {
            if (objError instanceof FieldError) {
                fieldErrors.add((FieldError) objError);
            } else {
                otherErrors.add(objError);
            }
        }
        Collector<CharSequence, ?, String> collector = Collectors.joining("; ");
        // FieldError
        String fieldErrorMsg = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + fieldError.getDefaultMessage())
                .collect(collector);
        // ObjectError
        String otherErrorMsg = otherErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(collector);
        return Arrays.stream(new String[]{fieldErrorMsg, otherErrorMsg})
                .filter(s -> !ObjectUtils.isEmpty(s))
                .collect(collector);
    }
}
