package org.lyncc.bazinga;

import org.lyncc.bazinga.anno.config.EnabledBazingaAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * spring boot 启动类
 *
 * @author liguolin
 * @create 2018-01-29 10:23
 **/
//@EnabledBazingaMethodAnnotation(basePackages = "org.lyncc.bazinga.service")
@SpringBootApplication
@EnabledBazingaAnnotation(basePackages = "org.lyncc.bazinga.service")
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }


}
