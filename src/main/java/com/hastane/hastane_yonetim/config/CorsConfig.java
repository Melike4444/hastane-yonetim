package com.hastane.hastane_yonetim.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins(
                                "http://localhost:5173",
                                "http://localhost:5174",
                                "http://localhost:5175",
                                "http://localhost:5176",
                                "http://localhost:5177",
                                "http://localhost:5178",
                                "http://localhost:5179",
                                "http://localhost:5180",
                                "http://localhost:5181",
                                "http://localhost:5182",
                                "http://localhost:5183",
                                "http://localhost:5184",
                                "http://localhost:5185",
                                "http://localhost:5186",
                                "http://localhost:5187",
                                "http://localhost:5188",
                                "http://localhost:5189"
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}

