package com.xphsc.easyjdbc.test;

import com.github.xtool.serialize.JSONHelper;
import com.xphsc.easyjdbc.EasyJdbcApplication;
import com.xphsc.easyjdbc.EasyJdbcTemplate;
import com.xphsc.easyjdbc.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author huipei.x
 * @data 创建时间 2018/6/24
 * @description 类说明 :
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = EasyJdbcApplication.class)
@Slf4j

public class UserDaoTest {

    @Autowired
    private UserDao userDao;


    @Test
    public void list() {
        log.info("-----" + JSONHelper.toJSON(userDao.findAll()));
    }
}
