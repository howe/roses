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
package cn.stylefeng.roses.kernel.group.api;

import cn.stylefeng.roses.kernel.group.api.pojo.SysGroupDTO;
import cn.stylefeng.roses.kernel.group.api.pojo.SysGroupRequest;

import java.util.List;

/**
 * 业务分组Api
 *
 * @author fengshuonan
 * @date 2022-06-24 17:15:41
 */
public interface GroupApi {

    /**
     * 获取当前用户某个业务，某个分类下的业务数据id
     *
     * @author fengshuonan
     * @date 2022/5/11 17:00
     */
    List<Long> findUserGroupDataList(SysGroupRequest sysGroupRequest);

    /**
     * 获取当前用户某个业务下的分组列表
     *
     * @param sysGroupRequest 请求参数，一般传递groupBizCode即可
     * @param getTotal        是否获取用户全部的数据
     * @author fengshuonan
     * @date 2022/5/11 17:00
     */
    List<SysGroupDTO> findGroupList(SysGroupRequest sysGroupRequest, boolean getTotal);

    /**
     * 清空业务id的分组
     *
     * @author fengshuonan
     * @date 2022/7/22 23:40
     */
    void removeGroup(String groupBizCode, Long bizId);

}
