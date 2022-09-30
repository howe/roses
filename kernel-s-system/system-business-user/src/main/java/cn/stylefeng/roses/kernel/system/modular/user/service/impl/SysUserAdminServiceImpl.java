package cn.stylefeng.roses.kernel.system.modular.user.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.system.api.RoleServiceApi;
import cn.stylefeng.roses.kernel.system.api.pojo.role.dto.SysRoleDTO;
import cn.stylefeng.roses.kernel.system.api.pojo.role.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.system.api.pojo.user.SysUserAdminDTO;
import cn.stylefeng.roses.kernel.system.api.pojo.user.request.SysAdminRequest;
import cn.stylefeng.roses.kernel.system.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.system.modular.user.entity.SysUserRole;
import cn.stylefeng.roses.kernel.system.modular.user.mapper.SysUserRoleMapper;
import cn.stylefeng.roses.kernel.system.modular.user.service.SysUserAdminService;
import cn.stylefeng.roses.kernel.system.modular.user.service.SysUserRoleService;
import cn.stylefeng.roses.kernel.system.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 管理员相关的业务
 *
 * @author fengshuonan
 * @date 2022/9/30 11:07
 */
@Service
public class SysUserAdminServiceImpl implements SysUserAdminService {

    @Resource
    private RoleServiceApi roleServiceApi;

    @Resource
    private SysUserRoleService sysUserRoleService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<SysUserAdminDTO> getAdminUserList() {

        // 获取所有管理员角色
        SysRoleRequest sysRoleRequest = new SysRoleRequest();
        sysRoleRequest.setAdminFlag(YesOrNotEnum.Y.getCode());
        List<SysRoleDTO> roleSelectList = roleServiceApi.getRoleSelectList(sysRoleRequest);
        List<Long> adminRoleIds = roleSelectList.stream().map(SysRoleDTO::getRoleId).collect(Collectors.toList());

        // 如果没有管理员，则返回空
        if (ObjectUtil.isEmpty(adminRoleIds)) {
            return new ArrayList<>();
        }

        // 获取管理员角色，对应的用户集合
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysUserRole::getRoleId, adminRoleIds);
        List<SysUserRole> sysUserRoleList = sysUserRoleService.list(queryWrapper);
        List<Long> userIdList = sysUserRoleList.stream().map(SysUserRole::getUserId).collect(Collectors.toList());

        // 没有相关的人员绑定管理员角色，则返回空
        if (ObjectUtil.isEmpty(userIdList)) {
            return new ArrayList<>();
        }

        // 查询对应的人员信息
        LambdaQueryWrapper<SysUser> sysUserLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysUserLambdaQueryWrapper.select(SysUser::getUserId, SysUser::getRealName);
        sysUserLambdaQueryWrapper.in(SysUser::getUserId, userIdList);
        sysUserLambdaQueryWrapper.ne(SysUser::getDelFlag, YesOrNotEnum.Y.getCode());
        List<SysUser> userList = sysUserService.list(sysUserLambdaQueryWrapper);
        if (ObjectUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }

        // 转化实体
        ArrayList<SysUserAdminDTO> sysUserAdminDTOS = new ArrayList<>();
        Map<Long, List<SysUserRole>> userIdRoles = sysUserRoleList.stream().collect(Collectors.groupingBy(SysUserRole::getUserId));
        for (SysUser sysUser : userList) {
            SysUserAdminDTO sysUserAdminDTO = new SysUserAdminDTO();
            sysUserAdminDTO.setUserId(sysUser.getUserId());

            // 设置角色id
            List<SysUserRole> userRoles = userIdRoles.get(sysUser.getUserId());
            if (userRoles != null && userRoles.size() > 0) {
                sysUserAdminDTO.setRoleId(userRoles.get(0).getRoleId());
            }

            // 设置用户名称
            sysUserAdminDTO.setRealName(sysUser.getRealName());
            sysUserAdminDTOS.add(sysUserAdminDTO);
        }

        return sysUserAdminDTOS;
    }

    @Override
    public void addAdminUser(SysAdminRequest sysAdminRequest) {

        // 获取用户有没有已经绑定管理员角色
        List<SysUserRole> sysUserRoleList = sysUserRoleMapper.getAdminUserRoleList(sysAdminRequest.getUserIdList());

        // 如果请求参数中已经有绑定的用户id集合
        Set<Long> haveAlreadyBindUserIds = sysUserRoleList.stream().map(SysUserRole::getUserId).collect(Collectors.toSet());

        // 给用户绑定管理员
        for (Long userId : sysAdminRequest.getUserIdList()) {
            if (!haveAlreadyBindUserIds.contains(userId)) {
                this.addSingleAdminUser(userId);
            }
        }
    }

    @Override
    public void deleteAdminUser(SysAdminRequest sysAdminRequest) {

        // 获取用户绑定的超级管理员角色
        List<SysUserRole> userRoleList = this.sysUserRoleMapper.getAdminUserRoleList(ListUtil.toList(sysAdminRequest.getUserId()));

        // 删除角色
        for (SysUserRole sysUserRole : userRoleList) {
            SysRoleRequest sysRoleRequest = new SysRoleRequest();
            sysRoleRequest.setRoleId(sysUserRole.getRoleId());
            this.roleServiceApi.del(sysRoleRequest);
        }

    }

    /**
     * 单个添加管理员用户
     *
     * @author fengshuonan
     * @date 2022/9/30 13:29
     */
    private void addSingleAdminUser(Long userId) {

        // 判断有没有此人
        SysUser user = this.sysUserService.getById(userId);
        if (user == null) {
            return;
        }

        // 创建用户对应的角色
        SysRoleRequest sysRoleRequest = new SysRoleRequest();
        sysRoleRequest.setRoleId(IdWorker.getId());
        sysRoleRequest.setRoleName("管理员权限-" + userId);
        sysRoleRequest.setRoleCode("admin-" + userId);
        this.roleServiceApi.addAdminRole(sysRoleRequest);

        // 创建用户和角色关联
        SysUserRole sysUserRole = new SysUserRole();
        sysUserRole.setUserId(userId);
        sysUserRole.setRoleId(sysRoleRequest.getRoleId());
        this.sysUserRoleService.save(sysUserRole);

        // 赋予默认的操作后台所有操作权限
        SysRoleRequest temp = new SysRoleRequest();
        temp.setRoleId(sysRoleRequest.getRoleId());
        temp.setTotalSelectFlag(true);
        this.roleServiceApi.grantButtonGrantAll(temp);

        // 添加用户默认的所有后端接口权限
        SysRoleRequest tempResRequest = new SysRoleRequest();
        tempResRequest.setRoleId(sysRoleRequest.getRoleId());
        tempResRequest.setTotalSelectFlag(true);
        tempResRequest.setResourceBizType(ResBizTypeEnum.SYSTEM.getCode());
        this.roleServiceApi.grantResourceV2GrantAll(tempResRequest);
    }

}
