package cn.stylefeng.roses.kernel.group.modular.entity;

import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务分组实例类
 *
 * @author fengshuonan
 * @date 2022/05/11 12:54
 */
@TableName("sys_group")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysGroup extends BaseEntity {

    /**
     * 分组id
     */
    @TableId(value = "group_id", type = IdType.ASSIGN_ID)
    @ChineseDescription("分组id")
    private Long groupId;

    /**
     * 所属业务编码
     */
    @TableField("group_biz_code")
    @ChineseDescription("所属业务编码")
    private String groupBizCode;

    /**
     * 分组名称
     */
    @TableField("group_name")
    @ChineseDescription("分组名称")
    private String groupName;

    /**
     * 业务主键id
     */
    @TableField("business_id")
    @ChineseDescription("业务主键id")
    private Long businessId;

    /**
     * 用户id
     */
    @TableField("user_id")
    @ChineseDescription("用户id")
    private Long userId;

}
