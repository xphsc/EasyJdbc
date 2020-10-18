/*
 * Copyright (c) 2018-2019  huipei.x
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
package com.xphsc.easyjdbc.core.metadata;

import javax.persistence.GenerationType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *  实体字段
 * Created by ${huipei.x}
 */
public class FieldElement implements Element {

	private static final long serialVersionUID = -4512772965217968519L;

	private String name;//字段名
	private String column;//列名
	private Class<?> type;//字段类型
	private Method readMethod;
	private Method writeMethod;
	//annotation Id
	private boolean primaryKey;
	//annotation GeneratedValueResolver
	private boolean generatedValue;
	private GenerationType strategy;
	private String generator;
	//annotation ColumnResolver
	private int precision;
	private int scale;
	private boolean nullable;//是否允许为null,默认为true
	private boolean unique;//是否唯一,默认为false
	private int length;//字段的长度,仅对String类型的字段有效
	private String columnDefinition;//表示该字段在数据库中的实际类型 通常ORM框架可以根据属性类型自动判断数据库中字段的类型,
					//但是对于Date类型仍无法确定数据库中字段类型究竟是DATE,TIME还是TIMESTAMP,
					//此外,String的默认映射类型为VARCHAR,如果要将String类型映射到特定数据库的BLOB或TEXT字段类型,该属性非常有用
					//如: @ColumnResolver(name="BIRTH",nullable="false",columnDefinition="DATE")
	private boolean insertable;//默认情况下,JPA持续性提供程序假设所有列始终包含在 SQL INSERT 语句中。如果该列不应包含在这些语句中，请将 insertable 设置为 false
	private boolean updatable;//列始终包含在 SQL UPDATE 语句中。如果该列不应包含在这些语句中，请将 updatable 设置为 false
	private String table;//实体的所有持久字段都存储到一个其名称为实体名称的数据库表中,如果该列与 @SecondaryTable表关联需将 name 设置为相应辅助表名称的String名称
	private boolean clob;//是否二进制clob类型
	private boolean blob;//是否二进制blob类型
	private boolean transientField;
	private boolean version;
	private boolean CreatedDateField;
	private boolean ModifieDateField;
	private EntityElement entityElement;
	private Field field;


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColumn() {
		return column;
	}
	public void setColumn(String column) {
		this.column = column;
	}
	public Class<?> getType() {
		return type;
	}
	public void setType(Class<?> type) {
		this.type = type;
	}
	public Method getReadMethod() {
		return readMethod;
	}
	public void setReadMethod(Method readMethod) {
		this.readMethod = readMethod;
	}
	public Method getWriteMethod() {
		return writeMethod;
	}
	public void setWriteMethod(Method writeMethod) {
		this.writeMethod = writeMethod;
	}
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(boolean primaryKey) {
		this.primaryKey = primaryKey;
	}
	public boolean isGeneratedValue() {
		return generatedValue;
	}
	public void setGeneratedValue(boolean generatedValue) {
		this.generatedValue = generatedValue;
	}
	public GenerationType getStrategy() {
		return strategy;
	}
	public void setStrategy(GenerationType strategy) {
		this.strategy = strategy;
	}
	public String getGenerator() {
		return generator;
	}
	public void setGenerator(String generator) {
		this.generator = generator;
	}
	public int getPrecision() {
		return precision;
	}
	public void setPrecision(int precision) {
		this.precision = precision;
	}
	public int getScale() {
		return scale;
	}
	public void setScale(int scale) {
		this.scale = scale;
	}
	public boolean isNullable() {
		return nullable;
	}
	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}
	public boolean isUnique() {
		return unique;
	}
	public void setUnique(boolean unique) {
		this.unique = unique;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public String getColumnDefinition() {
		return columnDefinition;
	}
	public void setColumnDefinition(String columnDefinition) {
		this.columnDefinition = columnDefinition;
	}
	public boolean isInsertable() {
		return insertable;
	}
	public void setInsertable(boolean insertable) {
		this.insertable = insertable;
	}
	public boolean isUpdatable() {
		return updatable;
	}
	public void setUpdatable(boolean updatable) {
		this.updatable = updatable;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public boolean isClob() {
		return clob;
	}
	public void setClob(boolean clob) {
		this.clob = clob;
	}
	public boolean isBlob() {
		return blob;
	}
	public void setBlob(boolean blob) {
		this.blob = blob;
	}
	public boolean isTransientField() {
		return transientField;
	}
	public void setTransientField(boolean transientField) {
		this.transientField = transientField;
	}
	public EntityElement getEntityElement() {
		return entityElement;
	}
	public void setEntityElement(EntityElement entityElement) {
		this.entityElement = entityElement;
	}
	public Field getField() {
		return field;
	}
	public void setField(Field field) {
		this.field = field;
	}

	public boolean isVersion() {
		return version;
	}

	public void setVersion(boolean version) {
		this.version = version;
	}

	public boolean isCreatedDateField() {
		return CreatedDateField;
	}

	public void setCreatedDateField(boolean createdDateField) {
		CreatedDateField = createdDateField;
	}

	public boolean isModifieDateField() {
		return ModifieDateField;
	}

	public void setModifieDateField(boolean modifieDateField) {
		ModifieDateField = modifieDateField;
	}

	@Override
	public String toString() {
		return "FieldElement{" +
				"name='" + name + '\'' +
				", column='" + column + '\'' +
				", type=" + type +
				", readMethod=" + readMethod +
				", writeMethod=" + writeMethod +
				", primaryKey=" + primaryKey +
				", generatedValue=" + generatedValue +
				", strategy=" + strategy +
				", generator='" + generator + '\'' +
				", precision=" + precision +
				", scale=" + scale +
				", nullable=" + nullable +
				", unique=" + unique +
				", length=" + length +
				", columnDefinition='" + columnDefinition + '\'' +
				", insertable=" + insertable +
				", updatable=" + updatable +
				", table='" + table + '\'' +
				", clob=" + clob +
				", blob=" + blob +
				", transientField=" + transientField +
				", version=" + version +
				", CreatedDateField=" + CreatedDateField +
				", ModifieDateField=" + ModifieDateField +
				", entityElement=" + entityElement +
				", field=" + field +
				'}';
	}
}