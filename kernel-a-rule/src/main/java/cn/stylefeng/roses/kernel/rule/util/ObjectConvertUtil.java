package cn.stylefeng.roses.kernel.rule.util;

import java.lang.reflect.Array;

/**
 * 对象转化为数组的工具
 *
 * @author fengshuonan
 * @date 2022/9/7 11:06
 */
public class ObjectConvertUtil {

    /**
     * Object转为一个array，确保object为数组类型
     *
     * @author fengshuonan
     * @date 2020/7/24 22:06
     */
    public static Object[] objToArray(Object object) {
        int length = Array.getLength(object);
        Object[] result = new Object[length];
        for (int i = 0; i < result.length; i++) {
            result[i] = Array.get(object, i);
        }
        return result;
    }

}
