package cn.stylefeng.roses.kernel.system.modular.user.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.base.SimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.rule.util.MixFieldTypeUtil;
import cn.stylefeng.roses.kernel.rule.util.ObjectConvertUtil;
import cn.stylefeng.roses.kernel.scanner.api.enums.FieldTypeEnum;
import cn.stylefeng.roses.kernel.scanner.api.util.ClassTypeUtil;
import cn.stylefeng.roses.kernel.system.api.UserServiceApi;
import cn.stylefeng.roses.kernel.system.api.pojo.user.SysUserDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Json响应的针对用户的处理
 *
 * @author fengshuonan
 * @date 2022/9/7 10:09
 */
public class UserFormatProcess implements SimpleFieldFormatProcess {

    @Override
    public boolean canFormat(Object originValue) {
        return MixFieldTypeUtil.whetherAssignClass(originValue, Long.class);
    }

    @Override
    public Object formatProcess(Object originValue) {

        // 先获取是基础类型还是集合类型
        FieldTypeEnum classFieldType = ClassTypeUtil.getClassFieldType(originValue.getClass());

        // 基础类型，直接转化
        if (FieldTypeEnum.BASIC.equals(classFieldType)) {
            Long userId = Convert.toLong(originValue);
            return this.userIdToName(userId);
        }

        // 集合类型
        if (FieldTypeEnum.BASE_COLLECTION.equals(classFieldType)) {
            Collection<?> userIdList = (Collection<?>) originValue;
            List<String> nameList = new ArrayList<>();
            for (Object userIdStr : userIdList) {
                Long userId = Convert.toLong(userIdStr);
                nameList.add(this.userIdToName(userId));
            }
            return nameList;
        }

        // 数组类型
        if (FieldTypeEnum.BASE_ARRAY.equals(classFieldType)) {
            Object[] objects = ObjectConvertUtil.objToArray(originValue);
            List<String> nameList = new ArrayList<>();
            for (Object userIdStr : objects) {
                Long userId = Convert.toLong(userIdStr);
                nameList.add(this.userIdToName(userId));
            }
            return nameList;
        }

        return originValue;
    }

    /**
     * 用户id转化为姓名的过程
     *
     * @author fengshuonan
     * @date 2022/9/7 10:59
     */
    private String userIdToName(Long userId) {
        UserServiceApi bean = SpringUtil.getBean(UserServiceApi.class);
        SysUserDTO userInfoByUserId = bean.getUserInfoByUserId(userId);
        return userInfoByUserId.getRealName();
    }

}
