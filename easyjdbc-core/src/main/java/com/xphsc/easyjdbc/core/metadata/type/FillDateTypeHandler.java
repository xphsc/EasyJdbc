/*
 * Copyright (c) 2020  huipei.x
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
package com.xphsc.easyjdbc.core.metadata.type;

import com.xphsc.easyjdbc.core.metadata.FieldElement;

import java.time.*;
import java.util.Date;

/**
 * {@link FieldElement}
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: FillDate类型处理器类，用于处理 JDBC 时间类型转换
 * @since 2.0.5
 */

public class FillDateTypeHandler {

    public static Object fillDate(FieldElement fieldElement) {
        Object value = null;
        if (LocalDate.class.equals(fieldElement.getType())) {
            value = LocalDate.now();
        } else if (fieldElement.getType().equals(Date.class)) {
            value = new Date();
        } else if (LocalDateTime.class.equals(fieldElement.getType())) {
            value = LocalDateTime.now();
        } else if (LocalTime.class.equals(fieldElement.getType())) {
            value = LocalTime.now();
        } else if (Instant.class.equals(fieldElement.getType())) {
            value = Instant.now();
        } else if (OffsetDateTime.class.equals(fieldElement.getType())) {
            value = OffsetDateTime.now();
        } else if (OffsetTime.class.equals(fieldElement.getType())) {
            value = OffsetTime.now();
        }
        return value;
    }
}
