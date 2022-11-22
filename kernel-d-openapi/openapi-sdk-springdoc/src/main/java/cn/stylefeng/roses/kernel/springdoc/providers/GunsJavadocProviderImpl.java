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
package cn.stylefeng.roses.kernel.springdoc.providers;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import cn.stylefeng.roses.kernel.scanner.api.annotation.ApiResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.GetResource;
import cn.stylefeng.roses.kernel.scanner.api.annotation.PostResource;
import org.springdoc.core.providers.JavadocProvider;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 文档转换
 *
 * @author caiti
 * @date 2022-11-11
 */
public class GunsJavadocProviderImpl implements JavadocProvider {

    @Override
    public String getClassJavadoc(Class<?> cl) {
        ApiResource annotation = AnnotatedElementUtils.getMergedAnnotation(cl, ApiResource.class);
        if (annotation != null) {
            return annotation.name();
        }
        return null;
    }

    @Override
    public String getMethodJavadocDescription(Method method) {
        return getResourceName(method);
    }

    @Override
    public String getMethodJavadocReturn(Method method) {
        return getResourceName(method);
    }

    @Nullable
    private String getResourceName(Method method) {
        GetResource getAnnotation = AnnotatedElementUtils.getMergedAnnotation(method, GetResource.class);
        if (getAnnotation != null) {
            return getAnnotation.name();
        }
        PostResource postAnnotation = AnnotatedElementUtils.getMergedAnnotation(method, PostResource.class);
        if (postAnnotation != null) {
            return postAnnotation.name();
        }
        ApiResource apiAnnotation = AnnotatedElementUtils.getMergedAnnotation(method, ApiResource.class);
        if (apiAnnotation != null) {
            return apiAnnotation.name();
        }
        return null;
    }

    @Override
    public Map<String, String> getMethodJavadocThrows(Method method) {

        return null;
    }

    @Override
    public String getParamJavadoc(Method method, String name) {
        return null;
    }

    @Override
    public String getFieldJavadoc(Field field) {
        ChineseDescription annotation = AnnotatedElementUtils.getMergedAnnotation(field, ChineseDescription.class);
        if (annotation != null) {
            return annotation.value();
        }
        return null;
    }

    @Override
    public String getFirstSentence(String text) {
        return text;
    }

}
