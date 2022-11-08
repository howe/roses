package cn.stylefeng.roses.kernel.security.starter;


import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.cache.redis.util.CreateRedisTemplateUtil;
import cn.stylefeng.roses.kernel.security.captcha.cache.CaptchaMemoryCache;
import cn.stylefeng.roses.kernel.security.captcha.cache.CaptchaRedisCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 安全模块，缓存的依赖
 *
 * @author fengshuonan
 * @date 2022/11/8 9:57
 */
@Configuration
public class SecurityCacheAutoConfiguration {

    /**
     * 验证码相关的缓存，内存缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 20:44
     */
    @Bean("captchaCache")
    @ConditionalOnMissingClass("org.springframework.data.redis.core.RedisTemplate")
    public CacheOperatorApi<String> captchaMemoryCache() {
        // 验证码过期时间 120秒
        TimedCache<String, String> timedCache = CacheUtil.newTimedCache(1000 * 120);
        return new CaptchaMemoryCache(timedCache);
    }

    /**
     * 验证码相关的缓存，Redis缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 20:44
     */
    @Bean("captchaCache")
    @ConditionalOnClass(name = "org.springframework.data.redis.core.RedisTemplate")
    public CacheOperatorApi<String> captchaRedisCache(RedisConnectionFactory redisConnectionFactory) {
        // 验证码过期时间 120秒
        RedisTemplate<String, String> redisTemplate = CreateRedisTemplateUtil.createString(redisConnectionFactory);
        return new CaptchaRedisCache(redisTemplate);
    }

}
