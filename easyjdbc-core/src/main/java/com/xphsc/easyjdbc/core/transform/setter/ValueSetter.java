/*
 * Copyright (c) 2018 huipei.x
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xphsc.easyjdbc.core.transform.setter;


import com.xphsc.easyjdbc.core.metadata.ValueElement;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.lob.LobHandler;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


/**
 * 普通的PreparedStatement
 * Created by ${huipei.x}
 */
public class ValueSetter implements PreparedStatementSetter {

	private final LobHandler lobHandler;
	private final List<ValueElement> valueElements;

	public ValueSetter(LobHandler lobHandler, List<ValueElement> valueElements) {
		this.lobHandler = lobHandler;
		this.valueElements = valueElements;
	}

	@Override
	public void setValues(PreparedStatement ps) throws SQLException {
		for (int i = 0; i < this.valueElements.size(); i++) {
			int paramIndex = i + 1;
			ValueElement param = this.valueElements.get(i);
			if (param.isClob()) {
				if (null != param.getValue()) {
					this.lobHandler.getLobCreator().setClobAsString(ps, paramIndex, (String) param.getValue());
				} else {
					ps.setObject(paramIndex, null);
				}
			} else if (param.isBlob()) {
				if (null != param.getValue()) {
					this.lobHandler.getLobCreator().setBlobAsBytes(ps, paramIndex, (byte[]) param.getValue());
				} else {
					ps.setObject(paramIndex, null);
				}
			} else {
				ps.setObject(paramIndex, param.getValue());
			}
		}
	}
}