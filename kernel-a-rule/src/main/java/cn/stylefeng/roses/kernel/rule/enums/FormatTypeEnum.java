package cn.stylefeng.roses.kernel.rule.enums;

/**
 * 字段格式化的类型
 *
 * @author fengshuonan
 * @date 2022/9/6 11:48
 */
public enum FormatTypeEnum {

    /**
     * 替换型，将原有字段替换成新的值
     * <p>
     * 例如：接口返回userId=1001，采用本种方式则返回：userId=张三
     */
    REPLACE,

    /**
     * 额外加字段，不改变原来字段值，在同级别字段中，增加一个 “源字段名+Wrapper" 字段
     * <p>
     * 例如：接口返回userId=1001，采用本种方式则返回：userId=1001，userIdWrapper=张三
     */
    ADD_FIELD

}
