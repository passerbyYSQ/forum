package top.ysqorz.forum.controller.front;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.dto.PageData;
import top.ysqorz.forum.dto.PostDTO;
import top.ysqorz.forum.dto.QueryPostCondition;
import top.ysqorz.forum.service.PostService;

import javax.annotation.Resource;
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

    // 注意不要这么写 {"/", "/index"}，这样写 /admin 访问不了，要 /admin/ 才能访问
    @GetMapping({"", "/index"})
    public String index() {
        return "front/index";
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
        PageHelper.startPage(page, limit);
        //  PageHelper.clearPage(); //不加报错
        List<PostDTO> postList = postService.getPostList(conditions);
        for(PostDTO post:postList){
            Document doc = Jsoup.parse(post.getContent());
            String  content=doc.text();  //获取html文本
            if(content.length()>=160){
                String substring = content.substring(0, 160);
                content=substring+"...";

            }
            post.setContent(content);
            Elements jpgs=doc.select("img[src]"); //　查找图片
            for(int i=0;i<jpgs.size();i++){
                Element jpg=jpgs.get(i);
                post.getImagesList().add(jpg.toString());
                if(i==3){
                    break;
                }
            }

        }
        // List<User> userList = userService.getUserList(conditions);
        PageInfo<PostDTO> pageInfo = new PageInfo<>(postList);
        PageData<PostDTO> pageData = new PageData<>();
        pageData.setList(postList);
        pageData.setTotal(pageInfo.getTotal());
        pageData.setPage(pageInfo.getPageNum());
        pageData.setCount(pageInfo.getPageSize());

        return ResultModel.success(pageData);
    }

}
