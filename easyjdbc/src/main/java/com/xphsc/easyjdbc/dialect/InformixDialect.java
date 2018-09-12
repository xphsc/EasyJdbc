package com.xphsc.easyjdbc.dialect;

/**
 * Created by ${huipei.x}
 */
public class InformixDialect extends AbstractDialect{


    @Override
    public String pagination(String sql, int startRow, int size) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 40);
        sqlBuilder.append("SELECT ");
        if(startRow > 0) {
            sqlBuilder.append(" SKIP ? ");
            sqlBuilder.append(startRow);
        }
        if(size > 0) {
            sqlBuilder.append(" FIRST ? ");
            sqlBuilder.append(size);
        }
        sqlBuilder.append(" * FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) TEMP_T ");
        return sqlBuilder.toString();
    }


}
