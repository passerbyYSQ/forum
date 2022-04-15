package top.ysqorz.forum.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import top.ysqorz.forum.shiro.ShiroUtils;

/**
 * @author passerbyYSQ
 * @create 2021-05-06 20:52
 */
@Controller
@RequestMapping("/admin")
public class AdminIndexController {

    // 注意不要这么写 {"/", "/index"}，这样写 /admin 访问不了，要 /admin/ 才能访问
   // @RequiresPermissions("admin:access")
    @GetMapping({"", "/index"})
    public String indexPage(Model model) {
        model.addAttribute("user", ShiroUtils.getLoginUser());
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
    @RequiresPermissions("perm:view")
    @GetMapping("/system/authorities")
    public String authoritiesPage() {
        return "admin/system/authorities";
    }

    /**
     * 角色管理页面
     */
    @RequiresPermissions("role:view")
    @GetMapping("/system/role")
    public String rolePage() {
        return "admin/system/role";
    }


    /**
     * 用户管理界面
     */
    @RequiresPermissions("user:view")
    @GetMapping("/system/user")
    public String userPage() {
        return "admin/system/user";
    }

    /**
     * 话题管理
     */
    @RequiresPermissions("topic:view")
    @GetMapping("/system/topic")
    public String topicPage() {
        return "admin/system/topic";
    }

    /**
     * 帖子管理
     */
    @RequiresPermissions("post:view")
    @GetMapping("/system/post")
    public String postPage() {
        return "admin/system/post";
    }

}
