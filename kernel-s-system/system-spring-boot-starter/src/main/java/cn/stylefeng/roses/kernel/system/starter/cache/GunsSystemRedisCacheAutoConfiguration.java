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
package cn.stylefeng.roses.kernel.system.starter.cache;

import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.cache.redis.util.CreateRedisTemplateUtil;
import cn.stylefeng.roses.kernel.system.api.pojo.user.SysUserDTO;
import cn.stylefeng.roses.kernel.system.api.pojo.user.SysUserOrgDTO;
import cn.stylefeng.roses.kernel.system.modular.home.cache.InterfaceStatisticsRedisCache;
import cn.stylefeng.roses.kernel.system.modular.role.cache.RoleDataScopeRedisCache;
import cn.stylefeng.roses.kernel.system.modular.role.cache.RoleRedisCache;
import cn.stylefeng.roses.kernel.system.modular.role.cache.RoleResourceRedisCache;
import cn.stylefeng.roses.kernel.system.modular.role.entity.SysRole;
import cn.stylefeng.roses.kernel.system.modular.theme.cache.ThemeRedisCache;
import cn.stylefeng.roses.kernel.system.modular.theme.pojo.DefaultTheme;
import cn.stylefeng.roses.kernel.system.modular.user.cache.SysUserRedisCache;
import cn.stylefeng.roses.kernel.system.modular.user.cache.UserOrgRedisCache;
import cn.stylefeng.roses.kernel.system.modular.user.cache.UserRoleRedisCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;

/**
 * 系统管理缓存的自动配置，Redis配置
 *
 * @author fengshuonan
 * @date 2022/11/8 23:26
 */
@Configuration
@ConditionalOnClass(name = "org.springframework.data.redis.connection.RedisConnectionFactory")
public class GunsSystemRedisCacheAutoConfiguration {

    /**
     * 用户的缓存，非在线用户缓存，此缓存为了加快查看用户相关操作
     *
     * @author fengshuonan
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<SysUserDTO> sysUserCacheOperatorApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, SysUserDTO> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new SysUserRedisCache(redisTemplate);
    }

    /**
     * 用户角色对应的缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<List<Long>> userRoleCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<Long>> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new UserRoleRedisCache(redisTemplate);
    }

    /**
     * 角色信息对应的缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<SysRole> roleInfoCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, SysRole> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new RoleRedisCache(redisTemplate);
    }

    /**
     * 用户组织机构的缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<SysUserOrgDTO> userOrgCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, SysUserOrgDTO> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new UserOrgRedisCache(redisTemplate);
    }

    /**
     * 用户资源绑定的缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<List<String>> roleResourceCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<String>> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new RoleResourceRedisCache(redisTemplate);
    }

    /**
     * 角色绑定的数据范围的缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<List<Long>> roleDataScopeCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, List<Long>> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new RoleDataScopeRedisCache(redisTemplate);
    }

    /**
     * 主题的缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<DefaultTheme> themeCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, DefaultTheme> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new ThemeRedisCache(redisTemplate);
    }

    /**
     * 接口统计的缓存
     *
     * @author fengshuonan
     * @date 2022/11/8 23:32
     */
    @Bean
    public CacheOperatorApi<Map<Long, Integer>> requestCountCacheApi(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Map<Long, Integer>> redisTemplate = CreateRedisTemplateUtil.createObject(redisConnectionFactory);
        return new InterfaceStatisticsRedisCache(redisTemplate);
    }

}
