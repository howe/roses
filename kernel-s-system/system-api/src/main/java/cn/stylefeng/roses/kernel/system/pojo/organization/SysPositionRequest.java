package cn.stylefeng.roses.kernel.system.pojo.organization;

import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import cn.stylefeng.roses.kernel.validator.validators.status.StatusValue;
import cn.stylefeng.roses.kernel.validator.validators.unique.TableUniqueValue;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 系统职位表
 *
 * @author fengshuonan
 * @date 2020/11/04 11:07
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPositionRequest extends BaseRequest {

    /**
     * 主键
     */
    @NotNull(message = "主键不能为空", groups = {edit.class, detail.class, delete.class})
    private Long id;

    /**
     * 职位名称
     */
    @NotBlank(message = "职位名称不能为空", groups = {add.class, edit.class})
    private String name;

    /**
     * 职位编码
     */
    @NotBlank(message = "职位编码不能为空", groups = {add.class, edit.class})
    @TableUniqueValue(
            message = "职位编码存在重复，请检查code参数",
            groups = {add.class, edit.class},
            tableName = "sys_position",
            columnName = "code")
    private String code;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空", groups = {add.class, edit.class})
    private BigDecimal sort;

    /**
     * 状态（1-启用，2-禁用）
     */
    @NotNull(message = "状态（1-启用，2-禁用）不能为空", groups = {updateStatus.class})
    @StatusValue(groups = updateStatus.class)
    private Integer statusFlag;

    /**
     * 备注
     */
    private String remark;

}
