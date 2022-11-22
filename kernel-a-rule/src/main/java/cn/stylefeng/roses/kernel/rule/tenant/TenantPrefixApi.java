package cn.stylefeng.roses.kernel.rule.tenant;

/**
 * 租户前缀获取
 *
 * @author fengshuonan
 * @date 2022/11/7 20:16
 */
public interface TenantPrefixApi {

    /**
     * 获取当前用户的租户前缀
     *
     * @author fengshuonan
     * @date 2022/11/7 20:29
     */
    String getTenantPrefix();

}
