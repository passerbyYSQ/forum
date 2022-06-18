package top.ysqorz.forum.controller.front;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.exception.ServiceFailedException;
import top.ysqorz.forum.po.DanmuMsg;
import top.ysqorz.forum.po.Video;
import top.ysqorz.forum.service.DanmuService;
import top.ysqorz.forum.service.VideoService;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

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
    @Resource
    private DanmuService danmuService;

    @ResponseBody
    @PostMapping("/danmu/send")
    public StatusCode sendDanmu(@NotNull Integer videoId, @Length(max = 255) String content,
                                @Range Long startMs, @NotBlank String channelId) {
        return danmuService.sendDanmu(videoId, content, startMs, channelId);
    }

    @GetMapping("/{videoId}")
    public String display(@PathVariable @NotNull Integer videoId, Model model) {
        Video video = videoService.getVideoDetailById(videoId);
        model.addAttribute("video", video);
        return "front/other/video";
    }

    @ResponseBody
    @GetMapping("/danmu/{videoId}")
    public List<DanmuMsg> getDanmuList(@PathVariable @NotNull Integer videoId) {
        Video video = videoService.getVideoById(videoId);
        if (ObjectUtils.isEmpty(video)) {
            throw new ServiceFailedException(StatusCode.VIDEO_NOT_EXIST);
        }
        return danmuService.getDanmuListByVideoId(videoId);
    }
}
