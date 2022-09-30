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
package cn.stylefeng.roses.kernel.system.modular.role.controller;

import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.system.api.MenuServiceApi;
import cn.stylefeng.roses.kernel.system.api.pojo.menu.MenuAndButtonTreeResponse;
import cn.stylefeng.roses.kernel.system.api.pojo.role.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.system.modular.role.service.SysRoleResourceService;
import cn.stylefeng.roses.kernel.system.modular.role.service.SysRoleService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统角色控制器
 *
 * @author majianguo
 * @date 2020/11/5 上午10:19
 */
@RestController
@ApiResource(name = "系统角色管理", resBizType = ResBizTypeEnum.SYSTEM)
public class SysRoleAuthController {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private MenuServiceApi menuServiceApi;

    @Resource
    private SysRoleResourceService sysRoleResourceService;

    /**
     * 获取角色分配菜单界面，绑定情况列表
     *
     * @author fengshuonan
     * @date 2022/9/28 16:04
     */
    @GetResource(name = "获取角色分配菜单界面，绑定情况列表", path = "/sysMenu/roleBindMenuList")
    public ResponseData<List<MenuAndButtonTreeResponse>> roleBindMenuList(@Validated(SysRoleRequest.roleBindMenuList.class) SysRoleRequest sysRoleRequest) {
        List<MenuAndButtonTreeResponse> treeResponseList = menuServiceApi.getRoleBindMenuList(sysRoleRequest);
        return new SuccessResponseData<>(treeResponseList);
    }

    /**
     * 获取角色分配操作权限，绑定情况列表
     *
     * @author fengshuonan
     * @date 2022/9/28 17:23
     */
    @GetResource(name = "获取角色分配操作权限，绑定情况列表", path = "/sysMenu/roleBindOperateList")
    public ResponseData<List<MenuAndButtonTreeResponse>> roleBindOperateList(@Validated(SysRoleRequest.roleBindMenuList.class) SysRoleRequest sysRoleRequest) {
        List<MenuAndButtonTreeResponse> treeResponseList = menuServiceApi.getRoleBindOperateList(sysRoleRequest);
        return new SuccessResponseData<>(treeResponseList);
    }

    /**
     * 角色权限界面：角色绑定菜单权限
     *
     * @author fengshuonan
     * @date 2022/9/28 20:28
     */
    @PostResource(name = "角色权限界面：角色绑定菜单权限", path = "/sysRole/grantRoleMenus")
    public ResponseData<List<MenuAndButtonTreeResponse>> grantRoleMenus(@RequestBody @Validated(SysRoleRequest.grantRoleMenus.class) SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.grantRoleMenus(sysRoleRequest));
    }

    /**
     * 角色权限界面，角色绑定操作权限
     *
     * @author fengshuonan
     * @date 2022/9/29 11:10
     */
    @PostResource(name = "角色权限界面，角色绑定操作权限", path = "/sysRole/grantButton")
    public ResponseData<List<MenuAndButtonTreeResponse>> grantButton(@RequestBody @Validated(SysRoleRequest.grantButton.class) SysRoleRequest sysRoleRequest) {
        List<MenuAndButtonTreeResponse> menuAndButtonTreeResponses = sysRoleService.grantButton(sysRoleRequest);
        return new SuccessResponseData<>(menuAndButtonTreeResponses);
    }

    /**
     * 角色权限界面：角色绑定菜单权限（全选操作）
     *
     * @author fengshuonan
     * @date 2022/9/28 20:28
     */
    @PostResource(name = "角色权限界面：角色绑定菜单权限（全选操作）", path = "/sysRole/grantRoleMenus/grantAll")
    public ResponseData<List<MenuAndButtonTreeResponse>> grantRoleMenusGrantAll(@RequestBody @Validated(SysRoleRequest.grantAll.class) SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.grantRoleMenusGrantAll(sysRoleRequest));
    }

    /**
     * 角色权限界面：角色绑定操作权限（全选操作）
     *
     * @author fengshuonan
     * @date 2022/9/28 20:28
     */
    @PostResource(name = "角色权限界面：角色绑定操作权限（全选操作）", path = "/sysRole/grantButton/grantAll")
    public ResponseData<List<MenuAndButtonTreeResponse>> grantButtonGrantAll(@RequestBody @Validated(SysRoleRequest.grantAll.class) SysRoleRequest sysRoleRequest) {
        return new SuccessResponseData<>(sysRoleService.grantButtonGrantAll(sysRoleRequest));
    }

    /**
     * 角色绑定接口数据V2
     *
     * @author fengshuonan
     * @date 2021/8/10 18:23
     */
    @PostResource(name = "角色绑定接口数据V2", path = "/sysRole/grantResourceV2")
    public ResponseData<?> grantResourceV2(@RequestBody @Validated(SysRoleRequest.grantResourceV2.class) SysRoleRequest sysRoleRequest) {
        sysRoleResourceService.grantResourceV2(sysRoleRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 角色绑定所有接口数据
     *
     * @author fengshuonan
     * @date 2021/8/10 18:23
     */
    @PostResource(name = "角色绑定所有接口数据", path = "/sysRole/grantResourceV2/grantAll")
    public ResponseData<?> grantResourceV2GrantAll(@RequestBody @Validated(SysRoleRequest.grantAll.class) SysRoleRequest sysRoleRequest) {
        this.sysRoleService.grantResourceV2GrantAll(sysRoleRequest);
        return new SuccessResponseData<>();
    }

}
