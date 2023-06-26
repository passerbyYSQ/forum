package top.ysqorz.forum.oauth;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import tk.mybatis.mapper.entity.Example;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.exception.ParamInvalidException;
import top.ysqorz.forum.dao.UserMapper;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.service.RedisService;
import top.ysqorz.forum.service.UserService;
import top.ysqorz.forum.shiro.ShiroUtils;
import top.ysqorz.forum.utils.CommonUtils;
import top.ysqorz.forum.utils.RandomUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * 第三方授权接口
 *
 * @param <T> getUser()返回值的类型
 * @author 阿灿
 * @create 2021-06-13 20:57
 */
@Getter
@Setter
public abstract class AbstractOauthProvider<T> implements OauthProvider<T> {

    protected String clientId;
    protected String clientSecret;
    protected String redirectUri;
    // 第三方账号的唯一标识在PO类中的属性名
    protected String poField;
    @Resource // 子类加上了@Component，父类可以注入成功
    protected UserMapper userMapper;
    @Resource
    protected UserService userService;
    @Resource
    private RedisService redisService;

    /**
     * 请求用户授权
     *
     * @param referer 用户在哪个页面点击了授权登录的按钮
     */
    @Override
    public String redirectAuthorize(String referer) {
        String originReferer = CommonUtils.getUrlParam(referer, "referer"); // 登录页面
        if (ObjectUtils.isEmpty(originReferer)) {
            originReferer = ObjectUtils.isEmpty(referer) ? "/" : referer; // 账号绑定页面
        }
        String key = RandomUtils.generateUUID();
        // 注意不允许有&符号
        String salt = RandomUtils.generateStrWithNumAndLetter(4);
        // 用于防止CSRF(跨站请求伪造)
        String state = String.format("%s,%s,%s", originReferer, key, salt);
        // 存入redis
        redisService.saveOauthState(key, state);
        return "redirect:" + this.joinAuthorizeUrl(state);
    }

    @Override
    public String oauthCallback(String state, String code, HttpServletResponse resp) {
        String referer = this.checkState(state);
        String accessToken = this.getAccessToken(code);
        T oauthUser = this.getOauthUser(accessToken);
        if (ObjectUtils.isEmpty(oauthUser)) {
            throw new ParamInvalidException(StatusCode.OAUTH_FAILED);
        }
        String uniqueId = this.getUniqueId(oauthUser);
        User dbUser = this.getDbUser(uniqueId);

        // 第一次判断是否有该第三方授权绑定的用户
        if (ObjectUtils.isEmpty(dbUser)) {
            // 第二次判断是已登录用户还是要注册用户
            // 已登录，说明此操作是：绑定第三方账号
            if (ShiroUtils.isAuthenticated()) {
                dbUser = userService.getUserById(ShiroUtils.getUserId());
                if (!ObjectUtils.isEmpty(dbUser.getEmail())) {
                    this.updateUniqueId(dbUser, uniqueId);
                    userMapper.updateByPrimaryKeySelective(dbUser);
                }
            } else {
                // 未登录，说明此操作是：通过第三方账号授权注册
                LocalDateTime now = LocalDateTime.now();
                dbUser = new User();
                dbUser.setPassword("")
                        .setRegisterTime(now)
                        .setModifyTime(now)
                        .setConsecutiveAttendDays(0)
                        .setRewardPoints(0)
                        .setFansCount(0)
                        .setJwtSalt("")
                        .setLoginSalt(RandomUtils.generateStr(8));
                this.updateUniqueId(dbUser, uniqueId);
                this.completeDbUser(oauthUser, dbUser);
                userMapper.insertUseGeneratedKeys(dbUser);
            }
        }
        if (!ShiroUtils.isAuthenticated()) {
            // 第三方授权登录，签发我们自己的token
            userService.login(dbUser.getId(), dbUser.getLoginSalt(), resp);
        }

        return this.oauthCallbackRedirect(referer, dbUser);
    }

    private void updateUniqueId(User dbUser, String uniqueId) {
        String setter = "set" + StrUtil.upperFirst(poField);
        ReflectUtil.invoke(dbUser, setter, uniqueId);
    }

    /**
     * 获取 DB User
     *
     * @param uniqueId 第三方账号的唯一标识
     */
    private User getDbUser(String uniqueId) {
        if (ObjectUtils.isEmpty(uniqueId)) {
            throw new ParamInvalidException(StatusCode.OAUTH_FAILED);
        }
        Example example = new Example(User.class);
        example.createCriteria().andEqualTo(poField, uniqueId);
        return userMapper.selectOneByExample(example);
    }

    /**
     * 校验state，防止CSRF
     */
    private String checkState(String state) {
        state = URLUtil.decode(state, StandardCharsets.UTF_8);
        String[] params = state.split(",");
        if (params.length != 3) {
            throw new ParamInvalidException(StatusCode.CSRF_ATTACK);
        }
        String correctState = redisService.getOauthState(params[1]);
        if (ObjectUtils.isEmpty(correctState) || !correctState.equals(state)) {
            throw new ParamInvalidException(StatusCode.CSRF_ATTACK);
        }
        return params[0]; // 返回真正的referer
    }

    private String oauthCallbackRedirect(String referer, User user) {
        StatusCode code = StatusCode.SUCCESS;
        // 如果是从绑定界面过来
        if (ShiroUtils.isAuthenticated()) {
            if (!ShiroUtils.getUserId().equals(user.getId())) { // 说明已经被其他账号绑定了
                code = StatusCode.ACCOUNT_IS_BOUND;
            } else if (ObjectUtils.isEmpty(user.getEmail())) { // 尚未设置邮箱，不能进行绑定或解绑操作
                code = StatusCode.EMAIL_NOT_SET;
            }
        }
        // 重定向携带token
        //redirectAttributes.addAttribute("token", token);
        // redirect:后不要加 "/"
        return String.format("redirect:%s?code=%d&msg=%s", referer, code.getCode(), URLUtil.encodeAll(code.getMsg()));
    }

    /**
     * 拼接请求授权的url
     */
    protected abstract String joinAuthorizeUrl(String state);

    protected abstract String getUniqueId(T oauthUser);

    protected abstract void completeDbUser(T oauthUser, User dbUser);
}
