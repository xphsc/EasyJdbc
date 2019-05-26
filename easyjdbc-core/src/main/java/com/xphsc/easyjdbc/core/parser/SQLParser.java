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
package com.xphsc.easyjdbc.core.parser;

import java.util.Map;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description
 */
public interface SQLParser {

      Boolean hasFieldPlaceHolder(String sqlString);
      Boolean hasOgnlPlaceHolder(String sqlString);
      Boolean hasInsertOrUpdatePlaceHolder(String sqlString);
      Object[] sqlPlaceHolder(String sql, Map<String, Object> params, boolean isOgnl);
      String removeOrders(String sql);
      Boolean hasOrders(String sql);
     Map<String,Object> entityMap();
}
