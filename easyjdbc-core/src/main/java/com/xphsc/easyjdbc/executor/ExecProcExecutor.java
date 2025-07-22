package com.xphsc.easyjdbc.executor;

import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.lambda.LambdaSupplier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.*;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huipei.x
 */
public class ExecProcExecutor<E> extends AbstractExecutor<E> {

    private final Class<?> persistentClass;
    private final String sql;
    private final Object[] parameters;
    private Map<Integer, Integer> outParameters;

    public <S> ExecProcExecutor(LambdaSupplier<S> jdbcBuilder, String sql, Class<?> persistentClass, Map<Integer, Integer> outParameters, Object[] parameters) {
        super(jdbcBuilder);
        this.persistentClass = persistentClass;
        this.sql = sql;
        this.parameters = parameters;
        this.outParameters = outParameters;
    }

    @Override
    protected void prepare() {
    }


    @Override
    protected E doExecute() throws JdbcDataException {
        final Map procResult = new HashMap();
        jdbcBuilder.execute(new CallableStatementCreator() {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException {
                CallableStatement cs = con.prepareCall(sql);
                if (parameters != null) {
                    for (int i = 0; i < parameters.length; i++) {
                        cs.setObject(i + 1, parameters[i]);
                    }
                }
                if (outParameters != null) {
                    for (Integer key : outParameters.keySet()) {
                        cs.registerOutParameter(key, outParameters.get(key));
                    }
                }
                return cs;
            }
        }, new CallableStatementCallback() {
            @Override
            public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                cs.execute();
                if (outParameters != null) {
                    Map<Integer, Object> outParameterResult = new HashMap<Integer, Object>();
                    for (Integer key : outParameters.keySet()) {
                        outParameterResult.put(key, cs.getObject(key));
                    }
                    procResult.put("outParameters", outParameterResult);
                }
                procResult.put("updateCount", cs.getUpdateCount());
                ResultSet result = cs.getResultSet();
                List<Object> list = new ArrayList();
                if (result != null) {
                    RowMapper<?> rowMapper = null;
                    if (Map.class.isAssignableFrom(persistentClass)) {
                        rowMapper = (RowMapper<?>) new ColumnMapRowMapper();
                    } else if (String.class.equals(persistentClass) || Integer.class.equals(persistentClass) || Long.class.equals(persistentClass)) {
                        rowMapper = new SingleColumnRowMapper(persistentClass);
                    } else {
                        rowMapper = new BeanPropertyRowMapper(persistentClass);
                    }
                    int rowNum = 1;
                    while (result.next()) {
                        list.add(rowMapper.mapRow(result, rowNum++));
                    }
                    procResult.put("list", list);
                }
                return null;
            }
        });
        return (E) procResult;

    }
}
