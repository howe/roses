package cn.stylefeng.roses.kernel.wrapper.api.util;

import cn.hutool.core.util.ArrayUtil;
import cn.stylefeng.roses.kernel.rule.util.ObjectConvertUtil;

import java.util.Collection;

/**
 * 字段类型判断
 *
 * @author fengshuonan
 * @date 2022/9/7 10:24
 */
public class MixFieldTypeUtil {

    /**
     * 判断fieldValue是否是Long、List<Long>、Long[]类型，如果是其中任何一种，则都返回true
     *
     * @author fengshuonan
     * @date 2022/9/7 10:24
     */
    public static boolean getLongFlag(Object fieldValue) {

        // 直接判断是否是Long
        if (fieldValue instanceof Long) {
            return true;
        }

        // 如果是集合类型
        else if (fieldValue instanceof Collection) {
            Collection<?> collectionList = (Collection<?>) fieldValue;
            for (Object item : collectionList) {
                if (item instanceof Long) {
                    return true;
                }
            }
        }

        // 如果是数组类型
        else if (ArrayUtil.isArray(fieldValue)) {
            Object[] objects = ObjectConvertUtil.objToArray(fieldValue);
            for (Object item : objects) {
                if (item instanceof Long) {
                    return true;
                }
            }
        }

        return false;
    }


}
