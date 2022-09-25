package cn.stylefeng.roses.kernel.system.modular.user.controller;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.system.modular.user.entity.SysUserGroup;
import cn.stylefeng.roses.kernel.system.modular.user.pojo.request.SysUserGroupRequest;
import cn.stylefeng.roses.kernel.system.modular.user.service.SysUserGroupService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限分组控制器
 *
 * @author fengshuonan
 * @date 2022/09/25 22:11
 */
@RestController
@ApiResource(name = "权限分组")
public class SysUserGroupController {

    @Resource
    private SysUserGroupService sysUserGroupService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    @PostResource(name = "添加", path = "/sysUserGroup/add")
    public ResponseData<SysUserGroup> add(@RequestBody @Validated(SysUserGroupRequest.add.class) SysUserGroupRequest sysUserGroupRequest) {
        sysUserGroupService.add(sysUserGroupRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    @PostResource(name = "删除", path = "/sysUserGroup/delete")
    public ResponseData<?> delete(@RequestBody @Validated(SysUserGroupRequest.delete.class) SysUserGroupRequest sysUserGroupRequest) {
        sysUserGroupService.del(sysUserGroupRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 编辑
     *
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    @PostResource(name = "编辑", path = "/sysUserGroup/edit")
    public ResponseData<?> edit(@RequestBody @Validated(SysUserGroupRequest.edit.class) SysUserGroupRequest sysUserGroupRequest) {
        sysUserGroupService.edit(sysUserGroupRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    @GetResource(name = "查看详情", path = "/sysUserGroup/detail")
    public ResponseData<SysUserGroup> detail(@Validated(SysUserGroupRequest.detail.class) SysUserGroupRequest sysUserGroupRequest) {
        return new SuccessResponseData<>(sysUserGroupService.detail(sysUserGroupRequest));
    }

    /**
     * 获取列表
     *
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    @GetResource(name = "获取列表", path = "/sysUserGroup/list")
    public ResponseData<List<SysUserGroup>> list(SysUserGroupRequest sysUserGroupRequest) {
        return new SuccessResponseData<>(sysUserGroupService.findList(sysUserGroupRequest));
    }

    /**
     * 获取列表（带分页）
     *
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    @GetResource(name = "分页查询", path = "/sysUserGroup/page")
    public ResponseData<PageResult<SysUserGroup>> page(SysUserGroupRequest sysUserGroupRequest) {
        return new SuccessResponseData<>(sysUserGroupService.findPage(sysUserGroupRequest));
    }

}
