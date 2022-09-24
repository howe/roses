package cn.stylefeng.roses.kernel.wrapper.field.jackson;

import cn.stylefeng.roses.kernel.rule.annotation.EnumFieldFormat;
import cn.stylefeng.roses.kernel.rule.annotation.SimpleFieldFormat;
import cn.stylefeng.roses.kernel.rule.base.ReadableEnum;
import cn.stylefeng.roses.kernel.rule.base.SimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.rule.enums.FormatTypeEnum;
import cn.stylefeng.roses.kernel.wrapper.field.enums.EnumFieldFormatDeserializer;
import cn.stylefeng.roses.kernel.wrapper.field.enums.EnumFieldFormatSerializer;
import cn.stylefeng.roses.kernel.wrapper.field.simple.SimpleFieldFormatSerializer;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * Json序列化，注解拦截器，针对自定义注解进行拓展性序列化
 *
 * @author fengshuonan
 * @date 2022/9/6 13:56
 */
public class CustomJacksonIntrospector extends JacksonAnnotationIntrospector {

    /**
     * 序列化
     */
    private static final long serialVersionUID = 3159434791568421355L;

    @Override
    public Object findSerializer(Annotated annotated) {

        // 自定义格式化过程
        SimpleFieldFormat simpleFieldFormat = annotated.getAnnotation(SimpleFieldFormat.class);
        if (simpleFieldFormat != null && simpleFieldFormat.processClass() != null) {
            // 获取格式化处理的方式
            FormatTypeEnum formatTypeEnum = simpleFieldFormat.formatType();

            // 获取具体的处理方法
            Class<? extends SimpleFieldFormatProcess> process = simpleFieldFormat.processClass();

            // 创建对应的序列化模式
            return new SimpleFieldFormatSerializer(formatTypeEnum, process);
        }

        // 枚举格式化过程
        EnumFieldFormat enumFieldFormat = annotated.getAnnotation(EnumFieldFormat.class);
        if (enumFieldFormat != null && enumFieldFormat.processEnum() != null) {
            // 获取格式化处理的方式
            FormatTypeEnum formatTypeEnum = enumFieldFormat.formatType();

            // 获取具体的处理枚举
            Class<? extends ReadableEnum<?>> process = enumFieldFormat.processEnum();

            // 创建对应的序列化模式
            return new EnumFieldFormatSerializer(formatTypeEnum, process);
        }

        return super.findSerializer(annotated);
    }

    @Override
    public Object findDeserializer(Annotated annotated) {

        // 枚举的反序列化
        EnumFieldFormat enumFieldFormat = annotated.getAnnotation(EnumFieldFormat.class);

        if (enumFieldFormat != null && enumFieldFormat.processEnum() != null) {

            // 获取具体的处理枚举
            Class<? extends ReadableEnum<?>> process = enumFieldFormat.processEnum();

            // 创建对应的序列化模式
            return new EnumFieldFormatDeserializer(process);
        }

        return super.findDeserializer(annotated);
    }
}
