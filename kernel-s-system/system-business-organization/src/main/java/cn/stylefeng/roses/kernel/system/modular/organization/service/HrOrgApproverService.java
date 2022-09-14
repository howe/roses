package cn.stylefeng.roses.kernel.system.modular.organization.service;

import cn.stylefeng.roses.kernel.system.modular.organization.entity.HrOrgApprover;
import cn.stylefeng.roses.kernel.system.modular.organization.pojo.request.HrOrgApproverRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 组织机构审批人 服务类
 *
 * @author fengshuonan
 * @date 2022/09/13 23:15
 */
public interface HrOrgApproverService extends IService<HrOrgApprover> {

    /**
     * 新增
     *
     * @param hrOrgApproverRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/13 23:15
     */
    void bindUserList(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 删除
     *
     * @param hrOrgApproverRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/13 23:15
     */
    void del(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 编辑
     *
     * @param hrOrgApproverRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/13 23:15
     */
    void edit(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 查询详情
     *
     * @param hrOrgApproverRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/13 23:15
     */
    HrOrgApprover detail(HrOrgApproverRequest hrOrgApproverRequest);

    /**
     * 获取组织机构审批员绑定信息
     *
     * @return List<HrOrgApprover>   返回结果
     * @author fengshuonan
     * @date 2022/09/13 23:15
     */
    List<HrOrgApprover> getBindingList(HrOrgApproverRequest hrOrgApproverRequest);

}