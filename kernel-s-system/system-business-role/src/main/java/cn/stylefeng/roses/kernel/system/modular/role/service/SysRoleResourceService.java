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
package cn.stylefeng.roses.kernel.system.modular.role.service;

import cn.stylefeng.roses.kernel.system.api.pojo.role.dto.SysRoleResourceDTO;
import cn.stylefeng.roses.kernel.system.api.pojo.role.request.SysRoleRequest;
import cn.stylefeng.roses.kernel.system.modular.role.entity.SysRoleResource;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 系统角色菜单service接口
 *
 * @author majianguo
 * @date 2020/11/5 上午11:17
 */
public interface SysRoleResourceService extends IService<SysRoleResource> {

    /**
     * 授权资源
     *
     * @param sysRoleRequest 授权参数
     * @author majianguo
     * @date 2020/11/5 上午11:17
     */
    void grantResource(SysRoleRequest sysRoleRequest);

    /**
     * 角色授权接口资源
     *
     * @author fengshuonan
     * @date 2021/8/10 18:28
     */
    void grantResourceV2(SysRoleRequest sysRoleRequest);

    /**
     * 根据资源id集合删除角色关联的资源
     *
     * @param resourceIds 资源id集合
     * @author majianguo
     * @date 2020/11/5 上午11:17
     */
    void deleteRoleResourceListByResourceIds(List<Long> resourceIds);

    /**
     * 根据角色id删除对应的角色资源信息
     *
     * @param roleId          角色id
     * @param resourceBizType 指定的资源类型，如果为空，则删除所有类型的
     * @author majianguo
     * @date 2020/11/5 上午11:18
     */
    void deleteRoleResourceListByRoleId(Long roleId, Integer resourceBizType);

    /**
     * 保存所有的角色资源
     *
     * @author fengshuonan
     * @date 2022/9/17 14:33
     */
    void quickSaveAll(List<SysRoleResource> sysRoleResourceList);

    /**
     * 批量保存角色和资源的绑定
     *
     * @author fengshuonan
     * @date 2022/9/29 14:34
     */
    void batchSaveResCodes(Long roleId, List<SysRoleResourceDTO> totalResourceCode);

    /**
     * 更新本表的资源编码，替换为新的app编码
     *
     * @param decisionFirstStart 判断是否是第一次启动，参数传true，则判断必须是第一次启动才执行update操作
     * @param newAppCode         新应用编码
     * @author fengshuonan
     * @date 2022/11/16 23:37
     */
    void updateNewAppCode(Boolean decisionFirstStart, String newAppCode);

}
