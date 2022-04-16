package top.ysqorz.forum.service.impl;

import com.github.pagehelper.PageHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.util.HtmlUtils;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.dao.LikeMapper;
import top.ysqorz.forum.dao.PostMapper;
import top.ysqorz.forum.dao.UserMapper;
import top.ysqorz.forum.dto.*;
import top.ysqorz.forum.dto.req.PublishPostDTO;
import top.ysqorz.forum.dto.req.QueryPostCondition;
import top.ysqorz.forum.dto.resp.PostDTO;
import top.ysqorz.forum.dto.resp.PostDetailDTO;
import top.ysqorz.forum.dto.resp.SimpleUserDTO;
import top.ysqorz.forum.po.*;
import top.ysqorz.forum.service.*;
import top.ysqorz.forum.shiro.ShiroUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author passerbyYSQ
 * @create 2021-05-24 23:29
 */
@Service
public class PostServiceImpl implements PostService {
    @Resource
    private PostMapper postMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private PostLabelService postLabelService;
    @Resource
    private TopicService topicService;
    @Resource
    private LabelService labelService;
    @Resource
    private UserService userService;
    @Resource
    private LikeMapper likeMapper;
    @Resource
    private LikeService likeService;
    @Resource
    private CollectService collectService;
    @Resource
    private RedisService redisService;
    @Resource
    private RewardPointsAction rewardPointsAction;
    @Resource
    private PermManager permManager;

    @Transactional
    @Override
    public void changeHighQuality(Integer userId, Integer postId, Boolean isHighQuality) {
        // 更新精品状态
        Post post = new Post();
        post.setId(postId).setIsHighQuality((byte) (isHighQuality ? 1 : 0));
        this.updatePostById(post);

        // 奖励积分 或者 撤销积分。TODO 到时候需要从配置中读取奖励的积分值，判断每日分值上限
        //userService.updateRewardPoints(userId, isHighQuality ? 5 : -5);
        if (isHighQuality) {
            rewardPointsAction.highQualityPost(userId);
        }
    }

    @Override
    public Post getPostById(Integer postId) {
        return postMapper.selectByPrimaryKey(postId);
    }

    @Override
    public int updatePostById(Post post) {
        return postMapper.updateByPrimaryKeySelective(post);
    }

    @Transactional
    @Override
    public StatusCode deletePostById(Integer postId) {
        Post post = this.getPostById(postId);
        if (ObjectUtils.isEmpty(post)) {
            return StatusCode.POST_NOT_EXIST;
        }
        // 权限判断
        if (!permManager.allowDelPost(post.getCreatorId())) {
            return StatusCode.NO_PERM;
        }
        // 删除标签关联
        postLabelService.delPostLabelByPostId(postId);
        // 不删除点赞
        // 不删除收藏。收藏记录会显示【已删除】
        // 一级、二级评论均不删除。
        // 删除帖子
        postMapper.deleteByPrimaryKey(postId);
        // 维护话题的帖子数量
        topicService.updatePostCountById(post.getTopicId(), -1);
        // TODO 考虑积分回退
        return StatusCode.SUCCESS;
    }

    @Override
    public List<PostDTO> getPostList(QueryPostCondition condition) {
        return postMapper.selectListByConditions(condition);
    }

    @Override
    public Post addPost(PublishPostDTO vo) {
        Post post = new Post();
        LocalDateTime now = LocalDateTime.now();
        post.setCreatorId(ShiroUtils.getUserId()) // 发帖人id
                .setTopicId(vo.getTopicId())  // 所属话题的id
                .setTitle(vo.getTitle()) // 帖子标题
                // 帖子内容，由于全局XSS防护做了转义，此处需要反转义
                .setContent(HtmlUtils.htmlUnescape(vo.getContent()))
                // [0, 3]：表示4种可见性。其中3表示：帖子需要花积分（[4,99]）购买才可见
                // 所以当VisibilityType为3时，干脆直接用VisibilityType来存积分数x
                .setVisibilityType(vo.getVisibilityType() == 3 ? vo.getPoints() : vo.getVisibilityType())
                .setIsLocked((byte) (vo.getIsLocked() ? 1 : 0))


                .setCreateTime(now)
                .setLastModifyTime(now)

                .setLastCommentTime(now)//默认第一次评论时间等于发布时间！！！

                .setCollectCount(0) // 收藏数
                .setCommentCount(0) // 评论数（包括一二级评论）
                .setVisitCount(0) // 访问数
                .setLikeCount(0) // 点赞数

                .setIsHighQuality((byte) 0) // 不是精品帖
                .setTopWeight(0); // 置顶权重，0：不置顶

        postMapper.insertUseGeneratedKeys(post);

        // 对应的话题下面的帖子数 +1
        topicService.updatePostCountById(vo.getTopicId(), 1);

        return post;
    }

    @Transactional // 开启事务
    @Override
    public Post publishPost(PublishPostDTO vo) {
        // 注意要用this对象，否则调用的不是被AOP代理后的对象的方法
        Post post = this.addPost(vo);
        // 批量插入帖子和标签的映射关系
        postLabelService.addPostLabelList(post.getId(), vo.splitLabels());
        // 奖励积分
        rewardPointsAction.publishPost();
        return post;
    }

    @Transactional
    @Override
    public Post updatePostAndLabels(PublishPostDTO vo) {
        Post post = new Post();
        post.setId(vo.getPostId())
                .setTopicId(vo.getTopicId())
                .setLastModifyTime(LocalDateTime.now()) // 最后一次的更新时间
                .setTitle(vo.getTitle())
                .setContent(HtmlUtils.htmlUnescape(vo.getContent())) // XSS防护做了转义，此处需要反转义
                .setVisibilityType(vo.getVisibilityType() == 3 ?
                        vo.getPoints() : vo.getVisibilityType())
                .setIsLocked((byte) (vo.getIsLocked() ? 1 : 0));
        this.updatePostById(post);

        // 先把当与前帖子相关标签的映射关系全部删除
        postLabelService.delPostLabelByPostId(vo.getPostId());

        // 批量插入帖子和标签的映射关系
        postLabelService.addPostLabelList(vo.getPostId(), vo.splitLabels());

        return post;
    }

    @Override
    public PostDetailDTO getPostDetailById(Post post) {
        // 发帖者
        SimpleUserDTO creator = userService.getSimpleUser(post.getCreatorId());
        // 所属话题
        Topic topic = topicService.getTopicById(post.getTopicId());
        // 相关标签
        List<Label> labelList = labelService.getLabelsByPostId(post.getId());
        PostDTO postDTO = new PostDTO(post, creator, topic, labelList);

        PostDetailDTO postDetailDTO = new PostDetailDTO();
        postDetailDTO.setDetail(postDTO);

        if (ShiroUtils.isAuthenticated()) { // 当前主体已登录
            Integer myId = ShiroUtils.getUserId(); // 当前登录用户的userId
            // 我是否已经点赞
            Like like = likeService.getLikeByUserIdAndPostId(myId, post.getId());
            if (!ObjectUtils.isEmpty(like)) {
                postDetailDTO.setLikeId(like.getId());
            }
            // 我是否已收藏
            Collect collect = collectService.getCollectByUserIdAndPostId(myId, post.getId());
            if (!ObjectUtils.isEmpty(collect)) {
                postDetailDTO.setCollectId(collect.getId());
            }
        }

        // 点赞列表
        List<PostDetailDTO.Liker> likerList = likeService.getLikerListByPostId(post.getId(), 16);
        postDetailDTO.setLikerList(likerList);
        // 权限
        boolean isCreator = creator.getId().equals(ShiroUtils.getUserId()); // 不登陆也不会报错
        postDetailDTO.setIsShowEdit(ShiroUtils.hasPerm("post:update") || isCreator); // 不登陆也会返回false
        postDetailDTO.setIsShowDelete(ShiroUtils.hasPerm("post:delete") || isCreator);
        postDetailDTO.setIsShowTop(ShiroUtils.hasPerm("post:top"));
        postDetailDTO.setIsShowQuality(ShiroUtils.hasPerm("post:quality"));
        // 是否热门
        Set<Integer> top5 = redisService.hostPostDayRankTop(5);
        postDetailDTO.setIsHot(top5.contains(post.getId()));
        return postDetailDTO;
    }

    @Override
    public Post addVisitCount(String ipAddress, Post post) {
        // post:visit:11:ip --> ""   5 minute
        // 不存在就设置，存在就不设置
        // 如果设置失败，说明：在5分钟之内，访问了多次，不加访问量。否则访问量+1
        if (redisService.tryAddPostVisitIp(ipAddress, post.getId())) {
            postMapper.addVisitCount(post.getId());
            post.setVisitCount(post.getVisitCount() + 1); // ！！！
            // 维护 帖子访问量日榜
            redisService.addHotPostDayRankScore(post.getId());
            // 维护 帖子访问量周榜
            redisService.addHotPostWeekRankScore(post.getId());
            // 判断是否增加积分
            rewardPointsAction.visitCountAdded(post.getCreatorId(), post.getVisitCount()); // ！！
        }
        return post;
    }

    @Override
    public int updateCommentCountAndLastTime(Integer postId, Integer dif) {
        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("dif", dif);
        return postMapper.updateCommentCountAndLastTime(params);
    }


    @Transactional
    @Override
    public Like addLike(Integer postId) {
        Like like = new Like();
        like.setUserId(ShiroUtils.getUserId())
                .setPostId(postId)
                .setCreateTime(LocalDateTime.now())
                .setIsRead((byte) 0);
        likeMapper.insertUseGeneratedKeys(like);

        // 点赞数 +1
        this.addLikeCount(postId, 1);

        return like;
    }

    @Transactional
    @Override
    public int cancelLike(Integer likeId, Integer postId) {
        this.addLikeCount(postId, -1);

        return likeMapper.deleteByPrimaryKey(likeId);
    }

    @Override
    public int addLikeCount(Integer postId, Integer dif) {
        Map<String, Object> params = new HashMap<>();
        params.put("postId", postId);
        params.put("dif", dif);
        return postMapper.addLikeCount(params);
    }

    @Override
    public PageData<PostDTO> getPostListByCreatorId(Integer userId, Integer page, Integer count) {
        PageHelper.startPage(page, count);
        List<PostDTO> postDTOList = postMapper.selectPostListByCreatorId(userId);
        return new PageData<>(postDTOList);
    }

    @Override
    public PageData<PostDTO> getIndexPost(Integer page, Integer count, QueryPostCondition conditions) {
        PageHelper.startPage(page, count);
        //  PageHelper.clearPage(); //不加报错
        if (ShiroUtils.isAuthenticated()) { // 当前主体已登录
            Integer myId = ShiroUtils.getUserId(); // 当前登录用户的userId
            conditions.setUserId(myId);
        }
        List<PostDTO> postList = this.getPostList(conditions);

        Set<Integer> top5 = redisService.hostPostDayRankTop(5);

        for (PostDTO post : postList) {
            Document doc = Jsoup.parse(post.getContent());
            String content = doc.text();  //获取文本。不包括html标签
            if (content.length() >= 100) {
                content= content.substring(0, 100);
            }
            post.setContent(content);
            // 排除表情图片。表情图片有个特点：alt="[xxx]"。排除掉这种情况的图片
            //Pattern.compile("^((?!\\[\\S+\\]).)*$"); // 正则表达式测试
            // https://www.jb51.net/article/52491.htm
            // https://blog.csdn.net/qq_24549805/article/details/52833463
            Elements images = doc.select("img[src]"); //[alt~=^((?!\[\S+\]).)*$]
            List<String> imageList = images.stream()
                    .filter(element -> {
                        String alt = element.attr("alt");
                        return ObjectUtils.isEmpty(alt) ||
                                !(alt.charAt(0) == '[' && alt.charAt(alt.length() - 1) == ']');
                    })
                    .map(element -> element.attr("src"))
                    .limit(3)
                    .collect(Collectors.toList());

            post.setImagesList(imageList);

            // 判断是否是日榜前5
            post.setIsHot(top5.contains(post.getId()));
        }
        // List<User> userList = userService.getUserList(conditions);
        return new PageData<>(postList);
    }

}
