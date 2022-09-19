package cn.stylefeng.roses.kernel.rule.util;

import java.util.regex.Pattern;

/**
 * 字符串过滤工具，主要过滤不合法的请求参数
 *
 * @author fengshuonan
 * @date 2022/9/19 13:37
 */
public class StrFilterUtil {

    /**
     * 过滤文件名
     *
     * @author fengshuonan
     * @date 2022/9/19 13:39
     */
    public static String filterFileName(String param) {
        Pattern fileNamePattern = Pattern.compile("[\\\\/:*?\"<>|\\s]");
        return fileNamePattern.matcher(param).replaceAll("");
    }

}
