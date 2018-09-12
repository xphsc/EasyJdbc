package com.xphsc.easyjdbc.executor;



import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.core.metadata.DynamicEntityElement;
import com.xphsc.easyjdbc.core.metadata.ElementResolver;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.core.metadata.FieldElement;
import com.xphsc.easyjdbc.page.PageHelper;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.transform.DynamicEntityRowMapper;
import com.xphsc.easyjdbc.transform.EntityRowMapper;
import com.xphsc.easyjdbc.util.ListUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import java.util.*;


/**
 * 查询全部记录的执行器
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
    protected List<String> excludePropertys=new ArrayList<>();
    protected LinkedList<String> selectPropertys=new LinkedList<>();
    private  Object[] parameters;
    public FindByExampleExecutor(SQL applyWhere,Class<?> persistentClass,Class<?> entityClass, PageInfo pageInfo, EntityElement entityElement,List<String> excludePropertys,Map<String,String> mappings,boolean distinct,LinkedList<String> selectPropertys,Object[] parameters,JdbcTemplate jdbcTemplate,String dialectName) {
        super(jdbcTemplate);
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


    @Override
    public void prepare() {
        if(this.isEntity(this.persistentClass)){
            this.isDynamic = false;
            this.entityElement = ElementResolver.resolve(this.persistentClass);
        } else {
            this.isDynamic = true;
            this.dynamicEntityElement = ElementResolver.resolveDynamic(this.persistentClass,this.dynamicMappings);
        }
       List<String> columns=new ArrayList();
        sqlBuilder.FROM(this.newEntityElement.getTable());
        Iterator i = this.newEntityElement.getFieldElements().values().iterator();
            while(i.hasNext()) {
                FieldElement fieldElement = (FieldElement)i.next();
                if(fieldElement.isTransientField()) {
                    continue;
                }
                columns.add(fieldElement.getColumn());
                if(excludePropertys.isEmpty()&&selectPropertys.isEmpty()){
                    buildSQL(fieldElement.getColumn());
                }

            }
        if(!excludePropertys.isEmpty()){
            columns= ListUtil.removeAll(columns, this.excludePropertys);
            for(String column:columns){
                buildSQL(column);
            }
        }
       if(!selectPropertys.isEmpty()){
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
          sql=  PageHelper.pagination(dialectName, this.sqlBuilder.toString(), startRow, this.limit);
        }


        if(this.isDynamic){
            rowMapper = new DynamicEntityRowMapper(LOBHANDLER,this.dynamicEntityElement,this.persistentClass);
        } else {
            rowMapper = new EntityRowMapper(LOBHANDLER,this.entityElement,this.persistentClass);
        }
                logger.debug("SQL:["+sql+"]");
                if(null==this.parameters||this.parameters.length==0){
                    return (T) this.jdbcTemplate.query(sql,rowMapper);
                } else {
                    getParameters(parameters);
                    return (T) this.jdbcTemplate.query(sql,this.parameters,rowMapper);
                }
        }


}
