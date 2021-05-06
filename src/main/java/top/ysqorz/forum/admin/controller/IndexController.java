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

    @GetMapping({"/index", "/"})
    public String index() {
        return "admin/index";
    }


    @GetMapping("/console/workplace")
    public String workplace() {
        return "admin/console/workplace";
    }

}
