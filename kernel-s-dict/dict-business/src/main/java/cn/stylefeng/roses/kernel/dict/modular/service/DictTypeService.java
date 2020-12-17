package cn.stylefeng.roses.kernel.dict.modular.service;


import cn.stylefeng.roses.kernel.dict.modular.entity.DictType;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import cn.stylefeng.roses.kernel.db.api.pojo.page.PageResult;
import cn.stylefeng.roses.kernel.dict.modular.pojo.request.DictTypeRequest;

import java.util.List;

/**
 * 字典类型管理
 *
 * @author fengshuonan
 * @date 2020/10/29 18:54
 */
public interface DictTypeService extends IService<DictType> {

    /**
     * 添加字典类型
     *
     * @param dictTypeRequest 字典类型请求
     * @author fengshuonan
     * @date 2020/10/29 18:55
     */
    void addDictType(DictTypeRequest dictTypeRequest);

    /**
     * 修改字典类型
     *
     * @param dictTypeRequest 字典类型请求
     * @author fengshuonan
     * @date 2020/10/29 18:55
     */
    void updateDictType(DictTypeRequest dictTypeRequest);

    /**
     * 修改字典状态
     *
     * @param dictTypeRequest 字典类型请求
     * @author fengshuonan
     * @date 2020/10/29 18:56
     */
    void updateDictTypeStatus(DictTypeRequest dictTypeRequest);

    /**
     * 删除字典类型
     *
     * @param dictTypeRequest 字典类型请求
     * @author fengshuonan
     * @date 2020/10/29 18:55
     */
    void deleteDictType(DictTypeRequest dictTypeRequest);

    /**
     * 获取字典类型列表
     *
     * @param dictTypeRequest 字典类型请求
     * @return 字典类型列表
     * @author fengshuonan
     * @date 2020/10/29 18:55
     */
    List<DictType> getDictTypeList(DictTypeRequest dictTypeRequest);

    /**
     * 获取字典类型列表（带分页）
     *
     * @param page            分页参数
     * @param dictTypeRequest 字典类型请求
     * @return 字典类型列表
     * @author fengshuonan
     * @date 2020/10/29 18:55
     */
    PageResult<DictType> getDictTypePageList(Page<DictType> page, DictTypeRequest dictTypeRequest);

    /**
     * code校验重复
     *
     * @param dictTypeRequest 字典类型请求
     * @return true-没有重复，false-有重复的
     * @author fengshuonan
     * @date 2020/10/29 18:56
     */
    boolean validateCodeAvailable(DictTypeRequest dictTypeRequest);

}