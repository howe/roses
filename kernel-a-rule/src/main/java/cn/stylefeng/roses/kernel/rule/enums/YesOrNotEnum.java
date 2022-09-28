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

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.base.ReadableEnum;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * 是或否的枚举，一般用在数据库字段，例如del_flag字段，char(1)，填写Y或N
 *
 * @author fengshuonan
 * @date 2020/4/13 22:59
 */
@Getter
public enum YesOrNotEnum implements ReadableEnum<YesOrNotEnum> {

    /**
     * 是
     */
    Y("Y", "是", true),

    /**
     * 否
     */
    N("N", "否", false);

    /**
     * 使用@EnumValue注解，标记mybatis-plus保存到库中使用code值
     */
    @EnumValue
    private final String code;

    private final String message;

    /**
     * 注解@JsonValue是返回给前端时候拿的值，而@JsonCreator是反序列化时候的方式
     */
    @JsonValue
    private final Boolean boolFlag;

    YesOrNotEnum(String code, String message, Boolean boolFlag) {
        this.code = code;
        this.message = message;
        this.boolFlag = boolFlag;
    }

    /**
     * 根据code获取枚举，用在接收前段传参
     *
     * @author fengshuonan
     * @date 2022/9/7 17:58
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static YesOrNotEnum codeToEnum(Boolean boolFlag) {
        if (null != boolFlag) {
            for (YesOrNotEnum item : YesOrNotEnum.values()) {
                if (item.getBoolFlag().equals(boolFlag)) {
                    return item;
                }
            }
        }
        return null;
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
    public YesOrNotEnum parseToEnum(String originValue) {
        if (ObjectUtil.isEmpty(originValue)) {
            return null;
        }
        for (YesOrNotEnum value : YesOrNotEnum.values()) {
            if (value.boolFlag.equals(Convert.toBool(originValue))) {
                return value;
            }
        }
        return null;
    }

}
