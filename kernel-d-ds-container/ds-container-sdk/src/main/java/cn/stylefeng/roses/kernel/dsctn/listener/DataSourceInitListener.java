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
package cn.stylefeng.roses.kernel.dsctn.listener;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.stylefeng.roses.kernel.db.api.factory.DruidDatasourceFactory;
import cn.stylefeng.roses.kernel.db.api.pojo.druid.DruidProperties;
import cn.stylefeng.roses.kernel.dsctn.api.exception.DatasourceContainerException;
import cn.stylefeng.roses.kernel.dsctn.context.DataSourceContext;
import cn.stylefeng.roses.kernel.rule.listener.ContextInitializedListener;
import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

import static cn.stylefeng.roses.kernel.dsctn.api.exception.enums.DatasourceContainerExceptionEnum.DB_CONNECTION_INFO_EMPTY_ERROR;
import static cn.stylefeng.roses.kernel.dsctn.api.exception.enums.DatasourceContainerExceptionEnum.INIT_DATASOURCE_CONTAINER_ERROR;


/**
 * 多数据源的初始化，加入到数据源Context中的过程
 *
 * @author fengshuonan
 * @date 2020/11/1 0:02
 */
@Slf4j
public class DataSourceInitListener extends ContextInitializedListener implements Ordered {

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE + 200;
    }

    @Override
    public void eventCallback(ApplicationContextInitializedEvent event) {

        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();

        // 获取数据库连接配置
        String dataSourceDriver = environment.getProperty("spring.datasource.driver-class-name");
        String dataSourceUrl = environment.getProperty("spring.datasource.url");
        String dataSourceUsername = environment.getProperty("spring.datasource.username");
        String dataSourcePassword = environment.getProperty("spring.datasource.password");

        // 如果有为空的配置，终止执行
        if (ObjectUtil.hasEmpty(dataSourceUrl, dataSourceUsername, dataSourcePassword)) {
            String userTip = StrUtil.format(DB_CONNECTION_INFO_EMPTY_ERROR.getUserTip(), dataSourceUrl, dataSourceUsername);
            throw new DatasourceContainerException(DB_CONNECTION_INFO_EMPTY_ERROR, userTip);
        }

        // 创建主数据源的properties
        DruidProperties druidProperties = new DruidProperties();
        druidProperties.setDriverClassName(dataSourceDriver);
        druidProperties.setUrl(dataSourceUrl);
        druidProperties.setUsername(dataSourceUsername);
        druidProperties.setPassword(dataSourcePassword);

        // 设置其他数据源配置
        this.setOtherDruidConfigs(environment, druidProperties);

        // 创建主数据源
        DruidDataSource druidDataSource = DruidDatasourceFactory.createDruidDataSource(druidProperties);

        // 初始化数据源容器
        try {
            DataSourceContext.initDataSource(druidProperties, druidDataSource);
        } catch (Exception exception) {
            log.error("初始化数据源容器错误!", exception);
            String userTip = StrUtil.format(INIT_DATASOURCE_CONTAINER_ERROR.getUserTip(), exception.getMessage());
            throw new DatasourceContainerException(INIT_DATASOURCE_CONTAINER_ERROR, userTip);
        }

    }

    /**
     * 配置其他的druid数据源配控制
     *
     * @author fengshuonan
     * @date 2022/9/15 11:00
     */
    private void setOtherDruidConfigs(ConfigurableEnvironment environment, DruidProperties druidProperties) {

        // 连接池的相关配置
        String initialSize = environment.getProperty("spring.datasource.initialSize");
        String maxActive = environment.getProperty("spring.datasource.maxActive");
        String minIdle = environment.getProperty("spring.datasource.minIdle");
        String maxWait = environment.getProperty("spring.datasource.maxWait");
        String poolPreparedStatements = environment.getProperty("spring.datasource.poolPreparedStatements");
        String maxPoolPreparedStatementPerConnectionSize = environment.getProperty("spring.datasource.maxPoolPreparedStatementPerConnectionSize");
        String validationQuery = environment.getProperty("spring.datasource.validationQuery");
        String validationQueryTimeout = environment.getProperty("spring.datasource.validationQueryTimeout");
        String testOnBorrow = environment.getProperty("spring.datasource.testOnBorrow");
        String testOnReturn = environment.getProperty("spring.datasource.testOnReturn");
        String testWhileIdle = environment.getProperty("spring.datasource.testWhileIdle");
        String keepAlive = environment.getProperty("spring.datasource.keepAlive");
        String timeBetweenEvictionRunsMillis = environment.getProperty("spring.datasource.timeBetweenEvictionRunsMillis");
        String minEvictableIdleTimeMillis = environment.getProperty("spring.datasource.minEvictableIdleTimeMillis");
        String filters = environment.getProperty("spring.datasource.filters");

        // 设置配置
        if (ObjectUtil.isNotEmpty(initialSize)) {
            Integer intValue = Convert.toInt(initialSize);
            druidProperties.setInitialSize(intValue);
        }

        if (ObjectUtil.isNotEmpty(maxActive)) {
            Integer intValue = Convert.toInt(maxActive);
            druidProperties.setMaxActive(intValue);
        }

        if (ObjectUtil.isNotEmpty(minIdle)) {
            Integer intValue = Convert.toInt(minIdle);
            druidProperties.setMinIdle(intValue);
        }

        if (ObjectUtil.isNotEmpty(maxWait)) {
            Integer intValue = Convert.toInt(maxWait);
            druidProperties.setMaxWait(intValue);
        }

        if (ObjectUtil.isNotEmpty(poolPreparedStatements)) {
            boolean booleanValue = Convert.toBool(poolPreparedStatements);
            druidProperties.setPoolPreparedStatements(booleanValue);
        }

        if (ObjectUtil.isNotEmpty(maxPoolPreparedStatementPerConnectionSize)) {
            Integer intValue = Convert.toInt(maxPoolPreparedStatementPerConnectionSize);
            druidProperties.setMaxPoolPreparedStatementPerConnectionSize(intValue);
        }

        if (ObjectUtil.isNotEmpty(validationQuery)) {
            druidProperties.setValidationQuery(validationQuery);
        }

        if (ObjectUtil.isNotEmpty(validationQueryTimeout)) {
            Integer intValue = Convert.toInt(validationQueryTimeout);
            druidProperties.setValidationQueryTimeout(intValue);
        }

        if (ObjectUtil.isNotEmpty(testOnBorrow)) {
            boolean booleanValue = Convert.toBool(testOnBorrow);
            druidProperties.setTestOnBorrow(booleanValue);
        }

        if (ObjectUtil.isNotEmpty(testOnReturn)) {
            boolean booleanValue = Convert.toBool(testOnReturn);
            druidProperties.setTestOnReturn(booleanValue);
        }

        if (ObjectUtil.isNotEmpty(testWhileIdle)) {
            boolean booleanValue = Convert.toBool(testWhileIdle);
            druidProperties.setTestWhileIdle(booleanValue);
        }

        if (ObjectUtil.isNotEmpty(keepAlive)) {
            boolean booleanValue = Convert.toBool(keepAlive);
            druidProperties.setKeepAlive(booleanValue);
        }

        if (ObjectUtil.isNotEmpty(timeBetweenEvictionRunsMillis)) {
            Integer intValue = Convert.toInt(timeBetweenEvictionRunsMillis);
            druidProperties.setTimeBetweenEvictionRunsMillis(intValue);
        }

        if (ObjectUtil.isNotEmpty(minEvictableIdleTimeMillis)) {
            Integer intValue = Convert.toInt(minEvictableIdleTimeMillis);
            druidProperties.setMinEvictableIdleTimeMillis(intValue);
        }

        if (ObjectUtil.isNotEmpty(filters)) {
            druidProperties.setFilters(filters);
        }
    }

}
