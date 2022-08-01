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
package cn.stylefeng.roses.kernel.rule.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Token签名工具
 *
 * @author fengshuonan
 * @date 2022/8/1 17:41
 */
@Slf4j
public class TokenSignUtil {

    /**
     * 根据时间戳和秘钥生成md5方式的签名字符串
     *
     * @param timestamp 生成sign时的时间戳
     * @param secretKey 生成时间戳需要的秘钥
     * @author fengshuonan
     * @date 2022/8/1 17:43
     */
    public static String createSignStr(Long timestamp, String secretKey) {
        if (ObjectUtil.isEmpty(timestamp) || ObjectUtil.isEmpty(secretKey)) {
            return null;
        } else {
            return SecureUtil.md5(timestamp + secretKey);
        }
    }

    /**
     * 验证签名是否过期
     *
     * @param timestamp      生成签名时候的时间戳
     * @param secretKey      秘钥
     * @param signStr        签名字符串
     * @param expiredSeconds 签名过期时间
     * @author fengshuonan
     * @date 2022/8/1 17:48
     */
    public static boolean validateSignStr(Long timestamp, String secretKey, String signStr, Integer expiredSeconds) {

        if (ObjectUtil.isEmpty(timestamp) || ObjectUtil.isEmpty(secretKey) || ObjectUtil.isEmpty(signStr) || ObjectUtil.isEmpty(expiredSeconds)) {
            return false;
        }

        long betweenSeconds = (System.currentTimeMillis() - timestamp) / 1000;

        // 如果当前时间和时间戳时间相隔太长，则返回校验失败，过期
        if (betweenSeconds > expiredSeconds) {
            return false;
        }

        // 正确的签名
        String signRight = TokenSignUtil.createSignStr(timestamp, secretKey);

        // 比对签名
        if (signRight.equals(signStr)) {
            return true;
        }

        return false;
    }

}
