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
package com.xphsc.easyjdbc.core.metadata.resultset;


import com.xphsc.easyjdbc.core.metadata.type.TypeBinding;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.*;
import java.util.Date;


/**
 * {@link ResultSet}
 * @author <a href="xiongpeih@163.com">huipei.x</a>
 * @description:
 * @since 2.1.0
 */
public final class DefaultResultSet {


    /**
     * 从结果集ResultSet中取值
     */
    public static Object getResultValue(ResultSet rs,int columnIndex,int jdbcType,Class<?> type) throws SQLException {
        TypeBinding typeBinding=new TypeBinding();
        String typeName = type.getName();
        if("java.math.BigDecimal".equals(typeName)){
            return rs.getBigDecimal(columnIndex);
        }
        if("java.math.BigInteger".equals(typeName)){
            BigDecimal bigDecimal = rs.getBigDecimal(columnIndex);
            return bigDecimal == null ? null : bigDecimal.toBigInteger();
        }
        if("boolean".equals(typeName)){
            return rs.getBoolean(columnIndex);
        }
        if("java.lang.Boolean".equals(typeName)){
            return rs.getBoolean(columnIndex);
        }
        if("byte".equals(typeName)){
            return rs.getByte(columnIndex);
        }
        if("java.lang.Byte".equals(typeName)){
            return rs.getByte(columnIndex);
        }
        if("char".equals(typeName)){
            String columnValue = rs.getString(columnIndex);
            if (columnValue != null) {
                return columnValue.charAt(0);
            } else {
                return null;
            }
        }
        if("java.lang.Character".equals(typeName)){
            String columnValue = rs.getString(columnIndex);
            if (columnValue != null) {
                return columnValue.charAt(0);
            } else {
                return null;
            }
        }
        if("java.util.Date".equals(typeName)){
            if(Types.DATE==jdbcType){
                java.sql.Date sqlDate = rs.getDate(columnIndex);
                if (sqlDate != null) {
                    return new Date(sqlDate.getTime());
                }
                return null;
            }
            if(Types.TIMESTAMP==jdbcType){
                Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
                if (sqlTimestamp != null) {
                    return new Date(sqlTimestamp.getTime());
                }
                return null;
            }
            if(Types.TIME==jdbcType){
                java.sql.Time sqlTime = rs.getTime(columnIndex);
                if (sqlTime != null) {
                    return new Date(sqlTime.getTime());
                }
                return null;
            }
        }
        if("java.time.LocalDate".equals(typeName)) {
            typeBinding.initDelegateType(LocalDate.class);
            LocalDate localDate= (LocalDate) typeBinding.getDelegate().getNullableResult(rs, columnIndex);
            typeBinding.clearDelegate();
            return localDate;

        }
        if("java.time.LocalDateTime".equals(typeName)) {
            typeBinding.initDelegateType(LocalDateTime.class);
            LocalDateTime localDateTime= (LocalDateTime) typeBinding.getDelegate().getNullableResult(rs, columnIndex);
            typeBinding.clearDelegate();
            return localDateTime;

        }

        if("java.time.LocalTime".equals(typeName)) {
            typeBinding.initDelegateType(LocalTime.class);
            LocalTime localTime= (LocalTime)
            typeBinding.getDelegate().getNullableResult(rs, columnIndex);
            typeBinding.clearDelegate();
            return localTime;
        }


        if("double".equals(typeName)){
            return rs.getDouble(columnIndex);
        }
        if("java.lang.Double".equals(typeName)){
            if(null!=rs.getObject(columnIndex)){
                return rs.getDouble(columnIndex);}
            return null;
        }
        if("float".equals(typeName)){
            return rs.getFloat(columnIndex);
        }
        if("java.lang.Float".equals(typeName)){
            if(null!=rs.getObject(columnIndex)) {
                return rs.getFloat(columnIndex);
            }
            return null;
        }
        if("int".equals(typeName)){
            return rs.getInt(columnIndex);
        }
        if("java.lang.Integer".equals(typeName)){
            if(null!=rs.getObject(columnIndex)){
                return rs.getInt(columnIndex);}
            return null;
        }
        if("long".equals(typeName)){
            return rs.getLong(columnIndex);
        }
        if("java.lang.Long".equals(typeName)){
            if(null!=rs.getObject(columnIndex)){
                return rs.getLong(columnIndex);}
            return null;
        }
        if("short".equals(typeName)){
            return rs.getShort(columnIndex);
        }
        if("java.lang.Short".equals(typeName)){
            if(null!=rs.getObject(columnIndex)){
                return rs.getShort(columnIndex);}
            return null;
        }
        if("java.sql.Date".equals(typeName)){
            return rs.getDate(columnIndex);
        }
        if("java.sql.Timestamp".equals(typeName)){
            return rs.getTimestamp(columnIndex);
        }
        if("java.sql.Time".equals(typeName)){
            return rs.getTime(columnIndex);
        }
        if("java.time.Instant".equals(typeName)){
            typeBinding.initDelegateType(Instant.class);
            Instant instant= (Instant) typeBinding.getDelegate().getNullableResult(rs, columnIndex);
            typeBinding.clearDelegate();
            return instant;
        }
        if("java.time.OffsetDateTime".equals(typeName)){
            typeBinding.initDelegateType(OffsetDateTime.class);
            OffsetDateTime instant= (OffsetDateTime) typeBinding.getDelegate().getNullableResult(rs, columnIndex);
            typeBinding.clearDelegate();
            return instant;
        }
        if("java.time.OffsetTime".equals(typeName)){
            typeBinding.initDelegateType(OffsetTime.class);
            OffsetTime instant= (OffsetTime) typeBinding.getDelegate().getNullableResult(rs, columnIndex);
            typeBinding.clearDelegate();
            return instant;
        }
        if("java.time.Year".equals(typeName)){
            typeBinding.initDelegateType(Year.class);
            Year instant= (Year) typeBinding.getDelegate().getResult(rs, columnIndex);
            typeBinding.clearDelegate();
            return instant;
        }
        return rs.getObject(columnIndex);
    }



}
