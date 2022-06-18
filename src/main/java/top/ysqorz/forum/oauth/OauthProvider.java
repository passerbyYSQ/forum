package top.ysqorz.forum.oauth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.common.exception.ParameterInvalidException;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dao.UserMapper;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.utils.CommonUtils;
import top.ysqorz.forum.utils.RandomUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * 第三方授权接口
 *
 * @param <T> getUser()返回值的类型
 * @author 阿灿
 * @create 2021-06-13 20:57
 */
@Getter
@Setter
public abstract class OauthProvider<T> {

    protected String clientId;
    protected String clientSecret;
    protected String redirectUri;
    // 第三方账号的唯一标识在PO类中的属性名
    protected String poField;

    @Resource // 子类加上了@Component，父类可以注入成功
    private UserMapper userMapper;

    @Resource
    private RedisService redisService;


    /**
     * 请求用户授权
     *
     * @param referer 用户在哪个页面点击了授权登录的按钮
     */
    public String redirectAuthorize(String referer) {
        String originReferer = CommonUtils.getUrlParam(referer, "referer");
        if (ObjectUtils.isEmpty(originReferer)) {
            originReferer = "/";
        }
        String key = RandomUtils.generateUUID();
        // 注意不允许有&符号
        String salt = RandomUtils.generateStrWithNumAndLetter(4);
        // 用于防止CSRF(跨站请求伪造)
        String state = String.format("%s,%s,%s", originReferer, key, salt);
        // 存入redis
        redisService.saveOauthState(key, state);
        return "redirect:" + joinAuthorizeUrl(state);
    }

    /**
     * 模板方法，不允许子类重写
     *
     * @param code
     */
    public final T getUser(String code) throws IOException {
        T oauthUser = getOauthUser(getAccessToken(code));
        if (ObjectUtils.isEmpty(oauthUser)) {
            throw new ParameterInvalidException(StatusCode.OAUTH_FAILED);
        }
        return oauthUser;
    }

    /**
     * 获取 DB User
     *
     * @param openId 第三方账号的唯一标识
     */
    public final User getDbUser(Object openId) {
        if (ObjectUtils.isEmpty(openId)) {
            throw new ParameterInvalidException(StatusCode.OAUTH_FAILED);
        }
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo(poField, openId); //gitee_id
        return userMapper.selectOneByExample(example);
    }

    /**
     * 校验state，防止CSRF
     *
     * @return
     */
    public String checkState(String state) {
        try {
            state = URLDecoder.decode(state, "utf-8"); // url decode
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] params = state.split(",");
        if (params.length != 3) {
            throw new ParameterInvalidException(StatusCode.CSRF_ATTACK);
        }
        String correctState = redisService.getOauthState(params[1]);
        if (ObjectUtils.isEmpty(correctState) || !correctState.equals(state)) {
            throw new ParameterInvalidException(StatusCode.CSRF_ATTACK);
        }
        return params[0]; // 返回真正的referer
    }

    /**
     * 拼接请求授权的url
     */
    protected abstract String joinAuthorizeUrl(String state);

    /**
     * 获取Token
     */
    protected abstract String getAccessToken(String code) throws IOException;

    /**
     * 获取返回用户信息，自行封装到T
     */
    protected abstract T getOauthUser(String accessToken) throws IOException;
}
