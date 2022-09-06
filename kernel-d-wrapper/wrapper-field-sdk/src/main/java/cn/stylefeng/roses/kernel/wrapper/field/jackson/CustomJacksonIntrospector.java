package cn.stylefeng.roses.kernel.wrapper.field.jackson;

import cn.stylefeng.roses.kernel.rule.annotation.SimpleFieldFormat;
import cn.stylefeng.roses.kernel.rule.base.SimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.rule.enums.FormatTypeEnum;
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
        SimpleFieldFormat formatter = annotated.getAnnotation(SimpleFieldFormat.class);

        if (formatter == null || formatter.processClass() == null) {
            return super.findSerializer(annotated);
        }

        // 获取格式化处理的方式
        FormatTypeEnum formatTypeEnum = formatter.formatType();

        // 获取具体的处理方法
        Class<? extends SimpleFieldFormatProcess> process = formatter.processClass();

        // 创建对应的序列化模式
        return new SimpleFieldFormatSerializer(formatTypeEnum, process);
    }

}
