package com.xphsc.easyjdbc.dao;

import com.xphsc.easyjdbc.annotation.EasyDao;
import com.xphsc.easyjdbc.annotation.SqlParam;
import com.xphsc.easyjdbc.annotation.SqlSelect;
import com.xphsc.easyjdbc.annotation.SqlSelectProvider;
import com.xphsc.easyjdbc.core.EasyJdbcDao;
import com.xphsc.easyjdbc.model.User;
import com.xphsc.easyjdbc.model.response.UserDTO;
import com.xphsc.easyjdbc.page.PageInfo;
import com.xphsc.easyjdbc.sql.UserSqlProvider;

import java.util.List;
import java.util.Map;

/**
 * @author huipei.x
 * @data 创建时间 2018/6/24
 * @description 类说明 :
 */
//@Repository
@EasyDao
public interface UserDao  extends EasyJdbcDao<User> {
    @SqlSelect(value = "select * from t_user",entityClass=User.class)
    public List<User> listUser();
    @SqlSelect(value = "select * from t_user where id=#{id}",entityClass=User.class)
    public List<User> getUserById(@SqlParam("id")Integer id);
    @SqlSelect(value = "select * from t_user where id=#{id}",entityClass=User.class)
    public List<User> getUserByMapId(Map map);
    @SqlSelect(value = "select * from t_user where user_name=#{userName}",entityClass=UserDTO.class)
    public PageInfo<UserDTO> listUser(@SqlParam("userName")String  userName, @SqlParam("pageNum")Integer pageNum, @SqlParam("pageSize")Integer pageSize);
    @SqlSelectProvider(type= UserSqlProvider.class,method = "listUserBySql",entityClass =UserDTO.class )
    public List<UserDTO> listUserBySql();
    @SqlSelectProvider(type= UserSqlProvider.class,method = "listUserBySqlMap",entityClass =UserDTO.class )
    public List<UserDTO> listUserBySqlMap(Map map);

}
