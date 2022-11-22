package cn.stylefeng.roses.kernel.cache.api.tenant;

import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.rule.tenant.OnceTenantCodeHolder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 租户缓存动态代理
 *
 * @author fengshuonan
 * @date 2022/11/8 0:45
 */
@SuppressWarnings("all")
public class TenantCacheProxy implements InvocationHandler {

    /**
     * 被代理的缓存操作类
     */
    private CacheOperatorApi<?> targetCacheObject = null;

    /**
     * 租户编码
     */
    private String tenantCode = null;

    /**
     * 绑定缓存操作原始类
     *
     * @param tenantCode 租户编码
     * @param target     被代理的缓存类
     * @author fengshuonan
     * @date 2022/11/8 1:05
     */
    public <T> CacheOperatorApi<T> bindCacheObject(String tenantCode, CacheOperatorApi<T> target) {
        this.targetCacheObject = target;
        this.tenantCode = tenantCode;
        return (CacheOperatorApi<T>) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getSuperclass().getInterfaces(), this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try {
            // 设置租户编码
            OnceTenantCodeHolder.setTenantCode(tenantCode);

            // 执行原有缓存操作类
            result = method.invoke(targetCacheObject, args);
        } finally {
            // 清除租户编码
            OnceTenantCodeHolder.clearTenantCode();
        }
        return result;
    }

}
