package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.constraints.NotNull;

/**
 * @author passerbyYSQ
 * @create 2022-01-16 16:14
 */
@Controller
@RequestMapping("/video")
@Validated
public class VideoController {

    @GetMapping("/{videoId}")
    public String display(@PathVariable @NotNull Integer videoId) {
        // TODO query video
        return "front/other/video";
    }
}
