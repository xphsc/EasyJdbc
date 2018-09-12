package com.xphsc.easyjdbc.core.entity;





import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.EasyJdbcException;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.executor.CountByExampleExecutor;
import com.xphsc.easyjdbc.executor.FindByExampleExecutor;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.page.PageInfoImpl;
import com.xphsc.easyjdbc.util.Assert;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.persistence.Entity;
import java.util.*;

/**
 * Created by ${huipei.x}
 */
public class Example extends AbstractExample{



    public Example(Class<?> persistentClass) {
        this.persistentClass = persistentClass;
        initEntityElement(persistentClass);
        oredCriteria = new ArrayList<Criteria>();
    }


    public Example(Class<?> persistentClass,JdbcTemplate jdbcTemplate,String dialectName) {
        this.jdbcTemplate=jdbcTemplate;
        this.dialectName=dialectName;
        this.persistentClass = persistentClass;
        initEntityElement(persistentClass);
        oredCriteria = new ArrayList<Criteria>();


    }


    public Example mapping(String persistentProperty,String field){
        Assert.hasText(checkProperty(persistentProperty).getColumn(), "映射的列不能为空");
        Assert.hasText(field, "映射的属性不能为空");
        this.mappings.put(field, checkProperty(persistentProperty).getColumn());
        return this;
    }


    public Example isDistinct(boolean distinct){
       this.distinct=distinct;
        return this;
    }

    public Example orderByClause(Sorts sorts){
        this.orderByClause=sorts;
        return this;
    }

    public Example entityClass(Class<?> entityClass){
        this.entityClass=entityClass;
        return this;
    }

    public Example pageInfo(int pageNum,int pageSize){
        Assert.isTrue(pageNum >= 1, "pageNum必须大于等于1");
        Assert.isTrue(pageSize > 0, "pageSize必要大于0");
        this.pageInfo.pageNum=pageNum;
        this.pageInfo.pageSize=pageSize;
        return this;
    }

    public Example excludePropertys(String... excludePersistentPropertys){
        List<String> columns=new ArrayList();
        for(String property:excludePersistentPropertys){
            columns.add(checkProperty(property).getColumn());
        }
        this.excludePropertys=columns;
        return this;
    }

    public Example selectPropertys(String... propertys){
        LinkedList<String> columns=new LinkedList();
        for(String property:propertys){
            columns.add(checkProperty(property).getColumn());
        }
        this.selectPropertys=columns;
        return this;
    }

   public Example selectPropertys(Aggregation aggregation,String... propertys){
        LinkedList<String> columns=new LinkedList();
       for(Aggregation.Aggregate aggregate :aggregation.getAggregates()){
           if(aggregate.getAsProperty()!=null){
               columns.add(aggregate.getType()+"("+checkProperty(aggregate.getProperty()).getColumn()+") as "+aggregate.getAsProperty());
               this.mappings.put(aggregate.getAsProperty(), aggregate.getAsProperty());
           }else{
               columns.add(aggregate.getType()+"("+checkProperty(aggregate.getProperty()).getColumn()+")  ");
           }

       }
       for(String property:propertys){
           columns.add(checkProperty(property).getColumn());
       }
        this.selectPropertys=columns;
       return this;
    }


     public Example groupByClause(String... groupBys){
         LinkedList<String> groupByClause=new LinkedList<>();
         for(String groupBy:groupBys){
             groupByClause.add(groupBy);
         }
        this.groupByClause=groupByClause;
        return this;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
       criteria.setAndOr("OR");
        oredCriteria.add(criteria);
    }
    public Example.Criteria or() {
        Example.Criteria criteria = this.createCriteriaInternal();
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
        protected List<Criterion> criteria ;
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
            addCriterion(checkProperty(property).getColumn()+ " is not null");
            return (Criteria) this;
        }

        public Example.Criteria orIsNull(String property) {
            this.addOrCriterion(checkProperty(property).getColumn() + " is null");
            return (Example.Criteria)this;
        }

        public Example.Criteria orIsNotNull(String property) {
            this.addOrCriterion(checkProperty(property).getColumn() + " is not null");
            return (Example.Criteria)this;
        }

        public Criteria andEqualTo(String property, Object value) {
          addCriterion(checkProperty(property).getColumn() + " =", value);
            return (Criteria) this;
        }
      public Criteria andNotEqualTo(String property, Object value) {
            addCriterion(checkProperty(property).getColumn() + " <>", value);
            return (Criteria) this;
        }
        public Example.Criteria orEqualTo(String property, Object value) {
            this.addOrCriterion(checkProperty(property).getColumn() + " =", value, checkProperty(property).getName());
            return (Example.Criteria)this;
        }

        public Example.Criteria orNotEqualTo(String property, Object value) {
            this.addOrCriterion(checkProperty(property).getColumn() + " <>", value, checkProperty(property).getName());
            return (Example.Criteria)this;
        }
        public Criteria andGreaterThan(String property, Object value) {
            addCriterion(checkProperty(property).getColumn() + " >", value);
            return (Criteria) this;
        }
        public Criteria andGreaterThanOrEqualTo(String property, Object value) {
            addCriterion(checkProperty(property).getColumn() + " >=", value, checkProperty(property).getName());
            return (Criteria) this;
        }

        public Criteria andLessThan(String property, Object value) {
            addCriterion(checkProperty(property).getColumn() + " <", value, checkProperty(property).getName());
            return (Criteria) this;
        }

        public Criteria andLessThanOrEqualTo(String property, Object value) {
            addCriterion(checkProperty(property).getColumn() + " <=", value, checkProperty(property).getName());
            return (Criteria) this;
        }

        public Example.Criteria orGreaterThan(String property, Object value) {
            this.addOrCriterion(checkProperty(property).getColumn()+ " >", value, checkProperty(property).getName());
            return (Example.Criteria)this;
        }

        public Example.Criteria orGreaterThanOrEqualTo(String property, Object value) {
            this.addOrCriterion(checkProperty(property).getColumn() + " >=", value, checkProperty(property).getName());
            return (Example.Criteria)this;
        }

        public Example.Criteria orLessThan(String property, Object value) {
            this.addOrCriterion(checkProperty(property).getColumn() + " <", value, checkProperty(property).getName());
            return (Example.Criteria)this;
        }

        public Example.Criteria orLessThanOrEqualTo(String property, Object value) {
            this.addOrCriterion(checkProperty(property).getColumn() + " <=", value, checkProperty(property).getName());
            return (Example.Criteria)this;
        }

        public Criteria andIn(String property, Iterable values) {
            addCriterion(checkProperty(property).getColumn() + " in", values, checkProperty(property).getName());
            return (Criteria) this;
        }

        public Criteria andNotIn(String property, Iterable values) {
            addCriterion(checkProperty(property).getColumn() + " not in", values, checkProperty(property).getName());
            return (Criteria) this;
        }

        public Criteria orIn(String property, Iterable values) {
            addOrCriterion(checkProperty(property).getColumn() + " in", values, checkProperty(property).getName());
            return (Criteria) this;
        }

        public Criteria orNotIn(String property, Iterable values) {
            addOrCriterion(checkProperty(property).getColumn() + " not in", values, checkProperty(property).getName());
            return (Criteria) this;
        }
        public Criteria orBetween(String property, Object value1, Object value2) {
            addOrCriterion(checkProperty(property).getColumn() + " between", value1, value2, checkProperty(property).getName());
            return (Criteria) this;
        }
        public Criteria andBetween(String property, Object value1, Object value2) {
            addCriterion(checkProperty(property).getColumn() + " between", value1, value2, checkProperty(property).getName());
            return (Criteria) this;
        }


        public Criteria orNotBetween(String property, Object value1, Object value2) {
            addOrCriterion(checkProperty(property).getColumn() + " not between", value1, value2, checkProperty(property).getName());
            return (Criteria) this;
        }

        public Criteria andNotBetween(String property, Object value1, Object value2) {
            addCriterion(checkProperty(property).getColumn() + " not between", value1, value2, checkProperty(property).getName());
            return (Criteria) this;
        }

        public Example.Criteria andLike(String property, String value) {
            addCriterion(checkProperty(property).getColumn() + "  like", value, checkProperty(property).getName());
            return (Example.Criteria)this;
        }

        public Example.Criteria andNotLike(String property, String value) {
            addCriterion(checkProperty(property).getColumn() + "  not like", value, checkProperty(property).getName());
            return (Example.Criteria)this;
        }
        public Criteria orLike(String property, String value) {
            addOrCriterion(checkProperty(property).getColumn() + "  like", value, checkProperty(property).getName());
            return (Criteria) this;
        }

        public Criteria orNotLike(String property, String value) {
            addOrCriterion(checkProperty(property).getColumn() + "  not like", value, checkProperty(property).getName());
            return (Criteria) this;
        }

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


        protected void addCriterion(String condition, Object value) {
            if (value == null) {
             return ;
            }
            criteria.add(new Criterion(condition, value));
        }


        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new EasyJdbcException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value,false));
        }
        protected void addOrCriterion(String condition) {
            if(condition == null) {
                throw new EasyJdbcException("Value for condition cannot be null");
            } else if(!condition.startsWith("null")) {
                this.criteria.add(new Example.Criterion(condition, true));
            }
        }
        protected void addOrCriterion(String condition, Object value, String property) {
            if (value == null) {
                    throw new EasyJdbcException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value, true));
        }

       protected void addOrCriterion(String condition, Object value1, Object value2,String property ) {
            if (value1 == null || value2 == null) {
                throw new EasyJdbcException("Between values for " + property + " cannot be null");
            }
        criteria.add(new Criterion(condition, value1, value2, true));
    }

        protected void addCriterion(String condition, Object value1, Object value2,String property ) {
            if (value1 == null || value2 == null) {
                throw new EasyJdbcException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2, false));
        }


    }


    public static class Criterion {
        private String condition;
        private Object value;

        private Object secondValue;

        private String andOr;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

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


    public void initEntityElement(Class<?> entityClass){
        this.checkEntity(entityClass);
        this.entityElement = ElementResolver.resolve(this.persistentClass);
    }

    protected boolean isEntity(Class<?> persistentClass) {
        return null != persistentClass.getAnnotation(Entity.class);
    }

    protected void checkEntity(Class<?> persistentClass) {
        Assert.isTrue(this.isEntity(persistentClass), persistentClass + " 如果是实体类型请使用@Entity注解进行标识");
    }


    protected FieldElement  checkProperty(String fieldName) {
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
            throw new EasyJdbcException("当前实体类不包含名为" + fieldName + "的属性!");
        }
    }


  public SQL applyWhere() {
        StringBuilder sb = new StringBuilder();
        List<Criteria> oredCriteria = this.getOredCriteria();
        boolean firstCriteria = true;
        if (!oredCriteria.isEmpty()) {
            for (int i = 0; i < oredCriteria.size(); i++) {
                Criteria criteria = oredCriteria.get(i);
                if (firstCriteria) {
                    firstCriteria = false;
                } else {
                    sb.append(" "+criteria.andOr+" ");
                }
                List<Criterion> criterions = criteria.getCriteria();
                boolean firstCriterion = true;
                for (int j = 0; j < criterions.size(); j++) {
                    Criterion criterion = criterions.get(j);
                    if (firstCriterion) {
                        firstCriterion = false;
                    } else {
                        sb.append(" ");
                        sb.append(" "+criteria.andOr+" ");
                    }
                     sb.append(" ");
                  if (criterion.getCondition().contains("in")||criterion.getCondition().contains("between")) {
                        sb.append(criterion.getCondition());
                    } else {
                            sb.append(criterion.getCondition() + " ?");
                    }
                    if (criterion.getValue() != null) {
                        if (criterion.isListValue()) {
                            StringBuffer stringBuffer = new StringBuffer();
                            String[] values = criterion.getValue().toString().split(",");
                            for (String value : values) {
                                stringBuffer.append("'" + value + "'" + ",");
                            }
                            sb.append(" ('" + stringBuffer.substring(2, stringBuffer.length() - 3) + "') ");
                        } else {
                            if (criterion.betweenValue) {
                                sb.append(" '" + criterion.getValue() + "'");
                                sb.append(" " + criteria.getAndOr() + " ");
                                sb.append("'" + criterion.getSecondValue() + "'");
                            } else {
                                parameters.add(criterion.getValue());
                            }
                        }


                    }

                }
                if (!criterions.isEmpty()) {
                    sqlBuilder.WHERE(sb.toString());
                }
            }
        }
          if (!groupByClause.isEmpty()) {
              for (String  groupBy : groupByClause) {
                  this.sqlBuilder = sqlBuilder.GROUP_BY(checkProperty(groupBy).getColumn());
              }

      }

      if (orderByClause != null) {
            List<Sorts.Order> orderList = orderByClause.getOrders();
            if (!orderList.isEmpty()) {
                for (Sorts.Order order : orderList) {
                    this.sqlBuilder = sqlBuilder.ORDER_BY(checkProperty(order.getProperty()).getColumn() + " " + order.getDirection());
                }
            }

        }

        return sqlBuilder;
    }

    public <T> List<T>  list() {
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

    public <T> T  get() {
        List<T> results=list();
        int size = (results != null ? results.size() : 0);
        if(size>1){
            new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.get(0);
    }

    public int  count()  {
        CountByExampleExecutor executor;
        if(jdbcTemplate!=null) {
            if (sqlBuilder == null || "".equals(sqlBuilder.toString())) {
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

    public <T> PageInfo<T> page() {
        List<T> results=list();
        long total=count();
        return new PageInfoImpl<T>(results,total,pageInfo.getPageNum(),pageInfo.getPageSize());
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
}
