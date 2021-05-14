package top.ysqorz.forum.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author passerbyYSQ
 * @create 2021-05-06 20:52
 */
@Controller
@RequestMapping("/admin")
public class IndexController {

    // 注意不要这么写 {"/", "/index"}，这样写 /admin 访问不了，要 /admin/ 才能访问
    @GetMapping({"", "/index"})
    public String index() {
        return "admin/index";
    }


    @GetMapping("/console/workplace")
    public String workplace() {
        return "admin/console/workplace";
    }

    /**
     * 权限管理页面
     */
    @GetMapping("/system/authorities")
    public String authorities() {
        return "admin/system/authorities";
    }

    /**
     * 角色管理页面
     */
    @GetMapping("/system/role")
    public String role() {
        return "admin/system/role";
    }


}
