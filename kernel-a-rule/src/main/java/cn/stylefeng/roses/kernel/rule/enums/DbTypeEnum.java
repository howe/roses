package cn.stylefeng.roses.kernel.rule.enums;

import lombok.Getter;

/**
 * 不同数据库类型的枚举
 * <p>
 * 用于标识mapping.xml中不同数据库的标识
 *
 * @author stylefeng
 * @date 2020/6/20 21:08
 */
@Getter
public enum DbTypeEnum {

    /**
     * mysql
     */
    MYSQL("jdbc:mysql", "mysql", "select 1"),

    /**
     * pgsql
     */
    PG_SQL("jdbc:postgresql", "pgsql", "select version()"),

    /**
     * oracle
     */
    ORACLE("jdbc:oracle", "oracle", "select 1 from dual"),

    /**
     * 达梦（使用oracle的mapping.xml）
     */
    DM("jdbc:dm", "oracle", "select 1 from dual"),

    /**
     * mssql
     */
    MS_SQL("jdbc:jtds:sqlserver", "mssql", "select 1");

    /**
     * spring.datasource.url中包含的关键字
     */
    private final String urlWords;

    /**
     * mapping.xml使用databaseId="xxx"来标识的关键字
     */
    private final String xmlDatabaseId;

    /**
     * validateQuery所使用的语句
     */
    private final String validateQuery;

    DbTypeEnum(String urlWords, String xmlDatabaseId, String validateQuery) {
        this.urlWords = urlWords;
        this.xmlDatabaseId = xmlDatabaseId;
        this.validateQuery = validateQuery;
    }

    /**
     * 通过url判断是哪种数据库
     *
     * @author fengshuonan
     * @date 2022/8/23 14:22
     */
    public static String getTypeByUrl(String url) {
        if (url == null) {
            return MYSQL.name();
        }

        for (DbTypeEnum value : DbTypeEnum.values()) {
            if (url.contains(value.getUrlWords())) {
                return value.name();
            }
        }

        return MYSQL.name();
    }

}
