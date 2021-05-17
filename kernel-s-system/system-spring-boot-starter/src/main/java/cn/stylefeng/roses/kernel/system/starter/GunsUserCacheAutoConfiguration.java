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
package cn.stylefeng.roses.kernel.system.starter;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.stylefeng.roses.kernel.cache.api.CacheOperatorApi;
import cn.stylefeng.roses.kernel.system.api.constants.SystemConstants;
import cn.stylefeng.roses.kernel.system.api.pojo.user.SysUserDTO;
import cn.stylefeng.roses.kernel.system.modular.user.cache.SysUserMemoryCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 用户缓存的自动配置
 *
 * @author fengshuonan
 * @date 2021/2/28 10:29
 */
@Configuration
public class GunsUserCacheAutoConfiguration {

    /**
     * 用户的缓存，非在线用户缓存，此缓存为了加快查看用户相关操作
     *
     * @author fengshuonan
     * @date 2021/2/28 10:30
     */
    @Bean
    @ConditionalOnMissingBean(name = "sysUserCacheOperatorApi")
    public CacheOperatorApi<SysUserDTO> sysUserCacheOperatorApi() {
        TimedCache<String, SysUserDTO> sysUserTimedCache = CacheUtil.newTimedCache(SystemConstants.USER_CACHE_TIMEOUT_SECONDS * 1000);
        return new SysUserMemoryCache(sysUserTimedCache);
    }

}