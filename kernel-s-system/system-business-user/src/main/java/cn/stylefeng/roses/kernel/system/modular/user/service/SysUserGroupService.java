package cn.stylefeng.roses.kernel.system.modular.user.service;

import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.system.modular.user.entity.SysUserGroup;
import cn.stylefeng.roses.kernel.system.modular.user.pojo.request.SysUserGroupRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 权限分组 服务类
 *
 * @author fengshuonan
 * @date 2022/09/25 22:11
 */
public interface SysUserGroupService extends IService<SysUserGroup> {

	/**
     * 新增
     *
     * @param sysUserGroupRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    void add(SysUserGroupRequest sysUserGroupRequest);

	/**
     * 删除
     *
     * @param sysUserGroupRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    void del(SysUserGroupRequest sysUserGroupRequest);

	/**
     * 编辑
     *
     * @param sysUserGroupRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    void edit(SysUserGroupRequest sysUserGroupRequest);

	/**
     * 查询详情
     *
     * @param sysUserGroupRequest 请求参数
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    SysUserGroup detail(SysUserGroupRequest sysUserGroupRequest);

	/**
     * 获取列表
     *
     * @param sysUserGroupRequest        请求参数
     * @return List<SysUserGroup>   返回结果
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    List<SysUserGroup> findList(SysUserGroupRequest sysUserGroupRequest);

	/**
     * 获取列表（带分页）
     *
     * @param sysUserGroupRequest              请求参数
     * @return PageResult<SysUserGroup>   返回结果
     * @author fengshuonan
     * @date 2022/09/25 22:11
     */
    PageResult<SysUserGroup> findPage(SysUserGroupRequest sysUserGroupRequest);

}