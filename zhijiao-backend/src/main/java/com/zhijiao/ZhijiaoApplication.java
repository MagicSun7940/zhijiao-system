package com.zhijiao;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 支教管理系统启动类
 */
@SpringBootApplication
@MapperScan("com.zhijiao.mapper")
public class ZhijiaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhijiaoApplication.class, args);
        System.out.println("====== 支教管理系统启动成功 ======");
    }
}
