package com.xphsc.easyjdbc.core.metadata.type;

import com.xphsc.easyjdbc.core.exception.EasyJdbcException;

import java.sql.*;

/**
 * {@link }
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description: 抽象类型处理器类，用于处理 JDBC 类型转换
 * 继承自 TypeReflect 并实现 TypeHandler 接口
 * @since 2.1.0
 */
public abstract class BaseTypeHandler<T> extends TypeReflect<T> implements TypeHandler<T> {

    public BaseTypeHandler() {
    }


    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            if (jdbcType == null) {
                throw new EasyJdbcException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
            }

            try {
                ps.setNull(i, jdbcType.TYPE);
            } catch (SQLException var7) {
                throw new EasyJdbcException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. Cause: " + var7, var7);
            }
        } else {
            try {
                this.setNonNullParameter(ps, i, parameter, jdbcType);
            } catch (Exception var6) {
                throw new EasyJdbcException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType + " . Try setting a different JdbcType for this parameter or a different configuration property. Cause: " + var6, var6);
            }
        }

    }

    @Override
    public T getResult(ResultSet rs, String columnName) throws SQLException {
        try {
            return this.getNullableResult(rs, columnName);
        } catch (Exception var4) {
            throw new EasyJdbcException("Error attempting to get column '" + columnName + "' from result set.  Cause: " + var4, var4);
        }
    }

    @Override
    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        try {
            return this.getNullableResult(rs, columnIndex);
        } catch (Exception var4) {
            throw new EasyJdbcException("Error attempting to get column #" + columnIndex + " from result set.  Cause: " + var4, var4);
        }
    }

    @Override
    public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
        try {
            return this.getNullableResult(cs, columnIndex);
        } catch (Exception var4) {
            throw new EasyJdbcException("Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + var4, var4);
        }
    }

    public abstract void setNonNullParameter(PreparedStatement var1, int var2, T var3, JdbcType var4) throws SQLException;

    public abstract T getNullableResult(ResultSet var1, String var2) throws SQLException;

    public abstract T getNullableResult(ResultSet var1, int var2) throws SQLException;

    public abstract T getNullableResult(CallableStatement var1, int var2) throws SQLException;
}