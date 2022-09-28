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
package cn.stylefeng.roses.kernel.rule.enums;

import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.base.ReadableEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 用来标识菜单或者接口资源的类别，1-系统类的菜单或资源，2-业务类的菜单或资源
 * <p>
 * 业务类的菜单就是项目开发时候的业务，例如订单管理，商品管理
 * 系统类就是用户管理，角色管理，日志管理这些
 *
 * @author fengshuonan
 * @date 2022/9/28 11:15
 */
@Getter
public enum ResBizTypeEnum implements ReadableEnum<ResBizTypeEnum> {

    /**
     * 业务类
     */
    BUSINESS(1, "业务类"),

    /**
     * 系统类
     */
    SYSTEM(2, "系统类"),

    /**
     * 默认
     * <p>
     * 如果是默认，则方法的业务类别，则会根据控制器上@ApiResource的类别决定具体方法的类别
     */
    DEFAULT(3, "默认类");

    /**
     * 使用@EnumValue注解，标记mybatis-plus保存到库中使用code值
     */
    @EnumValue
    @JsonValue
    private final Integer code;

    private final String message;

    ResBizTypeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public Object getKey() {
        return this.code;
    }

    @Override
    public Object getName() {
        return this.message;
    }

    @Override
    public ResBizTypeEnum parseToEnum(String originValue) {
        if (ObjectUtil.isEmpty(originValue)) {
            return null;
        }
        for (ResBizTypeEnum value : ResBizTypeEnum.values()) {
            if (String.valueOf(value.code).equals(originValue)) {
                return value;
            }
        }
        return null;
    }

}
