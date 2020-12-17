package cn.stylefeng.roses.kernel.auth.api.exception.enums;

import cn.stylefeng.roses.kernel.auth.api.constants.AuthConstants;
import cn.stylefeng.roses.kernel.rule.abstracts.AbstractExceptionEnum;
import cn.stylefeng.roses.kernel.rule.constants.RuleConstants;
import lombok.Getter;

/**
 * 认证相关异常
 *
 * @author fengshuonan
 * @date 2020/10/16 10:53
 */
@Getter
public enum AuthExceptionEnum implements AbstractExceptionEnum {

    /**
     * 认证异常
     */
    AUTH_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + AuthConstants.AUTH_EXCEPTION_STEP_CODE + "01", "认证失败，请检查您的登录是否过期"),

    /**
     * 登陆时，账号或密码为空
     */
    PARAM_EMPTY(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + AuthConstants.AUTH_EXCEPTION_STEP_CODE + "02", "登陆失败，账号或密码参数为空"),

    /**
     * 账号或密码错误
     */
    USERNAME_PASSWORD_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + AuthConstants.AUTH_EXCEPTION_STEP_CODE + "03", "账号或密码错误"),

    /**
     * 用户状态异常，可能被禁用可能被冻结，用StrUtil.format()格式化
     */
    USER_STATUS_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + AuthConstants.AUTH_EXCEPTION_STEP_CODE + "04", "当前用户被{}，请检查用户状态是否正常"),

    /**
     * 登陆失败，账号参数为空
     */
    ACCOUNT_IS_BLANK(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + AuthConstants.AUTH_EXCEPTION_STEP_CODE + "05", "登陆失败，账号参数为空"),

    /**
     * 获取token失败
     */
    TOKEN_GET_ERROR(RuleConstants.USER_OPERATION_ERROR_TYPE_CODE + AuthConstants.AUTH_EXCEPTION_STEP_CODE + "06", "获取token失败，请检查header和param中是否传递了用户token"),

    /**
     * 获取资源为空
     */
    RESOURCE_DEFINITION_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + AuthConstants.AUTH_EXCEPTION_STEP_CODE + "07", "获取资源为空，请检查当前请求url是否存在对应的ResourceDefinition"),

    /**
     * 获取不到token对应的用户信息，请检查登录是否过期
     */
    TOKEN_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + AuthConstants.AUTH_EXCEPTION_STEP_CODE + "08", "获取不到token对应的用户信息，请检查登录是否过期"),

    /**
     * 权限校验失败，请检查用户是否有该资源的权限
     */
    PERMISSION_RES_VALIDATE_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + AuthConstants.AUTH_EXCEPTION_STEP_CODE + "09", "权限校验失败，请检查用户是否有该资源的权限"),

    /**
     * 数据范围类型转化异常
     */
    DATA_SCOPE_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + AuthConstants.AUTH_EXCEPTION_STEP_CODE + "10", "数据范围类型转化异常，数据范围类型为：{}"),

    /**
     * 权限校验失败，只有超级管理员可以授权所有数据
     */
    ONLY_SUPER_ERROR(RuleConstants.BUSINESS_ERROR_TYPE_CODE + AuthConstants.AUTH_EXCEPTION_STEP_CODE + "11", "权限校验失败，只有超级管理员可以授权所有数据");

    /**
     * 错误编码
     */
    private final String errorCode;

    /**
     * 提示用户信息
     */
    private final String userTip;

    AuthExceptionEnum(String errorCode, String userTip) {
        this.errorCode = errorCode;
        this.userTip = userTip;
    }

}