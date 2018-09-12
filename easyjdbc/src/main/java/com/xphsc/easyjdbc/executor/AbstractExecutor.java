package com.xphsc.easyjdbc.executor;


import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.core.metadata.IdGenerators;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.EasyJdbcHelper;
import com.xphsc.easyjdbc.util.LogUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;
import java.util.UUID;

/**
 *  抽象执行器
 * Created by ${huipei.x}
 */
public abstract class AbstractExecutor<T> implements Executor<T> {
	protected final LogUtil logger = LogUtil.getLogger(getClass());
	protected static final LobHandler LOBHANDLER = new DefaultLobHandler();
	protected final JdbcTemplate jdbcTemplate;
	public AbstractExecutor(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public T execute() throws JdbcDataException {
		prepare();
		return doExecute();
	}


	protected abstract void prepare();
	protected abstract T doExecute() throws JdbcDataException;
	protected Object generatedId(Object persistent,FieldElement fieldElement,Object value){
		if((null == value||"".equals(value))
				&& fieldElement.isGeneratedValue()
				&& IdGenerators.UUID.equals(fieldElement.getGenerator())){
			String uuid = UUID.randomUUID().toString().replaceAll("-", "");
			String errorMsg = "实体："+persistent.getClass().getName()+" 主键："+fieldElement.getName()+" 设置值失败";
			EasyJdbcHelper.invokeMethod(persistent, fieldElement.getWriteMethod(), errorMsg, uuid);
			return uuid;
		}
		return value;	
	}
	
	protected boolean isEntity(Class<?> persistentClass){
		return null != persistentClass.getAnnotation(javax.persistence.Entity.class);	
	}
	
	protected void checkEntity(Class<?> persistentClass){
		Assert.isTrue(isEntity(persistentClass), persistentClass + " 如果是实体类型请使用@Entity注解进行标识");
	}

	protected void getParameters(Object[] parameters){
		String  params="";
		for(Object parameter:parameters){
			params+=parameter+",";
		}
		params = params.substring(0, params.length()-1);
		logger.debug("parameters: " + params + " ");
	}

}