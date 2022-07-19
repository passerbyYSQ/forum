package top.ysqorz.forum.shiro;

/**
 * 权限字符串常量
 *
 * @author passerbyYSQ
 * @create 2022-07-19 21:27
 */
public interface Permission {
    String ADMIN_ACCESS = "admin:access";
    //权限相关
    String PERM_VIEW = "perm:view";
    String PERM_ADD = "perm:add";
    String PERM_UPDATE = "perm:update";
    String PERM_DELETE = "perm:delete";

    // 角色相关
    String ROLE_VIEW = "role:view";
    String ROLE_ADD = "role:add";
    String ROLE_UPDATE = "role:update";
    String ROLE_ALLOT_PERM = "role:allotPerm";
    String ROLE_DELETE = "role:delete";


    // 用户相关
    String USER_VIEW = "user:view";
    String USER_RESET_PWD = "user:resetPwd";
    String USER_BLACKLIST = "user:blacklist";
    String USER_ALLOT_ROLE = "user:allotRole";
    String USER_DELETE_ROLE = "user:deleteRole";

    // 话题相关
    String TOPIC_VIEW = "topic:view";
    String TOPIC_ADD = "topic:add";
    String TOPIC_UPDATE = "topic:update";
    String TOPIC_ARCHIVE = "topic:archive";

    // 帖子相关
    String POST_VIEW = "post:view";
    String POST_UPDATE = "post:update";
    String POST_TOP = "post:top";
    String POST_LOCK = "post:lock";
    String POST_QUALITY = "post:quality";
    String POST_DELETE = "post:delete";

    // 评论相关
    String COMMENT_DELETE = "comment:delete";
}
