package cn.stylefeng.roses.kernel.system.api.pojo.user;

import cn.stylefeng.roses.kernel.rule.enums.SexEnum;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 第三方OAuth2用户信息
 *
 * @author fengshuonan
 * @date 2022/7/1 18:05
 */
@Data
public class OAuth2AuthUserDTO implements Serializable {
    /**
     * 用户第三方系统的唯一id。在调用方集成该组件时，可以用uuid + source唯一确定一个用户
     */
    private String uuid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 用户备注（各平台中的用户个人介绍）
     */
    private String remark;

    /**
     * 性别
     */
    private SexEnum sexEnum;

    /**
     * 用户来源
     */
    private String source;

    /**
     * 第三方平台返回的原始用户信息
     */
    private JSONObject rawUserInfo;

}
