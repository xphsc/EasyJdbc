package com.xphsc.easyjdbc.dialect;

/**
 * Created by ${huipei.x}
 */
public abstract class AbstractDialect implements Dialect{
    @Override
    public abstract String pagination(String sql, int startRow, int size);

}
