package cn.stylefeng.roses.kernel.db.mp.injector;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * 加了拓展方法的BaseMapper
 * <p>
 * 目前添加自定义InsertBatch方法
 *
 * @author fengshuonan
 * @date 2022/9/17 14:24
 */
public interface CustomBaseMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入，拼接insert方式，提高效率
     *
     * @author fengshuonan
     * @date 2022/9/17 14:27
     */
    int insertBatchSomeColumn(List<T> entityList);

}