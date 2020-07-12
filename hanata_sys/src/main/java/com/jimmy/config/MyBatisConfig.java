package com.jimmy.config;

import com.jimmy.servlet.SqlInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
// import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
/**
 * @author Jimmy He
 * @date 2020-06-10
 */

//@Configuration
public class MyBatisConfig {
    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @PostConstruct
    public void addMySqlInterceptor() {
        SqlInterceptor interceptor = new SqlInterceptor();
        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {

            // 添加自定义属性
//            Properties properties = new Properties();
//            properties.setProperty("prop1", "value1");
//            interceptor.setProperties(properties);
            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);

        }
    }
}
