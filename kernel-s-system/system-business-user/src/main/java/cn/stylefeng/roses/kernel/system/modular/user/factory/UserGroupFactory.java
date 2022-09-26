package cn.stylefeng.roses.kernel.system.modular.user.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.system.api.enums.UserGroupSelectTypeEnum;
import cn.stylefeng.roses.kernel.system.api.pojo.usergroup.SelectItem;
import cn.stylefeng.roses.kernel.system.modular.user.entity.SysUserGroup;
import cn.stylefeng.roses.kernel.system.modular.user.entity.SysUserGroupDetail;
import cn.stylefeng.roses.kernel.system.modular.user.pojo.request.SysUserGroupRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户组创建工厂
 *
 * @author fengshuonan
 * @date 2022/9/26 14:28
 */
public class UserGroupFactory {

    /**
     * 创建用户组详情
     *
     * @param userGroupId         用户组的组id
     * @param sysUserGroupRequest 前端传递的参数
     * @return 最终转化的用户组详情实体
     * @author fengshuonan
     * @date 2022/9/26 14:29
     */
    public static List<SysUserGroupDetail> createUserGroupDetail(Long userGroupId, SysUserGroupRequest sysUserGroupRequest) {

        ArrayList<SysUserGroupDetail> sysUserGroupDetails = new ArrayList<>();

        // 解析请求参数中选择的情况
        List<SelectItem> selectUserList = sysUserGroupRequest.getSelectUserList();
        List<SelectItem> selectOrgList = sysUserGroupRequest.getSelectOrgList();
        List<SelectItem> selectRoleList = sysUserGroupRequest.getSelectRoleList();
        List<SelectItem> selectPositionList = sysUserGroupRequest.getSelectPositionList();
        List<SelectItem> selectRelationList = sysUserGroupRequest.getSelectRelationList();
        List<SelectItem> selectOrgApproverTypeList = sysUserGroupRequest.getSelectOrgApproverTypeList();

        // 生成对应的userGroupDetail实体
        List<SysUserGroupDetail> users = parseToUserGroupDetail(userGroupId, UserGroupSelectTypeEnum.USER, selectUserList);
        List<SysUserGroupDetail> depts = parseToUserGroupDetail(userGroupId, UserGroupSelectTypeEnum.DEPT, selectOrgList);
        List<SysUserGroupDetail> roles = parseToUserGroupDetail(userGroupId, UserGroupSelectTypeEnum.ROLE, selectRoleList);
        List<SysUserGroupDetail> positions = parseToUserGroupDetail(userGroupId, UserGroupSelectTypeEnum.POSITION, selectPositionList);
        List<SysUserGroupDetail> relations = parseToUserGroupDetail(userGroupId, UserGroupSelectTypeEnum.RELATION, selectRelationList);
        List<SysUserGroupDetail> approvers = parseToUserGroupDetail(userGroupId, UserGroupSelectTypeEnum.APPROVER, selectOrgApproverTypeList);

        sysUserGroupDetails.addAll(users);
        sysUserGroupDetails.addAll(depts);
        sysUserGroupDetails.addAll(roles);
        sysUserGroupDetails.addAll(positions);
        sysUserGroupDetails.addAll(relations);
        sysUserGroupDetails.addAll(approvers);

        return sysUserGroupDetails;
    }

    /**
     * @author fengshuonan
     * @date 2022/9/26 15:37
     */
    public static SysUserGroup parseToEntity(SysUserGroup sysUserGroup, List<SysUserGroupDetail> sysUserGroupDetailList) {

        // 按类型将详情分组
        Map<Integer, List<SysUserGroupDetail>> userDetailList = sysUserGroupDetailList.stream().collect(Collectors.groupingBy(SysUserGroupDetail::getSelectType));

        // 设置绑定的用户列表
        List<SysUserGroupDetail> users = userDetailList.get(UserGroupSelectTypeEnum.USER.getCode());
        sysUserGroup.setSelectUserList(parseToSelectItem(users));

        // 设置绑定的部门列表
        List<SysUserGroupDetail> depts = userDetailList.get(UserGroupSelectTypeEnum.DEPT.getCode());
        sysUserGroup.setSelectOrgList(parseToSelectItem(depts));

        // 设置绑定的角色列表
        List<SysUserGroupDetail> roles = userDetailList.get(UserGroupSelectTypeEnum.ROLE.getCode());
        sysUserGroup.setSelectRoleList(parseToSelectItem(roles));

        // 设置绑定的职位列表
        List<SysUserGroupDetail> positions = userDetailList.get(UserGroupSelectTypeEnum.POSITION.getCode());
        sysUserGroup.setSelectPositionList(parseToSelectItem(positions));

        // 设置绑定的关系类型
        List<SysUserGroupDetail> relations = userDetailList.get(UserGroupSelectTypeEnum.RELATION.getCode());
        sysUserGroup.setSelectRelationList(parseToSelectItem(relations));

        // 设置绑定的部门审批人类型
        List<SysUserGroupDetail> approvers = userDetailList.get(UserGroupSelectTypeEnum.APPROVER.getCode());
        sysUserGroup.setSelectOrgApproverTypeList(parseToSelectItem(approvers));

        return sysUserGroup;
    }

    /**
     * 生成指定类型的userGroupDetail存储类型
     *
     * @author fengshuonan
     * @date 2022/9/26 14:36
     */
    public static List<SysUserGroupDetail> parseToUserGroupDetail(Long userGroupId, UserGroupSelectTypeEnum userGroupSelectTypeEnum, List<SelectItem> paramList) {

        ArrayList<SysUserGroupDetail> results = new ArrayList<>();

        if (ObjectUtil.isEmpty(paramList)) {
            return results;
        }

        // 转化请求的参数
        for (SelectItem selectItem : paramList) {
            SysUserGroupDetail sysUserGroupDetail = new SysUserGroupDetail();

            // 设置分组id
            sysUserGroupDetail.setUserGroupId(userGroupId);

            // 设置详情类型
            sysUserGroupDetail.setSelectType(userGroupSelectTypeEnum.getCode());

            // 设置选择的值和名称
            sysUserGroupDetail.setSelectValue(selectItem.getBizId());
            sysUserGroupDetail.setSelectValueName(selectItem.getName());

            // 如果请求的参数是部门审批人类型，则需要单独存储选择的审批人类型
            if (UserGroupSelectTypeEnum.APPROVER.equals(userGroupSelectTypeEnum)) {
                sysUserGroupDetail.setSubSelectValue(selectItem.getSubValue());
                sysUserGroupDetail.setSubSelectValueName(selectItem.getSubValueName());
            }

            results.add(sysUserGroupDetail);
        }

        return results;
    }

    /**
     * 解析到可选择的item
     *
     * @author fengshuonan
     * @date 2022/9/26 15:44
     */
    public static List<SelectItem> parseToSelectItem(List<SysUserGroupDetail> userGroupDetailList) {

        ArrayList<SelectItem> selectItems = new ArrayList<>();

        if (ObjectUtil.isEmpty(userGroupDetailList)) {
            return selectItems;
        }

        for (SysUserGroupDetail sysUserGroupDetail : userGroupDetailList) {
            SelectItem selectItem = new SelectItem();

            selectItem.setBizId(sysUserGroupDetail.getSelectValue());
            selectItem.setName(sysUserGroupDetail.getSelectValueName());

            // 如果请求的参数是部门审批人类型，则需要单独存储选择的审批人类型
            if (UserGroupSelectTypeEnum.APPROVER.getCode().equals(sysUserGroupDetail.getSelectType())) {
                selectItem.setSubValue(sysUserGroupDetail.getSubSelectValue());
                selectItem.setSubValueName(sysUserGroupDetail.getSubSelectValueName());
            }

            selectItems.add(selectItem);
        }

        return selectItems;
    }

}
