package cn.stylefeng.roses.kernel.cache.api.tenant;

import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;

import java.util.HashMap;
import java.util.Map;

/**
 * 租户缓存动态代理
 *
 * @author fengshuonan
 * @date 2022/11/8 0:45
 */
@SuppressWarnings("all")
public class TenantCacheProxyFactory {

    /**
     * 存放所有代理的缓存
     */
    private static final Map<String, CacheOperatorApi<?>> proxyCacheMap = new HashMap<>();

    /**
     * 创建带租户控制的缓存代理
     *
     * @author fengshuonan
     * @date 2022/11/8 1:18
     */
    public static <T> CacheOperatorApi<T> createTenantCacheProxy(String tenantCode, CacheOperatorApi<?> cacheOperatorApi) {

        // 计算缓存的key
        String key = tenantCode + cacheOperatorApi.getCommonKeyPrefix();

        // 直接获取缓存中的代理
        if (proxyCacheMap.containsKey(key)) {
            return (CacheOperatorApi<T>) proxyCacheMap.get(key);
        }

        TenantCacheProxy tenantCacheProxy = new TenantCacheProxy();
        CacheOperatorApi<?> resultCacheOperator = tenantCacheProxy.bindCacheObject(tenantCode, cacheOperatorApi);
        proxyCacheMap.put(key, resultCacheOperator);
        return (CacheOperatorApi<T>) resultCacheOperator;
    }

}
