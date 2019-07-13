package com.bjtu;

import com.bjtu.testmanageplatform.InitConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: gaofeng
 * @Date: 2019-07-03
 * @Description:
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        InitConfig.init();
        SpringApplication.run(Application.class, args);
    }
}
