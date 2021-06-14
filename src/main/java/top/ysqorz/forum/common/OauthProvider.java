package top.ysqorz.forum.common;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.dao.UserMapper;
import top.ysqorz.forum.po.User;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 第三方授权接口
 * @param <T>       getUser()返回值的类型
 *
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

    public abstract String joinAuthorizeUrl(String referer, HttpServletResponse response);

    /**
     * 请求用户授权
     * @param referer       用户在哪个页面点击了授权登录的按钮
     */
    public void requestAuthorize(String referer, HttpServletResponse response) throws IOException {
        if (referer.endsWith("/user/login")) {
            referer = "/";
        }
        response.sendRedirect(joinAuthorizeUrl(referer, response));
    };

    /**
     * 获取Token
     * @param code  授权码
     */
    protected abstract String getAccessTokenByCode(String code) throws IOException;

    /**
     * 获取返回用户信息，自行封装到T
     * @param accessToken
     */
    protected abstract T getUserByAccessToken(String accessToken) throws IOException;

    /**
     * 模板方法，不允许子类重写
     * @param code
     */
    public final T getUser(String code) throws IOException, ParameterErrorException {
        T oauthUser = getUserByAccessToken(getAccessTokenByCode(code));
        if (ObjectUtils.isEmpty(oauthUser)) {
            throw new ParameterErrorException("Oauth授权登录出错");
        }
        return oauthUser;
    }

    /**
     * 获取 DB User
     * @param openId    第三方账号的唯一标识
     */
    public final User getDbUser(Object openId) {
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo(poField, openId); //gitee_id
        return userMapper.selectOneByExample(example);
    }
}
