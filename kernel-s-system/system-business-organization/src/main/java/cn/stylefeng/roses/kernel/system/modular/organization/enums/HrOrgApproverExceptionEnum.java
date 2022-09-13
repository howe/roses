package cn.stylefeng.roses.kernel.system.modular.organization.enums;

import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import cn.stylefeng.roses.kernel.rule.exception.AbstractExceptionEnum;
import lombok.Getter;

/**
 * 组织机构审批人异常相关枚举
 *
 * @author fengshuonan
 * @date 2022/09/13 23:15
 */
@Getter
public enum HrOrgApproverExceptionEnum implements AbstractExceptionEnum {

    /**
     * 查询结果不存在
     */
    HR_ORG_APPROVER_NOT_EXISTED(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE +  "10001", "查询结果不存在");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    HrOrgApproverExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}