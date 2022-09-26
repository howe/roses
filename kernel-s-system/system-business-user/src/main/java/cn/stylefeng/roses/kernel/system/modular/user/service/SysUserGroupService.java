package cn.stylefeng.roses.kernel.system.modular.user.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.rule.pojo.dict.SimpleDict;
import cn.stylefeng.roses.kernel.system.modular.user.entity.SysUserGroup;
import cn.stylefeng.roses.kernel.system.modular.user.pojo.request.SysUserGroupRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 用户组 服务类
 *
 * @author fengshuonan
 * @date 2022/09/26 10:12
 */
public interface SysUserGroupService extends IService<SysUserGroup> {

    /**
     * 新增
     *
     * @param sysUserGroupRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    SysUserGroup add(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 删除
     *
     * @param sysUserGroupRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    void del(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 编辑
     *
     * @param sysUserGroupRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    void edit(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 查询详情
     *
     * @param sysUserGroupRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    SysUserGroup detail(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 获取列表
     *
     * @param sysUserGroupRequest 请求参数
     * @return List<SysUserGroup>   返回结果
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    List<SysUserGroup> findList(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 获取列表（带分页）
     *
     * @param sysUserGroupRequest 请求参数
     * @return PageResult<SysUserGroup>   返回结果
     * @author fengshuonan
     * @date 2022/09/26 10:12
     */
    PageResult<SysUserGroup> findPage(SysUserGroupRequest sysUserGroupRequest);

    /**
     * 获取选择关系列表
     *
     * @author fengshuonan
     * @date 2022/9/26 17:30
     */
    List<SimpleDict> getSelectRelationList();

}