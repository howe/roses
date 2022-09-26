package cn.stylefeng.roses.kernel.system.api.pojo.usergroup;

import lombok.Data;

/**
 * 通用组件选择器的选择对象的封装
 *
 * @author fengshuonan
 * @date 2022/9/26 11:46
 */
@Data
public class SelectItem {

    /**
     * 被选择的业务主键id
     */
    private Long bizId;

    /**
     * 业务的名称
     */
    private String name;

    /**
     * 选择的子类型（一般用在部门下审批人的类型）
     */
    private String subValue;

    /**
     * 选择的子类型（一般用在部门下审批人的类型）
     */
    private String subValueName;

}
