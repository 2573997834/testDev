package com.lym.testdev;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(scanBasePackages = {"com.lym.testdev"}, exclude = {DruidDataSourceAutoConfigure.class})
@MapperScan(value = {"com.lym.testdev.dao"})
public class TestdevApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestdevApplication.class, args);
    }

}
