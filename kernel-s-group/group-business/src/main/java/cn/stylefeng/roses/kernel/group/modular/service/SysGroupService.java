package cn.stylefeng.roses.kernel.group.modular.service;

import cn.stylefeng.roses.kernel.group.modular.entity.SysGroup;
import cn.stylefeng.roses.kernel.group.modular.pojo.SysGroupRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 业务分组 服务类
 *
 * @author fengshuonan
 * @date 2022/05/11 12:54
 */
public interface SysGroupService extends IService<SysGroup> {

    /**
     * 获取用户某个业务下的分组列表
     *
     * @author fengshuonan
     * @date 2022/5/11 17:00
     */
    List<SysGroup> findGroupList(SysGroupRequest sysGroupRequest);

    /**
     * 添加时候的选择分组列表
     *
     * @author fengshuonan
     * @date 2022/5/11 17:00
     */
    List<SysGroup> addSelect(SysGroupRequest sysGroupRequest);

    /**
     * 将某个业务记录，增加到某个分组中，如果分组没有则创建分组
     *
     * @param sysGroupRequest 请求参数
     * @author fengshuonan
     * @date 2022/05/11 12:54
     */
    void add(SysGroupRequest sysGroupRequest);

    /**
     * 删除
     *
     * @param sysGroupRequest 请求参数
     * @author fengshuonan
     * @date 2022/05/11 12:54
     */
    void del(SysGroupRequest sysGroupRequest);

    /**
     * 获取某个业务，某个人，某个分类下的业务数据id
     *
     * @author fengshuonan
     * @date 2022/5/11 17:00
     */
    List<Long> findUserGroupDataList(SysGroupRequest sysGroupRequest);

}
