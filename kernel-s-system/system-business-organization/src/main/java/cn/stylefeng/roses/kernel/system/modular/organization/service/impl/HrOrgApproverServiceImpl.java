package cn.stylefeng.roses.kernel.system.modular.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.system.api.enums.OrgApproverTypeEnum;
import cn.stylefeng.roses.kernel.system.modular.organization.entity.HrOrgApprover;
import cn.stylefeng.roses.kernel.system.modular.organization.enums.HrOrgApproverExceptionEnum;
import cn.stylefeng.roses.kernel.system.modular.organization.mapper.HrOrgApproverMapper;
import cn.stylefeng.roses.kernel.system.modular.organization.pojo.request.HrOrgApproverRequest;
import cn.stylefeng.roses.kernel.system.modular.organization.service.HrOrgApproverService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 组织机构审批人业务实现层
 *
 * @author fengshuonan
 * @date 2022/09/13 23:15
 */
@Service
public class HrOrgApproverServiceImpl extends ServiceImpl<HrOrgApproverMapper, HrOrgApprover> implements HrOrgApproverService {

    @Override
    public void add(HrOrgApproverRequest hrOrgApproverRequest) {
        HrOrgApprover hrOrgApprover = new HrOrgApprover();
        BeanUtil.copyProperties(hrOrgApproverRequest, hrOrgApprover);
        this.save(hrOrgApprover);
    }

    @Override
    public void del(HrOrgApproverRequest hrOrgApproverRequest) {
        HrOrgApprover hrOrgApprover = this.queryHrOrgApprover(hrOrgApproverRequest);
        this.removeById(hrOrgApprover.getOrgApproverId());
    }

    @Override
    public void edit(HrOrgApproverRequest hrOrgApproverRequest) {
        HrOrgApprover hrOrgApprover = this.queryHrOrgApprover(hrOrgApproverRequest);
        BeanUtil.copyProperties(hrOrgApproverRequest, hrOrgApprover);
        this.updateById(hrOrgApprover);
    }

    @Override
    public HrOrgApprover detail(HrOrgApproverRequest hrOrgApproverRequest) {
        return this.queryHrOrgApprover(hrOrgApproverRequest);
    }

    @Override
    public List<HrOrgApprover> getBindingList() {

        // 获取当前系统一共有哪些组织审批人类型
        OrgApproverTypeEnum[] values = OrgApproverTypeEnum.values();

        ArrayList<HrOrgApprover> resultList = new ArrayList<>();
        for (OrgApproverTypeEnum orgApproverTypeEnum : values) {
            HrOrgApprover hrOrgApprover = new HrOrgApprover();
            hrOrgApprover.setOrgApproverType(orgApproverTypeEnum.getCode());
            resultList.add(hrOrgApprover);
        }

        // 获取当前系统所有的绑定情况 todo
        List<HrOrgApprover> list = this.list();

        return resultList;
    }

    /**
     * 获取信息
     *
     * @author fengshuonan
     * @date 2022/09/13 23:15
     */
    private HrOrgApprover queryHrOrgApprover(HrOrgApproverRequest hrOrgApproverRequest) {
        HrOrgApprover hrOrgApprover = this.getById(hrOrgApproverRequest.getOrgApproverId());
        if (ObjectUtil.isEmpty(hrOrgApprover)) {
            throw new ServiceException(HrOrgApproverExceptionEnum.HR_ORG_APPROVER_NOT_EXISTED);
        }
        return hrOrgApprover;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2022/09/13 23:15
     */
    private LambdaQueryWrapper<HrOrgApprover> createWrapper(HrOrgApproverRequest hrOrgApproverRequest) {
        LambdaQueryWrapper<HrOrgApprover> queryWrapper = new LambdaQueryWrapper<>();

        Long orgApproverId = hrOrgApproverRequest.getOrgApproverId();
        Integer orgApproverType = hrOrgApproverRequest.getOrgApproverType();
        Long orgId = hrOrgApproverRequest.getOrgId();
        Long userId = hrOrgApproverRequest.getUserId();

        queryWrapper.eq(ObjectUtil.isNotNull(orgApproverId), HrOrgApprover::getOrgApproverId, orgApproverId);
        queryWrapper.eq(ObjectUtil.isNotNull(orgApproverType), HrOrgApprover::getOrgApproverType, orgApproverType);
        queryWrapper.eq(ObjectUtil.isNotNull(orgId), HrOrgApprover::getOrgId, orgId);
        queryWrapper.eq(ObjectUtil.isNotNull(userId), HrOrgApprover::getUserId, userId);

        return queryWrapper;
    }

}