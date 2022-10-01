package cn.stylefeng.roses.kernel.system.api.enums;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.stylefeng.roses.kernel.rule.base.ReadableEnum;
import lombok.Getter;

/**
 * 指定查找模式
 *
 * @author fengshuonan
 * @date 2022/10/1 18:02
 */
@Getter
public enum DetectModeEnum implements ReadableEnum<DetectModeEnum> {

    /**
     * 自下而上
     * <p>
     * 从下往上一级一级找
     */
    TO_TOP(1, "自下而上"),

    /**
     * 自上而下
     * <p>
     * 从最高级开始一级一级往下找
     */
    TO_DOWN(2, "自上而下");

    private final Integer code;

    private final String name;

    DetectModeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Object getKey() {
        return code;
    }

    @Override
    public Object getName() {
        return name;
    }

    @Override
    public DetectModeEnum parseToEnum(String originValue) {
        if (ObjectUtil.isEmpty(originValue)) {
            return null;
        }
        for (DetectModeEnum value : DetectModeEnum.values()) {
            if (value.code.equals(Convert.toInt(originValue))) {
                return value;
            }
        }
        return null;
    }

}
