package cn.stylefeng.roses.kernel.group.api.enums;

import lombok.Getter;

/**
 * 一个示例
 *
 * @author fengshuonan
 * @date 2022-06-24 17:15:41
 */
@Getter
public enum DemoEnum {

    /**
     * markdown格式
     */
    MARKDOWN(1, "markdown格式"),

    /**
     * 富文本格式
     */
    TEXT(2, "富文本格式");

    private final Integer code;

    private final String message;

    DemoEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
