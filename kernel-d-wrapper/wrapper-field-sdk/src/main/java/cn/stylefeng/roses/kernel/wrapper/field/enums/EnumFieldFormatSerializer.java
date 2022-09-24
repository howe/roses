package cn.stylefeng.roses.kernel.wrapper.field.enums;

import cn.stylefeng.roses.kernel.rule.base.ReadableEnum;
import cn.stylefeng.roses.kernel.rule.enums.FormatTypeEnum;
import cn.stylefeng.roses.kernel.wrapper.field.util.CommonFormatUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 针对@EnumFieldFormat注解的具体序列化过程
 *
 * @author fengshuonan
 * @date 2022/9/6 16:57
 */
@Slf4j
public class EnumFieldFormatSerializer extends JsonSerializer<Object> {

    /**
     * 序列化类型，覆盖还是wrapper模式
     */
    private final FormatTypeEnum formatTypeEnum;

    /**
     * 具体序列化需要的枚举
     */
    private final Class<? extends ReadableEnum<?>> processEnum;

    public EnumFieldFormatSerializer(FormatTypeEnum formatTypeEnum, Class<? extends ReadableEnum<?>> processEnum) {
        this.formatTypeEnum = formatTypeEnum;
        this.processEnum = processEnum;
    }

    @Override
    public void serialize(Object originValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        // 最终转化的值
        Object formattedValue = originValue;

        // 如果原始字段是枚举类型，则直接调用接口的getName()方法完成转化
        if (originValue instanceof ReadableEnum) {
            formattedValue = ((ReadableEnum<?>) originValue).getName();
        } else {

            // 如果是其他类型，则获取枚举的getKey()的类型是什么
            // getKey()类型和值类型一致才能进行转化
            if (processEnum.isEnum()) {
                ReadableEnum<?>[] enumConstants = processEnum.getEnumConstants();
                for (ReadableEnum<?> enumConstant : enumConstants) {
                    if (enumConstant.getKey().equals(originValue)) {
                        formattedValue = enumConstant.getName();
                    }
                }
            }
        }

        // 进行数据转化写入到渲染的JSON中
        CommonFormatUtil.writeField(formatTypeEnum, originValue, formattedValue, jsonGenerator);

    }

}
