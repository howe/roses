package cn.stylefeng.roses.kernel.scanner.api;

import cn.stylefeng.roses.kernel.scanner.api.pojo.devops.DevOpsReportResourceParam;

/**
 * 检测是否运维平台本地化部署的API
 *
 * @author fengshuonan
 * @date 2022/10/17 23:54
 */
public interface DevOpsDetectApi {

    /**
     * 本地化上报资源到运维平台
     *
     * @param devOpsReportResourceParam 上报时候的参数，里边包括了所有当前项目资源
     * @author fengshuonan
     * @date 2022/10/17 23:56
     */
    void saveResource(DevOpsReportResourceParam devOpsReportResourceParam);

}
