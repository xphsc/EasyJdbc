package com.xphsc.easyjdbc.dialect;

/**
 * Created by ${huipei.x}
 */
public class Db2Dialect extends AbstractDialect {
    @Override
    public String pagination(String sql, int startRow, int size) {

        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        sqlBuilder.append("SELECT * FROM (SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) AS TMP_PAGE) TMP_PAGE WHERE ROW_ID BETWEEN ");
        sqlBuilder.append(startRow + 1);
        sqlBuilder.append(" AND ");
        sqlBuilder.append(size);
        return sqlBuilder.toString();
    }
}
