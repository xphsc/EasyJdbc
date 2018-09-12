package com.xphsc.easyjdbc.core.entity;


import com.xphsc.easyjdbc.builder.SQL;
import com.xphsc.easyjdbc.core.metadata.EntityElement;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.util.LogUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.*;

/**
 * Created by ${huipei.x}
 */
public abstract class AbstractExample {
    protected final LogUtil logger = LogUtil.getLogger(getClass());
    protected SQL sqlBuilder= SQL.BUILD()  ;
    protected Sorts orderByClause;
    protected  boolean distinct;
    protected List<Example.Criteria> oredCriteria;
    protected Class<?> entityClass;
    protected Class<?> persistentClass;
    protected PageInfo pageInfo=new PageInfo();
    protected List<String> excludePropertys=new ArrayList<>();
    protected EntityElement entityElement;
    protected final Map<String,String> mappings = new HashMap();
    public JdbcTemplate jdbcTemplate;
    public  String dialectName;
    protected final LinkedList<Object> parameters = new LinkedList<Object>();
    protected LinkedList<String> selectPropertys=new LinkedList<>();
    protected LinkedList<String> groupByClause=new LinkedList<>();
}
