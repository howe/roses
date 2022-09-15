package cn.stylefeng.roses.kernel.rule.util;

import cn.hutool.db.DbUtil;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * 数据库检测相关的工具
 *
 * @author fengshuonan
 * @date 2022/9/15 11:50
 */
public class DbConnectionUtil {

    /**
     * 检测数据库的链接是否连通，默认5秒超时
     *
     * @author fengshuonan
     * @date 2022/9/15 11:49
     */
    public static boolean getValidFlag(String className, String url, String account, String password) {
        return getValidFlag(className, url, account, password, 5);
    }

    /**
     * 检测数据库的链接是否连通
     *
     * @author fengshuonan
     * @date 2022/9/15 11:49
     */
    public static boolean getValidFlag(String className, String url, String account, String password, Integer timeoutSeconds) {
        Connection connection = null;
        try {
            Class.forName(className);
            connection = DriverManager.getConnection(url, account, password);
            return connection.isValid(timeoutSeconds);
        } catch (Exception e) {
            return false;
        } finally {
            DbUtil.close(connection);
        }
    }

}
