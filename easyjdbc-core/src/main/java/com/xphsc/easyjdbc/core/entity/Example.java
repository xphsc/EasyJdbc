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







import com.xphsc.easyjdbc.core.lambda.LambdaFunction;
import com.xphsc.easyjdbc.core.lambda.Reflections;
import com.xphsc.easyjdbc.page.PageInfo;
import java.util.*;


/**
 * Created by ${huipei.x}
 */
public class Example extends AbstractExample<Example>{

    public Example(Class<?> persistentClass) {
        this.persistentClass = persistentClass;
        initEntityElement(persistentClass);
        oredCriteria = new ArrayList<Criteria>();
    }


    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     *Properties <--> Class Field Mapping
     * @param property Query Object Properties
     * @param field Class Field Mapping
     */
    @Override
    public Example mapping(String property, String field) {
        return super.mapping(property, field);
    }

    /**
     *Properties <--> Class Field Mapping
     * @param property Query Object Properties
     * @param field Class Field Mapping
     */
    public <T> Example mapping(LambdaFunction<T, Object> property, String field) {
        return super.mapping(Reflections.fieldNameForLambdaFunction(property), field);
    }
    /**
     *Properties <--> Class Field Mapping
     * @param property Query Object Properties
     * @param field Class Field Mapping
     */
    public <T,S> Example mapping(LambdaFunction<T, Object> property, LambdaFunction<S, Object> field) {
        return super.mapping(Reflections.fieldNameForLambdaFunction(property), Reflections.fieldNameForLambdaFunction(field));
    }

    /**
     *Exclude query attributes Propertys
     * @param excludePropertys
     */
    @Override
    public  Example excludePropertys(String... excludePropertys) {
        return super.excludePropertys(excludePropertys);
    }

    /**
     *Exclude query attributes Propertys
     * @param excludePropertys
     */
    public <T> Example excludePropertys(LambdaFunction<T, Object>... excludePropertys) {
        return super.excludePropertys(Reflections.fieldNameForLambdaFunction(excludePropertys).toArray(new String[excludePropertys.length]));
    }

    /**
     * select query attributes Propertys
     * @param propertys
     */
    @Override
    public Example selectPropertys(String... propertys) {
        return super.selectPropertys(propertys);
    }
    /**
     * select query attributes Propertys
     * @param propertys
     */

    public <T> Example selectPropertys(LambdaFunction<T, Object>... propertys) {
        return super.selectPropertys(Reflections.fieldNameForLambdaFunction(propertys).toArray(new String[propertys.length]));
    }

    /**
     * select aggregation query attributes Propertys
     * @param propertys
     */
    @Override
    public Example selectPropertys(Aggregation aggregation, String... propertys) {
        return super.selectPropertys(aggregation, propertys);
    }


    /**
     * select aggregation query attributes Propertys
     * @param propertys
     */
    public <T> Example selectPropertys(Aggregation aggregation, LambdaFunction<T, Object>... propertys) {
        return super.selectPropertys(aggregation, Reflections.fieldNameForLambdaFunction(propertys).toArray(new String[propertys.length]));
    }

    /**
     * Grouping for query
     * @param groupBys
     */
    @Override
    public Example groupByClause(String... groupBys) {
        return super.groupByClause(groupBys);
    }
    /**
     * Grouping for query
     * @param groupBys
     */
    public <T> Example groupByClause(LambdaFunction<T, Object>... groupBys) {
        return super.groupByClause(Reflections.fieldNameForLambdaFunction(groupBys).toArray(new String[groupBys.length]));
    }
    /**
     * Distinct for query
     */
    @Override
    public Example isDistinct(boolean distinct) {
        return super.isDistinct(distinct);
    }

    /**
     *  order for query
     */
    @Override
    public Example orderByClause(Sorts sorts) {
        return super.orderByClause(sorts);
    }

    /**
     *  entityClass for query
     */
    @Override
    public Example entityClass(Class<?> entityClass) {
        return super.entityClass(entityClass);
    }
    /**
     *  paging for query
     */
    @Override
    public Example pageInfo(int pageNum, int pageSize) {
        return super.pageInfo(pageNum, pageSize);
    }
    /**
     *  paging for query
     */
	 @Override
    public Example offsetPage(int offset, int limit) {
        return super.offsetPage(offset, limit);
    }

    public void or(Criteria criteria) {
       criteria.setAndOr("OR");
        oredCriteria.add(criteria);
    }
    public Example.Criteria or() {
        Criteria criteria = this.createCriteriaInternal();
        criteria.setAndOr("OR");
        this.oredCriteria.add(criteria);
        return criteria;

    }
    public void and(Criteria criteria) {
       criteria.setAndOr("AND");
        oredCriteria.add(criteria);
    }

    public Criteria and() {
        Criteria criteria = createCriteriaInternal();
       criteria.setAndOr("AND");
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            criteria.setAndOr("AND");
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }
    public  class Criteria extends GeneratedCriteria {
        protected Criteria() {
            super();
        }


    }
    protected abstract class GeneratedCriteria {
        protected String andOr;
        public String getAndOr() {
            return andOr;
        }

        public void setAndOr(String andOr) {
            this.andOr = andOr;
        }
        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }
        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public Criteria andIsNull(String property) {
            addCriterion(checkProperty(property).getColumn() + " is null");
            return (Criteria) this;
        }

        public Criteria andIsNotNull(String property) {
            addCriterion(checkProperty(property).getColumn() + " is not null");
            return (Criteria) this;
        }

        public Example.Criteria orIsNull(String property) {
           addOrCriterion(checkProperty(property).getColumn() + " is null");
            return (Example.Criteria)this;
        }

        public Example.Criteria orIsNotNull(String property) {
           addOrCriterion(checkProperty(property).getColumn() + " is not null");
            return (Example.Criteria)this;
        }

        public Criteria andEqualTo(String property, Object value) {
            addCriterion(property, "=", value);
            return (Criteria) this;
        }


      public Criteria andNotEqualTo(String property, Object value) {
          addCriterion(property,"<>",value);
          return (Criteria) this;
        }
        public Example.Criteria orEqualTo(String property, Object value) {
            addOrCriterion(property, "=", value);
            return (Example.Criteria)this;
        }

        public Example.Criteria orNotEqualTo(String property, Object value) {
            addOrCriterion(property, "<>", value);
            return (Example.Criteria)this;
        }
        public Criteria andGreaterThan(String property, Object value) {
            addCriterion(property, ">", value);
            return (Criteria) this;
        }

        public Example.Criteria orGreaterThan(String property, Object value) {
            addOrCriterion(property, ">", value);
            return (Example.Criteria)this;
        }

        public Criteria andGreaterThanOrEqualTo(String property, Object value) {
            addCriterion(property, ">=", value);
            return (Criteria) this;
        }

        public Example.Criteria orGreaterThanOrEqualTo(String property, Object value) {
            addOrCriterion(property, ">=", value);
            return (Example.Criteria)this;
        }

        public Criteria andLessThan(String property, Object value) {
            addCriterion(property, "<", value);
            return (Criteria) this;
        }

        public Example.Criteria orLessThan(String property, Object value) {
            addOrCriterion(property, "<", value);
            return (Example.Criteria)this;
        }

        public Criteria andLessThanOrEqualTo(String property, Object value) {
            addCriterion(property, "<=", value);
            return (Criteria) this;
        }


        public Example.Criteria orLessThanOrEqualTo(String property, Object value) {
            addOrCriterion(property, "<=", value);
            return (Example.Criteria)this;
        }

        public Criteria andIn(String property, Iterable values) {
            addCriterion(property, "in", values);
            return (Criteria) this;
        }

        public Criteria orIn(String property, Iterable values) {
            addOrCriterion(property, "in", values);
            return (Criteria) this;
        }

        public Criteria andNotIn(String property, Iterable values) {
            addCriterion(property, "not in", values);
            return (Criteria) this;
        }

        public Criteria orNotIn(String property, Iterable values) {
            addOrCriterion(property, "not in", values);
            return (Criteria) this;
        }

        public Criteria andBetween(String property, Object value1, Object value2) {
            addCriterion(property, "between", value1, value2);
            return (Criteria) this;
        }

        public Criteria orBetween(String property, Object value1, Object value2) {
            addOrCriterion(property, "between", value1, value2);
            return (Criteria) this;
        }



        public Criteria andNotBetween(String property, Object value1, Object value2) {
            addCriterion(property, "not between", value1, value2);
            return (Criteria) this;
        }


        public Criteria orNotBetween(String property, Object value1, Object value2) {
            addOrCriterion(property, "not between", value1, value2);
            return (Criteria) this;
        }


        public Example.Criteria andLike(String property, String value) {
            addCriterion(property, "like", value);
            return (Example.Criteria)this;
        }


        public Criteria orLike(String property, String value) {
            addOrCriterion(property, "like", value);
            return (Criteria) this;
        }

        public Example.Criteria andNotLike(String property, String value) {
            addCriterion(property, "not like", value);
            return (Example.Criteria)this;
        }

        public Criteria orNotLike(String property, String value) {
            addOrCriterion(property, "not like", value);
            return (Criteria) this;
        }

        public Example.Criteria andLike(String property, String value,LikeType likeType) {
            addCriterionLike(property, value, likeType, true);
            return (Example.Criteria)this;
        }

        public Criteria orLike(String property, String value,LikeType likeType) {
            addOrCriterionLike(property, value, likeType, true);
            return (Criteria) this;
        }

        public Example.Criteria andNotLike(String property, String value,LikeType likeType) {
            addCriterionLike(property, value, likeType, false);
            return (Example.Criteria)this;
        }

        public Criteria orNotLike(String property, String value,LikeType likeType) {
            addOrCriterionLike(property, value, likeType, false);
            return (Criteria) this;
        }





        public <T> Criteria andIsNull(LambdaFunction<T, Object> property) {
            addCriterion(checkProperty(Reflections.fieldNameForLambdaFunction(property)).getColumn() + " is null");
            return (Criteria) this;
        }

        public <T> Criteria andIsNotNull(LambdaFunction<T, Object> property) {
            addCriterion(checkProperty(Reflections.fieldNameForLambdaFunction(property)).getColumn() + " is not null");
            return (Criteria) this;
        }

        public <T> Example.Criteria orIsNull(LambdaFunction<T, Object> property) {
            addOrCriterion(checkProperty(Reflections.fieldNameForLambdaFunction(property)).getColumn() + " is null");
            return (Example.Criteria)this;
        }

        public <T> Example.Criteria orIsNotNull(LambdaFunction<T, Object> property) {
            addOrCriterion(checkProperty(Reflections.fieldNameForLambdaFunction(property)).getColumn() + " is not null");
            return (Example.Criteria)this;
        }

        public <T> Criteria andEqualTo(LambdaFunction<T, Object> property, Object value) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property), "=", value);
            return (Criteria) this;
        }


        public <T> Criteria andNotEqualTo(LambdaFunction<T, Object> property, Object value) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property),"<>",value);
            return (Criteria) this;
        }
        public <T> Example.Criteria orEqualTo(LambdaFunction<T, Object> property, Object value) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), "=", value);
            return (Example.Criteria)this;
        }

        public <T> Example.Criteria orNotEqualTo(LambdaFunction<T, Object> property, Object value) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), "<>", value);
            return (Example.Criteria)this;
        }
        public <T> Criteria andGreaterThan(LambdaFunction<T, Object> property, Object value) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property), ">", value);
            return (Criteria) this;
        }

        public <T> Example.Criteria orGreaterThan(LambdaFunction<T, Object> property, Object value) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), ">", value);
            return (Example.Criteria)this;
        }

        public <T> Criteria andGreaterThanOrEqualTo(LambdaFunction<T, Object> property, Object value) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property), ">=", value);
            return (Criteria) this;
        }

        public <T> Example.Criteria orGreaterThanOrEqualTo(LambdaFunction<T, Object> property, Object value) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), ">=", value);
            return (Example.Criteria)this;
        }

        public <T> Criteria andLessThan(LambdaFunction<T, Object> property, Object value) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property), "<", value);
            return (Criteria) this;
        }

        public <T> Example.Criteria orLessThan(LambdaFunction<T, Object> property, Object value) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), "<", value);
            return (Example.Criteria)this;
        }

        public <T> Criteria andLessThanOrEqualTo(LambdaFunction<T, Object> property, Object value) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property), "<=", value);
            return (Criteria) this;
        }


        public <T> Example.Criteria orLessThanOrEqualTo(LambdaFunction<T, Object> property, Object value) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), "<=", value);
            return (Example.Criteria)this;
        }

        public  <T> Criteria andIn(LambdaFunction<T, Object> property, Iterable values) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property), "in", values);
            return (Criteria) this;
        }

        public <T> Criteria orIn(LambdaFunction<T, Object> property, Iterable values) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), "in", values);
            return (Criteria) this;
        }

        public <T> Criteria andNotIn(LambdaFunction<T, Object> property, Iterable values) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property), "not in", values);
            return (Criteria) this;
        }

        public  <T> Criteria orNotIn(LambdaFunction<T, Object> property, Iterable values) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), "not in", values);
            return (Criteria) this;
        }

        public <T> Criteria andBetween(LambdaFunction<T, Object> property, Object value1, Object value2) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property), "between", value1, value2);
            return (Criteria) this;
        }

        public <T> Criteria orBetween(LambdaFunction<T, Object> property, Object value1, Object value2) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), "between", value1, value2);
            return (Criteria) this;
        }



        public  <T> Criteria andNotBetween(LambdaFunction<T, Object> property, Object value1, Object value2) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property), "not between", value1, value2);
            return (Criteria) this;
        }


        public <T> Criteria orNotBetween(LambdaFunction<T, Object> property, Object value1, Object value2) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), "not between", value1, value2);
            return (Criteria) this;
        }


        public <T> Example.Criteria andLike(LambdaFunction<T, Object> property, String value) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property), "like", value);
            return (Example.Criteria)this;
        }


        public <T> Criteria orLike(LambdaFunction<T, Object> property, String value) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), "like", value);
            return (Criteria) this;
        }

        public <T> Example.Criteria andNotLike(LambdaFunction<T, Object> property, String value) {
            addCriterion(Reflections.fieldNameForLambdaFunction(property), "not like", value);
            return (Example.Criteria)this;
        }

        public <T> Criteria orNotLike(LambdaFunction<T, Object> property, String value) {
            addOrCriterion(Reflections.fieldNameForLambdaFunction(property), "not like", value);
            return (Criteria) this;
        }

        public <T> Example.Criteria andLike(LambdaFunction<T, Object> property, String value,LikeType likeType) {
            addCriterionLike(Reflections.fieldNameForLambdaFunction(property), value, likeType, true);
            return (Example.Criteria)this;
        }

        public <T> Criteria orLike(LambdaFunction<T, Object> property, String value,LikeType likeType) {
            addOrCriterionLike(Reflections.fieldNameForLambdaFunction(property), value, likeType, true);
            return (Criteria) this;
        }

        public <T> Example.Criteria andNotLike(LambdaFunction<T, Object> property, String value,LikeType likeType) {
            addCriterionLike(Reflections.fieldNameForLambdaFunction(property), value, likeType, false);
            return (Example.Criteria)this;
        }

        public <T> Criteria orNotLike(LambdaFunction<T, Object> property, String value,LikeType likeType) {
            addOrCriterionLike(Reflections.fieldNameForLambdaFunction(property), value, likeType, false);
            return (Criteria) this;
        }
    }


    @Override
    public <T> List<T> list() {
        return super.list();
    }

    @Override
    public <T> T get() {
        return super.get();
    }


    public <T> Optional<T> getOne() {
        return Optional.ofNullable(super.get());
    }

    @Override
    public long count() {
        return super.count();
    }

    @Override
    public <T> PageInfo<T> page() {
        return super.page();
    }

    @Override
    public int delete() {
        return super.delete();
    }
    @Override
    public void clear() {
        super.clear();
    }
}
