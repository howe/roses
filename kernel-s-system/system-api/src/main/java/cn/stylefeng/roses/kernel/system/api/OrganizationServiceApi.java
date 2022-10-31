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
package cn.stylefeng.roses.kernel.system.api;

import cn.stylefeng.roses.kernel.system.api.enums.DetectModeEnum;
import cn.stylefeng.roses.kernel.system.api.pojo.organization.HrOrganizationDTO;
import cn.stylefeng.roses.kernel.system.api.pojo.organization.HrOrganizationRequest;
import cn.stylefeng.roses.kernel.system.api.pojo.organization.OrganizationTreeNode;

import java.util.List;

/**
 * 组织机构api
 *
 * @author liuhanqing
 * @date 2021/1/15 10:40
 */
public interface OrganizationServiceApi {

    /**
     * 查询系统组织机构
     *
     * @return 组织机构列表
     * @author liuhanqing
     * @date 2021/1/15 10:41
     */
    List<HrOrganizationDTO> orgList();

    /**
     * 获取组织机构详情
     *
     * @author yexing
     * @date 2022/3/8 23:32
     */
    HrOrganizationDTO getOrgDetail(Long orgId);

    /**
     * 批量获取组织机构信息详情
     *
     * @author fengshuonan
     * @date 2022/10/31 20:02
     */
    List<HrOrganizationDTO> getOrgDetailList(List<Long> orgIdList);

    /**
     * 获取组织机构下拉选择树
     *
     * @author fengshuonan
     * @date 2022/6/8 14:40
     */
    List<OrganizationTreeNode> getOrgTreeList(HrOrganizationRequest hrOrganizationRequest);

    /**
     * 获取某个公司下，所有部门树的集合
     *
     * @author fengshuonan
     * @date 2022/6/16 18:26
     */
    List<OrganizationTreeNode> getDeptOrgTree(Long orgId);

    /**
     * 获取指定组织机构的上级组织机构是什么
     * <p>
     * 自下而上：逐级向上获取直到获取到最高级
     * 自上而下：逐级向下获取，直到获取到本层机构
     *
     * @param orgId          指定机构id
     * @param parentLevelNum 上级机构的层级数，从0开始，0代表直接返回本部门
     * @param detectModeEnum 自上而下还是自下而上
     * @return 上级机构的id
     * @author fengshuonan
     * @date 2022/9/18 15:02
     */
    Long getParentLevelOrgId(Long orgId, Integer parentLevelNum, DetectModeEnum detectModeEnum);

}
