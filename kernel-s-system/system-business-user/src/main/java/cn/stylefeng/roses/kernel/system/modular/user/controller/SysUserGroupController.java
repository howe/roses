package cn.stylefeng.roses.kernel.system.modular.user.controller;

import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
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
 * 用户组控制器
 *
 * @author fengshuonan
 * @date 2022/09/26 10:12
 */
@RestController
@ApiResource(name = "用户组", resBizType = ResBizTypeEnum.SYSTEM)
public class SysUserGroupController {

    @Resource
    private SysUserGroupService sysUserGroupService;

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    @PostResource(name = "添加", path = "/sysUserGroup/add")
    public ResponseData<SysUserGroup> add(@RequestBody @Validated(SysUserGroupRequest.add.class) SysUserGroupRequest sysUserGroupRequest) {
        SysUserGroup result = sysUserGroupService.add(sysUserGroupRequest);
        return new SuccessResponseData<>(result);
    }

    /**
     * 查看详情
     *
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    @GetResource(name = "查看详情", path = "/sysUserGroup/detail")
    public ResponseData<SysUserGroup> detail(@Validated(SysUserGroupRequest.detail.class) SysUserGroupRequest sysUserGroupRequest) {
        return new SuccessResponseData<>(sysUserGroupService.detail(sysUserGroupRequest));
    }

    /**
     * 获取用户组-选择关系列表
     *
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    @GetResource(name = "获取用户组-选择关系列表", path = "/sysUserGroup/getSelectRelationList")
    public ResponseData<List<SimpleDict>> getSelectRelationList() {
        return new SuccessResponseData<>(sysUserGroupService.getSelectRelationList());
    }

}
