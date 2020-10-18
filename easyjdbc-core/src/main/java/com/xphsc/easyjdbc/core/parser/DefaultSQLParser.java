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
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author huipei.x
 * @date  2018-8-20
 * @description
 */
public class DefaultSQLParser implements SQLParser {
    private static final String EMPTY = "";
    private TreeMap<String,Object> entityMap;
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
    public  Boolean hasOgnlPlaceHolder(String sqlString) {
        Boolean flag = false;
        Matcher m = OGNL_PLACE_HOLDER_PATTERN.matcher(sqlString);
        while (m.find()) {
            flag = true;
        }
        return flag;
    }


    @Override
    public  Boolean hasInsertOrUpdatePlaceHolder(String sqlString) {
        Boolean flag = false;
        Matcher m = INSERT_UPDATE_PLACE_HOLDER_PATTERN.matcher(sqlString);
        while (m.find()) {
            flag = true;
        }
        return flag;
    }


    @Override
    public  Object[] sqlPlaceHolder(String sql, Map<String, Object> params,boolean isOgnl) {
        Matcher m=null;
        List<String> matchRegexList = new ArrayList<String>(10);
        List<Object> list = new ArrayList<Object>();
        String newkey = null;
        if(isOgnl){
            m = OGNL_PLACE_HOLDER_PATTERN.matcher(sql);
        }else{
            m = INSERT_UPDATE_PLACE_HOLDER_PATTERN.matcher(sql);

        }
        while (m.find()) {
            matchRegexList.add(m.group());
        }
        Object val=null;
        for (String key : matchRegexList) {
            if(isOgnl){
                key = key.substring(1, key.length()).replace(" ", EMPTY);
                sql = sql.replaceFirst("\\:\\s*" + key + "\\s*", "? ");
                newkey=key;
                val = params.get(key);
            }else{
                key = key.substring(2, key.length() - 1).replace(" ", EMPTY);
                sql = sql.replaceFirst("\\#\\{" + key + "\\s*\\}", "? ");
                newkey=key;
                val = params.get(key);
            }
            if(val!=null){
                list.add(val);
            }
        }
        if(val==null){
            if(params!=null){
                Object value = params.get(StringUtil.substringBeforeLast(newkey, "."));
                entityMap= Beans.beanToTreeMap(value);
                for(Map.Entry<String,Object> entity:entityMap.entrySet()){
                    if(!"class".equals(entity.getKey()) &&entity.getValue()!=null){
                        list.add(entity.getValue());
                    }

                }
            }

        }
        return new Object[]{sql,list.toArray()};
    }


    @Override
    public Map<String,Object> entityMap(){
        return entityMap;
    }
    @Override
    public  String removeOrders(String sql) {
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
    public  Boolean hasOrders(String sql) {
        Boolean flag = false;
        Pattern p = Pattern.compile(REGEX_HASORDERS, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sql);
        while (m.find()) {
            flag = true;
        }
        return flag;
    }

    private static String REGEX_HASORDERS="order\\s*by[\\w|\\W|\\s|\\S]*";

    private static final Pattern FIELD_PLACE_HOLDER_PATTERN = Pattern.compile("\\#\\{\\s*\\w+\\s*\\}"); // 正则匹配 #{key}

    private static final Pattern OGNL_PLACE_HOLDER_PATTERN = Pattern.compile(":[ tnx0Bfr]*[0-9a-z.A-Z_]+"); // 正则匹配 :

    private static final Pattern INSERT_UPDATE_PLACE_HOLDER_PATTERN = Pattern.compile("#\\{[ tnx0Bfr]*[0-9a-z.A-Z_]+\\}"); // 正则匹配 #{key}

}
