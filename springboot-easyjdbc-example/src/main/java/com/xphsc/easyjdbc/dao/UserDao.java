package com.xphsc.easyjdbc.dao;

import com.xphsc.easyjdbc.annotation.SqlParam;
import com.xphsc.easyjdbc.annotation.SqlSelect;
import com.xphsc.easyjdbc.core.EasyJdbcDao;
import com.xphsc.easyjdbc.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author huipei.x
 * @data 创建时间 2018/6/24
 * @description 类说明 :
 */
@Repository
public interface UserDao  extends EasyJdbcDao<User> {
    @SqlSelect(value = "select * from t_user",entityClass=User.class)
    public List<User> listUser();
    @SqlSelect(value = "select * from t_user where id=#{id}",entityClass=User.class)
    public List<User> getUserById(@SqlParam("id")Integer id);
}
