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
package cn.stylefeng.roses.kernel.db.api.constants;

/**
 * 数据库常用字段的枚举
 *
 * @author fengshuonan
 * @date 2020/10/16 17:07
 */
public interface DbFieldConstants {

    /**
     * 主键id的字段名
     */
    String ID = "id";

    /**
     * 创建用户的字段名
     */
    String CREATE_USER = "createUser";

    /**
     * 创建时间的字段名
     */
    String CREATE_TIME = "createTime";

    /**
     * 更新用户的字段名
     */
    String UPDATE_USER = "updateUser";

    /**
     * 更新时间的字段名
     */
    String UPDATE_TIME = "updateTime";

    /**
     * 删除标记的字段名
     */
    String DEL_FLAG = "delFlag";

    /**
     * 数据状态的字段
     * 状态：1-启用，2-禁用
     */
    String STATUS_FLAG = "statusFlag";

    /**
     * 乐观锁版本，从0开始
     */
    String VERSION_FLAG = "versionFlag";

    /**
     * 组织id
     */
    String ORG_ID = "orgId";
}
