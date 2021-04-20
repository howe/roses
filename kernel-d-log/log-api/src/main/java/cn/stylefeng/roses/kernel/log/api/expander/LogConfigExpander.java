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
package cn.stylefeng.roses.kernel.log.api.expander;

import cn.stylefeng.roses.kernel.config.api.context.ConfigContext;
import cn.stylefeng.roses.kernel.log.api.constants.LogFileConstants;

/**
 * 日志记录相关的配置
 *
 * @author fengshuonan
 * @date 2020/10/28 16:11
 */
public class LogConfigExpander {

    /**
     * 获取日志记录的文件存储的位置（windows服务器）
     * <p>
     * 末尾不带斜杠
     *
     * @author fengshuonan
     * @date 2020/10/28 16:14
     */
    public static String getLogFileSavePathWindows() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_LOG_FILE_SAVE_PATH_WINDOWS", String.class, LogFileConstants.DEFAULT_FILE_SAVE_PATH_WINDOWS);
    }

    /**
     * 获取日志记录的文件存储的位置（linux和mac服务器）
     * <p>
     * 末尾不带斜杠
     *
     * @author fengshuonan
     * @date 2020/10/28 16:14
     */
    public static String getLogFileSavePathLinux() {
        return ConfigContext.me().getSysConfigValueWithDefault("SYS_LOG_FILE_SAVE_PATH_LINUX", String.class, LogFileConstants.DEFAULT_FILE_SAVE_PATH_LINUX);
    }

}
