package cn.stylefeng.roses.kernel.system.modular.organization.controller;

import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.system.modular.organization.entity.HrOrgApprover;
import cn.stylefeng.roses.kernel.system.modular.organization.pojo.request.HrOrgApproverRequest;
import cn.stylefeng.roses.kernel.system.modular.organization.service.HrOrgApproverService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 组织机构审批人控制器
 *
 * @author fengshuonan
 * @date 2022/09/13 23:15
 */
@RestController
@ApiResource(name = "组织机构审批人")
public class HrOrgApproverController {

    @Resource
    private HrOrgApproverService hrOrgApproverService;

    /**
     * 获取组织机构审批人绑定列表
     *
     * @author fengshuonan
     * @date 2022/09/13 23:15
     */
    @GetResource(name = "获取组织机构审批人绑定列表", path = "/hrOrgApprover/getBindingList")
    public ResponseData<List<HrOrgApprover>> getBindingList(@Validated(HrOrgApproverRequest.list.class) HrOrgApproverRequest hrOrgApproverRequest) {
        return new SuccessResponseData<>(hrOrgApproverService.getBindingList(hrOrgApproverRequest));
    }

    /**
     * 添加
     *
     * @author fengshuonan
     * @date 2022/09/13 23:15
     */
    @PostResource(name = "添加", path = "/hrOrgApprover/add")
    public ResponseData<HrOrgApprover> add(@RequestBody @Validated(HrOrgApproverRequest.add.class) HrOrgApproverRequest hrOrgApproverRequest) {
        hrOrgApproverService.add(hrOrgApproverRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除
     *
     * @author fengshuonan
     * @date 2022/09/13 23:15
     */
    @PostResource(name = "删除", path = "/hrOrgApprover/delete")
    public ResponseData<?> delete(@RequestBody @Validated(HrOrgApproverRequest.delete.class) HrOrgApproverRequest hrOrgApproverRequest) {
        hrOrgApproverService.del(hrOrgApproverRequest);
        return new SuccessResponseData<>();
    }

}
