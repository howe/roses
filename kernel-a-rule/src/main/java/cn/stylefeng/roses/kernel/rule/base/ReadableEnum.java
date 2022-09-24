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
package cn.stylefeng.roses.kernel.rule.base;

/**
 * 可读性枚举的规范，必须包含一个key和一个value
 * <p>
 * key一般是编码，id，具有标识性的类型
 * value一般是String类型，是一串文字
 *
 * @author fengshuonan
 * @date 2022/9/6 11:27
 */
public interface ReadableEnum<T> {

    /**
     * 获取枚举中具有标识性的key或者id
     * <p>
     * 例如：状态枚举中的装填值，1 或 2
     *
     * @return 返回枚举具有标示性的key或id
     * @author fengshuonan
     * @date 2022/9/6 11:29
     */
    Object getKey();

    /**
     * 获取枚举中具有可读性的value值
     * <p>
     * 例如：状态枚举中的状态名称，"启用" 或 "禁用"
     *
     * @return 返回枚举具有可读性的value值
     * @author fengshuonan
     * @date 2022/9/6 11:30
     */
    Object getName();

    /**
     * 将原始值转化为具体枚举对象
     *
     * @param originValue 原始值
     * @return T 具体枚举
     * @author fengshuonan
     * @date 2022/9/24 15:17
     */
    T parseToEnum(String originValue);

}
