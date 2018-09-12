package com.xphsc.easyjdbc.dialect;

/**
 * Created by ${huipei.x}
 */
public class PostgreSqlDialect  extends AbstractDialect {
    @Override
    public String pagination(String sql, int startRow, int size) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(sql);
        if (startRow == 0) {
            sqlBuilder.append(" LIMIT ");
            sqlBuilder.append(size);
        } else {
            sqlBuilder.append(" LIMIT ");
            sqlBuilder.append(size);
            sqlBuilder.append(" OFFSET ");
            sqlBuilder.append(startRow);
        }
        return sqlBuilder.toString();
    }
}
