package cn.stylefeng.roses.kernel.system.modular.user.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * 权限分组详情封装类
 *
 * @author fengshuonan
 * @date 2022/09/25 22:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysUserGroupDetailRequest extends BaseRequest {

    /**
     * 详情id
     */
    @NotNull(message = "详情id不能为空", groups = {edit.class, delete.class})
    @ChineseDescription("详情id")
    private Long detailId;

    /**
     * 所属用户组id
     */
    @ChineseDescription("所属用户组id")
    private Long userGroupId;

    /**
     * 授权对象类型：1-用户，2-部门，3-角色，4-职位，5-关系
     */
    @ChineseDescription("授权对象类型：1-用户，2-部门，3-角色，4-职位，5-关系")
    private Integer selectType;

    /**
     * 授权对象id值，例如：用户id，部门id
     */
    @ChineseDescription("授权对象id值，例如：用户id，部门id")
    private Long selectValue;

    /**
     * 授权对象名称，例如：张三，研发部，管理员等
     */
    @ChineseDescription("授权对象名称，例如：张三，研发部，管理员等")
    private String selectValueName;

}
