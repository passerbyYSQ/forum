package top.ysqorz.forum.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.ysqorz.forum.admin.service.AuthorityService;
import top.ysqorz.forum.common.ResultModel;
import top.ysqorz.forum.common.StatusCode;
import top.ysqorz.forum.po.Resource;
import top.ysqorz.forum.vo.QueryAuthorityCondition;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 权限管理的相关API
 * @author passerbyYSQ
 * @create 2021-05-08 10:55
 */
@RestController
@RequestMapping("/admin/system/authorities")
@Validated
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

    /**
     * 获取权限树形数据。树形结构不需要在后端形成，前端会生成
     */
    @GetMapping("/tree")
    public ResultModel<List<Resource>> authorityTree(QueryAuthorityCondition conditions) {
        return ResultModel.success(authorityService.getAuthorityList(conditions));
    }

    /**
     * 新增权限
     */
    @PostMapping("/add")
    public ResultModel<Resource> addAuthority(@Validated(Resource.Add.class) Resource resource) {
        // 根据权限名查找
        Resource res = authorityService.getAuthorityByName(resource.getName());
        if (!ObjectUtils.isEmpty(res)) {
            return ResultModel.failed(StatusCode.AUTHORITY_NAME_EXIST); // 权限名称存在
        }
        // 插入新记录
        res = authorityService.addAuthority(resource);
        return ResultModel.success(res);
    }

    @PostMapping("/update")
    public ResultModel updateAuthority(@Validated(Resource.Update.class) Resource resource) {
        int cnt = authorityService.updateAuthorityById(resource);
        return cnt == 1 ? ResultModel.success() :
                ResultModel.failed(StatusCode.AUTHORITY_UPDATE_FAILED);
    }

    /**
     * 删除多个权限
     * @param authorityIds  id经过前端的筛选，传过来的一定是叶子节点的数据。
     *                      当然后端校验才是最安全的，可以在后端校验安全后才删除。暂时不考虑，直接删除
     */
    @PostMapping("/del")
    public ResultModel delAuthority(@RequestParam("authorityIds[]") @NotEmpty Integer[] authorityIds) {
        int cnt = authorityService.delAuthorityById(authorityIds);
        return cnt == authorityIds.length ? ResultModel.success() :
                ResultModel.failed(StatusCode.AUTHORITY_NOT_EXIST);
    }

}