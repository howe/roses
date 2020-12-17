package cn.stylefeng.roses.kernel.dsctn.modular.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.stylefeng.roses.kernel.db.api.pojo.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据库信息表
 *
 * @author fengshuonan
 * @date 2020/11/1 0:15
 */
@TableName("sys_database_info")
@Data
@EqualsAndHashCode(callSuper = true)
public class DatabaseInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 数据库名称（英文名称）
     */
    @TableField("db_name")
    private String dbName;

    /**
     * jdbc的驱动类型
     */
    @TableField("jdbc_driver")
    private String jdbcDriver;

    /**
     * 数据库连接的账号
     */
    @TableField("user_name")
    private String userName;

    /**
     * 数据库连接密码
     */
    @TableField("password")
    private String password;

    /**
     * jdbc的url
     */
    @TableField("jdbc_url")
    private String jdbcUrl;

    /**
     * 备注，摘要
     */
    @TableField("remarks")
    private String remarks;

}