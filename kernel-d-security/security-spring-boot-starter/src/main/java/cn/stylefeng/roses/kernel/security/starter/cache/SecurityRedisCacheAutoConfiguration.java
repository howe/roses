package cn.stylefeng.roses.kernel.security.starter.cache;


import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.cache.redis.util.CreateRedisTemplateUtil;
import cn.stylefeng.roses.kernel.security.blackwhite.cache.BlackListRedisCache;
import cn.stylefeng.roses.kernel.security.blackwhite.cache.WhiteListRedisCache;
import cn.stylefeng.roses.kernel.security.captcha.cache.CaptchaRedisCache;
import cn.stylefeng.roses.kernel.security.count.cache.CountValidateRedisCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
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
@ConditionalOnClass(name = "org.springframework.data.redis.connection.RedisConnectionFactory")
public class SecurityRedisCacheAutoConfiguration {

    /**
     * 验证码相关的缓存，Redis缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 20:44
     */
    @Bean("captchaCache")
    public CacheOperatorApi<String> captchaRedisCache(RedisConnectionFactory redisConnectionFactory) {
        // 验证码过期时间 120秒
        RedisTemplate<String, String> redisTemplate = CreateRedisTemplateUtil.createString(redisConnectionFactory);
        return new CaptchaRedisCache(redisTemplate);
    }

    /**
     * 黑名单的缓存，Redis缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 21:24
     */
    @Bean("blackListCache")
    public CacheOperatorApi<String> blackListRedisCache(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = CreateRedisTemplateUtil.createString(redisConnectionFactory);
        return new BlackListRedisCache(redisTemplate);
    }

    /**
     * 白名单的缓存，Redis缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 21:24
     */
    @Bean("whiteListCache")
    public CacheOperatorApi<String> whiteListRedisCache(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = CreateRedisTemplateUtil.createString(redisConnectionFactory);
        return new WhiteListRedisCache(redisTemplate);
    }

    /**
     * 计数缓存，Redis缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 21:24
     */
    @Bean("countValidateCache")
    public CacheOperatorApi<Long> countValidateRedisCache(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Long> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new CountValidateRedisCache(redisTemplate);
    }

}
