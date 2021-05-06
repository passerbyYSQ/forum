package top.ysqorz.forum.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.common.ResultModel;

/**
 * @author passerbyYSQ
 * @create 2021-05-06 15:32
 */
@Controller
@RequestMapping("/test")
public class TestCatchException {

    @RequestMapping("/ex")
    @ResponseBody
    public ResultModel<String> ex(String wd) {
        int num = Integer.parseInt(wd);
        return ResultModel.success();
    }

    @GetMapping("/ex/html")
    public String exHtml() {
        int i = 1 / 0;
        return "admin/index";
    }


}
