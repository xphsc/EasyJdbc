package com.xphsc.easyjdbc.core.metadata;

import javax.persistence.UniqueConstraint;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 实体元素
 * Created by ${huipei.x}
 */
public class EntityElement implements Element {

	private static final long serialVersionUID = -1010706706028472274L;
	
	private String name;// 实体名称
	private String table;//// 表名称
	private String catalog;// 指定数据库名称
	private String schema;// 指定数据库的用户名
	private FieldElement primaryKey;// 主键
	UniqueConstraint[] uniqueConstraints;//指定唯一性字段约束,如为personid 和name 字段指定唯一性约束 uniqueConstraints={ @UniqueConstraint(columnNames={"personid", "name"})}
	private Class<?> persistentClass;//持久化类
	private Map<String,FieldElement> fieldElements = new LinkedHashMap<String,FieldElement>();//属性

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public FieldElement getPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(FieldElement primaryKey) {
		this.primaryKey = primaryKey;
	}
	public UniqueConstraint[] getUniqueConstraints() {
		return uniqueConstraints;
	}
	public void setUniqueConstraints(UniqueConstraint[] uniqueConstraints) {
		this.uniqueConstraints = uniqueConstraints;
	}
	public Class<?> getPersistentClass() {
		return persistentClass;
	}
	public void setPersistentClass(Class<?> persistentClass) {
		this.persistentClass = persistentClass;
	}
	public Map<String, FieldElement> getFieldElements() {
		return Collections.unmodifiableMap(this.fieldElements);
	}
	public void addFieldElement(String columnName, FieldElement fieldElement) {
		this.fieldElements.put(columnName, fieldElement);
	}
	
}