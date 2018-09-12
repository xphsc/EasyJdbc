package com.xphsc.easyjdbc.core;

import com.xphsc.easyjdbc.EasyJdbcSelector;
import com.xphsc.easyjdbc.EasyJdbcTemplate;
import com.xphsc.easyjdbc.core.entity.Example;
import com.xphsc.easyjdbc.page.PageInfo;
import java.util.List;

/**
 * Created by ${huipei.x}
 */

public interface EasyJdbcDao<T> {
    public EasyJdbcTemplate getEasyJdbcTemplate();
    public int insert(Object persistent);
    public int batchInsert(List<?> persistents);
    public <T> T getByPrimaryKey(Object primaryKeyValue);
    public int deleteByPrimaryKey(Object primaryKeyValue);
    public int deleteByPrimaryKey(Class<?> persistentClass,Object primaryKeyValue);
    public int update(Object persistent);
    public int batchUpdate(List<?> persistents);
    public <T> T getByPrimaryKey(Class<?> persistentClass,Object primaryKeyValue);
    public <T> T getByExample(Example example);
    public <T> List<T> findByExample(Example example);
    public int countByExample(Example example) ;
    public <T> PageInfo<T> findByPage(Example example);
    public <T> List<T> findAll();
    public <T> List<T> findAll(Class<?> persistentClass);
    public int count();
   public EasyJdbcSelector selector();
    public Example example();
}
