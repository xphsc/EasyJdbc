package com.xphsc.easyjdbc.dialect;

/**
 * Created by ${huipei.x} on
 */
public class MySqlDialect  extends AbstractDialect {

    @Override
    public String pagination(String sql, int startRow, int size) {
    StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
    sqlBuilder.append(sql);
    if (startRow == 0) {
        sqlBuilder.append(" LIMIT ");
        sqlBuilder.append(size);
    } else {
        sqlBuilder.append(" LIMIT ");
        sqlBuilder.append(startRow);
        sqlBuilder.append(",");
        sqlBuilder.append(size);
    }
    return sqlBuilder.toString();
    }
}
