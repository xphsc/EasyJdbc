package com.xphsc.easyjdbc.dialect;

/**
 * Created by ${huipei.x}
 */
public class HsqldbDialect  extends AbstractDialect {
    @Override
    public String pagination(String sql, int startRow, int size) {
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 20);
        sqlBuilder.append(sql);
        if (size > 0) {
            sqlBuilder.append(" LIMIT ");
            sqlBuilder.append(size);
        }
        if (startRow > 0) {
            sqlBuilder.append(" OFFSET ");
            sqlBuilder.append(startRow);
        }
        return sqlBuilder.toString();
    }
}
