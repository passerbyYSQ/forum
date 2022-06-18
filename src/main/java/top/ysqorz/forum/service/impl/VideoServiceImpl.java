package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import top.ysqorz.forum.common.exception.ParameterInvalidException;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dao.VideoMapper;
import top.ysqorz.forum.po.DbFile;
import top.ysqorz.forum.po.Video;
import top.ysqorz.forum.service.VideoService;

import javax.annotation.Resource;
import java.time.Instant;

/**
 * @author passerbyYSQ
 * @create 2022-01-26 17:22
 */
@Service
public class VideoServiceImpl implements VideoService {
    @Resource
    private VideoMapper videoMapper;

    @Override
    public Video getVideoDetailById(Integer videoId) {
        Video video = videoMapper.selectVideoDetailById(videoId);
        if (ObjectUtils.isEmpty(video)) {
            throw new ParameterInvalidException(StatusCode.VIDEO_NOT_EXIST);
        }
        DbFile file = video.getFile();
        if (ObjectUtils.isEmpty(file)) {
            throw new ParameterInvalidException(StatusCode.FILE_NOT_EXIST);
        }
        if (!ObjectUtils.isEmpty(file.getLocalPath())) {
            String completedLocalPath = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(file.getLocalPath())
                    .queryParam("timestamp", Instant.now().toEpochMilli())
                    .toUriString();
            file.setLocalPath(completedLocalPath);
        }
        return video;
    }

    @Override
    public Video getVideoById(Integer videoId) {
        return videoMapper.selectByPrimaryKey(videoId);
    }
}
