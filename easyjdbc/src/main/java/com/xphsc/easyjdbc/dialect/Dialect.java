package com.xphsc.easyjdbc.dialect;

/**
 * Created by ${huipei.x}
 */
public interface Dialect {
     public String pagination(String sql, int startRow, int size);

}
