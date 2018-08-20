package com.xphsc.easyjdbc.model.response;


import lombok.Data;
import java.util.Date;

/**
 * @author huipei.x
 * @data 创建时间 2018/6/24
 * @description 类说明 :
 */
@Data
public class UserDTO {
    private Integer userId;
    private String  userName;
    private Integer age;
    private String password;
    private Date createTime;
}
