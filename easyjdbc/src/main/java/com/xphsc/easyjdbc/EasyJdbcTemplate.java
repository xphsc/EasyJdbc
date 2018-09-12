package com.xphsc.easyjdbc;




import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.entity.Example;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.executor.*;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.page.PageInfoImpl;
import com.xphsc.easyjdbc.support.EasyJdbcAccessor;
import com.xphsc.easyjdbc.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import javax.sql.DataSource;
import java.util.List;

/**
 * Created by ${huipei.x}
 */
public class EasyJdbcTemplate extends EasyJdbcAccessor {
    private  JdbcTemplate jdbcTemplate;

     public EasyJdbcTemplate() {
     }

    public EasyJdbcTemplate(DataSource dataSource, String dialectName) {
        this.setDialectName(dialectName);
        this.setDataSource(dataSource);
        this.afterPropertiesSet();
    }

    public EasyJdbcTemplate(JdbcTemplate jdbcTemplate,String dialectName) {
        this.setDialectName(dialectName);
       this.jdbcTemplate=jdbcTemplate;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 获取jdbcTemplate
     */
    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    /**
     * 插入
     * @param persistent 持久化实体
     * @return 受变更影响的行数
     */
    public int insert(Object persistent) throws JdbcDataException {
        Assert.notNull(persistent, "实体不能为空");
        InsertExecutor executor = new InsertExecutor( jdbcTemplate, persistent);
        int rows = executor.execute();
        executor = null;
        return rows;
    }

    /**
     * 批量插入
     * 如果数据量过大，建议分次插入，每次最好不超过一万条。
     * @return 插入的行数
     */
    public int batchInsert(List<?> persistents) throws JdbcDataException{
        Assert.notEmpty(persistents, "实体列表不能为空");
        BatchInsertExecutor executor = new BatchInsertExecutor(jdbcTemplate,persistents);
        int[] rows = executor.execute();
        executor = null;
        return rows.length;
    }

    /**
     * SQL插入
     * @param insertSql SQL构造器
     * @param parameters 参数
     * @return 插入的行数
     */
    public int insert(SQL insertSql,Object... parameters) throws JdbcDataException{
        Assert.hasText(insertSql.toString(), "SQL构造器不能为空");
        int rows = this.jdbcTemplate.update(insertSql.toString(), parameters);
        return rows;
    }

    /**
     * 删除
     * @param persistentClass 实体类
     * @param primaryKeyValue 主键值
     * @return 受影响的行数
     */
    public int deleteByPrimaryKey(Class<?> persistentClass,Object primaryKeyValue) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Assert.notNull(primaryKeyValue, "主键不能为空");
        Assert.hasText(primaryKeyValue.toString(), "主键不能为空");
        DeleteExecutor executor = new DeleteExecutor(jdbcTemplate,persistentClass,primaryKeyValue);
        int rows = executor.execute();
        executor = null;
        return rows;
    }

    /**
     * SQL删除
     * @param deleteSql SQL构造器
     * @param parameters 参数
     * @return 删除的行数
     */
    public int delete(SQL deleteSql,Object... parameters) throws JdbcDataException{
        Assert.hasText(deleteSql.toString(), "SQL构造器不能为空");
        int rows = this.jdbcTemplate.update(deleteSql.toString(), parameters);
        return rows;
    }



    /**
     * 更新
     * @param persistent 持久化实体
     * @return 受影响的行数
     */
    public int update(Object persistent) throws JdbcDataException{
        Assert.notNull(persistent, "实体不能为空");
        UpdateExecutor executor = new UpdateExecutor(jdbcTemplate,persistent,true);
        int rows = executor.execute();
        executor = null;
        return rows;
    }
    /**
     * 批量更新
     * @param ? 持久化实体列表
     * @return 受影响的行数
     */
    public int batchUpdate(List<?> persistents) throws JdbcDataException{
        Assert.notEmpty(persistents, "实体列表不能为空");
        BatchUpdateExecutor executor = new BatchUpdateExecutor(jdbcTemplate,persistents);
        int[] rows = executor.execute();
        executor = null;//hlep gc.
        return rows.length;
    }

    /**
     * SQL修改
     * @param updateSql SQL构造器
     * @param parameters 参数
     * @return 删除的行数
     */
    public int update(SQL updateSql,Object... parameters) throws JdbcDataException{
        Assert.notNull(updateSql, "SQL构造器不能为空");
        int rows = this.jdbcTemplate.update(updateSql.toString(), parameters);
        return rows;
    }

    /**
     * 获取
     * @param ? 持久化实体类
     * @param primaryKeyValue 主键值
     * @return 实体对象
     */
    public <T> T getByPrimaryKey(Class<?> persistentClass,Object primaryKeyValue) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Assert.notNull(primaryKeyValue, "主键不能为空");
        GetExecutor<T> executor = new GetExecutor<T>(jdbcTemplate,persistentClass,primaryKeyValue);
        try{
            T entity = executor.execute();
            return entity;
        } catch(EmptyResultDataAccessException e){
            //当查询结果为空时，会抛出：EmptyResultDataAccessException，这里规避这个异常直接返回null
        }
        executor = null;//hlep gc.
        return null;

    }

    /**
     * @param sql sql语句
     * @param persistentClass 实体类型
     * @param parameters 参数
     * @return
     */
    public <T> T get(String sql,Class<?> persistentClass,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        List<T> results = find( sql,persistentClass,parameters);
        int size = (results != null ? results.size() : 0);
        if(size>1){
            new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.get(0);
    }

    /**
     *  获取全部记录
     * @param persistentClass
     * @param <T>
     * @return
     * @throws JdbcDataException
     */
    public <T> List<T> findAll(Class<?> persistentClass) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Example example= example(persistentClass);
        return example.list();
    }

    /**
     *  获取全部记录
     * @param persistentClass
     * @param <T>
     * @return
     * @throws JdbcDataException
     */
    public <T> List<T> findAll(Class<?> persistentClass,PageInfo page) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Assert.isTrue(page.getPageNum()>=1, " pageNum必须大于1");
        Assert.isTrue(page.getPageSize() > 0, "pageSize必要大于0");
        Example example= example(persistentClass);
        example.pageInfo(page.getPageNum(),page.getPageSize());
        return example.list();
    }


    /**
     * 实体查询
     * @param selectSql 查询SQL构造器
     * @param persistentClass 持久化实体类
     * @param parameters 查询参数
     * @return 实体对象列表
     */
    public <T> List<T> find(SQL selectSql,Class<?> persistentClass,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Assert.hasText(selectSql.toString(), "sql语句不能为空");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(jdbcTemplate,this.getDialectName(),persistentClass,selectSql.toString(),parameters);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }

    /**
     * 实体查询
     * @param selectSql 查询SQL构造器
     * @param startRow 起始行
     * @param limit 条数
     * @param persistentClass 持久化实体类
     * @param parameters 查询参数
     * @return 实体对象列表
     */
    public <T> List<T> find(SQL selectSql,Class<?> persistentClass,Integer startRow,Integer limit,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Assert.hasText(selectSql.toString(), "sql语句不能为空");
        Assert.isTrue(startRow >= 0, "startRow必须大于等于0");
        Assert.isTrue(limit > 0, "limit必要大于0");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(jdbcTemplate,this.getDialectName(),persistentClass,selectSql.toString(),parameters,null,startRow,limit);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }

    /**
     * 实体查询
     * @param selectSql 查询SQL构造器
     * @param page 对象
     * @param persistentClass 持久化实体类
     * @param parameters 查询参数
     * @return 实体对象列表
     */
    public <T> List<T> find(SQL selectSql,Class<?> persistentClass,PageInfo page,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Assert.hasText(selectSql.toString(), "sql语句不能为空");
        Assert.isTrue(page.getPageNum()>=1, "pageNum必须大于1");
        Assert.isTrue(page.getPageSize()>0, "pageSize必要大于0");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(jdbcTemplate,this.getDialectName(),persistentClass,selectSql.toString(),parameters,null,page);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }

    /**
     * 实体查询
     * @param selectSql 查询SQL构造器
     * @param page 对象
     * @param persistentClass 持久化实体类
     * @param parameters 查询参数
     * @return Page对象
     */
    public <T> PageInfo<T> findByPage(SQL selectSql,Class<?> persistentClass,PageInfo page,Object... parameters) throws JdbcDataException{
        List<T> list= find(selectSql,persistentClass,page,parameters);
        int total= count(selectSql.toString(), parameters);
        return new PageInfoImpl<T>(list,total,page.getPageNum(),page.getPageSize());
    }


    /**
     * 实体查询
     * @param sql 查询SQL
     * @param persistentClass 持久化实体类
     * @param parameters 查询参数
     * @return 实体对象列表
     */
    public <T> List<T> find(String sql,Class<?> persistentClass,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Assert.hasText(sql, "sql语句不能为空");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(jdbcTemplate,this.getDialectName(),persistentClass,sql,parameters);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }

    /**
     * 实体查询
     * @param sql 查询SQL
     * @param   page 对象
     * @param persistentClass 持久化实体类
     * @param parameters 查询参数
     * @return 实体对象列表
     */
    public <T> List<T> find(String sql,Class<?> persistentClass,PageInfo page,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Assert.hasText(sql, "sql语句不能为空");
        Assert.isTrue(page.getPageNum() >= 1, "pageNum必须大于1");
        Assert.isTrue(page.getPageSize() > 0, "pageSize必要大于0");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(jdbcTemplate,this.getDialectName(),persistentClass,sql,parameters,null,page);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }
    /**
     * 实体查询
     * @param sql 查询SQL
     * @param startRow 起始行
     * @param limit 条数
     * @param persistentClass 持久化实体类
     * @param parameters 查询参数
     * @return 实体对象列表
     */
    public <T> List<T> find(String sql,Class<?> persistentClass,Integer startRow,Integer limit,Object... parameters) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Assert.hasText(sql, "sql语句不能为空");
        Assert.isTrue(startRow>=0, "startRow必须大于等于0");
        Assert.isTrue(limit > 0, "limit必要大于0");
        FindExecutor<List<T>> executor =  new FindExecutor<List<T>>(jdbcTemplate,this.getDialectName(),persistentClass,sql,parameters,null,startRow,limit);
        List<T> list = executor.execute();
        executor = null;
        return list;
    }

    /**
     * 条数统计
     * @param sql 统计SQL
     * @param parameters 统计参数
     * @return 条数
     */
    public int count(String sql,Object... parameters) throws JdbcDataException{
        Assert.hasText(sql, "sql语句不能为空");
        CountExecutor executor =  new CountExecutor(jdbcTemplate,sql,parameters);
        int count = executor.execute();
        executor = null;
        return count;
    }

    /**
     *
     * @param persistentClass 对象
     * @return
     * @throws JdbcDataException
     */
    public int count(Class<?> persistentClass) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        CountExecutor executor =  new CountExecutor(jdbcTemplate,persistentClass);
        int count = executor.execute();
        executor = null;
        return count;
    }

   public <T> List<T>  findByExample(Example example) throws JdbcDataException{
      example.jdbcTemplate=jdbcTemplate;
       example.dialectName=getDialectName();
       List<T> list=example.list();
        return list;
    }

    public <T> T  getByExample(Example example) throws JdbcDataException {
        List<T> results = findByExample(example);
        int size = (results != null ? results.size() : 0);
        if(size>1){
            new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.get(0);
    }

    public int countByExample(Example example) throws JdbcDataException{
       example.jdbcTemplate=jdbcTemplate;
       example.dialectName=getDialectName();
        int count=example.count();
        return count;
    }

    /**
     * 实体查询
     * @param example
     * @return Page对象
     */
   public <T> PageInfo<T> findByPage(Example example) throws JdbcDataException{
       example.jdbcTemplate=jdbcTemplate;
       example.dialectName=getDialectName();
       PageInfo<T> pageInfo=example.page();
        return pageInfo;
    }

    /**
     * 查询器
     */
    public EasyJdbcSelector selector(){
        return new EasyJdbcSelector(jdbcTemplate,this.getDialectName());
    }


    public Example example(Class<?> persistentClass){
        return new Example(persistentClass,jdbcTemplate,this.getDialectName());
    }


}
