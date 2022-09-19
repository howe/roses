package cn.stylefeng.roses.kernel.system.modular.organization.pojo.request;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.rule.pojo.request.BaseRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 组织机构审批人封装类
 *
 * @author fengshuonan
 * @date 2022/09/13 23:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class HrOrgApproverRequest extends BaseRequest {

    /**
     * 主键id
     */
    @ChineseDescription("主键id")
    private Long orgApproverId;

    /**
     * 组织审批类型：1-负责人，2-部长，3-体系负责人，4-部门助理，5-资产助理（专员），6-考勤专员，7-HRBP，8-门禁员，9-办公账号员，10-转岗须知员
     */
    @ChineseDescription("组织审批类型：1-负责人，2-部长，3-体系负责人，4-部门助理，5-资产助理（专员），6-考勤专员，7-HRBP，8-门禁员，9-办公账号员，10-转岗须知员")
    @NotNull(message = "组织审批类型不能为空", groups = {delete.class, add.class, getAssignOrgApprover.class})
    private Integer orgApproverType;

    /**
     * 组织机构id
     */
    @ChineseDescription("组织机构id")
    @NotNull(message = "组织机构id不能为空", groups = {list.class, add.class, delete.class})
    private Long orgId;

    /**
     * 用户id
     */
    @ChineseDescription("用户id")
    @NotNull(message = "用户id不能为空", groups = {delete.class})
    private Long userId;

    /**
     * 用户id集合，一般用在绑定多个用户
     */
    @ChineseDescription("用户id集合")
    @NotEmpty(message = "用户id集合不能为空", groups = {add.class})
    private List<Long> userIdList;


    /**
     * 上级负责人的级别，从0开始，0为同部门领导，1为上一级领导，以此类推
     */
    @ChineseDescription("上级负责人的级别，从0开始，0为同部门领导，1为上一级领导，以此类推")
    @NotNull(message = "上级负责人的级别不能为空，从0开始", groups = {getAssignOrgApprover.class})
    private Integer parentLevel;

    /**
     * 参数校验分组：获取指定部门的负责人
     */
    public @interface getAssignOrgApprover {
    }

}
