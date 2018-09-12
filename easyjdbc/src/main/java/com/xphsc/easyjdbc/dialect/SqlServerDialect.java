package com.xphsc.easyjdbc.dialect;

/**
 * Created by ${huipei.x}
 */
public class SqlServerDialect  extends AbstractDialect {
    @Override
    public String pagination(String sql, int startRow, int size) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(sql);
        sqlBuilder.append(" OFFSET ");
        sqlBuilder.append(startRow);
        sqlBuilder.append(" ROWS ");
        sqlBuilder.append(" FETCH NEXT ");
        sqlBuilder.append(size);
        sqlBuilder.append(" ROWS ONLY");
        return sqlBuilder.toString();
    }
}
