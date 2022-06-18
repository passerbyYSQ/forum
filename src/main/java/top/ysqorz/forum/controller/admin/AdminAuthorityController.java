package top.ysqorz.forum.controller.admin;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.common.TreeBuilder;
import top.ysqorz.forum.dto.req.QueryAuthorityCondition;
import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.service.AuthorityService;

import javax.validation.constraints.NotEmpty;
import java.io.IOException;
import java.util.List;

/**
 * 权限管理的相关API
 * @author passerbyYSQ
 * @create 2021-05-08 10:55
 */
@Controller
@ResponseBody
@RequestMapping("/admin/system/authorities")
@Validated
public class AdminAuthorityController {
    @Autowired
    private AuthorityService authorityService;

    /**
     * 获取权限树形数据。树形结构不需要在后端形成，前端会生成
     */
    @RequiresPermissions("perm:view")
    @GetMapping("/tree")
    public List<Resource> authorityTree(QueryAuthorityCondition conditions) {
        return authorityService.getAuthorityList(conditions);
    }

    /**
     * 新增权限
     */
    @RequiresPermissions("perm:add")
    @PostMapping("/add")
    public Resource addAuthority(@Validated(Resource.Add.class) Resource resource) {
        return authorityService.addAuthority(resource);
    }

    @RequiresPermissions("perm:update")
    @PostMapping("/update")
    public StatusCode updateAuthority(@Validated(Resource.Update.class) Resource resource) throws IOException {
        if (ObjectUtils.isEmpty(authorityService.getAuthorityById(resource.getId()))) {
            return StatusCode.AUTHORITY_NOT_EXIST;
        }

        // parentId为空，说明为根权限
        if (ObjectUtils.isEmpty(resource.getParentId())) {
            resource.setParentId(0);
        }

        // 查询权限列表
        List<Resource> resourceList = authorityService.getAuthorityList(null);
        // 构建一棵树
        try (TreeBuilder<Integer> builder = new TreeBuilder<>(resourceList, 0)) {
            // 权限id是否合法（是否存在）
            if (!builder.isValidId(resource.getId()) || !builder.isValidId(resource.getParentId())) {
                return StatusCode.AUTHORITY_NOT_EXIST;
            }

            // 判断新的parentId是否是当前id的子孙，如果是，parentId非法
            boolean flag = builder.isDescendant(resource.getId(), resource.getParentId());
            if (flag) {
                return StatusCode.AUTHORITY_PID_NOT_VALID;
            }
        }
        int cnt = authorityService.updateAuthorityById(resource);
        return cnt == 1 ? StatusCode.SUCCESS : StatusCode.AUTHORITY_UPDATE_FAILED;
    }

    /**
     * 删除多个权限
     * @param authorityIds  id经过前端的筛选，传过来的一定是叶子节点的数据。
     *                      当然后端校验才是最安全的，可以在后端校验安全后才删除。暂时不考虑，直接删除
     */
    @RequiresPermissions("perm:delete")
    @PostMapping("/del")
    public StatusCode delAuthority(@RequestParam("authorityIds[]") @NotEmpty Integer[] authorityIds) {
        int cnt = authorityService.delAuthorityById(authorityIds);
        return cnt == 1 ? StatusCode.SUCCESS : StatusCode.AUTHORITY_DEL_FAILED;
    }
}
