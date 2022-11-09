package cn.stylefeng.roses.kernel.auth.callback;

import cn.stylefeng.roses.kernel.rule.tenant.RequestTenantCodeHolder;
import cn.stylefeng.roses.kernel.rule.threadlocal.RemoveThreadLocalApi;
import org.springframework.stereotype.Component;

/**
 * 清除参数缓存相关的ThreadLocal
 *
 * @author fengshuonan
 * @date 2021/10/29 11:37
 */
@Component
public class TenantCodeRemoveThreadLocalHolder implements RemoveThreadLocalApi {

    @Override
    public void removeThreadLocalAction() {
        RequestTenantCodeHolder.clearTenantCode();
    }

}
