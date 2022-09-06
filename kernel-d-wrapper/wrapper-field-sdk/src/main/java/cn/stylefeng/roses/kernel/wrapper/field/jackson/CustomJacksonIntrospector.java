package cn.stylefeng.roses.kernel.wrapper.field.jackson;

import cn.stylefeng.roses.kernel.rule.annotation.JsonFieldFormat;
import cn.stylefeng.roses.kernel.rule.base.JsonFieldFormatProcess;
import cn.stylefeng.roses.kernel.rule.enums.FormatTypeEnum;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

/**
 * Json序列化，注解拦截器，针对自定义注解@JsonFieldFormat进行拓展性序列化
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
        JsonFieldFormat formatter = annotated.getAnnotation(JsonFieldFormat.class);

        if (formatter == null || formatter.processClass() == null) {
            return super.findSerializer(annotated);
        }

        // 获取格式化处理的方式
        FormatTypeEnum formatTypeEnum = formatter.formatType();

        // 获取具体的处理方法
        Class<? extends JsonFieldFormatProcess> process = formatter.processClass();

        // 创建对应的序列化模式
        return new CustomJsonSerializer(formatTypeEnum, process);
    }

}
