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
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: 默认的SQL选项类型解析器类
 * 该类负责解析方法上标注的SQL相关注解，以确定方法对应的SQL命令类型
 */
public interface SQLParser {

      Boolean hasFieldPlaceHolder(String sqlString);
      Boolean hasOgnlPlaceHolder(String sqlString);
      Boolean hasObjectPlaceHolder(String sqlString);
      Object[] sqlPlaceHolder(String sql, Map<String, Object> params, boolean isOgnl);
      String removeOrders(String sql);
      Boolean hasOrders(String sql);
      Map<String,Object> entityMap();
}
