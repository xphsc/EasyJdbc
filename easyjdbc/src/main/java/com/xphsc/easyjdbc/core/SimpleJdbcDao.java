package com.xphsc.easyjdbc.core;


import com.xphsc.easyjdbc.EasyJdbcSelector;
import com.xphsc.easyjdbc.EasyJdbcTemplate;
import com.xphsc.easyjdbc.core.entity.Example;
import com.xphsc.easyjdbc.core.exception.JdbcDataException;
import com.xphsc.easyjdbc.executor.*;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.util.Assert;
import com.xphsc.easyjdbc.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * Created by ${huipei.x}
 */
public abstract class SimpleJdbcDao<T>  implements EasyJdbcDao<T> {

    private Class<T> modelClass;

    public SimpleJdbcDao()  {
        if (this.getClass().getGenericSuperclass() instanceof ParameterizedType){
            ParameterizedType pt = (ParameterizedType) this.getClass().getGenericSuperclass();
            modelClass = (Class<T>) pt.getActualTypeArguments()[0];
        }
    }

    private EasyJdbcTemplate easyJdbcTemplate;

    @Autowired
    private void setEasyJdbcTemplate(EasyJdbcTemplate easyJdbcTemplate) {
        if(getEasyJdbcTemplate()!=null){
            this.easyJdbcTemplate = getEasyJdbcTemplate();
        }else{
            this.easyJdbcTemplate =easyJdbcTemplate;
        }
    }

    @Override
    public EasyJdbcTemplate getEasyJdbcTemplate() {
        return easyJdbcTemplate;
    }

    @Override
    public int insert(Object persistent) throws JdbcDataException {
        Assert.notNull(persistent, "实体不能为空");
        InsertExecutor executor = new InsertExecutor(easyJdbcTemplate.getJdbcTemplate(), persistent);
        int rows = executor.execute();
        executor = null;//hlep gc.
        return rows;
    }

    @Override
    public int batchInsert(List<?> persistents) throws JdbcDataException{
        Assert.notEmpty(persistents, "实体列表不能为空");
        BatchInsertExecutor executor = new BatchInsertExecutor(easyJdbcTemplate.getJdbcTemplate(),persistents);
        int[] rows = executor.execute();
        executor = null;//hlep gc.
        return rows.length;
    }

    @Override
    public int deleteByPrimaryKey(Object primaryKeyValue) {
        Assert.notNull(modelClass, "实体接口泛型类型不能为空");
        Assert.notNull(primaryKeyValue, "主键不能为空");
        Assert.hasText(primaryKeyValue.toString(), "主键不能为空");
        DeleteExecutor executor = new DeleteExecutor(easyJdbcTemplate.getJdbcTemplate(),modelClass,primaryKeyValue);
        int rows = executor.execute();
        executor = null;//hlep gc.
        return rows;
    }

    @Override
    public int deleteByPrimaryKey(Class<?> persistentClass,Object primaryKeyValue) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Assert.notNull(primaryKeyValue, "主键不能为空");
        Assert.hasText(primaryKeyValue.toString(), "主键不能为空");
        DeleteExecutor executor = new DeleteExecutor(easyJdbcTemplate.getJdbcTemplate(),persistentClass,primaryKeyValue);
        int rows = executor.execute();
        executor = null;//hlep gc.
        return rows;
    }

    /**
     * 更新
     * @param persistent 持久化实体
     * @return 受影响的行数
     */
    @Override
    public int update(Object persistent) throws JdbcDataException{
        Assert.notNull(persistent, "实体不能为空");
        UpdateExecutor executor = new UpdateExecutor(easyJdbcTemplate.getJdbcTemplate(),persistent,true);
        int rows = executor.execute();
        executor = null;//hlep gc.
        return rows;
    }
    /**
     * 批量更新
     * @param ? 持久化实体列表
     * @return 受影响的行数
     */
    @Override
    public int batchUpdate(List<?> persistents) throws JdbcDataException{
        Assert.notEmpty(persistents, "实体列表不能为空");
        BatchUpdateExecutor executor = new BatchUpdateExecutor(easyJdbcTemplate.getJdbcTemplate(),persistents);
        int[] rows = executor.execute();
        executor = null;//hlep gc.
        return rows.length;
    }


    @Override
    public <T> T getByPrimaryKey(Object primaryKeyValue) {
        Assert.notNull(modelClass, "实体接口泛型类型不能为空");
        Assert.notNull(primaryKeyValue, "主键不能为空");
        GetExecutor<T> executor = new GetExecutor<T>(easyJdbcTemplate.getJdbcTemplate(),modelClass,primaryKeyValue);
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
     * 获取
     * @param ? 持久化实体类
     * @param primaryKeyValue 主键值
     * @return 实体对象
     */
    @Override
    public <T> T getByPrimaryKey(Class<?> persistentClass,Object primaryKeyValue) throws JdbcDataException{
        Assert.notNull(persistentClass, "实体类型不能为空");
        Assert.notNull(primaryKeyValue, "主键不能为空");
        GetExecutor<T> executor = new GetExecutor<T>(easyJdbcTemplate.getJdbcTemplate(),persistentClass,primaryKeyValue);
        try{
            T entity = executor.execute();
            return entity;
        } catch(EmptyResultDataAccessException e){
            //当查询结果为空时，会抛出：EmptyResultDataAccessException，这里规避这个异常直接返回null
        }
        executor = null;//hlep gc.
        return null;

    }

    @Override
    public <T> T  getByExample(Example example) throws JdbcDataException {
        List<T> results = findByExample(example);
        int size = (results != null ? results.size() : 0);
        if(size>1){
            new IncorrectResultSizeDataAccessException(1, size);
        }
        return results.get(0);
    }

    @Override
    public <T> List<T>  findByExample(Example example) throws JdbcDataException {
        List<T> list= easyJdbcTemplate.findByExample(example);
        return list;
    }

    @Override
    public int countByExample(Example example) throws JdbcDataException {
        int count=easyJdbcTemplate.countByExample(example);
        return count;
    }

    @Override
    public int count() {
        Assert.notNull(modelClass, "实体接口泛型类型不能为空");
        Example example=example();
        return example.count();
    }

    /**
     * 实体查询
     * @param example
     * @return Page对象
     */
    @Override
    public <T> PageInfo<T> findByPage(Example example) throws JdbcDataException{
        PageInfo<T> pageInfo=easyJdbcTemplate.findByPage(example);
        return pageInfo;
    }

    @Override
    public <T> List<T> findAll(Class<?> persistentClass) throws JdbcDataException{
        Example  example=  new Example(persistentClass,getEasyJdbcTemplate().getJdbcTemplate(),getEasyJdbcTemplate().getDialectName());
        return example.list();
    }

    @Override
    public <T1> List<T1> findAll() {
        Assert.notNull(modelClass, "实体接口泛型类型不能为空");
        Example example=example();
        return example.list();
    }

    @Override
    public EasyJdbcSelector selector() {
        return easyJdbcTemplate.selector();
    }

    @Override
    public Example example() {
        Assert.notNull(modelClass, "实体接口泛型类型不能为空");
        return new Example(modelClass,easyJdbcTemplate.getJdbcTemplate(),getEasyJdbcTemplate().getDialectName());
    }
}
