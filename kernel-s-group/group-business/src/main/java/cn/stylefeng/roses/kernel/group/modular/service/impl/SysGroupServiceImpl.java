package cn.stylefeng.roses.kernel.group.modular.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.group.api.constants.GroupConstants;
import cn.stylefeng.roses.kernel.group.api.pojo.SysGroupDTO;
import cn.stylefeng.roses.kernel.group.api.pojo.SysGroupRequest;
import cn.stylefeng.roses.kernel.group.modular.entity.SysGroup;
import cn.stylefeng.roses.kernel.group.modular.mapper.SysGroupMapper;
import cn.stylefeng.roses.kernel.group.modular.service.SysGroupService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 业务分组业务实现层
 *
 * @author fengshuonan
 * @date 2022/05/11 12:54
 */
@Service
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements SysGroupService {

    @Override
    public List<SysGroupDTO> findGroupList(SysGroupRequest sysGroupRequest, boolean getTotal) {
        String groupBizCode = sysGroupRequest.getGroupBizCode();
        Long userId = LoginContext.me().getLoginUser().getUserId();
        List<SysGroupDTO> userGroupList = this.baseMapper.getUserGroupList(groupBizCode, userId, getTotal);

        // 增加两个固定的选中和取消选项
        addAllGroup(groupBizCode, userGroupList);

        return userGroupList;
    }

    @Override
    public List<SysGroupDTO> addSelect(SysGroupRequest sysGroupRequest) {
        String groupBizCode = sysGroupRequest.getGroupBizCode();
        Long userId = LoginContext.me().getLoginUser().getUserId();
        List<SysGroupDTO> userGroupList = this.baseMapper.getUserGroupList(groupBizCode, userId, false);

        // 增加两个固定的选中和取消选项
        addCommonGroup(groupBizCode, userGroupList);

        return userGroupList;
    }

    @Override
    public void add(SysGroupRequest sysGroupRequest) {
        ArrayList<SysGroup> sysGroups = new ArrayList<>();
        Long userId = LoginContext.me().getLoginUser().getUserId();
        List<Long> businessIdList = sysGroupRequest.getBusinessIdList();

        // 移除当前用户在这个bizCode下已经绑定的分组
        LambdaUpdateWrapper<SysGroup> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysGroup::getGroupBizCode, sysGroupRequest.getGroupBizCode());
        updateWrapper.in(SysGroup::getBusinessId, businessIdList);
        updateWrapper.eq(SysGroup::getUserId, userId);
        this.remove(updateWrapper);

        // 如果分组名称是未分组，则删除分组
        if (!GroupConstants.GROUP_DELETE_NAME.equals(sysGroupRequest.getGroupName())) {
            for (Long bizId : businessIdList) {
                SysGroup sysGroup = new SysGroup();
                sysGroup.setGroupName(sysGroupRequest.getGroupName());
                sysGroup.setGroupBizCode(sysGroupRequest.getGroupBizCode());
                sysGroup.setBusinessId(bizId);
                sysGroup.setUserId(userId);
                sysGroups.add(sysGroup);
            }

            this.saveBatch(sysGroups);
        }
    }

    @Override
    public void del(SysGroupRequest sysGroupRequest) {

        Long userId = LoginContext.me().getLoginUser().getUserId();

        LambdaUpdateWrapper<SysGroup> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysGroup::getUserId, userId);
        wrapper.eq(SysGroup::getGroupBizCode, sysGroupRequest.getGroupBizCode());
        wrapper.in(SysGroup::getBusinessId, sysGroupRequest.getBusinessIdList());

        this.remove(wrapper);
    }

    @Override
    public List<Long> findUserGroupDataList(SysGroupRequest sysGroupRequest) {

        Long userId = LoginContext.me().getLoginUser().getUserId();

        LambdaQueryWrapper<SysGroup> sysGroupLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysGroupLambdaQueryWrapper.eq(SysGroup::getUserId, userId);
        sysGroupLambdaQueryWrapper.eq(SysGroup::getGroupBizCode, sysGroupRequest.getGroupBizCode());
        sysGroupLambdaQueryWrapper.eq(StrUtil.isNotBlank(sysGroupRequest.getGroupName()), SysGroup::getGroupName, sysGroupRequest.getGroupName());

        List<SysGroup> list = this.list(sysGroupLambdaQueryWrapper);
        if (list == null || list.size() == 0) {
            return new ArrayList<>();
        } else {
            return list.stream().map(SysGroup::getBusinessId).collect(Collectors.toList());
        }
    }

    @Override
    public void removeGroup(String bizCode, Long bizId) {
        Long userId = LoginContext.me().getLoginUser().getUserId();

        LambdaUpdateWrapper<SysGroup> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysGroup::getUserId, userId);
        wrapper.eq(SysGroup::getGroupBizCode, bizCode);
        wrapper.eq(SysGroup::getBusinessId, bizId);

        this.remove(wrapper);
    }

    /**
     * 返回结果增加通用的两个分组名称
     *
     * @author fengshuonan
     * @date 2022/6/28 10:50
     */
    private void addCommonGroup(String groupBizCode, List<SysGroupDTO> result) {

        // 添加分组
        SysGroupDTO addGroup = new SysGroupDTO();
        addGroup.setGroupBizCode(groupBizCode);
        addGroup.setGroupName(GroupConstants.GROUP_ADD_NAME);

        // 未分组
        SysGroupDTO noneGroup = new SysGroupDTO();
        noneGroup.setGroupBizCode(groupBizCode);
        noneGroup.setGroupName(GroupConstants.GROUP_DELETE_NAME);

        result.add(0, noneGroup);
        result.add(0, addGroup);
    }

    /**
     * 返回所有分组和未分组的查询
     *
     * @author fengshuonan
     * @date 2022/6/28 10:50
     */
    private void addAllGroup(String groupBizCode, List<SysGroupDTO> result) {

        // 添加分组
        SysGroupDTO addGroup = new SysGroupDTO();
        addGroup.setGroupBizCode(groupBizCode);
        addGroup.setGroupName(GroupConstants.ALL_GROUP_NAME);

        // 未分组
        SysGroupDTO noneGroup = new SysGroupDTO();
        noneGroup.setGroupBizCode(groupBizCode);
        noneGroup.setGroupName(GroupConstants.GROUP_DELETE_NAME);

        result.add(0, noneGroup);
        result.add(0, addGroup);
    }


}
