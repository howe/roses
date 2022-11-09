package cn.stylefeng.roses.kernel.rule.tenant;

/**
 * 租户编码信息暂存，一般给缓存使用
 * <p>
 * 在一次Http请求中生效，一般用在登录接口，当前Context没有LoginUser时使用
 *
 * @author fengshuonan
 * @date 2022/11/8 0:58
 */
public class RequestTenantCodeHolder {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    /**
     * 设置租户编码
     *
     * @author fengshuonan
     * @date 2022/11/8 0:59
     */
    public static void setTenantCode(String aesKey) {
        CONTEXT_HOLDER.set(aesKey);
    }

    /**
     * 获取租户编码
     *
     * @author fengshuonan
     * @date 2022/11/8 0:59
     */
    public static String getTenantCode() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 清除租户编码
     *
     * @author fengshuonan
     * @date 2022/11/8 0:59
     */
    public static void clearTenantCode() {
        CONTEXT_HOLDER.remove();
    }

}
