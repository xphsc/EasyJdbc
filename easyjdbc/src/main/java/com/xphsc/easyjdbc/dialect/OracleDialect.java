package com.xphsc.easyjdbc.dialect;

/**
 * Created by ${huipei.x} on 2018-6-22.
 */
public class OracleDialect  extends AbstractDialect {
    @Override
    public String pagination(String sql, int startRow, int size) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        int endRow = startRow + size;
        sqlBuilder.append("SELECT * FROM ( SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) TMP_PAGE WHERE ROWNUM <= " + endRow + " ) WHERE ROW_ID > " + startRow);
        return sqlBuilder.toString();
    }

}
