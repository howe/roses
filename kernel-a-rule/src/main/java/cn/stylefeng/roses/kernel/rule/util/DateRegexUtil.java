package cn.stylefeng.roses.kernel.rule.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期正则工具
 *
 * @author fengshuonan
 * @date 2022/8/2 15:04
 */
public class DateRegexUtil {

    /**
     * 从指定字符串中提取日期的字符
     *
     * @author fengshuonan
     * @date 2022/8/2 15:04
     */
    public static String extractDate(String originStr) {

        String datePatternReg = ".*([0-9]{4}-[0-9]{2}-[0-9]{2}).*";
        String simpleDatePatternReg = ".*([0-9]{2}-[0-9]{2}).*";
        Pattern datePattern = Pattern.compile(datePatternReg);
        Pattern simpleDatePattern = Pattern.compile(simpleDatePatternReg);

        // 首先用年月日的表达式匹配
        Matcher datePatternMatcher = datePattern.matcher(originStr);
        if (datePatternMatcher.find()) {
            return datePatternMatcher.group(1);
        } else {
            Matcher simpleDatePatternMatcher = simpleDatePattern.matcher(originStr);
            if (simpleDatePatternMatcher.find()) {
                return simpleDatePatternMatcher.group(1);
            } else {
                return "";
            }
        }
    }

}
