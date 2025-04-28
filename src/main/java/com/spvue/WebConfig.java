package com.spvue;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5173/",
                        "https://localhost:5173/",
                        "http://localhost:80/",
                        "https://localhost:80/",
                        "http://localhost/",
                        "https://localhost/",
                        "https://localhost:443/",
                        "http://218.38.160.152:5173/",
                        "https://218.38.160.152:5173/",
                        "http://15.164.166.39/",
                        "https://15.164.166.39/",
                        "http://ec2-15-164-166-39.ap-northeast-2.compute.amazonaws.com/",
                        "https://ec2-15-164-166-39.ap-northeast-2.compute.amazonaws.com/"
                )
//                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }


    @Bean
        public HttpSessionEventPublisher httpSessionEventPublisher() {
            return new HttpSessionEventPublisher();
        }


}
