/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.auth.starter.cache;

import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.auth.cache.LoginErrorCountRedisCache;
import cn.stylefeng.roses.kernel.auth.session.cache.catoken.RedisCaClientTokenCache;
import cn.stylefeng.roses.kernel.auth.session.cache.logintoken.RedisLoginTokenCache;
import cn.stylefeng.roses.kernel.auth.session.cache.loginuser.RedisLoginUserCache;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.cache.redis.util.CreateRedisTemplateUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;


/**
 * 认证和鉴权模块的自动配置
 *
 * @author fengshuonan
 * @date 2020/11/30 22:16
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.data.redis.connection.RedisConnectionFactory")
public class GunsAuthTokenRedisCacheAutoConfiguration {

    /**
     * 登录用户的缓存，默认使用内存方式
     * <p>
     * 如需redis，可在项目创建一个名为 loginUserCache 的bean替代即可
     *
     * @author fengshuonan
     * @date 2021/1/31 21:04
     */
    @Bean
    public CacheOperatorApi<LoginUser> loginUserCache(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, LoginUser> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new RedisLoginUserCache(redisTemplate);
    }

    /**
     * 登录用户token的缓存，默认使用内存方式
     * <p>
     * 如需redis，可在项目创建一个名为 allPlaceLoginTokenCache 的bean替代即可
     *
     * @author fengshuonan
     * @date 2021/1/31 21:04
     */
    @Bean
    public CacheOperatorApi<Set<String>> allPlaceLoginTokenCache(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Set<String>> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new RedisLoginTokenCache(redisTemplate);
    }

    /**
     * 登录错误次数的缓存
     *
     * @author fengshuonan
     * @date 2022/3/15 17:25
     */
    @Bean
    public CacheOperatorApi<Integer> loginErrorCountCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Integer> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new LoginErrorCountRedisCache(redisTemplate);
    }

    /**
     * CaClient单点登录token的缓存
     *
     * @author fengshuonan
     * @date 2022/5/20 11:52
     */
    @Bean
    public CacheOperatorApi<String> caClientTokenCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> redisTemplate = CreateRedisTemplateUtil.createString(redisConnectionFactory);
        return new RedisCaClientTokenCache(redisTemplate);
    }

}
