package cn.stylefeng.roses.kernel.dsctn.api.pojo;

import lombok.Data;

/**
 * 数据库连接信息的DTO
 *
 * @author fengshuonan
 * @date 2022/8/23 14:06
 */
@Data
public class DataBaseInfoDto {

    /**
     * 数据库连接的id
     */
    private Long dbId;

    /**
     * 数据库类型
     */
    private String dbType;

    /**
     * 数据库连接的名称
     */
    private String dbName;

    /**
     * 数据库连接的备注
     */
    private String remarks;

}
