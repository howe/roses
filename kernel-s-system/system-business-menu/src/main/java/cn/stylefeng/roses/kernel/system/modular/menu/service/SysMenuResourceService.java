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
package cn.stylefeng.roses.kernel.system.modular.menu.service;

import cn.stylefeng.roses.kernel.system.api.pojo.menu.SysMenuResourceRequest;
import cn.stylefeng.roses.kernel.system.modular.menu.entity.SysMenuResource;
import cn.stylefeng.roses.kernel.system.modular.resource.pojo.ResourceTreeNode;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 菜单资源信息
 *
 * @author fengshuonan
 * @date 2021/8/8 21:38
 */
public interface SysMenuResourceService extends IService<SysMenuResource> {

    /**
     * 获取菜单或菜单按钮绑定资源的树
     *
     * @param businessId 业务id，菜单或按钮id
     * @return 资源树列表
     * @author fengshuonan
     * @date 2021/8/8 21:56
     */
    List<ResourceTreeNode> getMenuResourceTree(Long businessId);

    /**
     * 添加菜单和资源的绑定
     *
     * @author fengshuonan
     * @date 2021/8/10 13:58
     */
    void addMenuResourceBind(SysMenuResourceRequest sysMenuResourceRequest);

    /**
     * 更新本表的所有资源编码，改为新的应用code前缀
     *
     * @param decisionFirstStart 判断是否是第一次启动，参数传true，则判断必须是第一次启动才执行update操作
     * @param newAppCode         新应用编码
     * @author fengshuonan
     * @date 2022/11/16 23:13
     */
    void updateNewAppCode(Boolean decisionFirstStart, String newAppCode);

}
