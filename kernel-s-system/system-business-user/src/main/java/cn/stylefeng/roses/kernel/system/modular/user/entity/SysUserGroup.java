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
 * 权限分组实例类
 *
 * @author fengshuonan
 * @date 2022/09/25 22:11
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

}
