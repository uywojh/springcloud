package me.javaroad.mcloud.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author heyx
 */
@EnableEurekaClient
@SpringBootApplication
public class OAuthServer {

    public static void main(String[] args) {
        SpringApplication.run(OAuthServer.class, args);
    }

}
