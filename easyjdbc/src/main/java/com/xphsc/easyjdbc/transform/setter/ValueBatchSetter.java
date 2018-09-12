package com.xphsc.easyjdbc.transform.setter;


import com.xphsc.easyjdbc.core.metadata.ValueElement;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.support.lob.LobHandler;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;


/**
 *  支持批量处理的PreparedStatement
 * Created by ${huipei.x}
 */
public class ValueBatchSetter implements BatchPreparedStatementSetter {

	private final LobHandler lobHandler;
	private final LinkedList<LinkedList<ValueElement>> batchValueElements;

	public ValueBatchSetter(LobHandler lobHandler
				,LinkedList<LinkedList<ValueElement>> batchValueElements) {
		this.lobHandler = lobHandler;
		this.batchValueElements = batchValueElements;
	}
	
	@Override
	public void setValues(PreparedStatement ps,int i) throws SQLException {

		LinkedList<ValueElement> valueElements = this.batchValueElements.get(i);
		for (int j = 0; j < valueElements.size(); j++) {
			int paramIndex = j+1;
			ValueElement param = valueElements.get(j);
			if(param.isClob()){
				if(null != param.getValue()){
					this.lobHandler.getLobCreator().setClobAsString(ps,paramIndex,(String)param.getValue());
				} else {
					ps.setObject(paramIndex, null);
				}
			} else if(param.isBlob()){
				if(null != param.getValue()){
					this.lobHandler.getLobCreator().setBlobAsBytes(ps, paramIndex, (byte[])param.getValue());
				} else {
					ps.setObject(paramIndex, null);
				}
			} else {
				ps.setObject(paramIndex, param.getValue());
			}
		}
	}

	@Override
	public int getBatchSize() {
		return this.batchValueElements.size();
	}
	
}