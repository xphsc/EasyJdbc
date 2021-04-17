/*
 * Copyright (c) 2021  huipei.x
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
package com.xphsc.easyjdbc.core.metadata.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

/**
 * {@link BaseTypeHandler}
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:
 * @since 2.1.0
 */
public class LocalTimeTypeHandler extends BaseTypeHandler<LocalTime> {
    public LocalTimeTypeHandler() {
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, LocalTime parameter, JdbcType var4) throws SQLException {
        ps.setObject(i, parameter);
    }



    @Override
    public LocalTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return (LocalTime) rs.getObject(columnName, LocalTime.class);
    }

    @Override
    public LocalTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return (LocalTime) rs.getObject(columnIndex, LocalTime.class);
    }

    @Override
    public LocalTime getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return (LocalTime) cs.getObject(columnIndex, LocalTime.class);
    }
}