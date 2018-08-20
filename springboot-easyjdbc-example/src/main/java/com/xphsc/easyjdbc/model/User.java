package com.xphsc.easyjdbc.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author huipei.x
 * @data 创建时间 2018/6/24
 * @description 类说明 :
 */
@Data
@Entity
@Table(name="t_user")
public class User implements Serializable {
    @Id
    private Integer id;
    @Column(name="user_name")
    private String  userName;
    private Integer age;
    private String password;
    @Column(name="create_time")
private Date createTime;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
