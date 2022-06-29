
package cn.stylefeng.roses.kernel.rule.enums;

import lombok.Getter;

/**
 * 数据库正序倒序排列的枚举
 *
 * @author fengshuonan
 * @date 2022/6/29 14:11
 */
@Getter
public enum SortByEnum {

    /**
     * 正序排列
     */
    ASC("asc"),

    /**
     * 倒序排列
     */
    DESC("desc");

    /**
     * 关键字
     */
    private final String keyword;

    SortByEnum(String keyword) {
        this.keyword = keyword;
    }

}
