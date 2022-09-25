/*
 * Copyright [2020-2030] [https://www.stylefeng.cn]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Guns采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Guns源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/stylefeng/guns
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/stylefeng/guns
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */
package cn.stylefeng.roses.kernel.wrapper.starter;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Java8 string参数转日期类
 *
 * @author luopeng
 * @date 2022/9/24 21:15
 */
@Configuration
public class GunsDateConvertAutoConfiguration {

    /**
     * LocalDate转换器，用于转换RequestParam和PathVariable参数
     *
     * @author luopeng
     * @date 2022/9/24 21:16
     */
    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return LocalDate.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN));
            }
        };
    }

    /**
     * LocalDateTime转换器，用于转换RequestParam和PathVariable参数
     *
     * @author luopeng
     * @date 2022/9/24 21:16
     */
    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN));
            }
        };
    }

    /**
     * LocalTime转换器，用于转换RequestParam和PathVariable参数
     *
     * @author luopeng
     * @date 2022/9/24 21:18
     */
    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                return LocalTime.parse(source, DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN));
            }
        };
    }

    /**
     * Date转换器，用于转换RequestParam和PathVariable参数
     *
     * @author luopeng
     * @date 2022/9/24 21:40
     */
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                return DateUtil.parse(source.trim());
            }
        };
    }

}
