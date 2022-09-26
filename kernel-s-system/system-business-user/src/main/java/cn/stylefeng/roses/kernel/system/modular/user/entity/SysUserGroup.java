package cn.stylefeng.roses.kernel.system.modular.user.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.system.api.pojo.usergroup.SelectItem;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 用户组实例类
 *
 * @author fengshuonan
 * @date 2022/09/26 10:12
 */
@TableName("sys_user_group")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserGroup extends BaseEntity {

    /**
     * 用户组id
     */
    @TableId(value = "user_group_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("用户组id")
    private Long userGroupId;

    /**
     * 用户分组标题简称
     */
    @TableField("user_group_title")
    @ChineseDescription("用户分组标题简称")
    private String userGroupTitle;

    /**
     * 组内选择项的合并
     */
    @TableField("user_group_detail_name")
    @ChineseDescription("组内选择项的合并")
    private String userGroupDetailName;

    /**
     * 选中的用户列表
     */
    @TableField(exist = false)
    @ChineseDescription("选中的用户列表")
    private List<SelectItem> selectUserList;

    /**
     * 选中的组织机构列表
     */
    @TableField(exist = false)
    @ChineseDescription("选中的组织机构列表")
    private List<SelectItem> selectOrgList;

    /**
     * 选中的角色列表
     */
    @TableField(exist = false)
    @ChineseDescription("选中的角色列表")
    private List<SelectItem> selectRoleList;

    /**
     * 选中的职位列表
     */
    @TableField(exist = false)
    @ChineseDescription("选中的职位列表")
    private List<SelectItem> selectPositionList;

    /**
     * 选中的关系列表
     */
    @TableField(exist = false)
    @ChineseDescription("选中的关系列表")
    private List<SelectItem> selectRelationList;

    /**
     * 选中的部门审批人类型列表
     */
    @TableField(exist = false)
    @ChineseDescription("选中的部门审批人类型列表")
    private List<SelectItem> selectOrgApproverTypeList;

}
