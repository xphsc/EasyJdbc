/*
 * Copyright (c) 2018-2019  huipei.x
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xphsc.easyjdbc.core.metadata.resolver;

import com.xphsc.easyjdbc.core.metadata.Element;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.util.Assert;

import java.lang.annotation.Annotation;

/**
 * @author huipei.x
 * @date 创建时间 2019-1-20
 * @description 类说明 :
 */
public class VersionResolver implements Resolver{
    @Override
    public void resolve(Element element, Annotation annotation) {
        FieldElement fieldElement = (FieldElement)element;
        EntityElement entityElement = fieldElement.getEntityElement();
        Assert.isNull(entityElement.getVersion()
                , "实体：" + fieldElement.getName() + "只能有一个Version");
        fieldElement.setVersion(Boolean.TRUE);
        entityElement.setVersion(fieldElement);
    }
}
