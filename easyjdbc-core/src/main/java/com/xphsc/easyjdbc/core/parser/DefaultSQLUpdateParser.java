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

import com.xphsc.easyjdbc.util.StringUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description
 */
public class DefaultSQLUpdateParser implements SQLUpdateParser {

    @Override
    public String sqlProvider(String sql, SQLParser sqlParser) {
        int sqlStartIndex=  sql.toUpperCase().indexOf("SET");
        int sqlEndIndex=  sql.toUpperCase().indexOf("WHERE");
        int whereIndex=  sql.toUpperCase().indexOf("WHERE");
        String setValue= StringUtil.toCamelCase(sql.substring(sqlStartIndex, sqlEndIndex).replace("SET", ""));
        String whereValue= StringUtil.toCamelCase(sql.substring(whereIndex));
        String updateSql=  sql.substring(sql.toUpperCase().indexOf("UPDATE"), sql.toUpperCase().indexOf("SET"));
        Map setMap=new HashMap<>();
        Map whereMap=new HashMap<>();
        for(Map.Entry<String,Object> entity:sqlParser.entityMap().entrySet()){
            if(setValue.contains(entity.getKey())) {
                Object value=null;
                if(entity.getValue() instanceof Date){
                    Date sqlDate = (Date) entity.getValue();
                    if (sqlDate != null) {
                         value=QUOTATION+new java.sql.Date(sqlDate.getTime())+QUOTATION;
                    }
                }

               else if(entity.getValue() instanceof Boolean){
                        value=QUOTATION+Boolean.valueOf(entity.getValue().toString())+QUOTATION;
                }
               else if(entity.getValue() instanceof Integer){
                    value=QUOTATION+Integer.valueOf(entity.getValue().toString())+QUOTATION;
                }
              else  if(entity.getValue() instanceof Float){
                    value=QUOTATION+Float.valueOf(entity.getValue().toString())+QUOTATION;
                }
                else if(entity.getValue() instanceof Long){
                    value=QUOTATION+Long.valueOf(entity.getValue().toString())+QUOTATION;
                }
               else if(entity.getValue() instanceof Short){
                    value=QUOTATION+Short.valueOf(entity.getValue().toString())+QUOTATION;
                }
                else if(entity.getValue() instanceof String){
                    value=QUOTATION+String.valueOf(entity.getValue().toString())+QUOTATION;
                }

                else{
                    value=entity.getValue();
                }
                setMap.put(StringUtil.toUnderScoreCase(entity.getKey()), value);
            }
            if(whereValue.contains(entity.getKey())) {
                whereMap.put(StringUtil.toUnderScoreCase(entity.getKey()), entity.getValue());
            }
        }
        StringBuilder sqlBuilder=new StringBuilder();
        sqlBuilder.append(updateSql);
        sqlBuilder.append("SET " + setMap.toString().replace(LEFT_BRACE, EMPTY).replace(RIGHT_BRACE, EMPTY));
        sqlBuilder.append(" WHERE " + whereMap.toString().replace(LEFT_BRACE, EMPTY).replace(RIGHT_BRACE, EMPTY));
        return sqlBuilder.toString();
    }
    private static String LEFT_BRACE = "{";
    private static String RIGHT_BRACE = "}";
    private static String EMPTY = "";
    private static String QUOTATION = "'";

}
