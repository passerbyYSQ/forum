package top.ysqorz.forum.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.exception.ParameterInvalidException;
import top.ysqorz.forum.common.exception.ServiceFailedException;
import top.ysqorz.forum.utils.CommonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.List;

/**
 * 全局异常捕获
 *
 * @author passerbyYSQ
 * @create 2020-11-02 23:27
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // 注入容器中的ObjectMapper（被我们定制过的）
    @Autowired
    private ObjectMapper objectMapper;

    // 参数错误
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public ModelAndView handleBindException(BindException e, HttpServletRequest request) {
        List<FieldError> errors = e.getFieldErrors();
        return wrapModelAndView(StatusCode.PARAM_INVALID, joinErrorMsg(errors), request);
    }

    // 参数错误
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ModelAndView handleConstraintViolationException(ConstraintViolationException e, HttpServletRequest request) {
        Iterator<ConstraintViolation<?>> iterator = e.getConstraintViolations().iterator();
        StringBuilder errorMsg = new StringBuilder();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            errorMsg.append(cvl.getPropertyPath().toString().split("\\.")[1]) // .需要转义
                    .append(cvl.getMessage())
                    .append(";");
        }
        return wrapModelAndView(StatusCode.PARAM_INVALID,  errorMsg.toString(), request);
    }

    // 参数错误
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView handleArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        return wrapModelAndView(StatusCode.PARAM_INVALID, joinErrorMsg(errors), request);
    }

    // 参数错误
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ParameterInvalidException.class)
    public ModelAndView handleParameterInvalidException(ParameterInvalidException e, HttpServletRequest request) {
        return wrapModelAndView(StatusCode.PARAM_INVALID, e.getMessage(), request);
    }

    /**
     * 对于接口业务失败的情况(如：密码错误，验证码错误等)，这些不属于异常(http响应码仍为200)，但属于业务失败。
     * 为了对接口返回值进行统一处理，业务失败的情况通过异常抛出，在此处做统一包装处理
     */
    @ExceptionHandler(ServiceFailedException.class)
    public ModelAndView handleServiceFailedException(ServiceFailedException e, HttpServletRequest request) {
        return wrapModelAndView(e.getCode(), null, request);
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
    @ExceptionHandler({Exception.class})
    public ModelAndView handleOtherException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        return wrapModelAndView(StatusCode.UNKNOWN_ERROR, e.toString(), request);
    }

    private ModelAndView wrapModelAndView(StatusCode code, String errorMsg, HttpServletRequest request) {
        ResultModel<Object> res = ObjectUtils.isEmpty(errorMsg) ?
                ResultModel.failed(code) :
                ResultModel.failed(code.getCode(), errorMsg); // errorMsg覆盖code的msg
        ModelAndView modelAndView = CommonUtils.isApiRequest(request) ?
                new ModelAndView(new MappingJackson2JsonView(objectMapper)) : //new MappingJackson2JsonView()
                new ModelAndView("error/500");
        modelAndView.addObject("code", res.getCode())
                .addObject("msg", res.getMsg())
                .addObject("data", res.getData())
                .addObject("time", res.getTime());
        return modelAndView;
    }

    // 拼接错误信息
    private String joinErrorMsg(List<FieldError> errors) {
        StringBuilder errorMsg = new StringBuilder();
        for (FieldError error : errors) {
            errorMsg.append(error.getField())
                    .append(error.getDefaultMessage())
                    .append(";");
        }
        return errorMsg.toString();
    }

}
