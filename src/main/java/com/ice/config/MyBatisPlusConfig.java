package com.ice.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.ice.mapper")
public class MyBatisPlusConfig {
}
