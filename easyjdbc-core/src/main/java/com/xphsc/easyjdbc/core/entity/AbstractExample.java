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
package com.xphsc.easyjdbc.core.entity;


import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.EasyJdbcException;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.executor.example.CountByExampleExecutor;
import com.xphsc.easyjdbc.executor.example.DeleteByExampleExecutor;
import com.xphsc.easyjdbc.executor.example.FindByExampleExecutor;
import com.xphsc.easyjdbc.core.support.JdbcBuilder;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.page.PageInfoImpl;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.Collects;
import com.xphsc.easyjdbc.util.StringUtil;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.persistence.Entity;
import java.util.*;

/**
 * Created by ${huipei.x}
 */
public abstract class AbstractExample<T> {

    protected SQL sqlBuilder= SQL.BUILD()  ;
    protected Sorts orderByClause;
    protected  boolean distinct;
    protected List<Example.Criteria> oredCriteria;
    protected Class<?> entityClass;
    protected Class<?> persistentClass;
    protected PageInfo pageInfo;
	private Integer offset;
    private Integer limit;
    protected LinkedList<String> excludePropertys;
    protected EntityElement entityElement;
    protected  Map<String,String> mappings;
    public JdbcBuilder jdbcTemplate;
    public  String dialectName;
    protected  LinkedList<Object> parameters;
    protected LinkedList<String> selectPropertys;
    protected LinkedList<String> groupByClause;
    protected Boolean isAggregate=false;

    protected T mapping(String property,String field){
        Assert.hasText(checkProperty(property).getColumn(), "映射的列不能为空");
        Assert.hasText(field, "映射的属性不能为空");
        if(Collects.isEmpty(mappings)){
            mappings= new HashMap();
        }
        this.mappings.put(field, checkProperty(property).getColumn());
        return  (T) this;
    }


    public T isDistinct(boolean distinct){
        this.distinct=distinct;
        return  (T) this;
    }

    public T orderByClause(Sorts sorts){
        this.orderByClause=sorts;
        return (T) this;
    }

    public T entityClass(Class<?> entityClass){
        this.entityClass=entityClass;
        return (T) this;
    }

    public T pageInfo(int pageNum,int pageSize){
        Assert.isTrue(pageNum >= 1, "PageNum must be greater than or equal to 1");
        Assert.isTrue(pageSize > 0, "PageSize must be greater than 0");
        if(pageInfo==null){
            pageInfo=new PageInfo();
        }
        this.pageInfo.pageNum=pageNum;
        this.pageInfo.pageSize=pageSize;
        return (T) this;
    }

	 public T  offsetPage(int offset, int limit){
        Assert.isTrue(offset >= 0, "Offset must be greater than or equal to 0");
        Assert.isTrue(limit > 0, "Limit must be greater than 0");
        this.offset=offset;
        this.limit=limit;
        return (T) this;
    }

    protected T excludePropertys(String... excludePropertys){
        LinkedList<String> columns=new LinkedList();
        for(String property:excludePropertys){
            columns.add(checkProperty(property).getColumn());
        }
        this.excludePropertys=columns;
        return (T) this;
    }

    protected T selectPropertys(String... propertys){
        LinkedList<String> columns=new LinkedList();
        for(String property:propertys){
            columns.add(checkProperty(property).getColumn());
        }
        this.selectPropertys=columns;
        return (T) this;
    }

    protected T selectPropertys(Aggregation aggregation,String... propertys){
        LinkedList<String> columns=new LinkedList();
        if(Collects.isEmpty(mappings)){
            mappings= new HashMap();
        }
        for(Aggregation.Aggregate aggregate :aggregation.getAggregates()){
            if(aggregate.getAsProperty()!=null){
                columns.add(aggregate.getType()+"("+checkProperty(aggregate.getProperty()).getColumn()+") AS "+aggregate.getAsProperty());
                this.mappings.put(aggregate.getAsProperty(), aggregate.getAsProperty());
            }else{
                columns.add(aggregate.getType()+"("+checkProperty(aggregate.getProperty()).getColumn()+")  ");
            }

        }
        for(String property:propertys){
            columns.add(checkProperty(property).getColumn()+" AS "+checkProperty(property).getName());
            this.mappings.put(checkProperty(property).getName(), checkProperty(property).getName());
        }
        this.selectPropertys=columns;
        this.isAggregate=true;
        return (T) this;
    }


    protected T groupByClause(String... groupBys){
        LinkedList<String> groupByClause=new LinkedList<String>();
        for(String groupBy:groupBys){
            groupByClause.add(groupBy);
        }
        this.groupByClause=groupByClause;
        return (T) this;
    }

    public static class Criterion {
        protected String condition;
        protected Object value;

        protected Object secondValue;

        protected String andOr;

        protected boolean noValue;

        protected boolean singleValue;

        protected boolean betweenValue;

        protected boolean listValue;

        protected String typeHandler;

        protected Criterion(String condition) {
            this(condition, false);
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            this(condition, value, typeHandler, false);
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null, false);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            this(condition, value, secondValue, typeHandler, false);
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null, false);
        }

        protected Criterion(String condition, boolean isOr) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
            this.andOr = isOr ? "OR" : "AND";
        }

        protected Criterion(String condition, Object value, String typeHandler, boolean isOr) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            this.andOr = isOr ? "OR" : "AND";
            if (value instanceof Collection<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value, boolean isOr) {
            this(condition, value, null, isOr);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler, boolean isOr) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
            this.andOr = isOr ? "OR" : "AND";
        }

        protected Criterion(String condition, Object value, Object secondValue, boolean isOr) {
            this(condition, value, secondValue, null, isOr);
        }

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public String getAndOr() {
            return andOr;
        }

        public void setAndOr(String andOr) {
            this.andOr = andOr;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }
    }

    /**
     *
     */
    protected List<Criterion> criteria;
    protected void addCriterion(String condition) {
        if (condition == null) {
            try {
                throw new EasyJdbcException("Value for condition cannot be null");
            } catch (EasyJdbcException e) {
                e.printStackTrace();
            }
        }
        if (condition.startsWith("null")) {
            return;
        }
        criteria.add(new Criterion(condition));
    }

    protected void addOrCriterion(String condition) {
        if (condition == null) {
            throw new EasyJdbcException("Value for condition cannot be null");
        } else if (!condition.startsWith("null")) {
            this.criteria.add(new Example.Criterion(condition, true));
        }
    }

    protected void addCriterion(String condition, Object value) {
        if (value == null) {
            return;
        }
        criteria.add(new Criterion(condition, value));
    }

    protected void addCriterion(String property, String condition, Object value) {
        if (value == null) {
            throw new EasyJdbcException("Value for " + property + " cannot be null");
        }
        if (isAggregate) {
            condition = property + " " + condition;
        } else {
            condition = checkProperty(property).getColumn() + " " + condition;
        }

        criteria.add(new Criterion(condition, value, false));
    }


    protected void addOrCriterion(String property, String condition, Object value) {
        if (value == null) {
            throw new EasyJdbcException("Value for " + property + " cannot be null");
        }
        if (isAggregate) {
            condition = property + " " + condition;
        } else {
            condition = checkProperty(property).getColumn() + " " + condition;
        }
        criteria.add(new Criterion(condition, value, true));
    }


    protected void addCriterion(String property, String condition, Object value1, Object value2) {
        if (value1 == null || value2 == null) {
            throw new EasyJdbcException("Between values for " + property + " cannot be null");
        }
        if (isAggregate) {
            condition = property + " " + condition;
        } else {
            condition = checkProperty(property).getColumn() + " " + condition;
        }
        criteria.add(new Criterion(condition, value1, value2, false));
    }


    protected void addOrCriterion(String property, String condition, Object value1, Object value2) {
        if (value1 == null || value2 == null) {
            throw new EasyJdbcException("Between values for " + property + " cannot be null");
        }
        if (isAggregate) {
            condition = property + " " + condition;
        } else {
            condition = checkProperty(property).getColumn() + " " + condition;
        }
        criteria.add(new Criterion(condition, value1, value2, true));
    }

    protected void addCriterionLike(String property, Object value, LikeType likeType, boolean islike) {
        String like = islike ? "like" : "not like";
        if (StringUtil.isNotBlank(likeType.getType())) {
            switch (likeType.getType()) {
                case "left":
                    addCriterion(property, like, value + "%");
                    break;
                case "right":
                    addCriterion(property, like, "%" + value);
                    break;
                case "custom":
                    addCriterion(property, like, value);
                    break;
                case "default":
                    addCriterion(property, like, "%" + value + "%");
                    break;
                default:
            }
        }
    }
    protected void addOrCriterionLike(String property,Object value,LikeType likeType,boolean islike) {
        String like= islike ? "like" : "not like";
        if(StringUtil.isNotBlank(likeType.getType())){
            switch (likeType.getType()) {
                case "left":
                    addOrCriterion(property, like, value + "%");
                    break;
                case "right":
                    addOrCriterion(property, like, "%" + value);
                    break;
                case "custom":
                    addOrCriterion(property, like, value);
                    break;
                case "default":
                    addOrCriterion(property, like, "%" + value + "%");
                    break;
                default:
            }
        }
    }

    /**
     *
     */

    protected SQL applyWhere() {
        List<?> newOredCriteria=null;
            newOredCriteria=oredCriteria;
        StringBuilder sb = new StringBuilder();
        boolean firstCriteria = true;
        if (!newOredCriteria.isEmpty()) {
            for (int i = 0; i < newOredCriteria.size(); i++) {
                List<Criterion> criterions=null;
                    Example.Criteria criteria = (Example.Criteria) newOredCriteria.get(i);
                    if (firstCriteria) {
                        firstCriteria = false;
                    } else {
                        sb.append(" "+criteria.andOr+" ");
                    }
                    criterions = criteria.getCriteria();



                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" ");
                        sb.append(" "+criterion.andOr+" ");
                    }
                    sb.append(" ");
                    if(criterion.getCondition().trim().endsWith("in")||
                            criterion.noValue
                            ){
                        sb.append(criterion.getCondition());
                    }else{
                        sb.append(criterion.getCondition() + " ?");
                    }
                    if (criterion.getValue() != null) {
                        if (criterion.isListValue()) {
                            String inValues="";
                            List<Object> list= (List) criterion.getValue();
                            for (Object value : list) {
                                inValues+="'"+value+"',";
                            }
                            inValues = inValues.substring(0, inValues.length()-1);
                            sb.append(" (" + inValues+")");
                        }
                        if (criterion.betweenValue) {
                            if(Collects.isEmpty(parameters)){
                                parameters= new LinkedList<Object>();
                            }
                            parameters.add(criterion.getValue());
                            sb.append(" " + "and"+ " ? ");
                            parameters.add(criterion.getSecondValue());
                        }
                        if(criterion.singleValue){
                            if(Collects.isEmpty(parameters)){
                                parameters= new LinkedList<Object>();
                            }
                            parameters.add(criterion.getValue());
                        }
                    }
                }

                if (!criterions.isEmpty()) {
                    if (Collects.isNotEmpty(selectPropertys)&&this.selectPropertys.toString().contains("(")) {
                        sqlBuilder.HAVING(sb.toString());
                    }else{
                        sqlBuilder.WHERE(sb.toString());}
                }
            }
        }
        if(Collects.isEmpty(groupByClause)) {
            this.groupByClause =new LinkedList<>();
        }
        if (Collects.isNotEmpty(groupByClause)) {
            for (Object  groupBy : groupByClause) {
                this.sqlBuilder = sqlBuilder.GROUP_BY(checkProperty(groupBy.toString()).getColumn());
            }

        }

        if (orderByClause != null) {
            List<Sorts.Order> orderList = orderByClause.getOrders();
            if (Collects.isNotEmpty(orderList)) {
                for (Sorts.Order order : orderList) {
                    this.sqlBuilder = sqlBuilder.ORDER_BY(checkProperty(order.getProperty()).getColumn() + " " + order.getDirection());
                }
            }

        }

        return sqlBuilder;
    }

    protected <T> List<T>  list() {
        if(Collects.isEmpty(parameters)){
            parameters=new LinkedList<>();
        }
        if(jdbcTemplate!=null){
            FindByExampleExecutor<List<T>> executor =  new FindByExampleExecutor<List<T>>(
                    applyWhere(), persistentClass,entityClass,pageInfo
                    ,entityElement,excludePropertys,mappings,distinct,selectPropertys,parameters.toArray(),jdbcTemplate,dialectName);
            List<T> results = executor.execute();
            executor = null;
            return results;
        }
        return null;
    }

    protected <T> T  get() {
        List<T> results=list();
        int size = (results != null ? results.size() : 0);
        if(size>1){
            new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.get(0);
    }

    protected int  count()  {
        CountByExampleExecutor executor;
        if (Collects.isNotEmpty(selectPropertys)) {
            Assert.isTrue(!this.selectPropertys.toString().contains("("),
                    "The current SQL statement contains the default count (1) aggregate function, and there must be no aggregate function.");
        }
        if(Collects.isEmpty(parameters)){
            parameters=new LinkedList<>();
        }
        if(jdbcTemplate!=null) {
            if (sqlBuilder == null ||
                "".equals(sqlBuilder.toString())
               ) {
                bulidSelect();
                executor = new CountByExampleExecutor(applyWhere(), jdbcTemplate, parameters.toArray());
            } else {
                executor = new CountByExampleExecutor(sqlBuilder, jdbcTemplate, parameters.toArray());
            }
            int count = executor.execute();
            executor = null;
            return count;
        }
        return 0;
    }

     protected <T> PageInfo<T> page() {
         List<T> results=null;
         long total=1L;
       if(offset==null&&limit==null){
            if(pageInfo==null){
                pageInfo=new PageInfo();
          }
         results=list();
         total=count();
            return new PageInfoImpl<T>(results,total,pageInfo.getPageNum(),pageInfo.getPageSize());
     }else{
           if(Collects.isEmpty(parameters)){
               parameters=new LinkedList<>();
           }
            if(jdbcTemplate!=null){
                FindByExampleExecutor<List<T>> executor =  new FindByExampleExecutor<List<T>>(
                        applyWhere(), persistentClass,entityClass,offset,limit
                        ,entityElement,excludePropertys,mappings,distinct,selectPropertys,parameters.toArray(),jdbcTemplate,dialectName);
                results= executor.execute();
            }
           total=count();
           if(pageInfo==null){
               pageInfo=new PageInfo();
           }
           this.pageInfo.pageNum=(int) Math.ceil((double) ((offset +limit) / limit));
           this.pageInfo.pageSize=limit;
            return new PageInfoImpl<T>(results,total,pageInfo.getPageNum(),pageInfo.pageSize);

        }

    }

    protected  int delete() {
        if(Collects.isEmpty(parameters)){
            parameters=new LinkedList<>();
        }
		 Assert.notEmpty(oredCriteria,"Criteria conditional objects cannot be empty!");
        DeleteByExampleExecutor  executor=new DeleteByExampleExecutor(this.jdbcTemplate,applyWhere(),parameters.toArray(),persistentClass);
       int result=executor.execute();
        return result;
    }


    private void bulidSelect(){
        sqlBuilder.FROM(this.entityElement.getTable());
        Iterator i = this.entityElement.getFieldElements().values().iterator();
        while(i.hasNext()) {
            FieldElement fieldElement = (FieldElement)i.next();
            if(fieldElement.isTransientField()) {
                continue;
            }
            if(distinct){
                sqlBuilder.SELECT_DISTINCT(fieldElement.getColumn());
            }else{
                sqlBuilder.SELECT(fieldElement.getColumn());
            }
        }
    }

    protected void initEntityElement(Class<?> entityClass){
        this.checkEntity(entityClass);
        this.entityElement = ElementResolver.resolve(this.persistentClass);
    }

    protected boolean isEntity(Class<?> persistentClass) {
        return null != persistentClass.getAnnotation(Entity.class);
    }

    protected void checkEntity(Class<?> persistentClass) {
        Assert.isTrue(this.isEntity(persistentClass), persistentClass + " 如果是实体类型请使用@Entity注解进行标识");
    }


    protected FieldElement checkProperty(String fieldName) {
        List nameList=new ArrayList<>();
        FieldElement newfieldElement=new FieldElement();
        for (FieldElement fieldElement: entityElement.getFieldElements().values()) {
            if(fieldElement.isTransientField()) {
                continue;
            }
            if(fieldElement.getName().equals(fieldName)){
                newfieldElement.setColumn(fieldElement.getColumn());
                newfieldElement.setType(fieldElement.getType());
            }
            nameList.add(fieldElement.getName());
        }
        newfieldElement.setName(fieldName);
        if (nameList.contains(fieldName)) {
            return newfieldElement;
        } else{
            throw new EasyJdbcException("The current entity class does not contain the name" + fieldName + "Properties!");
        }
    }


    protected void clear() {
        this.oredCriteria.clear();
        if (this.parameters!=null){
            this.parameters.clear();
        }
        if (this.groupByClause!=null){
            this.groupByClause.clear();
        }
        if (this.groupByClause!=null){
            this.groupByClause.clear();
        }
        this.distinct = false;

    }
}
