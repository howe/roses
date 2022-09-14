package cn.stylefeng.roses.kernel.system.modular.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.system.api.UserServiceApi;
import cn.stylefeng.roses.kernel.system.api.enums.OrgApproverTypeEnum;
import cn.stylefeng.roses.kernel.system.api.pojo.organization.BindUserItem;
import cn.stylefeng.roses.kernel.system.api.pojo.user.SysUserDTO;
import cn.stylefeng.roses.kernel.system.modular.organization.entity.HrOrgApprover;
import cn.stylefeng.roses.kernel.system.modular.organization.enums.HrOrgApproverExceptionEnum;
import cn.stylefeng.roses.kernel.system.modular.organization.mapper.HrOrgApproverMapper;
import cn.stylefeng.roses.kernel.system.modular.organization.pojo.request.HrOrgApproverRequest;
import cn.stylefeng.roses.kernel.system.modular.organization.service.HrOrgApproverService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 组织机构审批人业务实现层
 *
 * @author fengshuonan
 * @date 2022/09/13 23:15
 */
@Service
public class HrOrgApproverServiceImpl extends ServiceImpl<HrOrgApproverMapper, HrOrgApprover> implements HrOrgApproverService {

    @Resource
    private UserServiceApi userServiceApi;

    @Override
    public void bindUserList(HrOrgApproverRequest hrOrgApproverRequest) {

        // 获取组织机构id
        Long orgId = hrOrgApproverRequest.getOrgId();

        // 获取绑定的审批人类型
        Integer orgApproverType = hrOrgApproverRequest.getOrgApproverType();

        // 获取需要绑定的用户列表
        List<Long> needToBindUsers = hrOrgApproverRequest.getUserIdList();

        // 获取改组织下，该种类型已经绑定的人
        LambdaQueryWrapper<HrOrgApprover> hrOrgApproverLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrgApproverLambdaQueryWrapper.eq(HrOrgApprover::getOrgId, orgId);
        hrOrgApproverLambdaQueryWrapper.eq(HrOrgApprover::getOrgApproverType, orgApproverType);
        List<HrOrgApprover> alreadyBindUsers = this.list(hrOrgApproverLambdaQueryWrapper);

        // 如果已绑定的用户是空的，则直接绑定参数的userList
        if (ObjectUtil.isEmpty(alreadyBindUsers)) {
            ArrayList<HrOrgApprover> tempApprovers = new ArrayList<>();
            for (Long userId : needToBindUsers) {
                HrOrgApprover hrOrgApprover = new HrOrgApprover();
                hrOrgApprover.setOrgId(orgId);
                hrOrgApprover.setOrgApproverType(orgApproverType);
                hrOrgApprover.setUserId(userId);
                tempApprovers.add(hrOrgApprover);
            }
            this.saveBatch(tempApprovers);
        }

        // 如果有已经绑定的人，则需要判断请求参数中的人是否已经包含在内，包含在内则不用从新绑定
        List<Long> alreadyBindUserIdList = alreadyBindUsers.stream().map(HrOrgApprover::getUserId).collect(Collectors.toList());
        ArrayList<HrOrgApprover> tempApprovers = new ArrayList<>();
        for (Long needToBindUserId : needToBindUsers) {
            boolean needToAdd = true;
            for (Long tempUserId : alreadyBindUserIdList) {
                if (tempUserId.equals(needToBindUserId)) {
                    needToAdd = false;
                    break;
                }
            }
            if (needToAdd) {
                HrOrgApprover hrOrgApprover = new HrOrgApprover();
                hrOrgApprover.setOrgApproverId(orgId);
                hrOrgApprover.setOrgApproverType(orgApproverType);
                hrOrgApprover.setUserId(needToBindUserId);
                tempApprovers.add(hrOrgApprover);
            }
            this.saveBatch(tempApprovers);
        }
    }

    @Override
    public void del(HrOrgApproverRequest hrOrgApproverRequest) {
        LambdaQueryWrapper<HrOrgApprover> hrOrgApproverLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrgApproverLambdaQueryWrapper.eq(HrOrgApprover::getOrgId, hrOrgApproverRequest.getOrgId());
        hrOrgApproverLambdaQueryWrapper.eq(HrOrgApprover::getOrgApproverType, hrOrgApproverRequest.getOrgApproverType());
        hrOrgApproverLambdaQueryWrapper.eq(HrOrgApprover::getUserId, hrOrgApproverRequest.getUserId());
        this.remove(hrOrgApproverLambdaQueryWrapper);
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
    public List<HrOrgApprover> getBindingList(HrOrgApproverRequest hrOrgApproverRequest) {

        // 获取当前系统一共有哪些组织审批人类型
        OrgApproverTypeEnum[] values = OrgApproverTypeEnum.values();

        // 获取指定机构的绑定情况
        LambdaQueryWrapper<HrOrgApprover> hrOrgApproverLambdaQueryWrapper = new LambdaQueryWrapper<>();
        hrOrgApproverLambdaQueryWrapper.eq(HrOrgApprover::getOrgId, hrOrgApproverRequest.getOrgId());
        List<HrOrgApprover> orgTotalBindingList = this.list(hrOrgApproverLambdaQueryWrapper);

        // 将每个类型的用户分组
        Map<Integer, List<HrOrgApprover>> groupingByUsers = orgTotalBindingList.stream().collect(Collectors.groupingBy(HrOrgApprover::getOrgApproverType));

        ArrayList<HrOrgApprover> resultList = new ArrayList<>();
        for (OrgApproverTypeEnum orgApproverTypeEnum : values) {
            HrOrgApprover hrOrgApprover = new HrOrgApprover();

            // 设置类型
            hrOrgApprover.setOrgApproverType(orgApproverTypeEnum.getCode());

            // 获取改类型下有没有人
            List<HrOrgApprover> userList = groupingByUsers.get(orgApproverTypeEnum.getCode());
            List<BindUserItem> bindUserItems = this.convertUserItem(userList);

            hrOrgApprover.setBindUserItemList(bindUserItems);
            resultList.add(hrOrgApprover);
        }

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

    /**
     * 将approver信息转化为用户详情信息
     *
     * @author fengshuonan
     * @date 2022/9/14 22:58
     */
    private List<BindUserItem> convertUserItem(List<HrOrgApprover> userList) {

        // 获取所有用户id
        List<Long> userIdList = userList.stream().map(HrOrgApprover::getUserId).collect(Collectors.toList());

        if (ObjectUtil.isNotEmpty(userIdList)) {
            List<BindUserItem> users = new ArrayList<>();
            for (Long userId : userIdList) {
                BindUserItem bindUserItem = new BindUserItem();
                bindUserItem.setUserId(userId);

                // 获取用户详情信息
                SysUserDTO sysUserDTO = userServiceApi.getUserInfoByUserId(userId);
                bindUserItem.setName(sysUserDTO.getRealName());
                bindUserItem.setAvatarUrl(sysUserDTO.getAvatarUrl());

                users.add(bindUserItem);
            }
            return users;
        }

        return new ArrayList<>();
    }


}