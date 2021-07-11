package top.ysqorz.forum.controller.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dto.*;
import top.ysqorz.forum.po.Attendance;
import top.ysqorz.forum.po.Label;
import top.ysqorz.forum.po.Topic;
import top.ysqorz.forum.service.*;
import top.ysqorz.forum.shiro.ShiroUtils;

import javax.annotation.Resource;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author passerbyYSQ
 * @create 2021-05-22 23:52
 */
@Controller
@Validated
public class IndexController {

    @Resource
    private PostService postService;
    @Resource
    private UserService userService;
    @Resource
    private TopicService topicService;
    @Resource
    private AttendService attendService;
    @Resource
    private LabelService labelService;
    @Resource
    private RedisService redisService;

    // 注意不要这么写 {"/", "/index"}，这样写 /admin 访问不了，要 /admin/ 才能访问
    @GetMapping({"", "/index"})
    public String index(Model model) {
        SimpleUserDTO loginUser = userService.getLoginUser();
        model.addAttribute("myUser", loginUser);

        boolean isAttendToady = false;
        if (ShiroUtils.isAuthenticated()) { // 已登录
            LocalDateTime lastTime = loginUser.getLastAttendTime();
            isAttendToady = !ObjectUtils.isEmpty(lastTime) &&
                    LocalDate.now().equals(lastTime.toLocalDate());
            if (isAttendToady) { // 今天签到了
                Integer rank = attendService.attendRankNum(loginUser.getId(), lastTime);
                model.addAttribute("attendRank", rank); // 签到排名
            }
        }
        model.addAttribute("isAttendToady", isAttendToady); // 今天是否签到

        // 已签到的人数
        Integer attendCount = attendService.attendedCount(LocalDateTime.now());
        model.addAttribute("attendCount", attendCount);

        // 话题
        List<Topic> allTopic = topicService.getTopicByHot();
        model.addAttribute("topics", allTopic);

        // 热议周榜是否有信息
        Boolean isHaveWeekHotPost = redisService.isHaveWeekHotPost();
        model.addAttribute("isHaveWeekHotPost" ,isHaveWeekHotPost);
        return "front/index";
    }

    @GetMapping("/head")
    public String head(Model model) {
        SimpleUserDTO loginUser = userService.getLoginUser();
        model.addAttribute("myUser", loginUser);
        return "front/head";
    }

    /**
     * 获取Post列表
     */
    @ResponseBody
    @GetMapping("/index/list")
    public ResultModel<PageData<PostDTO>> getPostList(@RequestParam(defaultValue = "10") Integer limit,
                                                      @RequestParam(defaultValue = "1") Integer page,
                                                      QueryPostCondition conditions) {
        if (limit <= 0) {
            limit = 10;
        }
        conditions.splitLabelsStr();
        PageData<PostDTO> indexPost = postService.getIndexPost(page, limit, conditions);
        return ResultModel.success(indexPost);
    }

    /**
     * 签到
     */
    //@RequiresAuthentication // 不能使用这个注解，因为认证检查不会经过我们自定义的jwtAuth拦截器
    @PostMapping("/attend")
    @ResponseBody
    public ResultModel<AttendDTO> attend() {
        Attendance attend = attendService.getMyAttendToday(); // 判断今天我是否已经签到过
        if (!ObjectUtils.isEmpty(attend)) {
            return ResultModel.failed(StatusCode.DO_NOT_REPEAT_OPERATE); // 请勿重复签到
        }
        AttendDTO attendDTO = attendService.attend();
        return ResultModel.success(attendDTO);
    }

    /**
     * 截至现在的签到日榜情况
     */
    @GetMapping("/attend/rank")
    @ResponseBody
    public ResultModel<List<AttendDTO>> attendRankList(@NotNull @Min(1) @Max(20)
                                                                   Integer count) {
        List<AttendDTO> rankList = attendService.rankList(count);
        return ResultModel.success(rankList);
    }

    /**
     * 最近一次连续签到的天数 排行榜
     */
    @GetMapping("/attend/consecutive/rank")
    @ResponseBody
    public ResultModel consecutiveDaysRankList(@NotNull @Min(1) @Max(20)
                                                       Integer count) {
        List<AttendDTO> rankList = attendService.consecutiveDaysRankList(count);
        return ResultModel.success(rankList);
    }

    /**
     * 随机获取指定数量个标签
     */
    @GetMapping("/index/label")
    @ResponseBody
    public ResultModel achieveRandomLabels() {
        Integer total = 10;
        List<Label> labelList = labelService.achieveRandomLabels(total);
        return ResultModel.success(labelList);
    }

    @GetMapping("/index/week/post")
    @ResponseBody
    public ResultModel getWeekTopPostList() {
        Integer count = 5;
        List<WeekTopPostDTO> postList = redisService.hostPostWeekRankTop(count);
        return ResultModel.success(postList);
    }

}
