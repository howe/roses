package cn.stylefeng.roses.kernel.system.api.format;

import cn.hutool.core.convert.Convert;
import cn.hutool.extra.spring.SpringUtil;
import cn.stylefeng.roses.kernel.rule.format.BaseSimpleFieldFormatProcess;
import cn.stylefeng.roses.kernel.system.api.UserServiceApi;
import cn.stylefeng.roses.kernel.system.api.pojo.user.SysUserDTO;

/**
 * Json响应的针对用户的处理
 *
 * @author fengshuonan
 * @date 2022/9/7 10:09
 */
public class UserFormatProcess extends BaseSimpleFieldFormatProcess {

    /**
     * 未知人员的名称
     */
    private static final String NOT_FIND_USER_NAME = "未知人员";

    @Override
    public Class<?> getItemClass() {
        return Long.class;
    }

    @Override
    public Object simpleItemFormat(Object businessId) {
        Long userId = Convert.toLong(businessId);
        UserServiceApi bean = SpringUtil.getBean(UserServiceApi.class);
        SysUserDTO userInfoByUserId = bean.getUserInfoByUserId(userId);
        if (userInfoByUserId == null) {
            return NOT_FIND_USER_NAME;
        }
        return userInfoByUserId.getRealName();
    }

}
