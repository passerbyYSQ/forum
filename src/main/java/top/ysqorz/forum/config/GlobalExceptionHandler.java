package top.ysqorz.forum.config;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        ResultModel res = ResultModel.failed(StatusCode.PARAM_IS_INVALID.getCode(),
                joinErrorMsg(errors));
        return wrapModelAndView(res, request);
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
                    .append(cvl.getMessage());
        }
        ResultModel res = ResultModel.failed(StatusCode.PARAM_IS_INVALID.getCode(),
                errorMsg.toString());
        return wrapModelAndView(res, request);
    }

    // 参数错误
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ModelAndView methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<FieldError> errors = e.getBindingResult().getFieldErrors();
        ResultModel<Object> res = ResultModel.failed(StatusCode.PARAM_IS_INVALID.getCode(),
                joinErrorMsg(errors));
        return wrapModelAndView(res, request);
    }

    // jwt校验错误
    @ResponseStatus(HttpStatus.FORBIDDEN) // 无登录权限。Jwt之后会结合shiro，之后再改
    @ExceptionHandler(JWTVerificationException.class)
    public ModelAndView handlerJwtVerificationException(JWTVerificationException e, HttpServletRequest request) {
        // 主要分为两类错误
        ResultModel res = ResultModel.failed(e instanceof TokenExpiredException ?
                        StatusCode.TOKEN_IS_EXPIRED : StatusCode.TOKEN_IS_INVALID); // token过期，登录状态过期
        return wrapModelAndView(res, request);
    }


    // 前后端联调时和正式上线后开启
    // 后端编码时，为了方便测试，先注释掉
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 非业务层面的异常，表示出现了服务端错误。
    @ExceptionHandler({Exception.class})
    public ModelAndView handleOtherException(Exception e, HttpServletRequest request) {
        e.printStackTrace();
        ResultModel<Object> res = ResultModel.failed(StatusCode.UNKNOWN_ERROR.getCode(), e.toString());
        return wrapModelAndView(res, request);
    }

    private ModelAndView wrapModelAndView(ResultModel resultModel, HttpServletRequest request) {
        ModelAndView modelAndView = isApiRequest(request) ?
                new ModelAndView(new MappingJackson2JsonView(objectMapper)) : //new MappingJackson2JsonView()
                new ModelAndView("error/500");
        modelAndView.addObject("code", resultModel.getCode());
        modelAndView.addObject("msg", resultModel.getMsg());
        modelAndView.addObject("data", resultModel.getData());
        modelAndView.addObject("time", resultModel.getTime());
        return modelAndView;
    }

    /**
     * 判断出错的API方法是返回json数据还是页面
     * 出错的API方法有ResponseBody注解，表示接口返回json数组。否则表示接口渲染页面
     */
    private boolean isApiRequest(HttpServletRequest request) {
        return request.getHeader("Accept") == null ||
                !request.getHeader("Accept").contains("text/html");
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
