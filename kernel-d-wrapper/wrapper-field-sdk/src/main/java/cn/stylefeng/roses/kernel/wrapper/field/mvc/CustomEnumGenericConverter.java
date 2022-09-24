package cn.stylefeng.roses.kernel.wrapper.field.mvc;

import cn.stylefeng.roses.kernel.rule.base.ReadableEnum;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义枚举转化器
 * <p>
 * 将string，int，boolean 转化为 枚举类型
 * <p>
 * 枚举类型必须是实现 {@link ReadableEnum}接口的
 *
 * @author fengshuonan
 * @date 2022/9/24 18:31
 */
public class CustomEnumGenericConverter implements GenericConverter {

    /**
     * 获取可支持的转化类型
     *
     * @author fengshuonan
     * @date 2022/9/24 18:34
     */
    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {

        Set<ConvertiblePair> convertiblePairs = new HashSet<>();

        // 这里我们可以定义多组的类型转换关系
        ConvertiblePair stringPair = new ConvertiblePair(String.class, ReadableEnum.class);
        ConvertiblePair intPair = new ConvertiblePair(Integer.class, ReadableEnum.class);
        ConvertiblePair booleanPair = new ConvertiblePair(Boolean.class, ReadableEnum.class);

        convertiblePairs.add(stringPair);
        convertiblePairs.add(intPair);
        convertiblePairs.add(booleanPair);

        return convertiblePairs;
    }

    /**
     * 具体转化过程
     *
     * @param source     源数值
     * @param sourceType 源类型
     * @param targetType 目标类型
     * @return 被转化后的值
     * @author fengshuonan
     * @date 2022/9/24 18:34
     */
    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {

        // 如果目标是枚举并且是实现了ReadableEnum接口的
        if (ReadableEnum.class.isAssignableFrom(targetType.getObjectType())) {
            if (targetType.getObjectType().isEnum()) {
                ReadableEnum<?>[] enumConstants = (ReadableEnum<?>[]) targetType.getObjectType().getEnumConstants();
                for (ReadableEnum<?> enumConstant : enumConstants) {
                    return enumConstant.parseToEnum(String.valueOf(source));
                }
            }
        }

        return source;
    }

}