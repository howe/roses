package cn.stylefeng.roses.kernel.system.modular.user.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

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
    @NotNull(message = "用户组id不能为空", groups = {edit.class, delete.class})
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

}
