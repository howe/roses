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
package cn.stylefeng.roses.kernel.springdoc.converter;

import cn.stylefeng.roses.kernel.rule.annotation.ChineseDescription;
import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverter;
import io.swagger.v3.core.converter.ModelConverterContext;
import io.swagger.v3.oas.models.media.Schema;

import java.lang.annotation.Annotation;
import java.util.Iterator;
import java.util.Objects;

/**
 * 文档转换
 *
 * @author caiti
 * @date 2022-11-11
 */
public class GunsModelConverterImpl implements ModelConverter {

    @Override
    public Schema resolve(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> chain) {
        Schema schema = next(annotatedType, context, chain);
        Annotation[] ctxAnnotations = annotatedType.getCtxAnnotations();
        if (Objects.isNull(ctxAnnotations)) {
            return schema;
        }
        ChineseDescription chineseDescription = null;
        for (Annotation ctxAnnotation : ctxAnnotations) {
            if (ctxAnnotation instanceof ChineseDescription) {
                chineseDescription = (ChineseDescription) ctxAnnotation;
                break;
            }
        }
        if (chineseDescription == null) {
            return schema;
        }

        if (schema != null) {
            schema.setDescription(chineseDescription.value());
        }
        return schema;
    }

    private Schema next(AnnotatedType annotatedType, ModelConverterContext context, Iterator<ModelConverter> chain) {
        if (chain.hasNext()) {
            return chain.next().resolve(annotatedType, context, chain);
        } else {
            return null;
        }
    }

}
