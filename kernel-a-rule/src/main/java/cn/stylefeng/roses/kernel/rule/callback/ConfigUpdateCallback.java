package cn.stylefeng.roses.kernel.rule.callback;

/**
 * 系统配置修改的回调事件
 *
 * @author fengshuonan
 * @date 2022/10/22 21:35
 */
public interface ConfigUpdateCallback {

    /**
     * 系统配置修改的回调事件
     *
     * @param code  系统配置的编码，例如：SYS_SERVER_DEPLOY_HOST
     * @param value 新修改的配置值
     * @author fengshuonan
     * @date 2022/10/22 21:36
     */
    void configUpdate(String code, String value);

}
