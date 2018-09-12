package com.xphsc.easyjdbc.transform.setter;


import com.xphsc.easyjdbc.core.metadata.ValueElement;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.lob.LobHandler;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;


/**
 * 普通的PreparedStatement
 * Created by ${huipei.x}
 */
public class ValueSetter implements PreparedStatementSetter {
	
	private final LobHandler lobHandler;
	private final LinkedList<ValueElement> valueElements;

	public ValueSetter(LobHandler lobHandler,LinkedList<ValueElement> valueElements) {
		this.lobHandler = lobHandler;
		this.valueElements = valueElements;
	}

	@Override
	public void setValues(PreparedStatement ps) throws SQLException {
		for (int i = 0; i < this.valueElements.size(); i++) {
			int paramIndex = i+1;
			ValueElement param = this.valueElements.get(i);
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
}