package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.ysqorz.forum.po.Video;
import top.ysqorz.forum.service.VideoService;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author passerbyYSQ
 * @create 2022-01-16 16:14
 */
@Controller
@RequestMapping("/video")
@Validated
public class VideoController {
    @Resource
    private VideoService videoService;

    @GetMapping("/{videoId}")
    public String display(@PathVariable @NotNull Integer videoId, Model model) {
        Video video = videoService.getVideoDetailById(videoId);
        model.addAttribute("video", video);
        return "front/other/video";
    }
}
