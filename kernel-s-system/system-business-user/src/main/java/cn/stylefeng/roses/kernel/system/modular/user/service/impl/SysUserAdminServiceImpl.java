package cn.stylefeng.roses.kernel.system.modular.user.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.system.api.RoleServiceApi;
import cn.stylefeng.roses.kernel.system.api.pojo.role.dto.SysRoleDTO;
import cn.stylefeng.roses.kernel.system.api.pojo.role.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.system.api.pojo.user.SysUserAdminDTO;
import cn.stylefeng.roses.kernel.system.modular.user.entity.SysUser;
import cn.stylefeng.roses.kernel.system.modular.user.entity.SysUserRole;
import cn.stylefeng.roses.kernel.system.modular.user.service.SysUserAdminService;
import cn.stylefeng.roses.kernel.system.modular.user.service.SysUserRoleService;
import cn.stylefeng.roses.kernel.system.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        sysUserLambdaQueryWrapper.in(SysUser::getUserId);
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
            sysUserAdminDTO.setRealName(sysUser.getNickName());
        }

        return sysUserAdminDTOS;
    }

}
