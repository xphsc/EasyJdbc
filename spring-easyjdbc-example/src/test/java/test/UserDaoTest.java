package test;


import com.alibaba.fastjson.JSON;
import com.github.xtool.collect.Maps;
import com.xphsc.easyjdbc.config.WebMvcConfiguration;
import com.xphsc.easyjdbc.core.entity.Example;
import com.xphsc.easyjdbc.dao.UserDao;
import com.xphsc.easyjdbc.model.User;
import com.xphsc.easyjdbc.page.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Map;

/**
 * @author huipei.x
 * @data 创建时间 2018/6/24
 * @description 类说明 :
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(loader = AnnotationConfigWebContextLoader.class,
        classes = {WebMvcConfiguration.class})
@Slf4j
public class UserDaoTest {

    @Autowired
    private UserDao userDao;


    @Test
    public void list() {
        Map map= Maps.newHashMap();
        map.put("id",1);
        System.out.println("-----" + ( JSON.toJSON(userDao.listUserBySqlMap(map))));
    }
}
