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
package cn.stylefeng.roses.kernel.scanner.api.pojo.resource;

import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * 字典描述类
 *
 * @author fengshuonan
 * @date 2020/12/8 18:25
 */
@Data
public class FieldMetadata {

    /**
     * 生成给前端用
     * <p>
     * uuid，标识一个字段的唯一性
     */
    private String metadataId;

    /**
     * 字段中文名称，例如：创建用户
     */
    private String chineseName;

    /**
     * 字段类型，例如：String
     */
    private String fieldClassType;

    /**
     * 字段名称，例如：createUser
     */
    private String fieldName;

    /**
     * 字段的注解，例如：NotBlack，NotNull
     */
    private Set<String> annotations;

    /**
     * 按校验组分的注解集合
     * <p>
     * 例如：
     * key = add, value = [不能为空，最大多少位，邮箱类型]
     */
    private Map<String, Set<String>> groupValidationMessage;

    /**
     * 校验信息的提示信息
     */
    private String validationMessages;

    /**
     * 泛型或object类型的字段的描述
     */
    private Set<FieldMetadata> genericFieldMetadata;

}