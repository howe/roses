package cn.stylefeng.roses.kernel.system.modular.organization.service;

import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.auth.api.enums.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.db.api.DbOperatorApi;
import cn.stylefeng.roses.kernel.system.exception.DataScopeException;
import cn.stylefeng.roses.kernel.system.exception.enums.DataScopeExceptionEnum;
import cn.stylefeng.roses.kernel.system.pojo.organization.SysEmployeeResponse;
import cn.stylefeng.roses.kernel.system.DataScopeApi;
import cn.stylefeng.roses.kernel.system.RoleServiceApi;
import cn.stylefeng.roses.kernel.system.SysEmployeeApi;
import cn.stylefeng.roses.kernel.system.UserServiceApi;
import cn.stylefeng.roses.kernel.system.pojo.organization.DataScopeResponse;
import cn.stylefeng.roses.kernel.system.pojo.role.response.SysRoleResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 数据范围服务
 *
 * @author fengshuonan
 * @date 2020/11/6 12:01
 */
@Service
public class DataScopeService implements DataScopeApi {

    @Resource
    private UserServiceApi userServiceApi;

    @Resource
    private RoleServiceApi roleServiceApi;

    @Resource
    private SysEmployeeApi sysEmployeeApi;

    @Resource
    private DbOperatorApi dbOperatorApi;

    @Override
    public DataScopeResponse getDataScope(Long userId) {

        // 初始化返回结果
        DataScopeResponse dataScopeResponse = new DataScopeResponse();
        Set<Long> userIds = new HashSet<>();
        Set<Long> organizationIds = new HashSet<>();

        if (userId == null) {
            String userTip = StrUtil.format(DataScopeExceptionEnum.USER_ID_EMPTY_ERROR.getUserTip(), userId);
            throw new DataScopeException(DataScopeExceptionEnum.USER_ID_EMPTY_ERROR, userTip);
        }

        // 获取用户的所有角色
        List<Long> userRoleIdList = userServiceApi.getUserRoleIdList(userId);
        if (userRoleIdList.isEmpty()) {
            String userTip = StrUtil.format(DataScopeExceptionEnum.ROLE_EMPTY_ERROR.getUserTip(), userId);
            throw new DataScopeException(DataScopeExceptionEnum.ROLE_EMPTY_ERROR, userTip);
        }

        // 获取角色信息详情
        List<SysRoleResponse> sysRoles = roleServiceApi.getRolesByIds(userRoleIdList);
        dataScopeResponse.setSysRoleResponses(sysRoles);

        // 获取角色中的数据范围类型
        Set<DataScopeTypeEnum> dataScopeTypeEnums = sysRoles.stream().map(SysRoleResponse::getDataScopeTypeEnum).collect(Collectors.toSet());
        dataScopeResponse.setDataScopeTypeEnums(dataScopeTypeEnums);

        // 包含全部数据类型的，直接不用设置组织机构，直接放开
        if (dataScopeTypeEnums.contains(DataScopeTypeEnum.ALL)) {
            return dataScopeResponse;
        }

        // 包含指定部门数据，将指定数据范围增加到Set中
        if (dataScopeTypeEnums.contains(DataScopeTypeEnum.DEFINE)) {

            // 获取角色对应的组织机构范围
            List<Long> roleIds = sysRoles.stream().map(SysRoleResponse::getId).collect(Collectors.toList());
            List<Long> orgIds = roleServiceApi.getRoleDataScopes(roleIds);
            organizationIds.addAll(orgIds);
        }

        // 获取用户的主要部门信息
        SysEmployeeResponse userMainEmployee = sysEmployeeApi.getUserMainEmployee(userId);

        // 本部门和本部门以下，查出用户的主要部门，并且查询该部门本部门及以下的组织机构id列表
        if (dataScopeTypeEnums.contains(DataScopeTypeEnum.DEPT_WITH_CHILD)) {

            // 获取部门及以下部门的id列表
            Long organizationId = userMainEmployee.getOrganizationId();
            Set<Long> subOrgIds = dbOperatorApi.findSubListByParentId("sys_organization", "pids", "id", organizationId);
            organizationIds.add(organizationId);
            organizationIds.addAll(subOrgIds);
        }

        // 如果是本部门，则查出本部门并添加到组织机构列表
        if (dataScopeTypeEnums.contains(DataScopeTypeEnum.DEPT)) {

            // 获取本部门的id
            Long organizationId = userMainEmployee.getOrganizationId();
            organizationIds.add(organizationId);
        }

        // 如果只是本用户数据，将用户本身装进userId数据范围
        if (dataScopeTypeEnums.contains(DataScopeTypeEnum.SELF)) {
            userIds.add(userId);
        }

        // 获取用户单独绑定的组织机构id
        List<Long> userBindDataScope = userServiceApi.getUserBindDataScope(userId);
        organizationIds.addAll(userBindDataScope);

        dataScopeResponse.setUserIds(userIds);
        dataScopeResponse.setOrganizationIds(organizationIds);

        return dataScopeResponse;
    }

}