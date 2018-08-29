package com.xphsc.easyjdbc.sql;

import com.github.xtool.collect.Maps;
import com.xphsc.easyjdbc.builder.SQL;

import java.util.Map;


/**
 * @author huipei.x
 * @data 创建时间 2018/8/27
 * @description 类说明 :
 */
public class UserSqlProvider {

    public String listUserBySql(){
        return SQL.BUILD().
                SELECT("*").
                FROM("t_user").
                toString();
    }

    public String getUserBySql(){
        return SQL.BUILD().
                SELECT("*").
                FROM("t_user").
                WHERE("id=#{id}").
                toString();
    }
    public String listUserBySqlMap(Map map){
        Maps.containsKey(map,"id");
        SQL sql=SQL.BUILD();
        sql.SELECT("*").
                FROM("t_user");
        if(Maps.containsKey(map,"id")){
            if(Maps.getInteger(map,"id")!=null){
                sql.WHERE("id=#{id}");
            }
        }

        return sql.toString();
    }

}
