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
package cn.stylefeng.roses.kernel.system.modular.user.controller;

import cn.stylefeng.roses.kernel.rule.enums.ResBizTypeEnum;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.rule.pojo.response.ResponseData;
import cn.stylefeng.roses.kernel.rule.pojo.response.SuccessResponseData;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import cn.stylefeng.roses.kernel.system.api.pojo.user.SysUserAdminDTO;
import cn.stylefeng.roses.kernel.system.api.pojo.user.request.SysAdminRequest;
import cn.stylefeng.roses.kernel.system.modular.user.service.SysUserAdminService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理员相关接口
 * <p>
 * 管理员角色只能维护后台相关菜单
 *
 * @author fengshuonan
 * @date 2022/9/30 10:44
 */
@RestController
@ApiResource(name = "管理员相关接口", resBizType = ResBizTypeEnum.SYSTEM)
public class SysUserAdminController {

    @Resource
    private SysUserAdminService sysUserAdminService;

    /**
     * 获取后台管理员列表
     *
     * @author fengshuonan
     * @date 2022/9/30 10:44
     */
    @GetResource(name = "获取后台管理员列表", path = "/sysUser/backAuth/getAdminList")
    public ResponseData<List<SysUserAdminDTO>> getAdminList() {
        List<SysUserAdminDTO> adminUserList = sysUserAdminService.getAdminUserList();
        return new SuccessResponseData<>(adminUserList);
    }

    /**
     * 添加后台管理员
     *
     * @author fengshuonan
     * @date 2022/9/28 20:28
     */
    @PostResource(name = "添加后台管理员", path = "/sysUser/backAuth/addAdmin")
    public ResponseData<?> addAdmin(@RequestBody @Validated(BaseRequest.add.class) SysAdminRequest sysAdminRequest) {
        this.sysUserAdminService.addAdminUser(sysAdminRequest);
        return new SuccessResponseData<>();
    }

    /**
     * 删除后台管理员
     *
     * @author fengshuonan
     * @date 2022/9/28 20:28
     */
    @PostResource(name = "删除后台管理员", path = "/sysUser/backAuth/delAdmin")
    public ResponseData<?> delAdmin(@RequestBody @Validated(BaseRequest.delete.class) SysAdminRequest sysAdminRequest) {
        this.sysUserAdminService.deleteAdminUser(sysAdminRequest);
        return new SuccessResponseData<>();
    }

}
