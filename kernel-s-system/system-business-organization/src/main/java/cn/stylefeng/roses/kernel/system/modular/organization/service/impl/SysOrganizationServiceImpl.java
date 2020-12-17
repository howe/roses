package cn.stylefeng.roses.kernel.system.modular.organization.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.system.constants.SystemConstants;
import cn.stylefeng.roses.kernel.system.exception.SystemModularException;
import cn.stylefeng.roses.kernel.system.exception.enums.DataScopeExceptionEnum;
import cn.stylefeng.roses.kernel.system.exception.enums.OrganizationExceptionEnum;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.enums.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.db.api.context.DbOperatorContext;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.rule.factory.DefaultTreeBuildFactory;
import cn.stylefeng.roses.kernel.rule.pojo.tree.DefaultTreeNode;
import cn.stylefeng.roses.kernel.system.RoleServiceApi;
import cn.stylefeng.roses.kernel.system.UserServiceApi;
import cn.stylefeng.roses.kernel.system.modular.organization.entity.SysEmployee;
import cn.stylefeng.roses.kernel.system.modular.organization.entity.SysOrganization;
import cn.stylefeng.roses.kernel.system.modular.organization.mapper.SysOrganizationMapper;
import cn.stylefeng.roses.kernel.system.modular.organization.service.SysEmployeeService;
import cn.stylefeng.roses.kernel.system.modular.organization.service.SysOrganizationService;
import cn.stylefeng.roses.kernel.system.pojo.organization.SysEmployeeRequest;
import cn.stylefeng.roses.kernel.system.pojo.organization.SysOrganizationRequest;
import cn.stylefeng.roses.kernel.system.util.DataScopeUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 组织架构管理
 *
 * @author fengshuonan
 * @date 2020/11/04 11:05
 */
@Service
public class SysOrganizationServiceImpl extends ServiceImpl<SysOrganizationMapper, SysOrganization> implements SysOrganizationService {

    @Resource
    private SysEmployeeService sysEmployeeService;

    @Resource
    private RoleServiceApi roleServiceApi;

    @Resource
    private UserServiceApi userServiceApi;

    @Override
    public void add(SysOrganizationRequest sysOrganizationRequest) {

        // 获取父id
        Long pid = sysOrganizationRequest.getPid();

        // 校验数据范围
        if (DataScopeUtil.validateDataScopeByOrganizationId(pid)) {
            String userTip = StrUtil.format(DataScopeExceptionEnum.DATA_SCOPE_ERROR.getUserTip(), DataScopeUtil.getDataScopeTip());
            throw new SystemModularException(DataScopeExceptionEnum.DATA_SCOPE_ERROR, userTip);
        }

        SysOrganization sysOrganization = new SysOrganization();
        BeanUtil.copyProperties(sysOrganizationRequest, sysOrganization);

        // 填充parentIds
        this.fillParentIds(sysOrganization);

        // 设置状态为启用，未删除状态
        sysOrganization.setStatusFlag(StatusEnum.ENABLE.getCode());

        this.save(sysOrganization);
    }

    @Override
    public void edit(SysOrganizationRequest sysOrganizationRequest) {

        SysOrganization sysOrganization = this.querySysOrganization(sysOrganizationRequest);
        Long id = sysOrganization.getId();

        // 校验数据范围
        if (DataScopeUtil.validateDataScopeByOrganizationId(id)) {
            String userTip = StrUtil.format(DataScopeExceptionEnum.DATA_SCOPE_ERROR.getUserTip(), DataScopeUtil.getDataScopeTip());
            throw new SystemModularException(DataScopeExceptionEnum.DATA_SCOPE_ERROR, userTip);
        }

        BeanUtil.copyProperties(sysOrganizationRequest, sysOrganization);

        // 填充parentIds
        this.fillParentIds(sysOrganization);

        // 不能修改状态，用修改状态接口修改状态
        sysOrganization.setStatusFlag(null);

        // 更新这条记录
        this.updateById(sysOrganization);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(SysOrganizationRequest sysOrganizationRequest) {

        SysOrganization sysOrganization = this.querySysOrganization(sysOrganizationRequest);
        Long organizationId = sysOrganization.getId();

        // 校验数据范围
        if (DataScopeUtil.validateDataScopeByOrganizationId(organizationId)) {
            String userTip = StrUtil.format(DataScopeExceptionEnum.DATA_SCOPE_ERROR.getUserTip(), DataScopeUtil.getDataScopeTip());
            throw new SystemModularException(DataScopeExceptionEnum.DATA_SCOPE_ERROR, userTip);
        }

        // 该机构下有员工，则不能删
        SysEmployeeRequest sysEmployeeRequest = new SysEmployeeRequest();
        sysEmployeeRequest.setOrganizationId(organizationId);
        List<SysEmployee> sysEmployees = sysEmployeeService.list(sysEmployeeRequest);
        if (sysEmployees != null && !sysEmployees.isEmpty()) {
            throw new SystemModularException(OrganizationExceptionEnum.DELETE_ORGANIZATION_ERROR);
        }

        // 级联删除子节点，逻辑删除
        Set<Long> childIdList = DbOperatorContext.me().findSubListByParentId("sys_organization", "pids", "id", organizationId);
        childIdList.add(organizationId);
        LambdaUpdateWrapper<SysOrganization> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(SysOrganization::getId, childIdList)
                .set(SysOrganization::getDelFlag, YesOrNotEnum.Y.getCode());
        this.update(updateWrapper);

        // 删除角色对应的组织架构数据范围
        roleServiceApi.deleteRoleDataScopeListByOrgIdList(childIdList);

        // 删除用户对应的组织架构数据范围
        userServiceApi.deleteUserDataScopeListByOrgIdList(childIdList);
    }

    @Override
    public void updateStatus(SysOrganizationRequest sysOrganizationRequest) {

        LambdaUpdateWrapper<SysOrganization> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysOrganization::getId, sysOrganizationRequest.getId());
        updateWrapper.set(SysOrganization::getStatusFlag, sysOrganizationRequest.getStatusFlag());

        this.update(updateWrapper);
    }

    @Override
    public SysOrganization detail(SysOrganizationRequest sysOrganizationRequest) {
        return this.querySysOrganization(sysOrganizationRequest);
    }

    @Override
    public PageResult<SysOrganization> page(SysOrganizationRequest sysOrganizationRequest) {

        // 构造条件
        LambdaQueryWrapper<SysOrganization> wrapper = createWrapper(sysOrganizationRequest);

        // 获取分页参数
        Page<SysOrganization> page = PageFactory.defaultPage();

        // 返回分页结果
        return PageResultFactory.createPageResult(this.page(page, wrapper));
    }

    @Override
    public List<SysOrganization> list(SysOrganizationRequest sysOrganizationRequest) {

        // 构造条件
        LambdaQueryWrapper<SysOrganization> wrapper = createWrapper(sysOrganizationRequest);

        return this.list(wrapper);
    }

    @Override
    public List<DefaultTreeNode> tree(SysOrganizationRequest sysOrganizationRequest) {

        // 定义返回结果
        List<DefaultTreeNode> treeNodeList = CollectionUtil.newArrayList();

        LambdaQueryWrapper<SysOrganization> queryWrapper = new LambdaQueryWrapper<>();

        // 如果是超级管理员 或 数据范围是所有，则不过滤数据范围
        boolean needToDataScope = true;
        if (LoginContext.me().getSuperAdminFlag()) {
            Set<DataScopeTypeEnum> dataScopeTypes = LoginContext.me().getLoginUser().getDataScopeTypes();
            if (dataScopeTypes != null && dataScopeTypes.contains(DataScopeTypeEnum.ALL)) {
                needToDataScope = false;
            }
        }

        // 如果需要数据范围过滤，则获取用户的数据范围，拼接查询条件
        if (needToDataScope) {
            Set<Long> dataScope = LoginContext.me().getLoginUser().getOrganizationIdDataScope();

            // 数据范围没有，直接返回空
            if (ObjectUtil.isEmpty(dataScope)) {
                return treeNodeList;
            }

            // 根据组织机构数据范围的上级组织，用于展示完整的树形结构
            Set<Long> allLevelParentIdsByOrganizations = this.findAllLevelParentIdsByOrganizations(dataScope);

            // 拼接查询条件
            queryWrapper.in(SysOrganization::getId, allLevelParentIdsByOrganizations);
        }

        // 只查询未删除的
        queryWrapper.eq(SysOrganization::getDelFlag, YesOrNotEnum.N.getCode());

        // 根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(SysOrganization::getSort);

        // 组装节点
        List<SysOrganization> list = this.list(queryWrapper);
        for (SysOrganization sysOrganization : list) {
            DefaultTreeNode orgTreeNode = new DefaultTreeNode();
            orgTreeNode.setId(String.valueOf(sysOrganization.getId()));
            orgTreeNode.setPId(String.valueOf(sysOrganization.getPid()));
            orgTreeNode.setName(sysOrganization.getName());
            orgTreeNode.setSort(sysOrganization.getSort());
            treeNodeList.add(orgTreeNode);
        }

        // 构建树并返回
        return new DefaultTreeBuildFactory<DefaultTreeNode>().doTreeBuild(treeNodeList);
    }

    @Override
    public Set<Long> findAllLevelParentIdsByOrganizations(Set<Long> organizationIds) {

        // 定义返回结果
        Set<Long> allLevelParentIds = new HashSet<>(organizationIds);

        // 查询出这些节点的pids字段
        LambdaQueryWrapper<SysOrganization> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysOrganization::getId, organizationIds);
        queryWrapper.select(SysOrganization::getPids);

        List<SysOrganization> organizationList = this.list(queryWrapper);
        if (organizationList == null || organizationList.isEmpty()) {
            return allLevelParentIds;
        }

        // 把所有的pids分割，并放入到set中
        for (SysOrganization sysOrganization : organizationList) {

            // 获取pids值
            String pids = sysOrganization.getPids();

            // 去掉所有的左中括号
            String cutLeft = StrUtil.removeAll(pids, SystemConstants.PID_LEFT_DIVIDE_SYMBOL);

            // 去掉所有的右中括号
            String cutRight = StrUtil.removeAll(cutLeft, SystemConstants.PID_RIGHT_DIVIDE_SYMBOL);

            // 按逗号分割这个字符串，得到pid的数组
            String[] finalPidArray = cutRight.split(StrUtil.COMMA);

            // 遍历这些值，放入到最终的set
            for (String pid : finalPidArray) {
                allLevelParentIds.add(Convert.toLong(pid));
            }
        }

        return allLevelParentIds;
    }

    /**
     * 创建组织架构的通用条件查询wrapper
     *
     * @author fengshuonan
     * @date 2020/11/6 10:16
     */
    private LambdaQueryWrapper<SysOrganization> createWrapper(SysOrganizationRequest sysOrganizationRequest) {
        LambdaQueryWrapper<SysOrganization> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(sysOrganizationRequest)) {

            // 拼接机构名称查询条件
            if (ObjectUtil.isNotEmpty(sysOrganizationRequest.getName())) {
                queryWrapper.like(SysOrganization::getName, sysOrganizationRequest.getName());
            }

            // 拼接机构id查询条件
            if (ObjectUtil.isNotEmpty(sysOrganizationRequest.getId())) {
                queryWrapper.eq(SysOrganization::getId, sysOrganizationRequest.getId());
            }

            // 拼接父机构id查询条件
            if (ObjectUtil.isNotEmpty(sysOrganizationRequest.getPid())) {
                queryWrapper
                        .eq(SysOrganization::getId, sysOrganizationRequest.getPid())
                        .or()
                        .like(SysOrganization::getPids, sysOrganizationRequest.getPid());
            }
        }

        // 查询未删除状态的
        queryWrapper.eq(SysOrganization::getDelFlag, YesOrNotEnum.N.getCode());

        // 根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(SysOrganization::getSort);

        return queryWrapper;
    }


    /**
     * 获取系统组织机构
     *
     * @author fengshuonan
     * @date 2020/11/04 11:05
     */
    private SysOrganization querySysOrganization(SysOrganizationRequest sysOrganizationRequest) {
        SysOrganization sysorganization = this.getById(sysOrganizationRequest.getId());
        if (ObjectUtil.isEmpty(sysorganization)) {
            throw new SystemModularException(OrganizationExceptionEnum.CANT_FIND_ORG);
        }
        return sysorganization;
    }

    /**
     * 填充该节点的pIds
     *
     * @author fengshuonan
     * @date 2020/11/5 13:45
     */
    private void fillParentIds(SysOrganization sysOrganization) {

        // 如果是一级节点（一级节点的pid是0）
        if (sysOrganization.getPid().equals(SystemConstants.DEFAULT_PARENT_ID)) {
            // 设置一级节点的pid为[0],
            sysOrganization.setPids(SystemConstants.PID_LEFT_DIVIDE_SYMBOL + SystemConstants.DEFAULT_PARENT_ID + SystemConstants.PID_RIGHT_DIVIDE_SYMBOL + ",");
        } else {
            // 获取父组织机构
            SysOrganization parentSysOrganization = this.getById(sysOrganization.getPid());

            // 设置本节点的父ids为 (上一个节点的pids + (上级节点的id) )
            sysOrganization.setPids(
                    parentSysOrganization.getPids() + SystemConstants.PID_LEFT_DIVIDE_SYMBOL + parentSysOrganization.getId() + SystemConstants.PID_RIGHT_DIVIDE_SYMBOL + ",");
        }
    }

}