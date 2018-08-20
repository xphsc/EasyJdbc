package test;


import com.xphsc.easyjdbc.config.WebMvcConfiguration;
import com.xphsc.easyjdbc.dao.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

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
        System.out.println("-----" + (userDao.findAll()));
    }
}
