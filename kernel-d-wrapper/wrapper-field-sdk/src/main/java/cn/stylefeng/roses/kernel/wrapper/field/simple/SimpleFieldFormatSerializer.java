package cn.stylefeng.roses.kernel.wrapper.field.simple;

import cn.hutool.core.util.ClassUtil;
import cn.stylefeng.roses.kernel.rule.base.SimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.rule.enums.FormatTypeEnum;
import cn.stylefeng.roses.kernel.wrapper.api.constants.WrapperConstants;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * 针对@SimpleFieldFormat注解的具体序列化过程
 *
 * @author fengshuonan
 * @date 2022/9/6 14:09
 */
@Slf4j
public class SimpleFieldFormatSerializer extends JsonSerializer<Object> {

    /**
     * 序列化类型，覆盖还是wrapper模式
     */
    private final FormatTypeEnum formatTypeEnum;

    /**
     * 具体序列化过程
     */
    private final Class<? extends SimpleFieldFormatProcess> processClass;

    public SimpleFieldFormatSerializer(FormatTypeEnum formatTypeEnum, Class<? extends SimpleFieldFormatProcess> processClass) {
        this.formatTypeEnum = formatTypeEnum;
        this.processClass = processClass;
    }

    @Override
    public void serialize(Object originValue, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {

        // 序列化字段名称
        String fieldName = jsonGenerator.getOutputContext().getCurrentName();

        // 创建具体字段转化的实现类
        SimpleFieldFormatProcess simpleFieldFormatProcess = null;
        try {
            simpleFieldFormatProcess = processClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("执行json的字段序列化出错", e);
            return;
        }

        // 判断当前字段值是否可以转化
        boolean canFormat = simpleFieldFormatProcess.canFormat(originValue);
        if (!canFormat) {
            return;
        }

        // 执行转化，获取转化过的值
        Object formattedValue = simpleFieldFormatProcess.formatProcess(originValue);

        try {
            // 如果转化模式是替换类型
            if (formatTypeEnum.equals(FormatTypeEnum.REPLACE)) {
                jsonGenerator.writeObject(formattedValue);
            }

            // 如果转化模式是新增一个包装字段
            else {
                // 先写入原有值，保持不变
                jsonGenerator.writeObject(originValue);

                // 构造新的字段名，为原字段名+Wrapper
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

        } catch (IOException e) {
            log.error("执行json的字段序列化出错", e);
        }
    }

}
