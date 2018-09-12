package com.xphsc.easyjdbc.executor;


import com.xphsc.easyjdbc.core.exception.JdbcDataException;

/**
 *  执行器接口
 * Created by ${huipei.x}
 */
public interface Executor<T> {

	T execute() throws JdbcDataException;

}