package top.ysqorz.forum.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author passerbyYSQ
 * @create 2021-05-06 20:52
 */
@Controller
@RequestMapping("/admin")
public class AdminIndexController {

    // 注意不要这么写 {"/", "/index"}，这样写 /admin 访问不了，要 /admin/ 才能访问
    @RequiresPermissions("admin:access")
    @GetMapping({"", "/index"})
    public String indexPage() {
        return "admin/index";
    }

    @RequiresPermissions("admin:access")
    @GetMapping("/console/workplace")
    public String workplacePage() {
        return "admin/console/workplace";
    }

    /**
     * 权限管理页面
     */
    @RequiresPermissions("admin:access")
    @GetMapping("/system/authorities")
    public String authoritiesPage() {
        return "admin/system/authorities";
    }

    /**
     * 角色管理页面
     */
    @RequiresPermissions("admin:access")
    @GetMapping("/system/role")
    public String rolePage() {
        return "admin/system/role";
    }


    /**
     * 用户管理界面
     */
    @RequiresPermissions("admin:access")
    @GetMapping("/system/user")
    public String userPage() {
        return "admin/system/user";
    }

    /**
     * 话题管理
     */
    @RequiresPermissions("admin:access")
    @GetMapping("/system/topic")
    public String topicPage() {
        return "admin/system/topic";
    }

    /**
     * 帖子管理
     */
    @RequiresPermissions("admin:access")
    @GetMapping("/system/post")
    public String postPage() {
        return "admin/system/post";
    }

}
