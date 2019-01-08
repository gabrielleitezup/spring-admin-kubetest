package com.example.zup.admintest;

import com.ecwid.consul.v1.ConsulClient;
import de.codecentric.boot.admin.config.EnableAdminServer;
import de.codecentric.boot.admin.discovery.ServiceInstanceConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@EnableAdminServer
@EnableDiscoveryClient
public class AdminTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminTestApplication.class, args);
    }

    @Bean
    public ServiceInstanceConverter serviceInstanceConverter(ConsulClient consulClient) {
        return new RealwaveServiceInstanceConverter(consulClient);
    }

}

