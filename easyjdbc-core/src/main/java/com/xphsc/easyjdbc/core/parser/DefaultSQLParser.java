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



import com.xphsc.easyjdbc.util.Beans;
import com.xphsc.easyjdbc.util.StringUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: 默认的SQL选项类型解析器实现类
 * 该类负责解析方法上标注的SQL相关注解，以确定方法对应的SQL命令类型
 */
public class DefaultSQLParser implements SQLParser {
    private static final String EMPTY = "";
    private Map<String, Object> objectMap;

    @Override
    public Boolean hasFieldPlaceHolder(String sqlString) {
        Boolean flag = false;
        Matcher m = FIELD_PLACE_HOLDER_PATTERN.matcher(sqlString);
        while (m.find()) {
            flag = true;
        }
        return flag;
    }

    @Override
    public Boolean hasOgnlPlaceHolder(String sqlString) {
        Boolean flag = false;
        Matcher m = OGNL_PLACE_HOLDER_PATTERN.matcher(sqlString);
        while (m.find()) {
            flag = true;
        }
        return flag;
    }


    @Override
    public Boolean hasObjectPlaceHolder(String sqlString) {
        Boolean flag = false;
        Matcher m = OBJECT_PLACE_HOLDER_PATTERN.matcher(sqlString);
        while (m.find()) {
            flag = true;
        }
        return flag;
    }


    @Override
    public Object[] sqlPlaceHolder(String sql, Map<String, Object> params, boolean isOgnl) {
        Matcher m = null;
        List<String> matchRegexList = new ArrayList<String>(10);
        List<Object> list = new ArrayList<Object>();
        String newkey = null;
        if (isOgnl) {
            m = OGNL_PLACE_HOLDER_PATTERN.matcher(sql);
        } else {
            m = OBJECT_PLACE_HOLDER_PATTERN.matcher(sql);

        }
        while (m.find()) {
            matchRegexList.add(m.group());
        }
        Object val = null;
        for (String key : matchRegexList) {
            if (isOgnl) {
                key = key.substring(1, key.length()).replace(" ", EMPTY);
                sql = sql.replaceFirst("\\:\\s*" + key + "\\s*", "? ");
                newkey = key;
                val = params.get(key);
            } else {
                key = key.substring(2, key.length() - 1).replace(" ", EMPTY);
                sql = sql.replaceFirst("\\#\\{" + key + "\\s*\\}", "? ");
                newkey = key;
                val = params.get(key);
            }
            if (val != null) {
                list.add(val);
            }

        }

        if (val == null) {
            if (params != null) {
                Object value = params.get(StringUtil.substringBeforeLast(newkey, "."));
                if (sql.toUpperCase().trim().startsWith("UPDATE")) {
                    // 提取字段顺序
                    String upperSql = sql.toUpperCase();
                    int setStart = upperSql.indexOf("SET") + 3;
                    int whereStart = upperSql.indexOf("WHERE");
                    String setPart = sql.substring(setStart, whereStart).trim(); // user_name=?, age=?
                    String wherePart = sql.substring(whereStart + 5).trim();     // id=?
                    List<String> fields = new ArrayList<>();

                    for (String pair : setPart.split(",")) {
                        String field = pair.split("=")[0].trim();
                        fields.add(field);
                    }
                    for (String cond : wherePart.split("AND|OR")) {
                        String field = cond.split("=")[0].replaceAll("[()]", "").trim();
                        fields.add(field);
                    }
                    // 转换为 Map
                    Map<String, Object> entityMap = Beans.beanToMap(value);
                    for (String field : fields) {
                        String javaField =StringUtil.toCamelCase(field);
                        Object paramValue = entityMap.get(javaField);
                        list.add(paramValue);
                    }
                } else {
                    Map<String, Object> entityMap = Beans.beanToMap(value);
                    objectMap = entityMap;
                    for (Map.Entry<String, Object> entity : entityMap.entrySet()) {
                        if (!"class".equals(entity.getKey()) && entity.getValue() != null) {
                            list.add(entity.getValue());
                        }

                    }
                }


            }

        }
        return new Object[]{sql, list.toArray()};
    }


    @Override
    public Map<String, Object> entityMap() {
        return objectMap;
    }

    @Override
    public String removeOrders(String sql) {
        Pattern p = Pattern.compile(REGEX_HASORDERS, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            m.appendReplacement(sb, "");
        }
        m.appendTail(sb);
        return sb.toString();
    }


    @Override
    public Boolean hasOrders(String sql) {
        Boolean flag = false;
        Pattern p = Pattern.compile(REGEX_HASORDERS, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        while (m.find()) {
            flag = true;
        }
        return flag;
    }

    private static String REGEX_HASORDERS = "order\\s*by[\\w|\\W|\\s|\\S]*";
    private static String UPDATE = "UPDATE";
    private static final Pattern FIELD_PLACE_HOLDER_PATTERN = Pattern.compile("\\#\\{\\s*\\w+\\s*\\}"); // 正则匹配 #{key}

    private static final Pattern OGNL_PLACE_HOLDER_PATTERN = Pattern.compile(":[ tnx0Bfr]*[0-9a-z.A-Z_]+"); // 正则匹配 :

    private static final Pattern OBJECT_PLACE_HOLDER_PATTERN = Pattern.compile("#\\{[ tnx0Bfr]*[0-9a-z.A-Z_]+\\}"); // 正则匹配 #{key}

}
