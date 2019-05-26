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
package com.xphsc.easyjdbc.core.metadata;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huipei.x
 */
public enum SQLOptionType {
    UNKNOWN,SQLSELECT, SQLUPDATE, SQLINSERT, SQLDELETE;

    private static final Map<String, SQLOptionType> MAPPINGS = new HashMap<>(2);

    static {
        for (SQLOptionType sqlOptionType : values()) {
            MAPPINGS.put(sqlOptionType.name(), sqlOptionType);
        }
    }

    public static SQLOptionType resolve(String sqlOptionType) {
        return sqlOptionType == null ? null : MAPPINGS.get(sqlOptionType);
    }

    public  boolean matches(String sqlOptionType) {
        return this == resolve(sqlOptionType);
    }

}
