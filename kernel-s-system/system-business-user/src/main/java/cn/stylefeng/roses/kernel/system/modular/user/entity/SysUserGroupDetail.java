package cn.stylefeng.roses.kernel.system.modular.user.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户组详情实例类
 *
 * @author fengshuonan
 * @date 2022/09/26 10:12
 */
@TableName("sys_user_group_detail")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserGroupDetail extends BaseEntity {

    /**
     * 详情id
     */
    @TableId(value = "detail_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("详情id")
    private Long detailId;

    /**
     * 所属用户组id
     */
    @TableField("user_group_id")
    @ChineseDescription("所属用户组id")
    private Long userGroupId;

    /**
     * 授权对象类型：1-用户，2-部门，3-角色，4-职位，5-关系，6-部门审批人
     */
    @TableField("select_type")
    @ChineseDescription("授权对象类型：1-用户，2-部门，3-角色，4-职位，5-关系，6-部门审批人")
    private Integer selectType;

    /**
     * 授权对象id值，例如：用户id，部门id
     */
    @TableField("select_value")
    @ChineseDescription("授权对象id值，例如：用户id，部门id")
    private Long selectValue;

    /**
     * 授权对象名称，例如：张三，研发部，管理员等
     */
    @TableField("select_value_name")
    @ChineseDescription("授权对象名称，例如：张三，研发部，管理员等")
    private String selectValueName;

    /**
     * 子选择对象值，目前只用在select_type为6时，代表审批人类型
     */
    @TableField("sub_select_value")
    @ChineseDescription("子选择对象值，目前只用在select_type为6时，代表审批人类型")
    private String subSelectValue;

    /**
     * 子选择对象值的名称，目前只用在select_type为6时，代表审批人类型
     */
    @TableField("sub_select_value_name")
    @ChineseDescription("子选择对象值的名称，目前只用在select_type为6时，代表审批人类型")
    private String subSelectValueName;

}
