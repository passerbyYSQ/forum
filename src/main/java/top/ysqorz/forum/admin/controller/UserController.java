package top.ysqorz.forum.admin.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.admin.service.UserService;
import top.ysqorz.forum.vo.PageData;
import top.ysqorz.forum.vo.ResultModel;
import top.ysqorz.forum.vo.StatusCode;
import top.ysqorz.forum.po.Blacklist;
import top.ysqorz.forum.po.User;
import top.ysqorz.forum.vo.MyUser;
import top.ysqorz.forum.vo.QueryUserCondition;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author 阿灿
 * @create 2021-05-10 16:08
 */
@RestController
@RequestMapping("/admin/system/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 获取User数据
     */
    @GetMapping("/table")
    public ResultModel<PageData<MyUser>> authorityTree(@RequestParam(defaultValue = "10") Integer limit,
                                                     @RequestParam(defaultValue = "1") Integer page,
                                                     QueryUserCondition conditions) {
        //System.out.println(conditions);
        if (limit<=0){
            limit=10;
        }
        PageHelper.startPage(page,limit);
      //  PageHelper.clearPage(); //不加报错
        List<MyUser> myUserList = userService.getMyUserList(conditions);
       // List<User> userList = userService.getUserList(conditions);
        PageInfo<MyUser> pageinfo = new PageInfo<>(myUserList);
        PageData<MyUser> pageData=new PageData<>();
        pageData.setList(myUserList);
        pageData.setTotal(pageinfo.getTotal());
        pageData.setPage(pageinfo.getPageNum());
        pageData.setCount(pageinfo.getPageSize());

        return ResultModel.success(pageData);
    }

    /**
     * 重置密码
     *
     */
    @PostMapping("/resetpsw")
    public ResultModel Resetpsw(@RequestParam("userId") Integer userId){
        User user = userService.getinfobyId(userId);
        int i = userService.updatePsw(user);


        return i == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode. USER_NOT_EXIST);
    }
    /**
     * 取消拉黑
     */
    @PostMapping("/cancelblock")
    public ResultModel cancelblock(@RequestParam("userId") Integer userId){
        int i = userService.cancelblock(userId);
        return i == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode. USER_NOT_EXIST);
    }

    /**
     * 拉黑
     */
    @PostMapping("/block")
    public ResultModel block(@RequestParam("adminId") Integer adminId,
                             @RequestParam("id") Integer id,
                             @RequestParam("data") String data,
                             @RequestParam("reason") String reason){
        //System.out.println(data+reason+id+adminId);
        String[] time=data.split(" - ");
       // System.out.println(time[0]+time[1]);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime starttime = LocalDateTime.parse(time[0],df);
        LocalDateTime endtime = LocalDateTime.parse(time[1],df);
        Blacklist blacklist = new Blacklist();
        blacklist.setAdminId(adminId);
        blacklist.setUserId(id);
        blacklist.setReason(reason);
        blacklist.setCreateTime(LocalDateTime.now());
        blacklist.setEndTime(endtime);
        blacklist.setStartTime(starttime);
        blacklist.setIsRead((byte) 0);
        int i = userService.block(blacklist);
        return i == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode. USERNAME_IS_EXIST);

    }

}
