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
package cn.stylefeng.roses.kernel.system.api.pojo.organization;

import cn.stylefeng.roses.kernel.expand.modular.api.pojo.ExpandDataInfo;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.validator.api.validators.status.StatusValue;
import cn.stylefeng.roses.kernel.validator.api.validators.unique.TableUniqueValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 系统组织机构表
 *
 * @author fengshuonan
 * @date 2020/11/04 11:05
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HrOrganizationRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, delete.class, detail.class, updateStatus.class})
    @ChineseDescription("主键")
    private Long orgId;

    /**
     * 父id
     */
    @NotNull(message = "父id不能为空", groups = {add.class, edit.class})
    @ChineseDescription("父id")
    private Long orgParentId;

    /**
     * 父ids
     */
    @ChineseDescription("父ids")
    private String orgPids;

    /**
     * 组织名称
     */
    @NotBlank(message = "组织名称不能为空", groups = {add.class, edit.class})
    @ChineseDescription("组织名称")
    private String orgName;

    /**
     * 组织编码
     */
    @NotBlank(message = "组织编码不能为空", groups = {add.class, edit.class})
    @TableUniqueValue(
            message = "组织编码存在重复",
            groups = {add.class, edit.class},
            tableName = "hr_organization",
            columnName = "org_code",
            idFieldName = "org_id",
            excludeLogicDeleteItems = true)
    @ChineseDescription("组织编码")
    private String orgCode;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    @ChineseDescription("排序")
    private BigDecimal orgSort;

    /**
     * 状态：1-启用，2-禁用
     */
    @NotNull(message = "状态不能为空", groups = {updateStatus.class})
    @StatusValue(groups = updateStatus.class)
    @ChineseDescription("状态：1-启用，2-禁用")
    private Integer statusFlag;

    /**
     * 组织机构类型：1-公司，2-部门
     */
    @ChineseDescription("组织机构类型：1-公司，2-部门")
    private Integer orgType;

    /**
     * 描述
     */
    @ChineseDescription("描述")
    private String orgRemark;

    /**
     * 角色id
     */
    @NotNull(message = "角色id不能为空", groups = roleBindOrgScope.class)
    @ChineseDescription("角色id")
    private Long roleId;

    /**
     * 用户id（作为查询条件）
     */
    @NotNull(message = "用户id不能为空", groups = userBindOrgScope.class)
    @ChineseDescription("用户id（作为查询条件）")
    private Long userId;

    /**
     * 动态表单数据
     */
    @ChineseDescription("动态表单数据")
    private ExpandDataInfo expandDataInfo;

    /**
     * 组织机构树zTree形式
     */
    public @interface roleBindOrgScope {
    }

    /**
     * 查询用户的数据范围
     */
    public @interface userBindOrgScope {
    }

}
