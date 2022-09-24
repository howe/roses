package cn.stylefeng.roses.kernel.wrapper.field.enums;

import cn.stylefeng.roses.kernel.rule.base.ReadableEnum;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 针对@EnumFieldFormat注解的具体反序列化过程
 *
 * @author fengshuonan
 * @date 2022/9/24 15:04
 */
@Slf4j
public class EnumFieldFormatDeserializer extends JsonDeserializer<Object> {

    /**
     * 具体反序列化需要的枚举
     */
    private final Class<? extends ReadableEnum<?>> processEnum;

    public EnumFieldFormatDeserializer(Class<? extends ReadableEnum<?>> processEnum) {
        this.processEnum = processEnum;
    }

    @Override
    public Object deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {

        // 获取当前对象原始值
        String originRequestValue = jsonParser.getValueAsString();

        // 判断是否是枚举类型
        if (processEnum.isEnum()) {
            ReadableEnum<?>[] enumConstants = processEnum.getEnumConstants();
            for (ReadableEnum<?> enumConstant : enumConstants) {
                return enumConstant.parseToEnum(originRequestValue);
            }
        }

        return null;
    }

}
