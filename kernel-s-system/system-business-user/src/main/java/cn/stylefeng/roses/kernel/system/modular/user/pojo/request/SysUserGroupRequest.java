package cn.stylefeng.roses.kernel.system.modular.user.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.system.api.pojo.usergroup.SelectItem;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户组封装类
 *
 * @author fengshuonan
 * @date 2022/09/26 10:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserGroupRequest extends BaseRequest {

    /**
     * 用户组id
     */
    @NotNull(message = "用户组id不能为空", groups = {detail.class})
    @ChineseDescription("用户组id")
    private Long userGroupId;

    /**
     * 用户分组标题简称
     */
    @ChineseDescription("用户分组标题简称")
    private String userGroupTitle;

    /**
     * 组内选择项的合并
     */
    @ChineseDescription("组内选择项的合并")
    private String userGroupDetailName;

    /**
     * 选中的用户列表
     */
    @ChineseDescription("选中的用户列表")
    private List<SelectItem> selectUserList;

    /**
     * 选中的组织机构列表
     */
    @ChineseDescription("选中的组织机构列表")
    private List<SelectItem> selectOrgList;

    /**
     * 选中的角色列表
     */
    @ChineseDescription("选中的角色列表")
    private List<SelectItem> selectRoleList;

    /**
     * 选中的职位列表
     */
    @ChineseDescription("选中的职位列表")
    private List<SelectItem> selectPositionList;

    /**
     * 选中的关系列表
     */
    @ChineseDescription("选中的关系列表")
    private List<SelectItem> selectRelationList;

    /**
     * 选中的部门审批人类型列表
     */
    @ChineseDescription("选中的部门审批人类型列表")
    private List<SelectItem> selectOrgApproverTypeList;

}
