package com.ocean;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@MapperScan("com.ocean.dao.mapper")
public class OceanIcelandApplication {
    public static void main(String[] args) {
        SpringApplication.run(OceanIcelandApplication.class, args);
    }
}
