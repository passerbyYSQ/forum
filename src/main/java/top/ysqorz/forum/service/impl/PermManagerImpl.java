package top.ysqorz.forum.service.impl;

import org.springframework.stereotype.Service;
import top.ysqorz.forum.service.PermManager;
import top.ysqorz.forum.shiro.Permission;
import top.ysqorz.forum.shiro.ShiroUtils;

/**
 * @author passerbyYSQ
 * @create 2021-10-20 20:30
 */
@Service
public class PermManagerImpl implements PermManager {

    @Override
    public boolean allowDelComment(Integer creatorId) {
        return isAdminOrCreator(Permission.COMMENT_DELETE, creatorId);
    }

    @Override
    public boolean allowDelPost(Integer creatorId) {
        return isAdminOrCreator(Permission.POST_DELETE, creatorId);
    }

    @Override
    public boolean allowUpdatePost(Integer creatorId) {
        return isAdminOrCreator(Permission.POST_UPDATE, creatorId);
    }

    private boolean isAdminOrCreator(String perm, Integer creatorId) {
        return ShiroUtils.isAuthenticated() &&
                (ShiroUtils.getUserId().equals(creatorId) || ShiroUtils.hasPerm(perm));
    }
}
