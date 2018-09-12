package com.xphsc.easyjdbc.builder;

/**
 * Created by ${huipei.x}
 */
public class SQL extends AbstractSQL<SQL> {
    public SQL(){}
    public static SQL BUILD(){
        return new SQL();
    }
    @Override
    public SQL getSelf() {
        return this;
    }
}