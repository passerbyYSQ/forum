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
import top.ysqorz.forum.shiro.Permission;
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
        // ??????????????????
        Post post = new Post();
        post.setId(postId).setIsHighQuality((byte) (isHighQuality ? 1 : 0));
        this.updatePostById(post);

        // ???????????? ?????? ???????????????TODO ??????????????????????????????????????????????????????????????????????????????
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
        // ????????????
        if (!permManager.allowDelPost(post.getCreatorId())) {
            return StatusCode.NO_PERM;
        }
        // ??????????????????
        postLabelService.delPostLabelByPostId(postId);
        // ???????????????
        // ??????????????????????????????????????????????????????
        // ????????????????????????????????????
        // ????????????
        postMapper.deleteByPrimaryKey(postId);
        // ???????????????????????????
        topicService.updatePostCountById(post.getTopicId(), -1);
        // TODO ??????????????????
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
        post.setCreatorId(ShiroUtils.getUserId()) // ?????????id
                .setTopicId(vo.getTopicId())  // ???????????????id
                .setTitle(vo.getTitle()) // ????????????
                // ???????????????????????????XSS??????????????????????????????????????????
                .setContent(HtmlUtils.htmlUnescape(vo.getContent()))
                // [0, 3]?????????4?????????????????????3?????????????????????????????????[4,99]??????????????????
                // ?????????VisibilityType???3?????????????????????VisibilityType???????????????x
                .setVisibilityType(vo.getVisibilityType() == 3 ? vo.getPoints() : vo.getVisibilityType())
                .setIsLocked((byte) (vo.getIsLocked() ? 1 : 0))


                .setCreateTime(now)
                .setLastModifyTime(now)

                .setLastCommentTime(now)//??????????????????????????????????????????????????????

                .setCollectCount(0) // ?????????
                .setCommentCount(0) // ????????????????????????????????????
                .setVisitCount(0) // ?????????
                .setLikeCount(0) // ?????????

                .setIsHighQuality((byte) 0) // ???????????????
                .setTopWeight(0); // ???????????????0????????????

        postMapper.insertUseGeneratedKeys(post);

        // ????????????????????????????????? +1
        topicService.updatePostCountById(vo.getTopicId(), 1);

        return post;
    }

    @Transactional // ????????????
    @Override
    public Post publishPost(PublishPostDTO vo) {
        // ????????????this?????????????????????????????????AOP???????????????????????????
        Post post = this.addPost(vo);
        // ??????????????????????????????????????????
        postLabelService.addPostLabelList(post.getId(), vo.splitLabels());
        // ????????????
        rewardPointsAction.publishPost();
        return post;
    }

    @Transactional
    @Override
    public Post updatePostAndLabels(PublishPostDTO vo) {
        Post post = new Post();
        post.setId(vo.getPostId())
                .setTopicId(vo.getTopicId())
                .setLastModifyTime(LocalDateTime.now()) // ???????????????????????????
                .setTitle(vo.getTitle())
                .setContent(HtmlUtils.htmlUnescape(vo.getContent())) // XSS??????????????????????????????????????????
                .setVisibilityType(vo.getVisibilityType() == 3 ?
                        vo.getPoints() : vo.getVisibilityType())
                .setIsLocked((byte) (vo.getIsLocked() ? 1 : 0));
        this.updatePostById(post);

        // ????????????????????????????????????????????????????????????
        postLabelService.delPostLabelByPostId(vo.getPostId());

        // ??????????????????????????????????????????
        postLabelService.addPostLabelList(vo.getPostId(), vo.splitLabels());

        return post;
    }

    @Override
    public PostDetailDTO getPostDetailById(Post post) {
        // ?????????
        SimpleUserDTO creator = userService.getSimpleUser(post.getCreatorId());
        // ????????????
        Topic topic = topicService.getTopicById(post.getTopicId());
        // ????????????
        List<Label> labelList = labelService.getLabelsByPostId(post.getId());
        PostDTO postDTO = new PostDTO(post, creator, topic, labelList);

        PostDetailDTO postDetailDTO = new PostDetailDTO();
        postDetailDTO.setDetail(postDTO);

        if (ShiroUtils.isAuthenticated()) { // ?????????????????????
            Integer myId = ShiroUtils.getUserId(); // ?????????????????????userId
            // ?????????????????????
            Like like = likeService.getLikeByUserIdAndPostId(myId, post.getId());
            if (!ObjectUtils.isEmpty(like)) {
                postDetailDTO.setLikeId(like.getId());
            }
            // ??????????????????
            Collect collect = collectService.getCollectByUserIdAndPostId(myId, post.getId());
            if (!ObjectUtils.isEmpty(collect)) {
                postDetailDTO.setCollectId(collect.getId());
            }
        }

        // ????????????
        List<PostDetailDTO.Liker> likerList = likeService.getLikerListByPostId(post.getId(), 16);
        postDetailDTO.setLikerList(likerList);
        // ??????
        boolean isCreator = creator.getId().equals(ShiroUtils.getUserId()); // ????????????????????????
        postDetailDTO.setIsShowEdit(ShiroUtils.hasPerm(Permission.POST_UPDATE) || isCreator); // ?????????????????????false
        postDetailDTO.setIsShowDelete(ShiroUtils.hasPerm(Permission.POST_DELETE) || isCreator);
        postDetailDTO.setIsShowTop(ShiroUtils.hasPerm(Permission.POST_TOP));
        postDetailDTO.setIsShowQuality(ShiroUtils.hasPerm(Permission.POST_QUALITY));
        // ????????????
        Set<Integer> top5 = redisService.hostPostDayRankTop(5);
        postDetailDTO.setIsHot(top5.contains(post.getId()));
        return postDetailDTO;
    }

    @Override
    public Post addVisitCount(String ipAddress, Post post) {
        // post:visit:11:ip --> ""   5 minute
        // ???????????????????????????????????????
        // ?????????????????????????????????5??????????????????????????????????????????????????????????????????+1
        if (redisService.tryAddPostVisitIp(ipAddress, post.getId())) {
            postMapper.addVisitCount(post.getId());
            post.setVisitCount(post.getVisitCount() + 1); // ?????????
            // ?????? ?????????????????????
            redisService.addHotPostDayRankScore(post.getId());
            // ?????? ?????????????????????
            redisService.addHotPostWeekRankScore(post.getId());
            // ????????????????????????
            rewardPointsAction.visitCountAdded(post.getCreatorId(), post.getVisitCount()); // ??????
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

        // ????????? +1
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
        //  PageHelper.clearPage(); //????????????
        if (ShiroUtils.isAuthenticated()) { // ?????????????????????
            Integer myId = ShiroUtils.getUserId(); // ?????????????????????userId
            conditions.setUserId(myId);
        }
        List<PostDTO> postList = this.getPostList(conditions);

        Set<Integer> top5 = redisService.hostPostDayRankTop(5);

        for (PostDTO post : postList) {
            Document doc = Jsoup.parse(post.getContent());
            String content = doc.text();  //????????????????????????html??????
            if (content.length() >= 100) {
                content= content.substring(0, 100);
            }
            post.setContent(content);
            // ????????????????????????????????????????????????alt="[xxx]"?????????????????????????????????
            //Pattern.compile("^((?!\\[\\S+\\]).)*$"); // ?????????????????????
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

            // ????????????????????????5
            post.setIsHot(top5.contains(post.getId()));
        }
        // List<User> userList = userService.getUserList(conditions);
        return new PageData<>(postList);
    }

}
