/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.system.modular.role.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.enums.DataScopeTypeEnum;
import cn.stylefeng.roses.kernel.auth.api.exception.AuthException;
import cn.stylefeng.roses.kernel.auth.api.exception.enums.AuthExceptionEnum;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.basic.SimpleRoleInfo;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.db.api.factory.PageFactory;
import cn.stylefeng.roses.kernel.db.api.factory.PageResultFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.constants.SymbolConstant;
import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
import cn.stylefeng.roses.kernel.rule.enums.StatusEnum;
import cn.stylefeng.roses.kernel.rule.enums.YesOrNotEnum;
import cn.stylefeng.roses.kernel.rule.exception.base.ServiceException;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.system.api.MenuServiceApi;
import cn.stylefeng.roses.kernel.system.api.ResourceServiceApi;
import cn.stylefeng.roses.kernel.system.api.UserServiceApi;
import cn.stylefeng.roses.kernel.system.api.enums.AntdvFrontTypeEnum;
import cn.stylefeng.roses.kernel.system.api.exception.SystemModularException;
import cn.stylefeng.roses.kernel.system.api.exception.enums.role.SysRoleExceptionEnum;
import cn.stylefeng.roses.kernel.system.api.pojo.menu.MenuAndButtonTreeResponse;
import cn.stylefeng.roses.kernel.system.api.pojo.menu.SysMenuButtonDTO;
import cn.stylefeng.roses.kernel.system.api.pojo.role.dto.*;
import cn.stylefeng.roses.kernel.system.api.pojo.role.request.SysRoleMenuButtonRequest;
import cn.stylefeng.roses.kernel.system.api.pojo.role.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.system.api.util.DataScopeUtil;
import cn.stylefeng.roses.kernel.system.modular.role.entity.*;
import cn.stylefeng.roses.kernel.system.modular.role.mapper.SysRoleMapper;
import cn.stylefeng.roses.kernel.system.modular.role.service.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cn.stylefeng.roses.kernel.system.api.constants.SystemConstants.SUPER_ADMIN_ROLE_CODE;


/**
 * 系统角色service接口实现类
 *
 * @author majianguo
 * @date 2020/11/5 上午11:33
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Resource
    private UserServiceApi userServiceApi;

    @Resource
    private SysRoleResourceService sysRoleResourceService;

    @Resource
    private SysRoleDataScopeService sysRoleDataScopeService;

    @Resource
    private SysRoleMenuService roleMenuService;

    @Resource
    private SysRoleMenuButtonService sysRoleMenuButtonService;

    @Resource
    private MenuServiceApi menuServiceApi;

    @Resource
    private CacheOperatorApi<SysRole> roleInfoCacheApi;

    @Resource(name = "roleResourceCacheApi")
    private CacheOperatorApi<List<String>> roleResourceCacheApi;

    @Resource(name = "roleDataScopeCacheApi")
    private CacheOperatorApi<List<Long>> roleDataScopeCacheApi;

    @Resource
    private ResourceServiceApi resourceServiceApi;

    @Override
    public void add(SysRoleRequest sysRoleRequest) {

        SysRole sysRole = new SysRole();
        BeanUtil.copyProperties(sysRoleRequest, sysRole);

        // 默认设置为启用
        sysRole.setAdminFlag(YesOrNotEnum.N.getCode());

        // 默认设置为启用
        sysRole.setStatusFlag(StatusEnum.ENABLE.getCode());

        // 默认设置为普通角色
        sysRole.setRoleSystemFlag(YesOrNotEnum.N.getCode());

        //默认数据范围
        sysRole.setDataScopeType(DataScopeTypeEnum.SELF.getCode());

        this.save(sysRole);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void del(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);

        // 超级管理员不能删除
        if (SUPER_ADMIN_ROLE_CODE.equals(sysRole.getRoleCode())) {
            throw new ServiceException(SysRoleExceptionEnum.SYSTEM_ROLE_CANT_DELETE);
        }

        Long roleId = sysRole.getRoleId();

        // 级联删除该角色对应的角色-数据范围关联信息
        sysRoleDataScopeService.delByRoleId(roleId);

        // 级联删除该角色对应的用户-角色表关联信息
        userServiceApi.deleteUserRoleListByRoleId(roleId);

        // 级联删除该角色对应的角色-菜单表关联信息
        sysRoleResourceService.deleteRoleResourceListByRoleId(roleId, null);

        // 删除角色缓存信息
        roleInfoCacheApi.remove(String.valueOf(roleId));

        // 删除角色的数据范围缓存
        roleDataScopeCacheApi.remove(String.valueOf(sysRoleRequest.getRoleId()));

        // 删除角色
        this.removeById(roleId);
    }

    @Override
    public void grantResourceV2GrantAll(SysRoleRequest sysRoleRequest) {

        // 删除角色绑定的所有资源
        this.sysRoleResourceService.deleteRoleResourceListByRoleId(sysRoleRequest.getRoleId(), sysRoleRequest.getResourceBizType());

        // 获取是全部选中，还是全部取消，如果是全部取消，则直接返回
        if (!sysRoleRequest.getTotalSelectFlag()) {
            return;
        }

        // 如果是全部选中，则查询一共有多少资源，将角色赋予全部资源
        ResBizTypeEnum resBizTypeEnum = null;
        if (ObjectUtil.isNotEmpty(sysRoleRequest.getResourceBizType())) {
            resBizTypeEnum = ResBizTypeEnum.DEFAULT.parseToEnum(sysRoleRequest.getResourceBizType().toString());
        }
        List<SysRoleResourceDTO> totalResourceCode = resourceServiceApi.getTotalResourceCode(resBizTypeEnum);
        this.sysRoleResourceService.batchSaveResCodes(sysRoleRequest.getRoleId(), totalResourceCode);
    }

    @Override
    public SysRoleDTO getRoleByCode(String roleCode) {
        if (roleCode == null) {
            return null;
        }

        LambdaQueryWrapper<SysRole> sysRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleLambdaQueryWrapper.eq(SysRole::getRoleCode, roleCode);
        sysRoleLambdaQueryWrapper.ne(SysRole::getDelFlag, YesOrNotEnum.Y.getCode());
        SysRole sysRole = this.getOne(sysRoleLambdaQueryWrapper, false);

        if (sysRole == null) {
            return null;
        }

        SysRoleDTO roleResponse = new SysRoleDTO();
        BeanUtil.copyProperties(sysRole, roleResponse);

        return roleResponse;
    }

    @Override
    public RoleAuthorizeInfo getRoleAuthorizeInfo(List<Long> roleIdList) {

        HashSet<String> result = new HashSet<>();

        for (Long roleId : roleIdList) {

            // 从缓存获取所有角色绑定的资源
            String key = String.valueOf(roleId);
            List<String> resourceCodesCache = roleResourceCacheApi.get(key);
            if (ObjectUtil.isNotEmpty(resourceCodesCache)) {
                result.addAll(resourceCodesCache);
                continue;
            }

            // 从数据库查询角色绑定的资源
            LambdaQueryWrapper<SysRoleResource> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.select(SysRoleResource::getResourceCode);
            queryWrapper.eq(SysRoleResource::getRoleId, roleId);
            List<SysRoleResource> sysRoleResources = sysRoleResourceService.list(queryWrapper);
            List<String> sysResourceCodes = sysRoleResources.parallelStream().map(SysRoleResource::getResourceCode).collect(Collectors.toList());
            if (ObjectUtil.isNotEmpty(sysResourceCodes)) {
                result.addAll(sysResourceCodes);
                roleResourceCacheApi.put(key, sysResourceCodes);
            }
        }

        // 获取角色的所有菜单
        LambdaQueryWrapper<SysRoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(SysRoleMenu::getRoleId, roleIdList);
        wrapper.select(SysRoleMenu::getMenuId);
        List<SysRoleMenu> list = this.roleMenuService.list(wrapper);
        List<Long> menuIds = list.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());

        // 获取角色绑定的所有按钮
        LambdaQueryWrapper<SysRoleMenuButton> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.in(SysRoleMenuButton::getRoleId, roleIdList);
        wrapper2.select(SysRoleMenuButton::getButtonId);
        List<SysRoleMenuButton> roleMenuButtons = this.sysRoleMenuButtonService.list(wrapper2);
        List<Long> buttonIds = roleMenuButtons.stream().map(SysRoleMenuButton::getButtonId).collect(Collectors.toList());

        // 获取菜单和按钮所有绑定的资源
        ArrayList<Long> businessIds = new ArrayList<>();
        businessIds.addAll(menuIds);
        businessIds.addAll(buttonIds);

        // 获取菜单和按钮
        List<String> menuButtonResources = menuServiceApi.getResourceCodesByBusinessId(businessIds);
        result.addAll(menuButtonResources);

        // 封装返回结果
        return new RoleAuthorizeInfo(menuIds, buttonIds, result);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void edit(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);

        // 不能修改超级管理员编码
        if (SUPER_ADMIN_ROLE_CODE.equals(sysRole.getRoleCode())) {
            if (!sysRole.getRoleCode().equals(sysRoleRequest.getRoleCode())) {
                throw new SystemModularException(SysRoleExceptionEnum.SUPER_ADMIN_ROLE_CODE_ERROR);
            }
        }

        // 拷贝属性
        BeanUtil.copyProperties(sysRoleRequest, sysRole);

        // 不能修改状态，用修改状态接口修改状态
        sysRole.setStatusFlag(null);

        this.updateById(sysRole);

        // 删除角色缓存信息
        roleInfoCacheApi.remove(String.valueOf(sysRoleRequest.getRoleId()));
    }

    @Override
    public SysRoleDTO detail(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);
        SysRoleDTO roleResponse = new SysRoleDTO();
        BeanUtil.copyProperties(sysRole, roleResponse);

        // 填充数据范围类型枚举
        roleResponse.setDataScopeTypeEnum(DataScopeTypeEnum.codeToEnum(sysRole.getDataScopeType()));

        return roleResponse;
    }

    @Override
    public PageResult<SysRole> findPage(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> wrapper = createWrapper(sysRoleRequest);

        // 不查询管理员类型的
        wrapper.eq(SysRole::getAdminFlag, YesOrNotEnum.N.getCode());

        Page<SysRole> sysRolePage = this.page(PageFactory.defaultPage(), wrapper);
        return PageResultFactory.createPageResult(sysRolePage);
    }

    @Override
    public List<SimpleDict> findList(SysRoleRequest sysRoleParam) {
        List<SimpleDict> dictList = CollectionUtil.newArrayList();
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();
        if (ObjectUtil.isNotNull(sysRoleParam)) {

            // 根据角色名称或编码模糊查询
            if (ObjectUtil.isNotEmpty(sysRoleParam.getRoleName())) {
                queryWrapper.and(i -> i.like(SysRole::getRoleName, sysRoleParam.getRoleName()).or().like(SysRole::getRoleCode, sysRoleParam.getRoleName()));
            }
        }

        // 只查询正常状态
        queryWrapper.eq(SysRole::getStatusFlag, StatusEnum.ENABLE.getCode());

        // 根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(SysRole::getRoleSort);
        this.list(queryWrapper).forEach(sysRole -> {
            SimpleDict simpleDict = new SimpleDict();
            simpleDict.setId(sysRole.getRoleId());
            simpleDict.setName(sysRole.getRoleName() + SymbolConstant.LEFT_SQUARE_BRACKETS + sysRole.getRoleCode() + SymbolConstant.RIGHT_SQUARE_BRACKETS);
            dictList.add(simpleDict);
        });
        return dictList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantMenuAndButton(SysRoleRequest sysRoleMenuButtonRequest) {

        // 清除该角色之前的菜单
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLqw = new LambdaQueryWrapper<>();
        sysRoleMenuLqw.eq(SysRoleMenu::getRoleId, sysRoleMenuButtonRequest.getRoleId());
        roleMenuService.remove(sysRoleMenuLqw);

        // 清除该角色之前的按钮授权
        LambdaQueryWrapper<SysRoleMenuButton> menuButtonLqw = new LambdaQueryWrapper<>();
        menuButtonLqw.eq(SysRoleMenuButton::getRoleId, sysRoleMenuButtonRequest.getRoleId());
        sysRoleMenuButtonService.remove(menuButtonLqw);

        // 新增菜单
        List<Long> menuIdList = sysRoleMenuButtonRequest.getGrantMenuIdList();
        if (ObjectUtil.isNotEmpty(menuIdList)) {
            List<SysRoleMenu> sysRoleMenus = new ArrayList<>();

            // 角色ID
            Long roleId = sysRoleMenuButtonRequest.getRoleId();

            // 查询菜单的所有父菜单
            Set<Long> allParentMenuId = menuServiceApi.getMenuAllParentMenuId(new HashSet<>(menuIdList));

            // 处理所有父菜单
            for (Long menuId : allParentMenuId) {
                SysRoleMenu item = new SysRoleMenu();
                item.setRoleId(roleId);
                item.setMenuId(menuId);
                sysRoleMenus.add(item);
            }

            // 处理菜单本身
            for (Long menuId : menuIdList) {
                SysRoleMenu item = new SysRoleMenu();
                item.setRoleId(roleId);
                item.setMenuId(menuId);
                sysRoleMenus.add(item);
            }
            roleMenuService.saveBatch(sysRoleMenus);
        }

        // 新增按钮
        List<SysRoleMenuButtonRequest> menuButtonList = sysRoleMenuButtonRequest.getGrantMenuButtonIdList();
        if (ObjectUtil.isNotEmpty(menuButtonList)) {
            List<SysRoleMenuButton> sysRoleMenuButtons = new ArrayList<>();
            for (SysRoleMenuButtonRequest menuButton : menuButtonList) {
                SysRoleMenuButton item = new SysRoleMenuButton();
                item.setRoleId(sysRoleMenuButtonRequest.getRoleId());
                item.setButtonId(menuButton.getButtonId());
                item.setButtonCode(menuButton.getButtonCode());
                sysRoleMenuButtons.add(item);
            }
            sysRoleMenuButtonService.saveBatch(sysRoleMenuButtons);
        }
    }

    @Override
    public void grantMenu(SysRoleRequest sysRoleMenuButtonRequest) {

        // 获取新增还是取消绑定
        Boolean grantAddMenuFlag = sysRoleMenuButtonRequest.getGrantAddMenuFlag();

        // 如果是新增绑定菜单
        if (grantAddMenuFlag) {
            SysRoleMenu item = new SysRoleMenu();
            item.setRoleId(sysRoleMenuButtonRequest.getRoleId());
            item.setMenuId(sysRoleMenuButtonRequest.getGrantMenuId());
            this.roleMenuService.save(item);
        } else {
            //如果是解除绑定菜单
            LambdaUpdateWrapper<SysRoleMenu> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(SysRoleMenu::getRoleId, sysRoleMenuButtonRequest.getRoleId());
            wrapper.eq(SysRoleMenu::getMenuId, sysRoleMenuButtonRequest.getGrantMenuId());
            this.roleMenuService.remove(wrapper);
        }
    }

    @Override
    public List<MenuAndButtonTreeResponse> grantButton(SysRoleRequest sysRoleMenuButtonRequest) {
        // 该模块下角色绑定的按钮全部删除
        List<Long> modularButtonIds = sysRoleMenuButtonRequest.getModularButtonIds();
        if (ObjectUtil.isNotEmpty(modularButtonIds)) {
            LambdaUpdateWrapper<SysRoleMenuButton> wrapper = new LambdaUpdateWrapper<>();
            wrapper.in(SysRoleMenuButton::getButtonId, modularButtonIds);
            wrapper.eq(SysRoleMenuButton::getRoleId, sysRoleMenuButtonRequest.getRoleId());
            this.sysRoleMenuButtonService.remove(wrapper);
        }

        // 该模块下，勾选的按钮，添加到角色绑定
        List<Long> selectedButtonIds = sysRoleMenuButtonRequest.getSelectedButtonIds();
        if (ObjectUtil.isNotEmpty(selectedButtonIds)) {
            ArrayList<SysRoleMenuButton> sysRoleMenuButtons = new ArrayList<>();
            for (Long selectButtonId : selectedButtonIds) {
                SysRoleMenuButton sysRoleMenuButton = new SysRoleMenuButton();
                sysRoleMenuButton.setRoleId(sysRoleMenuButtonRequest.getRoleId());
                sysRoleMenuButton.setButtonId(selectButtonId);

                // 通过buttonId获取buttonCode
                String buttonCode = this.menuServiceApi.getMenuButtonCodeByButtonId(selectButtonId);
                sysRoleMenuButton.setButtonCode(buttonCode);

                sysRoleMenuButtons.add(sysRoleMenuButton);
            }
            this.sysRoleMenuButtonService.saveBatch(sysRoleMenuButtons, sysRoleMenuButtons.size());
        }

        // 返回角色绑定的按钮列表
        return this.menuServiceApi.getRoleBindOperateList(sysRoleMenuButtonRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void grantDataScope(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);

        // 获取当前用户是否是超级管理员
        boolean superAdmin = LoginContext.me().getSuperAdminFlag();

        // 获取请求参数的数据范围类型
        Integer dataScopeType = sysRoleRequest.getDataScopeType();
        DataScopeTypeEnum dataScopeTypeEnum = DataScopeTypeEnum.codeToEnum(dataScopeType);

        // 如果登录用户不是超级管理员，则进行数据权限校验
        if (!superAdmin) {

            // 只有超级管理员可以授权全部范围
            if (DataScopeTypeEnum.ALL.equals(dataScopeTypeEnum)) {
                throw new AuthException(AuthExceptionEnum.ONLY_SUPER_ERROR);
            }

            // 数据范围类型为自定义，则判断当前用户有没有该公司的权限
            if (DataScopeTypeEnum.DEFINE.getCode().equals(dataScopeType)) {
                if (ObjectUtil.isEmpty(sysRoleRequest.getGrantOrgIdList())) {
                    throw new SystemModularException(SysRoleExceptionEnum.PLEASE_FILL_DATA_SCOPE);
                }
                for (Long orgId : sysRoleRequest.getGrantOrgIdList()) {
                    DataScopeUtil.quickValidateDataScope(orgId);
                }
            }
        }

        sysRole.setDataScopeType(sysRoleRequest.getDataScopeType());
        this.updateById(sysRole);

        // 绑定角色数据范围关联
        sysRoleDataScopeService.grantDataScope(sysRoleRequest);

        // 删除角色的数据范围缓存
        roleDataScopeCacheApi.remove(String.valueOf(sysRoleRequest.getRoleId()));
    }

    @Override
    public List<SimpleDict> dropDown() {
        List<SimpleDict> dictList = CollectionUtil.newArrayList();
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();

        // 如果当前登录用户不是超级管理员，则查询自己拥有的角色
        if (!LoginContext.me().getSuperAdminFlag()) {

            // 查询自己拥有的
            List<SimpleRoleInfo> roles = LoginContext.me().getLoginUser().getSimpleRoleInfoList();

            // 取出所有角色id
            Set<Long> loginUserRoleIds = roles.stream().map(SimpleRoleInfo::getRoleId).collect(Collectors.toSet());
            if (ObjectUtil.isEmpty(loginUserRoleIds)) {
                return dictList;
            }
            queryWrapper.in(SysRole::getRoleId, loginUserRoleIds);
        }

        // 只查询正常状态
        queryWrapper.eq(SysRole::getStatusFlag, StatusEnum.ENABLE.getCode()).eq(SysRole::getDelFlag, YesOrNotEnum.N.getCode());

        // 只查询非管理员的角色
        queryWrapper.eq(SysRole::getAdminFlag, YesOrNotEnum.N.getCode());

        this.list(queryWrapper).forEach(sysRole -> {
            SimpleDict simpleDict = new SimpleDict();
            simpleDict.setId(sysRole.getRoleId());
            simpleDict.setCode(sysRole.getRoleCode());
            simpleDict.setName(sysRole.getRoleName());
            dictList.add(simpleDict);
        });
        return dictList;
    }

    @Override
    public List<Long> getRoleDataScope(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = this.querySysRole(sysRoleRequest);
        return sysRoleDataScopeService.getRoleDataScopeIdList(CollectionUtil.newArrayList(sysRole.getRoleId()));
    }

    @Override
    public void grantMenusAndButtons(SysRoleRequest sysRoleRequest) {

        // 授权角色绑定的菜单
        this.grantRoleMenus(sysRoleRequest);

        // 授权按钮
        if (ObjectUtil.isNotEmpty(sysRoleRequest.getModularButtonIds())) {
            this.grantButton(sysRoleRequest);
        }

    }

    @Override
    public List<MenuAndButtonTreeResponse> grantMenusAndButtonsAndGetResult(SysRoleRequest sysRoleRequest) {
        this.grantMenusAndButtons(sysRoleRequest);
        return menuServiceApi.getRoleMenuAndButtons(sysRoleRequest);
    }

    @Override
    public List<MenuAndButtonTreeResponse> grantRoleMenus(SysRoleRequest sysRoleRequest) {

        // 获取新增绑定还是取消绑定菜单
        Boolean selectBindFlag = sysRoleRequest.getSelectBindFlag();

        // 获取角色和绑定菜单
        Long roleId = sysRoleRequest.getRoleId();
        List<Long> grantMenuIdList = sysRoleRequest.getGrantMenuIdList();

        // 如果是新增绑定菜单
        if (selectBindFlag) {
            // 获取所有菜单的父级节点，把选中状态的菜单的所有父级节点都选择上
            Set<Long> allParentMenuId = menuServiceApi.getMenuAllParentMenuId(new HashSet<>(grantMenuIdList));
            grantMenuIdList.addAll(allParentMenuId);

            // 批量保存绑定的菜单集合
            List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
            for (Long menuId : grantMenuIdList) {
                SysRoleMenu item = new SysRoleMenu();
                item.setRoleId(roleId);
                item.setMenuId(menuId);
                sysRoleMenus.add(item);
            }
            this.roleMenuService.saveBatch(sysRoleMenus);
        } else {
            // 如果是解除绑定菜单
            LambdaUpdateWrapper<SysRoleMenu> wrapper = new LambdaUpdateWrapper<>();
            wrapper.eq(SysRoleMenu::getRoleId, roleId);
            wrapper.in(SysRoleMenu::getMenuId, grantMenuIdList);
            this.roleMenuService.remove(wrapper);
        }

        // 获取当前角色分配的菜单权限
        return menuServiceApi.getRoleBindMenuList(sysRoleRequest);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public List<MenuAndButtonTreeResponse> grantRoleMenusGrantAll(SysRoleRequest sysRoleRequest) {

        // 根据参数的前后台类型，获取所有菜单id
        AntdvFrontTypeEnum antdvFrontTypeEnum = AntdvFrontTypeEnum.parseToEnum(sysRoleRequest.getResourceBizType());
        List<Long> totalMenuIdList = this.menuServiceApi.getTotalMenuIdList(antdvFrontTypeEnum);

        // 删除角色绑定的所有菜单
        LambdaUpdateWrapper<SysRoleMenu> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysRoleMenu::getRoleId, sysRoleRequest.getRoleId());
        wrapper.in(SysRoleMenu::getMenuId, totalMenuIdList);
        this.roleMenuService.remove(wrapper);

        // 获取当前角色分配的菜单权限
        List<MenuAndButtonTreeResponse> roleBindMenuList = menuServiceApi.getRoleBindMenuList(sysRoleRequest);

        // 如果是取消权限，则直接返回
        if (!sysRoleRequest.getTotalSelectFlag()) {
            return roleBindMenuList;
        }
        
        // 批量保存绑定的菜单集合
        List<SysRoleMenu> sysRoleMenus = new ArrayList<>();
        for (Long menuId : totalMenuIdList) {
            SysRoleMenu item = new SysRoleMenu();
            item.setRoleId(sysRoleRequest.getRoleId());
            item.setMenuId(menuId);
            sysRoleMenus.add(item);
        }
        this.roleMenuService.saveBatch(sysRoleMenus);

        return roleBindMenuList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, isolation = Isolation.READ_UNCOMMITTED)
    public List<MenuAndButtonTreeResponse> grantButtonGrantAll(SysRoleRequest sysRoleRequest) {

        // 删除角色绑定的所有按钮权限
        LambdaUpdateWrapper<SysRoleMenuButton> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(SysRoleMenuButton::getRoleId, sysRoleRequest.getRoleId());
        this.sysRoleMenuButtonService.remove(wrapper);

        // 获取当前角色分配的操作权限
        List<MenuAndButtonTreeResponse> roleBindOperateList = menuServiceApi.getRoleBindOperateList(sysRoleRequest);

        // 如果是取消权限，则直接返回
        if (!sysRoleRequest.getTotalSelectFlag()) {
            return roleBindOperateList;
        }

        // 获取所有前台按钮集合
        AntdvFrontTypeEnum antdvFrontTypeEnum = AntdvFrontTypeEnum.parseToEnum(sysRoleRequest.getResourceBizType());
        List<SysMenuButtonDTO> totalButtonIds = this.menuServiceApi.getTotalMenuButtonIdList(antdvFrontTypeEnum);

        // 批量保存绑定的按钮集合
        List<SysRoleMenuButton> sysRoleMenuButtons = new ArrayList<>();
        for (SysMenuButtonDTO buttonDTO : totalButtonIds) {
            SysRoleMenuButton item = new SysRoleMenuButton();
            item.setRoleId(sysRoleRequest.getRoleId());
            item.setButtonId(buttonDTO.getButtonId());
            item.setButtonCode(buttonDTO.getButtonCode());
            sysRoleMenuButtons.add(item);
        }
        this.sysRoleMenuButtonService.batchSaveRoleMenuButton(sysRoleMenuButtons);

        return roleBindOperateList;
    }

    @Override
    public List<SysRoleDTO> getRolesByIds(List<Long> roleIds) {
        ArrayList<SysRoleDTO> sysRoleResponses = new ArrayList<>();
        for (Long roleId : roleIds) {
            SysRoleRequest sysRoleRequest = new SysRoleRequest();
            sysRoleRequest.setRoleId(roleId);
            SysRoleDTO detail = this.detail(sysRoleRequest);
            sysRoleResponses.add(detail);
        }
        return sysRoleResponses;
    }

    @Override
    public List<Long> getRoleDataScopes(List<Long> roleIds) {

        ArrayList<Long> result = new ArrayList<>();

        if (ObjectUtil.isEmpty(roleIds)) {
            return result;
        }

        for (Long roleId : roleIds) {
            // 从缓存获取数据范围
            String key = String.valueOf(roleId);
            List<Long> scopes = roleDataScopeCacheApi.get(key);
            if (scopes != null) {
                result.addAll(scopes);
            }

            // 从数据库查询数据范围
            LambdaQueryWrapper<SysRoleDataScope> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysRoleDataScope::getRoleId, roleId);
            List<SysRoleDataScope> list = this.sysRoleDataScopeService.list(queryWrapper);
            if (!list.isEmpty()) {
                List<Long> realScopes = list.stream().map(SysRoleDataScope::getOrganizationId).collect(Collectors.toList());
                result.addAll(realScopes);

                // 添加结果到缓存中
                roleDataScopeCacheApi.put(key, realScopes);
            }
        }

        return result;
    }

    @Override
    public List<Long> getMenuIdsByRoleIds(List<Long> roleIds) {

        if (roleIds == null || roleIds.isEmpty()) {
            return new ArrayList<>();
        }

        // 获取角色绑定的菜单
        LambdaQueryWrapper<SysRoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenu::getRoleId, roleIds);
        queryWrapper.select(SysRoleMenu::getMenuId);

        List<SysRoleMenu> roleMenus = this.roleMenuService.list(queryWrapper);
        if (roleMenus == null || roleMenus.isEmpty()) {
            return new ArrayList<>();
        }

        return roleMenus.stream().map(SysRoleMenu::getMenuId).collect(Collectors.toList());
    }

    @Override
    public Set<String> getRoleResourceCodeList(List<Long> roleIdList) {
        RoleAuthorizeInfo roleAuthorizeInfo = this.getRoleAuthorizeInfo(roleIdList);
        return roleAuthorizeInfo.getResourceCodeList();
    }

    @Override
    public List<SysRoleResourceDTO> getRoleResourceList(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleResource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleResource::getRoleId, roleIdList);
        List<SysRoleResource> sysRoleResources = sysRoleResourceService.list(queryWrapper);
        return sysRoleResources.parallelStream().map(item -> BeanUtil.copyProperties(item, SysRoleResourceDTO.class)).collect(Collectors.toList());
    }

    @Override
    public Set<String> getRoleButtonCodes(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleMenuButton> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(SysRoleMenuButton::getRoleId, roleIdList);
        queryWrapper.select(SysRoleMenuButton::getButtonCode);
        List<SysRoleMenuButton> list = sysRoleMenuButtonService.list(queryWrapper);
        return list.stream().map(SysRoleMenuButton::getButtonCode).collect(Collectors.toSet());
    }

    @Override
    public List<SysRoleMenuDTO> getRoleMenuList(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleMenu> sysRoleMenuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuLambdaQueryWrapper.in(SysRoleMenu::getRoleId, roleIdList);
        List<SysRoleMenu> roleMenus = roleMenuService.list(sysRoleMenuLambdaQueryWrapper);
        return roleMenus.parallelStream().map(item -> BeanUtil.copyProperties(item, SysRoleMenuDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<SysRoleMenuButtonDTO> getRoleMenuButtonList(List<Long> roleIdList) {
        LambdaQueryWrapper<SysRoleMenuButton> sysRoleMenuButtonLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRoleMenuButtonLambdaQueryWrapper.in(SysRoleMenuButton::getRoleId, roleIdList);
        List<SysRoleMenuButton> sysRoleMenuButtons = sysRoleMenuButtonService.list(sysRoleMenuButtonLambdaQueryWrapper);
        return sysRoleMenuButtons.parallelStream().map(item -> BeanUtil.copyProperties(item, SysRoleMenuButtonDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<SysRoleDTO> getRoleSelectList(SysRoleRequest sysRoleRequest) {

        LambdaQueryWrapper<SysRole> wrapper = this.createWrapper(sysRoleRequest);
        List<SysRole> list = this.list(wrapper);

        ArrayList<SysRoleDTO> sysRoleDTOS = new ArrayList<>();
        for (SysRole sysRole : list) {
            SysRoleDTO sysRoleDTO = new SysRoleDTO();
            BeanUtil.copyProperties(sysRole, sysRoleDTO);
            sysRoleDTOS.add(sysRoleDTO);
        }

        return sysRoleDTOS;
    }

    @Override
    public void addAdminRole(SysRoleRequest sysRoleRequest) {
        SysRole sysRole = new SysRole();

        sysRole.setRoleId(sysRoleRequest.getRoleId());
        sysRole.setRoleName(sysRoleRequest.getRoleName());
        sysRole.setRoleCode(sysRoleRequest.getRoleCode());

        sysRole.setRoleSort(new BigDecimal(9999));
        sysRole.setDataScopeType(DataScopeTypeEnum.ALL.getCode());
        sysRole.setStatusFlag(StatusEnum.ENABLE.getCode());
        sysRole.setAdminFlag(YesOrNotEnum.Y.getCode());
        sysRole.setRoleSystemFlag(YesOrNotEnum.Y.getCode());
        sysRole.setDelFlag(YesOrNotEnum.N.getCode());

        this.save(sysRole);
    }

    /**
     * 获取系统角色
     *
     * @param sysRoleRequest 请求信息
     * @author majianguo
     * @date 2020/11/5 下午4:12
     */
    private SysRole querySysRole(SysRoleRequest sysRoleRequest) {

        // 从缓存中获取角色信息
        String key = String.valueOf(sysRoleRequest.getRoleId());
        SysRole sysRoleCache = roleInfoCacheApi.get(key);
        if (sysRoleCache != null) {
            return sysRoleCache;
        }

        SysRole sysRole = this.getById(sysRoleRequest.getRoleId());
        if (ObjectUtil.isNull(sysRole)) {
            throw new SystemModularException(SysRoleExceptionEnum.ROLE_NOT_EXIST);
        }

        // 加入缓存
        roleInfoCacheApi.put(key, sysRole);

        return sysRole;
    }

    /**
     * 创建查询wrapper
     *
     * @author fengshuonan
     * @date 2020/11/22 15:14
     */
    private LambdaQueryWrapper<SysRole> createWrapper(SysRoleRequest sysRoleRequest) {
        LambdaQueryWrapper<SysRole> queryWrapper = new LambdaQueryWrapper<>();

        // 查询未删除的
        queryWrapper.eq(SysRole::getDelFlag, YesOrNotEnum.N.getCode());

        // 根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(SysRole::getRoleSort);

        if (ObjectUtil.isEmpty(sysRoleRequest)) {
            return queryWrapper;
        }

        // 根据名称模糊查询
        queryWrapper.like(ObjectUtil.isNotEmpty(sysRoleRequest.getRoleName()), SysRole::getRoleName, sysRoleRequest.getRoleName());

        // 根据编码模糊查询
        queryWrapper.like(ObjectUtil.isNotEmpty(sysRoleRequest.getRoleCode()), SysRole::getRoleCode, sysRoleRequest.getRoleCode());

        // 根据是否是管理员类型查询
        queryWrapper.eq(ObjectUtil.isNotEmpty(sysRoleRequest.getAdminFlag()), SysRole::getAdminFlag, sysRoleRequest.getAdminFlag());

        return queryWrapper;
    }

}
