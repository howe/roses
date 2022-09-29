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
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.system.api.pojo.menu.MenuAndButtonTreeResponse;
import cn.stylefeng.roses.kernel.system.api.pojo.role.request.SysRoleRequest;
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
        sysRoleService.grantButton(sysRoleRequest);
        return new SuccessResponseData<>();
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

}
