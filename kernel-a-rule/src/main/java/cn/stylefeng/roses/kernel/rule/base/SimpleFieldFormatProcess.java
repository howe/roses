package cn.stylefeng.roses.kernel.rule.base;

/**
 * json字段格式化的过程接口规范
 *
 * @author fengshuonan
 * @date 2022/9/6 11:54
 */
public interface SimpleFieldFormatProcess {

    /**
     * 是否可以进行格式化转化
     *
     * @param originValue 原来的值，格式化之前的值
     * @return 返回true，代表可以进行转化
     * @author fengshuonan
     * @date 2022/9/6 13:16
     */
    boolean canFormat(Object originValue);

    /**
     * 执行格式转化
     *
     * @param originValue 格式转化之前的值
     * @return 格式转化之后的值
     * @author fengshuonan
     * @date 2022/9/6 13:28
     */
    Object formatProcess(Object originValue);

}
