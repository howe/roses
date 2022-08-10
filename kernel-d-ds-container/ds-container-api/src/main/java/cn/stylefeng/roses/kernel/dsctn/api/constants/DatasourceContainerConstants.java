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
package cn.stylefeng.roses.kernel.dsctn.api.constants;

/**
 * 数据源容器的常量
 *
 * @author fengshuonan
 * @date 2020/10/31 21:58
 */
public interface DatasourceContainerConstants {

    /**
     * db模块的名称
     */
    String DS_CTN_MODULE_NAME = "kernel-d-ds-container";

    /**
     * 异常枚举的步进值
     */
    String DS_CTN_EXCEPTION_STEP_CODE = "16";

    /**
     * 主数据源名称
     */
    String MASTER_DATASOURCE_NAME = "master";

    /**
     * 多数据源切换的aop的顺序
     */
    int MULTI_DATA_SOURCE_EXCHANGE_AOP = 1;

    /**
     * 数据源的分组标识
     */
    String DATASOURCE_GROUP_CODE = "DATASOURCE";

}
