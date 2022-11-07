package cn.stylefeng.roses.kernel.system.modular.user.service;

import cn.stylefeng.roses.kernel.auth.api.context.LoginContext;
import cn.stylefeng.roses.kernel.auth.api.pojo.login.LoginUser;
import cn.stylefeng.roses.kernel.rule.tenant.TenantPrefixApi;
import org.springframework.stereotype.Service;

/**
 * 获取当前登录用户的租户code
 *
 * @author fengshuonan
 * @date 2022/11/7 21:01
 */
@Service
public class TenantPrefixService implements TenantPrefixApi {

    @Override
    public String getTenantPrefix() {

        LoginUser loginUser = LoginContext.me().getLoginUserNullable();

        if (loginUser == null) {
            return null;
        }

        return loginUser.getTenantCode();
    }

}
