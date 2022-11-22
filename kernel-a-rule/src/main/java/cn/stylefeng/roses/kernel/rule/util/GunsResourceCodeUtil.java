package cn.stylefeng.roses.kernel.rule.util;

import cn.hutool.core.util.StrUtil;

/**
 * Guns资源缓存前缀标识替换工具
 * <p>
 * Guns资源编码为固定的guns$开头，如果项目编码修改后，应将资源标识前缀进行修改
 *
 * @author fengshuonan
 * @date 2022/11/16 23:07
 */
public class GunsResourceCodeUtil {

    /**
     * Guns默认的资源前缀标识
     */
    public static final String GUNS_RES_PREFIX = "guns\\$";

    /**
     * 将参数的资源编码，改为携带新的应用编码的资源编码
     * <p>
     * 例如之前是：guns$sys_notice$add
     * <p>
     * 修改之后为：{newAppCode参数}$sys_notice$add
     *
     * @author fengshuonan
     * @date 2022/11/16 23:09
     */
    public static String replace(String resourceCode, String newAppCode) {

        // 前缀为空则直接返回空串
        if (StrUtil.isEmpty(resourceCode)) {
            return "";
        }

        // 计算出新的前缀
        String newPrefix = newAppCode + "\\$";

        // 如果资源编码已经是新的前缀则直接返回
        if (resourceCode.startsWith(newPrefix)) {
            return resourceCode;
        }

        return resourceCode.replaceFirst(GUNS_RES_PREFIX, newPrefix);
    }

}
