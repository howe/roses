package cn.stylefeng.roses.kernel.security.starter.cache;


import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.cache.api.constants.CacheConstants;
import cn.stylefeng.roses.kernel.security.blackwhite.cache.BlackListMemoryCache;
import cn.stylefeng.roses.kernel.security.blackwhite.cache.WhiteListMemoryCache;
import cn.stylefeng.roses.kernel.security.captcha.cache.CaptchaMemoryCache;
import cn.stylefeng.roses.kernel.security.count.cache.CountValidateMemoryCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 安全模块，缓存的依赖
 *
 * @author fengshuonan
 * @date 2022/11/8 9:57
 */
@Configuration
@ConditionalOnMissingClass("org.springframework.data.redis.connection.RedisConnectionFactory")
public class SecurityMemoryCacheAutoConfiguration {

    /**
     * 验证码相关的缓存，内存缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 20:44
     */
    @Bean("captchaCache")
    public CacheOperatorApi<String> captchaMemoryCache() {
        // 验证码过期时间 120秒
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(1000 * 120);
        return new CaptchaMemoryCache(timedCache);
    }

    /**
     * 黑名单的缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 21:24
     */
    @Bean("blackListCache")
    public CacheOperatorApi<String> blackListMemoryCache() {
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(CacheConstants.NONE_EXPIRED_TIME);
        return new BlackListMemoryCache(timedCache);
    }

    /**
     * 白名单的缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 21:24
     */
    @Bean("whiteListCache")
    public CacheOperatorApi<String> whiteListMemoryCache() {
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(CacheConstants.NONE_EXPIRED_TIME);
        return new WhiteListMemoryCache(timedCache);
    }

    /**
     * 计数缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 21:24
     */
    @Bean("countValidateCache")
    public CacheOperatorApi<Long> countValidateMemoryCache() {
        TimedCache<String, Long> timedCache = CacheUtil.newTimedCache(CacheConstants.NONE_EXPIRED_TIME);
        return new CountValidateMemoryCache(timedCache);
    }

}
