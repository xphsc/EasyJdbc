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
package com.xphsc.easyjdbc.executor.example;



import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.lambda.LambdaSupplier;
import com.xphsc.easyjdbc.core.lambda.Reflections;
import com.xphsc.easyjdbc.core.metadata.DynamicEntityElement;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.executor.AbstractExecutor;
import com.xphsc.easyjdbc.page.PageRowBounds;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.core.transform.DynamicEntityRowMapper;
import com.xphsc.easyjdbc.core.transform.EntityRowMapper;
import com.xphsc.easyjdbc.util.Collects;
import org.springframework.jdbc.core.RowMapper;
import java.util.*;


/**
 * 根据Example查询全部记录的执行器
 * Created by ${huipei.x}
 */
public class FindByExampleExecutor<T>  extends AbstractExecutor<T> {
    private final Class<?> persistentClass;
    private SQL sqlBuilder ;
    private EntityElement entityElement;
    private EntityElement newEntityElement;
    private  Integer startRow;
    private  Integer limit;
    private  String dialectName;
    private DynamicEntityElement dynamicEntityElement;
    private boolean isDynamic;
    private  Map<String,String> dynamicMappings;
    protected  boolean distinct=false;
    protected LinkedList<String> excludePropertys;
    protected LinkedList<String> selectPropertys;
    private  Object[] parameters;
    public <S> FindByExampleExecutor(SQL applyWhere,Class<?> persistentClass,Class<?> entityClass, PageInfo pageInfo, EntityElement entityElement,LinkedList<String> excludePropertys,Map<String,String> mappings,boolean distinct,LinkedList<String> selectPropertys,Object[] parameters,LambdaSupplier<S> jdbcBuilder,String dialectName) {
        super(jdbcBuilder);
        this.sqlBuilder = applyWhere;
        this.persistentClass=(entityClass==null?persistentClass:entityClass);
        if(pageInfo!=null){
            startRow=(pageInfo.getPageNum()-1)*pageInfo.pageSize;
            limit=pageInfo.pageSize;
        }
        this.dialectName = dialectName;
        this.excludePropertys=excludePropertys;
        dynamicMappings=mappings;
        this.distinct = distinct;
        this.newEntityElement=entityElement;
        this.parameters=parameters;
        this.selectPropertys=selectPropertys;
    }

    public <S> FindByExampleExecutor(SQL applyWhere,Class<?> persistentClass,Class<?> entityClass, Integer offset, Integer limit, EntityElement entityElement,LinkedList<String> excludePropertys,Map<String,String> mappings,boolean distinct,LinkedList<String> selectPropertys,Object[] parameters,LambdaSupplier<S> jdbcBuilder,String dialectName) {
        super(jdbcBuilder);
        this.sqlBuilder = applyWhere;
        this.persistentClass=(entityClass==null?persistentClass:entityClass);
        if(offset!=null&&limit!=null){
            this.startRow=offset;
            this.limit=limit;
        }
        this.dialectName = dialectName;
        this.excludePropertys=excludePropertys;
        dynamicMappings=mappings;
        this.distinct = distinct;
        this.newEntityElement=entityElement;
        this.parameters=parameters;
        this.selectPropertys=selectPropertys;
    }


    @Override
    public void prepare() {
        if(this.isEntity(this.persistentClass)){
            this.isDynamic = false;
            this.entityElement = ElementResolver.resolve(this.persistentClass);
        } else {
            this.isDynamic = true;
            this.dynamicEntityElement = ElementResolver.resolveDynamic(this.persistentClass,this.dynamicMappings);
        }
       List<String> columns=new ArrayList(10);
        sqlBuilder.FROM(this.newEntityElement.getTable());
        Iterator i = this.newEntityElement.getFieldElements().values().iterator();
            while(i.hasNext()) {
                FieldElement fieldElement = (FieldElement)i.next();
                if(fieldElement.isTransientField()) {
                    continue;
                }
                columns.add(fieldElement.getColumn());
                if(Collects.isEmpty(excludePropertys)&&Collects.isEmpty(selectPropertys)){
                    buildSQL(fieldElement.getColumn());
                }

            }

        if(Collects.isNotEmpty(excludePropertys)&&Collects.isEmpty(selectPropertys)){
            columns= Collects.removeAll(columns, this.excludePropertys);
            for(String column:columns){
                buildSQL(column);
            }
        }

       if(Collects.isNotEmpty(selectPropertys)&&Collects.isEmpty(excludePropertys)){
            Iterator<String> selecti = selectPropertys.iterator();
            while(selecti.hasNext()) {
                String column =selecti.next();
                buildSQL(column);

            }
        }
    }

    private SQL buildSQL(String column){
        if(distinct){
            sqlBuilder.SELECT_DISTINCT(column);
        }else{
            sqlBuilder.SELECT(column);
        }
        return sqlBuilder;
    }
    @Override
    protected T doExecute() throws JdbcDataException {
        RowMapper rowMapper = null;
        String sql = this.sqlBuilder.toString();
        if(null!=this.startRow&&-1!=this.startRow&& null!=this.limit&&this.limit>0){
          sql=  PageRowBounds.pagination(dialectName, this.sqlBuilder.toString(), startRow, this.limit);
        }


        if(this.isDynamic){
            rowMapper = new DynamicEntityRowMapper(LOBHANDLER,this.dynamicEntityElement,this.persistentClass);
        } else {
            rowMapper = new EntityRowMapper(LOBHANDLER,this.entityElement,this.persistentClass);
        }
                if(null==this.parameters||this.parameters.length==0){
                    return (T) this.jdbcBuilder.query(sql,rowMapper);
                } else {
                    return (T) this.jdbcBuilder.query(sql,this.parameters,rowMapper);
                }
        }


}
