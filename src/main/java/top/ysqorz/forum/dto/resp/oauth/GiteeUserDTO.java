package top.ysqorz.forum.dto.resp.oauth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 阿灿
 * @create 2021-06-11 14:03
 * gitee返回的用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GiteeUserDTO {
//        "id": 8346403
//        "login": "acanccc"
//        "name": "赖旋"
//        "avatar_url": "https://gitee.com/assets/no_portrait.png"
//        "url": "https://gitee.com/api/v5/users/acanccc"
//        "html_url": "https://gitee.com/acanccc"
//        "followers_url": "https://gitee.com/api/v5/users/acanccc/followers"
//        "following_url": "https://gitee.com/api/v5/users/acanccc/following_url{/other_user}"
//        "gists_url": "https://gitee.com/api/v5/users/acanccc/gists{/gist_id}"
//        "starred_url": "https://gitee.com/api/v5/users/acanccc/starred{/owner}{/repo}"
//        "subscriptions_url": "https://gitee.com/api/v5/users/acanccc/subscriptions"
//        "organizations_url": "https://gitee.com/api/v5/users/acanccc/orgs"
//        "repos_url": "https://gitee.com/api/v5/users/acanccc/repos"
//        "events_url": "https://gitee.com/api/v5/users/acanccc/events{/privacy}"
//        "received_events_url": "https://gitee.com/api/v5/users/acanccc/received_events"
//        "type": "User"
//        "blog": null
//        "weibo": null
//        "bio": null
//        "public_repos": 0
//        "public_gists": 0
//        "followers": 0
//        "following": 0
//        "stared": 0
//        "watched": 3
//        "created_at": "2020-11-21T19:42:12+08:00"
//        "updated_at": "2021-06-11T13:29:14+08:00"
//        "email": null

    private String id;
    private String login;
    private String name;
    private String email;
    private String avatarUrl;
}
