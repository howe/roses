package cn.stylefeng.roses.kernel.wrapper.field.util;

import cn.hutool.core.util.ClassUtil;
import cn.stylefeng.roses.kernel.rule.enums.FormatTypeEnum;
import cn.stylefeng.roses.kernel.wrapper.api.constants.WrapperConstants;
import com.fasterxml.jackson.core.JsonGenerator;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 通用的格式化方法
 *
 * @author fengshuonan
 * @date 2022/9/6 17:14
 */
@Slf4j
public class CommonFormatUtil {

    /**
     * 根据格式化类型，将原始的json字段数据，进行包装转化为新的字段数据过程
     *
     * @param formatTypeEnum 格式化类型，是新增字段还是替换字段
     * @param originValue    原始的字段值
     * @param formattedValue 新产生的字段值
     * @param jsonGenerator  jackson的写入json字段数据的工具类
     * @author fengshuonan
     * @date 2022/9/6 17:16
     */
    public static void writeField(FormatTypeEnum formatTypeEnum, Object originValue, Object formattedValue, JsonGenerator jsonGenerator) throws IOException {
        // 如果原始值和转化值一样，则直接返回
        if (originValue.equals(formattedValue)) {
            jsonGenerator.writeObject(originValue);
            return;
        }

        // 如果转化模式是替换类型
        if (formatTypeEnum.equals(FormatTypeEnum.REPLACE)) {
            jsonGenerator.writeObject(formattedValue);
        }

        // 如果转化模式是新增一个包装字段
        else {
            // 先写入原有值，保持不变
            jsonGenerator.writeObject(originValue);

            // 构造新的字段名，为原字段名+Wrapper
            String fieldName = jsonGenerator.getOutputContext().getCurrentName();
            String newWrapperFieldName = fieldName + WrapperConstants.FILED_WRAPPER_SUFFIX;

            // 获取当前正在转化的对象
            Object currentObj = jsonGenerator.getOutputContext().getCurrentValue();

            // 如果当前正在转化的对象中已经含有了字段名+Wrapper的字段，则生成时候带一个数字2
            Field declaredField = ClassUtil.getDeclaredField(currentObj.getClass(), newWrapperFieldName);
            if (declaredField != null) {
                newWrapperFieldName = newWrapperFieldName + "2";
            }

            // 写入新的字段名
            jsonGenerator.writeFieldName(newWrapperFieldName);
            jsonGenerator.writeObject(formattedValue);
        }
    }

}
